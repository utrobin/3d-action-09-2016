package ru.javajava.services;

import ru.javajava.model.UserProfile;

/**
 * Created by ivan on 05.10.16.
 */
public interface AccountService {
    public UserProfile addUser(String login, String password, String email);
    public UserProfile getUserByLogin(String login);
    public UserProfile getUserById(Long Id);
}
