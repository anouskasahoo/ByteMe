package com.iiitd.byteme;

import static com.iiitd.byteme.Main.userMap;

public class User {
    protected String email;
    protected String password;
    protected String name;

    public User () {
        this.email = "";
        this.password = "";
        this.name="";
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    static User findUser(String email) {
        return userMap.get(email);
    }
}
