package main.users;

import static main.users.enums.Role.REPORTER;

/**
 * Class representing a Reporter user.
 */
public final class Reporter extends User {
    public Reporter(final String username, final String email) {
        super(username, email, REPORTER);
    }
}
