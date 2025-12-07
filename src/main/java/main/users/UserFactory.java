package main.users;

import main.fileio.UserInput;
import main.globals.ExpertiseArea;
import main.users.enums.Role;
import main.users.enums.Seniority;

/**
 * Factory class for creating User instances based on UserInput data.
 */
public final class UserFactory {

    private UserFactory() {

    }

    public static User createUser(UserInput userInput) {
        String username = userInput.getUsername();
        String email = userInput.getEmail();
        Role role = Role.getRoleByName(userInput.getRole());
        assert role != null;
        return switch (role) {
            case MANAGER -> new Manager(username, email, userInput.getHireDate(),
                    userInput.getSubordinates());
            case DEVELOPER -> new Developer(username, email, userInput.getHireDate(),
                    ExpertiseArea.fromString(userInput.getExpertiseArea()),
                    Seniority.getSeniorityByName(userInput.getSeniority()));
            default -> new Reporter(username, email);
        };
    }
}
