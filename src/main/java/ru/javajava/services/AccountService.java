package ru.javajava.services;

import org.springframework.stereotype.Service;
import ru.javajava.model.UserProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
    private final Map<String, UserProfile> loginToProfile = new ConcurrentHashMap<>();

    public UserProfile addUser(String login, String password, String email) {
        final UserProfile userProfile = new UserProfile(login, email, password);
        loginToProfile.put(login, userProfile);
        return userProfile;
    }

    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }
}
