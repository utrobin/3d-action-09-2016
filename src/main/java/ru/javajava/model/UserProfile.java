package ru.javajava.model;


import java.util.concurrent.atomic.AtomicInteger;

public class UserProfile {
    private final String login;
    private final String email;
    private String password;
    private int visits;
    private int rating;
    private long id;

    private final AtomicInteger visitsGenerator;


    public UserProfile(String login, String password, String email) {
        this(login, password, email, 1);
    }

    public UserProfile(String login, String password, String email, int visits) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.visits = visits;
        visitsGenerator = new AtomicInteger(visits + 1);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public void incrementVisits() {
        visits = visitsGenerator.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
