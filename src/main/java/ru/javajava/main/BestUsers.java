package ru.javajava.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by ivan on 24.10.16.
 */
@RestController
public class BestUsers {
        private final AccountService accountService;

        public BestUsers(AccountService accountService) {
            this.accountService = accountService;
        }

        @RequestMapping(path = "/api/best", method = RequestMethod.GET)
        public ResponseEntity rating (@RequestParam(value = "page", required = false) Integer page) {
            if (page == null) {
                page = 1;
            }
            return ResponseEntity.ok(accountService.getBestUsers(page));
        }
}
