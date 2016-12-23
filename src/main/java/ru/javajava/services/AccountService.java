package ru.javajava.services;

import ru.javajava.DAO.UserDAO;
import ru.javajava.exceptions.AlreadyExistsException;
import ru.javajava.model.UserProfile;

/**
 * Created by ivan on 05.10.16.
 */
public interface AccountService {
    UserProfile addUser(String login, String password, String email) throws AlreadyExistsException;
    UserProfile getUserByLogin(String login);
    UserProfile getUserById(long id);
    int removeUser (long id);
    UserDAO.ResultBean getBestUsers(int page, int limit);
    void incrementVisits(long userId);
    void setRating(long userId, int rating);
    void incrementRating(long userId);
    void incrementRating(long userId, int value);
}
