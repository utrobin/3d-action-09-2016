package ru.javajava.model;


import java.util.concurrent.atomic.AtomicLong;

public class UserProfile {
    private final String login;
    private final String email;
    private final String password;
    private int amount = 1;

    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);
    private final long id;



    public UserProfile(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = ID_GENETATOR.getAndIncrement();
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

    public void incrementAmount() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }

    public long getId() {
        return id;
    }
}
