package main.users;

import main.fileio.UserInput;
import main.globals.userenums.ExpertiseArea;
import main.globals.userenums.Role;
import main.globals.userenums.Seniority;

/**
 * Factory class for creating User instances based on UserInput data.
 */
public final class UserFactory {

    /**
     * Private constructor to prevent instantiation of the factory class.
     */
    private UserFactory() {

    }

    /**
     * Creates a User instance based on the provided UserInput.
     * @param userInput The input data for creating the user.
     * @return A User instance corresponding to the input data.
     */
    public static User createUser(final UserInput userInput) {
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
