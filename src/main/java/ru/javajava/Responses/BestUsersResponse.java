package ru.javajava.Responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Администратор on 20.11.2016.
 */
public class BestUsersResponse implements Serializable {
    @JsonProperty("page")
    private int page;

    @JsonProperty("users")
    private List<User> users;

    public BestUsersResponse(int page, List<User> users) {
        this.page = page;
        this.users = users;
    }

    public static class User {
        @JsonProperty("id")
        private long number;
        @JsonProperty("login")
        private String login;
        @JsonProperty("rating")
        private int rating;
        @JsonIgnore
        private static final AtomicLong ID_GENERATOR = new AtomicLong();

        public User(String login, int rating) {
            this.login = login;
            this.rating = rating;
            number = ID_GENERATOR.getAndIncrement();
        }

        public long getNumber() {
            return number;
        }

        public String getLogin() {
            return login;
        }

        public int getRating() {
            return rating;
        }
    }
}
