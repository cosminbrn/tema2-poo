package main.command.commands.search.specifications;

import main.globals.Specification;
import main.tickets.Ticket;

import java.time.LocalDate;

/**
 * Specification for filtering tickets by creation date.
 */
public class CreatedAtSpecification implements Specification<Ticket> {
    private final LocalDate referenceDate;
    private final SpecificationFlag referenceFlag;

    public CreatedAtSpecification(final LocalDate referenceDate,
                                  final SpecificationFlag flag) {
        this.referenceDate = referenceDate;
        this.referenceFlag = flag;
    }

    @Override
    public boolean isSatisfiedBy(final Ticket ticket) {
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        if (referenceFlag == SpecificationFlag.BEFORE) {
            return ticketDate.isBefore(referenceDate);
        } else if (referenceFlag == SpecificationFlag.AFTER) {
            return ticketDate.isAfter(referenceDate);
        } else if (referenceFlag == SpecificationFlag.AT) {
            return ticketDate.isEqual(referenceDate);
        }
        return false;
    }
}
