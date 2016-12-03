package ru.javajava.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Администратор on 20.11.2016.
 */
public class BestUsersResponse implements Serializable {
    @JsonProperty("pages")
    private int pages;

    @JsonProperty("users")
    private List<User> users;

    public BestUsersResponse(int pages, List<User> users) {
        this.pages = pages;
        this.users = users;
    }

    public static class User {
        @JsonProperty("id")
        private long number;
        @JsonProperty("login")
        private String login;
        @JsonProperty("rating")
        private int rating;

        public User(String login, int rating, long number) {
            this.login = login;
            this.rating = rating;
            this.number = number;
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
