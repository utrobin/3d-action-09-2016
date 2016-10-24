package ru.javajava.DAO;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.javajava.model.UserProfile;

import java.util.List;

/**
 * Created by ivan on 24.10.16.
 */
public interface UserDAO {
    UserProfile addUser(String login, String password, String email) throws DuplicateKeyException;
    UserProfile getUserByLogin(String login) throws EmptyResultDataAccessException;
    UserProfile getUserById(long id) throws EmptyResultDataAccessException;
    void incrementVisits(long userId) throws EmptyResultDataAccessException;
    void setRating(long userId, int rating) throws EmptyResultDataAccessException;
    void incrementRating(long userId) throws EmptyResultDataAccessException;
    List<UserProfile> getAll() throws EmptyResultDataAccessException;
}
