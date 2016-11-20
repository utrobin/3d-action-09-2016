package ru.javajava.main;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javajava.DAO.UserDAO;
import ru.javajava.Responses.BestUsersResponse;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 24.10.16.
 */
@RestController
public class BestUsers {
        private final AccountService accountService;
        private static final int limit = 7;

        public BestUsers(AccountService accountService) {
            this.accountService = accountService;
        }

        @RequestMapping(path = "/api/best", method = RequestMethod.GET)
        public ResponseEntity rating (@RequestParam(value = "page", required = false) Integer page) {
            if (page == null) {
                page = 1;
            }

            UserDAO.ResultBean result = accountService.getBestUsers(page, limit);
            List<UserProfile> users = result.users;

            List<BestUsersResponse.User> resultUsers = new ArrayList<>();
            for (UserProfile user: users) {
                String login = user.getLogin();
                int rating = user.getRating();
                BestUsersResponse.User newUser = new BestUsersResponse.User(login, rating);
                resultUsers.add(newUser);
            }

            return ResponseEntity.ok(new BestUsersResponse(result.numPages, resultUsers));
        }
}
