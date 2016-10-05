package ru.javajava.main;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import javax.servlet.http.HttpSession;


@RestController
public class RegistrationController {

    private final AccountService accountService;

    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }


    @RequestMapping(path = "/api/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody RequestUser jsonString, HttpSession httpSession)
    {
        String login = jsonString.getLogin();
        final String password = jsonString.getPassword();
        final String email = jsonString.getEmail();

        if (StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "bad parameters"));
        }

        if (StringUtils.isEmpty(login)) {
            login = email;
        }


        final UserProfile existingUser = accountService.getUserByLogin(login);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "user already exists"));
        }

        final UserProfile newUser = accountService.addUser(login, password, email);
        final String sessionId = httpSession.getId();
        httpSession.setAttribute(sessionId, newUser);
        return ResponseEntity.ok(new SuccessSignupResponse(login, email));
    }





    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody RequestUser jsonString, HttpSession httpSession) {

        final String login = jsonString.getLogin();
        final String password = jsonString.getPassword();

        if(StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password) ) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "bad parameters"));
        }

        final UserProfile user = accountService.getUserByLogin(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
        }


        if(user.getPassword().equals(password)) {
            user.incrementAmount();
            final String sessionId = httpSession.getId();
            httpSession.setAttribute(sessionId, user);
            return ResponseEntity.ok(new SuccessLoginResponse(login, user.getEmail(), user.getAmount()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "incorrect password"));
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


    private static final class ErrorResponse {
        private final int code;
        private final String reason;

        private ErrorResponse(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        @SuppressWarnings("unused")
        public int getCode() {
            return code;
        }

        @SuppressWarnings("unused")
        public String getReason() {
            return reason;
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
