package ru.javajava.mechanics.base;

/**
 * Created by ivan on 22.12.16.
 */
public class VictimModel {
    private long id;
    private String login;

    public VictimModel() {

    }

    public VictimModel(long id, String login) {
        this.id = id;
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
