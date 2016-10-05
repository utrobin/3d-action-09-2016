package ru.javajava.services;

import org.springframework.stereotype.Service;
import ru.javajava.model.UserProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class AccountServiceImpl implements AccountService {
    private final Map<String, UserProfile> loginToProfile = new ConcurrentHashMap<>();
    private final Map<Long, UserProfile> IdToProfile = new ConcurrentHashMap<>();

    public UserProfile addUser(String login, String password, String email) {
        final UserProfile userProfile = new UserProfile(login, email, password);
        loginToProfile.put(login, userProfile);
        IdToProfile.put(userProfile.getId(), userProfile);
        return userProfile;
    }

    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public UserProfile getUserById(Long Id) {
        return IdToProfile.get(Id);
    }
}
