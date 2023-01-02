package com.moreos.sockets.repositories;

import com.zaxxer.hikari.HikariDataSource;
import com.moreos.sockets.models.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final JdbcTemplate template;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatRoomRepositoryImpl(HikariDataSource dataSource, @Lazy MessageRepository messageRepository) {
        this.template = new JdbcTemplate(dataSource);
        this.messageRepository = messageRepository;
        createTable();
    }

    private void createTable() {
        String querySchema = "CREATE SCHEMA IF NOT EXISTS server;";
        String queryTable = "CREATE TABLE IF NOT EXISTS server.chatroom (" +
                "id serial PRIMARY KEY," +
                "name VARCHAR NOT NULL," +
                "owner_id numeric NOT NULL);";
        template.execute(querySchema);
        template.execute(queryTable);
    }

    @Override
    public List<ChatRoom> findAll() {
        List<ChatRoom> list = template.query("SELECT * FROM server.chatRoom", new ChatRoomRowMapper());
        return list;
    }

    @Override
    public Optional<ChatRoom> findById(Long id) {
        List<ChatRoom> list = template.query("SELECT * FROM server.chatRoom WHERE login = ?", new ChatRoomRowMapper(), id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO server.chatRoom VALUES(DEFAULT, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, chatRoom.getName());
            preparedStatement.setLong(2, chatRoom.getOwner());
            return preparedStatement;

        }, generatedKeyHolder);

//        template.update("INSERT INTO server.chatRoom VALUES(DEFAULT, ?, ?)", chatRoom.getName(), chatRoom.getOwner(), Statement.RETURN_GENERATED_KEYS);
        chatRoom.setId(Objects.requireNonNull(generatedKeyHolder.getKey()).longValue());
        return chatRoom;
    }

    public class ChatRoomRowMapper implements RowMapper<ChatRoom> {
        @Override
        public ChatRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ChatRoom(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("owner_id"));
        }
    }
}
