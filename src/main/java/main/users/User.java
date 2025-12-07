package main.users;

import main.users.enums.Role;

public abstract class User {
    private final String username;
    private final String mail;
    private final Role role;

    protected User(final String username, final String mail, final Role role) {
        this.username = username;
        this.mail = mail;
        this.role = role;
    }
}
