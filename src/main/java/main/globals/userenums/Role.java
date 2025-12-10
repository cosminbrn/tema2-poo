package main.globals.userenums;

import lombok.Getter;

public enum Role {
    REPORTER("Reporter"),
    DEVELOPER("Developer"),
    MANAGER("Manager");

    @Getter
    private final String roleName;

    Role(final String roleName) {
        this.roleName = roleName;
    }

    /**
     * Get Role by its name
     * @param name the name of the role
     * @return the Role enum value, or null if not found
     */
    public static Role getRoleByName(final String name) {
        for (Role role : Role.values()) {
            if (role.getRoleName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }
}
