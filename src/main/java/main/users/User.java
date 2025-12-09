package main.users;

import lombok.Getter;
import main.tickets.Ticket;
import main.users.enums.Role;

import java.util.ArrayList;
import java.util.List;

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

    public void addCommentedTicket(final Ticket ticket) {
        this.commentedTickets.add(ticket);
    }

    public void removeLastCommentedTicket() {
        if (!this.commentedTickets.isEmpty()) {
            this.commentedTickets.removeLast();
        }
    }
}
