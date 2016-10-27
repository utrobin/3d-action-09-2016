package ru.javajava.services;

import ru.javajava.exceptions.AlreadyExistsException;
import ru.javajava.model.UserProfile;

import java.util.List;

/**
 * Created by ivan on 05.10.16.
 */
public interface AccountService {
    UserProfile addUser(String login, String password, String email) throws AlreadyExistsException;
    UserProfile getUserByLogin(String login);
    UserProfile getUserById(long id);
    List<UserProfile> getBestUsers(int page);
    void incrementVisits(long userId);
    void setRating(long userId, int rating);
    void incrementRating(long userId);
}
