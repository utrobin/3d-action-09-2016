package ru.javajava.services;

import ru.javajava.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Администратор on 23.10.2016.
 */
public class AccountServiceDatabaseImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceDatabaseImpl.class.getName());

    @Override
    public UserProfile addUser(String login, String password, String email) {
        return null;
    }

    @Override
    public UserProfile getUserByLogin(String login) {
        return null;
    }

    @Override
    public UserProfile getUserById(Long id) {
        return null;
    }
}
