package ru.javajava.main;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.javajava.exceptions.AlreadyExistsException;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import javax.servlet.http.HttpSession;


@RestController
public class RegistrationController {
    private final AccountService accountService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class.getName());

    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }


    @RequestMapping(path = "/api/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody RequestUser request, HttpSession httpSession)
            throws AlreadyExistsException {
        final String login = request.getLogin();
        final String password = request.getPassword();
        final String email = request.getEmail();

        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            LOGGER.info("Registration failed (bad parametres)");
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.BAD_REQUEST, "bad parameters"));
        }


        final UserProfile existingUser = accountService.getUserByLogin(login);
        if (existingUser != null) {
            LOGGER.info("Registration failed because user {} already exists", login);
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.CONFLICT, "This login is already taken"));
        }

        final UserProfile newUser;
        try {
            newUser = accountService.addUser(login, password, email);
        }
        catch (DuplicateKeyException e) {
            LOGGER.info("Registration failed because user with email already exists", email);
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.CONFLICT, "This email is already taken"));
        }
        final String sessionId = httpSession.getId();
        httpSession.setAttribute(sessionId, newUser.getId());
        LOGGER.info("Creating new user \"{}\" is successful", login);
        return ResponseEntity.ok(new SuccessSignupResponse(login, email));
    }


    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody RequestUser request, HttpSession httpSession) {

        final String login = request.getLogin();
        final String password = request.getPassword();

        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            LOGGER.info("Authorization failed (bad parametres)");
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.BAD_REQUEST, "bad parameters"));
        }

        final UserProfile user = accountService.getUserByLogin(login);
        if (user == null) {
            LOGGER.info("Authorization failed because user {} does not exist", login);
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.NOT_FOUND, "User not found"));
        }

        if (user.getPassword().equals(password)) {
            accountService.incrementVisits(user.getId());
            user.incrementVisits();
            final String sessionId = httpSession.getId();
            httpSession.setAttribute(sessionId, user.getId());
            LOGGER.info("Authorization OK for user {}", login);
            return ResponseEntity.ok(new SuccessLoginResponse(login, user.getEmail(), user.getVisits()));
        }

        LOGGER.info("Authorization failed - incorrect password for user {}", login);
        return ResponseEntity.ok(new ErrorResponse(HttpStatus.UNAUTHORIZED, "incorrect password"));
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.POST)
    public HttpStatus logout(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        httpSession.removeAttribute(sessionId);
        LOGGER.info("Log out OK");
        return HttpStatus.OK;
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

    public static final class ErrorResponse {
        private final HttpStatus code;
        private final String reason;

        public ErrorResponse(HttpStatus code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        @SuppressWarnings("unused")
        public int getCode() {
            return code.value();
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
        public RequestUser() {

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
