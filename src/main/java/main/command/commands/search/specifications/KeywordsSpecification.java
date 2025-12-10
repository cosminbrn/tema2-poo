package main.command.commands.search.specifications;

import main.globals.Specification;
import main.tickets.Ticket;

import java.util.List;

public class KeywordsSpecification implements Specification<Ticket> {
    private final List<String> keywords;

    public KeywordsSpecification(final List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean isSatisfiedBy(Ticket ticket) {
        String content = ticket.getTitle().toLowerCase() + ticket.getDescription().toLowerCase();
        for (String keywords : keywords) {
            if (content.contains(keywords.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
