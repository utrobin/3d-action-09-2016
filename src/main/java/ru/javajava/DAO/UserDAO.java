package ru.javajava.DAO;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javajava.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Created by ivan on 24.10.16.
 */
@Service
@Transactional
public class UserDAO {

    private final JdbcTemplate template;
    private int numPagesCached = -1;

    @SuppressWarnings("unused")
    public UserDAO(JdbcTemplate template) {
        this.template = template;
        initTable();
    }

    public void initTable() {
        final String query = "CREATE TABLE IF NOT EXISTS user (" +
                "id BIGINT NOT NULL auto_increment PRIMARY KEY," +
                "login VARCHAR(30) NOT NULL UNIQUE," +
                "password VARCHAR(100) NOT NULL," +
                "email VARCHAR(30) UNIQUE," +
                "rating INT NOT NULL DEFAULT 0," +
                "visits INT NOT NULL DEFAULT 1) DEFAULT CHARSET utf8 DEFAULT COLLATE utf8_general_ci;";
        template.execute(query);
    }

    public UserProfile addUser(String login, String password, String email) throws DuplicateKeyException {
        final UserProfile user = new UserProfile(login, password, email);
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(new UserPstCreator(user), keyHolder);
        final Map<String, Object> keys = keyHolder.getKeys();
        user.setId((Long)keys.get("GENERATED_KEY"));
        numPagesCached = -1;
        return user;
    }

    public int removeUser (long id) {
        final String query = "DELETE FROM user WHERE id = ?;";
        numPagesCached = -1;
        return template.update(query, id);
    }


    public UserProfile getUserByLogin(String login) throws EmptyResultDataAccessException {
        final String query = "SELECT * FROM user WHERE login = ?;";
        return template.queryForObject(query, userMapper, login);
    }

    public UserProfile getUserById(long id) throws EmptyResultDataAccessException {
        final String query = "SELECT * FROM user WHERE id = ?;";
        return template.queryForObject(query, userMapper, id);
    }

    public void incrementVisits(long userId) throws EmptyResultDataAccessException {
        final String query = "UPDATE user SET visits = visits + 1 WHERE id = ?;";
        final int affectedRows = template.update(query, userId);
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException("User not found", 1);
        }
    }

    public void setRating(long userId, int rating) throws EmptyResultDataAccessException {
        final String query = "UPDATE user SET rating = ? WHERE id = ?;";
        final int affectedRows = template.update(query, rating, userId);
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException("User not found", 1);
        }
    }

    public void incrementRating(long userId) throws EmptyResultDataAccessException {
        final String query = "UPDATE user SET rating = rating + 1 WHERE id = ?;";
        final int affectedRows = template.update(query, userId);
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException("User not found", 1);
        }
    }



    public ResultBean getBestUsers(int page, int limit) throws EmptyResultDataAccessException {
        if (numPagesCached == -1) {
            final String count = "SELECT count(*) FROM user;";
            final int numRows = template.queryForObject(count, Integer.class);
            numPagesCached = numRows / limit + 1;
        }
        final int offset = limit * (page - 1);
        final String query = "SELECT * FROM user ORDER BY rating DESC LIMIT ? OFFSET ?;";
        final List<UserProfile> users = template.query(query, userMapper, limit, offset);
        return new ResultBean(users, numPagesCached);
    }

    private static class UserPstCreator implements PreparedStatementCreator {
        private final UserProfile user;
        UserPstCreator(UserProfile user) {
            this.user = user;
        }
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            final String query = "INSERT INTO user (login, password, email) VALUES (?,?,?);";
            final PreparedStatement pst = con.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getLogin());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getEmail());
            return pst;
        }
    }

    private final RowMapper<UserProfile> userMapper = (rs, rowNum) -> {
        final String login = rs.getString("login");
        final String password = rs.getString("password");
        final String email = rs.getString("email");
        final int visits = rs.getInt("visits");
        final int rating = rs.getInt("rating");
        final long id = rs.getLong("id");
        final UserProfile result =  new UserProfile(login, password, email, visits);
        result.setRating(rating);
        result.setId(id);
        return result;
    };


    public static class ResultBean {
        public List<UserProfile> users;
        public int numPages;

        public ResultBean(List<UserProfile> users, int numPages) {
            this.users = users;
            this.numPages = numPages;
        }
    }
}





