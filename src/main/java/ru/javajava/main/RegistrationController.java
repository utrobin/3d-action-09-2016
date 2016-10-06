package ru.javajava.main;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javajava.exceptions.CustomException;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountServiceImpl;

import javax.servlet.http.HttpSession;


@RestController
@SuppressWarnings("unused")
public class RegistrationController {

    private final AccountServiceImpl accountService;

    @Autowired
    public RegistrationController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }


    @RequestMapping(path = "/api/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody RequestUser jsonString, HttpSession httpSession) throws CustomException
    {
        final String login = jsonString.getLogin();
        final String password = jsonString.getPassword();
        final String email = jsonString.getEmail();

        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "bad parameters");
        }


        final UserProfile existingUser = accountService.getUserByLogin(login);
        if (existingUser != null) {
            throw new CustomException(HttpStatus.CONFLICT, "user already exists");
        }

        final UserProfile newUser = accountService.addUser(login, password, email);
        final String sessionId = httpSession.getId();
        httpSession.setAttribute(sessionId, newUser.getId());
        return ResponseEntity.ok(new SuccessSignupResponse(login, email));
    }





    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody RequestUser jsonString, HttpSession httpSession) throws CustomException {

        final String login = jsonString.getLogin();
        final String password = jsonString.getPassword();

        if(StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password) ) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "bad parameters");
        }

        final UserProfile user = accountService.getUserByLogin(login);
        if (user == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "user not found");
        }

        if(user.getPassword().equals(password)) {
            user.incrementAmount();
            final String sessionId = httpSession.getId();
            httpSession.setAttribute(sessionId, user.getId());
            return ResponseEntity.ok(new SuccessLoginResponse(login, user.getEmail(), user.getAmount()));
        }

        throw new CustomException(HttpStatus.UNAUTHORIZED, "incorrect password");
    }



    private static final class SuccessSignupResponse {
        private final String login;
        private final String email;

        private SuccessSignupResponse(String login, String email) {
            this.login = login;
            this.email = email;
        }

        @SuppressWarnings("unused")
        public String getLogin() {
            return login;
        }
        @SuppressWarnings("unused")
        public String getEmail() {
            return email;
        }
    }

    private static final class SuccessLoginResponse {
        private final String login;
        private final String email;
        private final int amount;

        private SuccessLoginResponse(String login, String email, int amount) {
            this.login = login;
            this.email = email;
            this.amount = amount;
        }

        @SuppressWarnings("unused")
        public String getLogin() {
            return login;
        }
        @SuppressWarnings("unused")
        public String getEmail() {
            return email;
        }
        @SuppressWarnings("unused")
        public int getAmount() {
            return amount;
        }
    }



    public static class RequestUser {
        private String login;
        private String password;
        private String email;

        @SuppressWarnings("unused")
        public RequestUser () {

        }
        @SuppressWarnings("unused")
        public RequestUser(String login, String password, String email) {
            this.login = login;
            this.password = password;
            this.email = email;
        }

        public String getLogin() {
            return login;
        }
        public String getPassword() {
            return password;
        }
        public String getEmail() {
            return email;
        }
    }
}
