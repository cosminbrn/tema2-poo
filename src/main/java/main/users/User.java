package main.users;

import lombok.Getter;
import main.users.enums.Role;

public abstract class User {
    @Getter
    private final String username;
    @Getter
    private final String mail;
    @Getter
    private final Role role;

    protected User(final String username, final String mail, final Role role) {
        this.username = username;
        this.mail = mail;
        this.role = role;
    }
}
