package main.users;

import static main.users.enums.Role.MANAGER;

/**
 * Class representing a manager user.
 */
public final class Manager extends User {
    private final String hireDate;
    private final String[] subordinates;

    public Manager(final String name, final String email,
                   final String hireDate, final String[] subordinates) {
        super(name, email, MANAGER);
        this.hireDate = hireDate;
        this.subordinates = subordinates;
    }
}
