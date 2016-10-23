package ru.javajava.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.javajava.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by Администратор on 23.10.2016.
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final JdbcTemplate template;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class.getName());

    @SuppressWarnings("unused")
    public AccountServiceImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public UserProfile addUser(String login, String password, String email) {
        final UserProfile user = new UserProfile(login, password, email);
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(new UserPstCreator(user), keyHolder);
        }
        catch (DuplicateKeyException e) {
            LOGGER.info("Creating user \"{}\" failed because he already exists!", login);
            return null;
        }
        final Map<String, Object> keys = keyHolder.getKeys();
        user.setId((Long)keys.get("GENERATED_KEY"));
        LOGGER.info("User \"{}\" was successfully created", login);
        return user;
    }

    @Override
    public UserProfile getUserByLogin(String login) {
        try {
            return template.queryForObject(
                    "SELECT * FROM user WHERE login = ?", userMapper, login);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public UserProfile getUserById(long id) {
        try {
            return template.queryForObject(
                    "SELECT * FROM user WHERE id = ?", userMapper, id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int incrementVisits(long userId) {
        final String query = "UPDATE user SET visits = visits + 1 WHERE id = ?;";
        return template.update(query, userId);
    }

    @Override
    public int setRating(long userId, int rating) {
        final String query = "UPDATE user SET rating = ? WHERE id = ?;";
        return template.update(query, rating, userId);
    }

    @Override
    public int incrementRating(long userId) {
        final String query = "UPDATE user SET rating = rating + 1 WHERE id = ?;";
        return template.update(query, userId);
    }


    private static class UserPstCreator implements PreparedStatementCreator {
        private final UserProfile user;
        UserPstCreator(UserProfile user) {
            this.user = user;
        }
        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            final String query = "INSERT INTO user (login, password, email, visits) VALUES (?,?,?,?);";
            final PreparedStatement pst = con.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getLogin());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getEmail());
            pst.setInt(4, user.getVisits());
            return pst;
        }
    }

    private final RowMapper<UserProfile> userMapper = (rs, rowNum) -> {
        final String login = rs.getString("login");
        final String password = rs.getString("password");
        final String email = rs.getString("email");
        final int visits = rs.getInt("visits");
        final long id = rs.getLong("id");
        final UserProfile result =  new UserProfile(login, email, password);
        result.setVisits(visits);
        result.setId(id);
        return result;
    };
}
