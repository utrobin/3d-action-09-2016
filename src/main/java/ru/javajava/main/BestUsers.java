package ru.javajava.main;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javajava.DAO.UserDAO;
import ru.javajava.Responses.BestUsersResponse;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ivan on 24.10.16.
 */
@RestController
public class BestUsers {
        private final AccountService accountService;
        private static final int LIMIT = 7;

        public BestUsers(AccountService accountService) {
            this.accountService = accountService;
        }

        @ResponseBody
        @RequestMapping(path = "/api/best", method = RequestMethod.GET)
        public ResponseEntity rating (@RequestParam(value = "page", required = false) Integer page) {
            if (page == null) {
                page = 1;
            }

            final UserDAO.ResultBean result = accountService.getBestUsers(page, LIMIT);
            final List<UserProfile> users = result.users;

            List<BestUsersResponse.User> resultUsers = new ArrayList<>();
            final AtomicLong ID_GENERATOR = new AtomicLong(LIMIT*(page-1)+1);
            for (UserProfile user: users) {
               final String login = user.getLogin();
                final int rating = user.getRating();
                final long id = ID_GENERATOR.getAndIncrement();
                final BestUsersResponse.User newUser = new BestUsersResponse.User(login, rating, id);
                resultUsers.add(newUser);
            }

            return ResponseEntity.ok(new BestUsersResponse(result.numPages, resultUsers));
        }
}
