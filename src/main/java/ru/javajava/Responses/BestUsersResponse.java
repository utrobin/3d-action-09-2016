package ru.javajava.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

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
        @JsonProperty("login")
        public String login;
        @JsonProperty("rating")
        public int rating;
    }
}
