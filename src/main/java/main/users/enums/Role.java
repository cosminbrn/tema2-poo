package main.users.enums;

import lombok.Getter;

public enum Role {
    REPORTER("Reporter"),
    DEVELOPER("Developer"),
    MANAGER("Manager");

    @Getter
    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public static Role getRoleByName(String name) {
        for (Role role : Role.values()) {
            if (role.getRoleName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }
}
