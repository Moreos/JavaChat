package com.moreos.sockets.repositories;

import com.zaxxer.hikari.HikariDataSource;
import com.moreos.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcTemplate template;

    @Autowired
    public UsersRepositoryImpl(HikariDataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        createTable();
    }

    private void createTable() {
        String querySchema = "CREATE SCHEMA IF NOT EXISTS server;";
        String queryTable = "CREATE TABLE IF NOT EXISTS server.users (" +
                "id serial PRIMARY KEY," +
                "login VARCHAR NOT NULL," +
                "password VARCHAR NOT NULL," +
                "authenticate BOOLEAN);";
        template.execute(querySchema);
        template.execute(queryTable);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        List<User> list = template.query("SELECT * FROM server.users WHERE login = ?", new UserRowMapper(), login);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public void save(User user) {
        template.update("INSERT INTO server.users VALUES(DEFAULT, ?, ?, ?)", user.getLogin(), user.getPassword(), false);

//        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
//        template.update(conn -> {
//            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO server.users VALUES(DEFAULT, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setString(1, user.getLogin());
//            preparedStatement.setString(2, user.getPassword());
//            preparedStatement.setBoolean(3, user.isAuthenticationStatus());
//            return preparedStatement;
//
//        }, generatedKeyHolder);
//
//        user.setId(Objects.requireNonNull(generatedKeyHolder.getKey()).longValue());
    }

    @Override
    public User update(User user) {
        template.update("UPDATE server.users SET login = ?, password = ?, authenticate = ? WHERE id = ?",
                user.getLogin(), user.getPassword(), user.isAuthenticationStatus(), user.getId());

        return user;
    }

    public class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getBoolean("authenticate"));
        }
    }
}
