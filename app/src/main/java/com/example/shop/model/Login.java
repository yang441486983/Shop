package com.example.shop.model;

/**
 * Created by 杨超 on 2017/9/12.
 */

public class Login {
    private String login;
    private String JSESSIONID;

    public Login(String login, String JSESSIONID) {
        this.login = login;
        this.JSESSIONID = JSESSIONID;
    }

    @Override
    public String toString() {
        return "Login{" +
                "login='" + login + '\'' +
                ", JSESSIONID='" + JSESSIONID + '\'' +
                '}';
    }

    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String JSESSIONID) {
        this.JSESSIONID = JSESSIONID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
