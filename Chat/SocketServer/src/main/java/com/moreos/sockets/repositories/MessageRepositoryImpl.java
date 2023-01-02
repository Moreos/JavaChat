package com.moreos.sockets.repositories;

import com.zaxxer.hikari.HikariDataSource;
import com.moreos.sockets.models.ChatRoom;
import com.moreos.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MessageRepositoryImpl implements MessageRepository {

    private final JdbcTemplate template;
    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public MessageRepositoryImpl(HikariDataSource dataSource, @Lazy UsersRepository usersRepository, @Lazy ChatRoomRepository chatRoomRepository) {
        this.template = new JdbcTemplate(dataSource);
        this.usersRepository = usersRepository;
        this.chatRoomRepository = chatRoomRepository;
        createTable();
    }

    private void createTable() {
        template.execute("DROP TABLE server.messages;");
        String querySchema = "CREATE SCHEMA IF NOT EXISTS server;";
        String queryTable = "CREATE TABLE IF NOT EXISTS server.messages (" +
                "id serial PRIMARY KEY," +
                "author VARCHAR NOT NULL," +
                "room_id numeric NOT NULL," +
                "message VARCHAR NOT NULL," +
                "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        template.execute(querySchema);
        template.execute(queryTable);
    }


    @Override
    public List<Message> findByRoom(ChatRoom chatRoom) {
        List<Message> list = template.query("SELECT * FROM server.chatRoom WHERE room_id = ? ORDER BY time DESC LIMIT 30", new MessageRoomRowMapper());
        return list;
    }

    @Override
    public void save(Message message) {
        template.update("INSERT INTO server.messages VALUES(DEFAULT, ?, ?, ?, DEFAULT)", message.getAuthor().getLogin(), message.getChatRoom().getId(), message.getText());
    }

    public class MessageRoomRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Message(rs.getLong("id"),
                    usersRepository.findByLogin(rs.getString("author")).get(),
                    chatRoomRepository.findById(rs.getLong("room_id")).get(),
                    rs.getString("message"),
                    rs.getDate("time"));
        }
    }
}
