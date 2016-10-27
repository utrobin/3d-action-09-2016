package ru.javajava.main;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javajava.services.AccountService;

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
