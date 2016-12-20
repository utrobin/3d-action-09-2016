package ru.javajava.Responses;

import java.util.List;

/**
 * Created by Администратор on 20.11.2016.
 */
public class BestUsersResponse {
    private int pages;
    private List<User> users;

    public BestUsersResponse(int pages, List<User> users) {
        this.pages = pages;
        this.users = users;
    }

    public static class User {
        private long id;
        private String login;
        private int rating;

        public User(String login, int rating, long id) {
            this.login = login;
            this.rating = rating;
            this.id = id;
        }

        public long getNumber() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public int getRating() {
            return rating;
        }
    }
}
