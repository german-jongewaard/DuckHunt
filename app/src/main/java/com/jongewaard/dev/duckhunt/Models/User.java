package com.jongewaard.dev.duckhunt.Models;

public class User {
    private String nick;
    private int duck;

    public User() {
    }

    public User(String nick, int duck) {
        this.nick = nick;
        this.duck = duck;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getDuck() {
        return duck;
    }

    public void setDuck(int duck) {
        this.duck = duck;
    }
}
