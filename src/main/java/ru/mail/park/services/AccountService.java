package ru.mail.park.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Solovyev on 17/09/16.
 * По поводу аннотации {@link Service}. В доках написано, что эта аннотация может по разному трактоваться разными командами.
 * Из эффектов же она:
 * This annotation serves as a specialization of {@link Component @Component},
 * allowing for implementation classes to be autodetected through classpath scanning.
 * Аннотация {@link Component @Component} помечает класс как Spring Bean("bean" - кофейное зерно, дословно), т.е. как класс,
 * Жизненным циклом которого управляет Spring {@see http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html}
 * Есть еще аннотация {@link Scope @Scope}, которая определяет когда будет создаваться новый экземпляр класса. По умолчанию
 * это Singletone, что нас вполне устраивает
 */
@Service
public class AccountService {
    private Map<String, UserProfile> userNameToUser = new HashMap<>();

    public UserProfile addUser(String login, String password, String email) {
        final UserProfile userProfile = new UserProfile(login, email, password);
        userNameToUser.put(login, userProfile);
        return userProfile;
    }

    public UserProfile getUser(String login) {
        return userNameToUser.get(login);
    }

}
