package main.users;

import lombok.Getter;
import main.tickets.Ticket;
import main.globals.userenums.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a user.
 */
public abstract class User {
    @Getter
    private final String username;
    @Getter
    private final String mail;
    @Getter
    private final Role role;

    private final List<Ticket> commentedTickets = new ArrayList<>();

    protected User(final String username, final String mail, final Role role) {
        this.username = username;
        this.mail = mail;
        this.role = role;
    }

    /**
     * Adds a ticket to the commented tickets list.
     * @param ticket the ticket to add
     */
    public final void addCommentedTicket(final Ticket ticket) {
        this.commentedTickets.add(ticket);
    }

    /**
     * Removes the last ticket from the commented tickets list.
     * If the list is empty, no action is taken.
     */
    public final void removeLastCommentedTicket() {
        if (!this.commentedTickets.isEmpty()) {
            this.commentedTickets.removeLast();
        }
    }
}
