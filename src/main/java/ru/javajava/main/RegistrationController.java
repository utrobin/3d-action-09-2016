package ru.javajava.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

@RestController
public class RegistrationController {

    private final AccountService accountService;

    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }


    @RequestMapping(path = "/api/signup", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestParam(name = "login") String login,
                                 @RequestParam (name = "password") String password,
                                 @RequestParam (name = "email") String email)
    {
//        final String login = body.getLogin();
//        final String password = body.getPassword();
//        final String email = body.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1, "Bad parameters"));
        }
        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(2, "User already exists"));
        }

        final UserProfile user = accountService.addUser(login, password, email);
        return ResponseEntity.ok(new SuccessResponse(login, email, user.getAmount()));
    }





    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity signIn(@RequestParam(name = "login") String login,
                                @RequestParam(name = "password") String password) {

        if(StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password) ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1, "Bad parameters"));
        }
        final UserProfile user = accountService.getUser(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1, "User doesn't exist"));
        }
        if(user.getPassword().equals(password)) {
            user.increment();
            return ResponseEntity.ok(new SuccessResponse(user.getLogin(), user.getEmail(), user.getAmount()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(3, "Incorrect password"));
    }






    private static final class SuccessResponse {
        private final String login;
        private final String email;
        private final int amount;


        private SuccessResponse(String login, String email, int amount) {
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

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }
    }


}
