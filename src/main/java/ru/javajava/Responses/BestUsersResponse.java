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

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
