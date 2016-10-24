package ru.javajava.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.javajava.DAO.UserDAO;
import ru.javajava.model.UserProfile;

import java.util.Collections;
import java.util.List;

/**
 * Created by ivan on 23.10.2016.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserProfile addUser(String login, String password, String email) throws DuplicateKeyException {
        return userDAO.addUser(login, password, email);
    }

    @Override
    public UserProfile getUserByLogin(String login) {
        try {
            return userDAO.getUserByLogin(login);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public UserProfile getUserById(long id) {
        try {
            return userDAO.getUserById(id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void incrementVisits(long userId) {
        userDAO.incrementVisits(userId);
    }

    @Override
    public void setRating(long userId, int rating) {
        userDAO.setRating(userId, rating);
    }

    @Override
    public void incrementRating(long userId) {
        userDAO.incrementRating(userId);
    }


    @Override
    public List<UserProfile> getBestUsers() {
        final List<UserProfile> users = userDAO.getAll();
        Collections.sort(users, (a, b) -> b.getRating() - a.getRating());
        return users;
    }
}
