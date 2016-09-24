package ru.javajava.model;

public class UserProfile {
    private final String login;
    private final String email;
    private final String password;
    private int amount = 1;

    public UserProfile(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void increment() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }
}
