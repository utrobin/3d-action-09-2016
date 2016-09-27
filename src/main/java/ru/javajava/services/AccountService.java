package ru.javajava.services;

import org.springframework.stereotype.Service;
import ru.javajava.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private final Map<String, UserProfile> loginToProfile = new HashMap<>();
    private final Map<String, UserProfile> sessionIdToProfile = new HashMap<>();

    public UserProfile addUser(String login, String password, String email) {
        final UserProfile userProfile = new UserProfile(login, email, password);
        loginToProfile.put(login, userProfile);
        return userProfile;
    }

    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public UserProfile getUserBySessionId (String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }

}
