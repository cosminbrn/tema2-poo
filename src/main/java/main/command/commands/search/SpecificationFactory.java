package main.command.commands.search;

import main.command.commands.search.specifications.AvailableForAssignmentSpecification;
import main.command.commands.search.specifications.BusinessPrioritySpecification;
import main.command.commands.search.specifications.ConjunctionSpecification;
import main.command.commands.search.specifications.CreatedAtSpecification;
import main.command.commands.search.specifications.ExpertiseAreaSpecification;
import main.command.commands.search.specifications.KeywordsSpecification;
import main.command.commands.search.specifications.PerformanceScoreSpecification;
import main.command.commands.search.specifications.SenioritySpecification;
import main.command.commands.search.specifications.SpecificationFlag;
import main.command.commands.search.specifications.TicketTypeSpecification;
import main.fileio.FiltersInput;
import main.globals.Specification;
import main.globals.ticketenums.BusinessPriority;
import main.globals.ticketenums.TicketType;
import main.globals.userenums.ExpertiseArea;
import main.globals.userenums.Seniority;
import main.tickets.Ticket;
import main.users.Developer;
import main.users.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public final class SpecificationFactory {
    private SpecificationFactory() { }

    private static List<Specification<Ticket>> createTicketSpecifications(final FiltersInput
                                                                                 filters,
                                                                         final User user) {
        List<Specification<Ticket>> results = new ArrayList<>();
        BusinessPriority businessPriority =
                BusinessPriority.fromString(filters.getBusinessPriority());
        TicketType ticketType = TicketType.fromString(filters.getType());
        String createdAfter = filters.getCreatedAfter();
        String createdBefore = filters.getCreatedBefore();
        List<String> keywords = filters.getKeywords();
        boolean availableForAssignment = filters.isAvailableForAssignment();

        if (businessPriority != null) {
            results.add(new BusinessPrioritySpecification(businessPriority));
        }

        if (ticketType != null) {
            results.add(new TicketTypeSpecification(ticketType));
        }

        if (createdAfter != null && !createdAfter.isEmpty()) {
            LocalDate date = LocalDate.parse(createdAfter);
            results.add(new CreatedAtSpecification(date, SpecificationFlag.AFTER));
        }

        if (createdBefore != null && !createdBefore.isEmpty()) {
            LocalDate date = LocalDate.parse(createdBefore);
            results.add(new CreatedAtSpecification(date, SpecificationFlag.BEFORE));
        }

        if (availableForAssignment) {
            results.add(new AvailableForAssignmentSpecification((Developer) user));
        }

        if (keywords != null && !keywords.isEmpty()) {
            results.add(new KeywordsSpecification(keywords));
        }
        return results;
    }

    private static List<Specification<Developer>> createDeveloperSpecifications(final FiltersInput
                                                                                       filters) {
        List<Specification<Developer>> results = new ArrayList<>();
        ExpertiseArea expertiseArea = ExpertiseArea.fromString(filters.getExpertiseArea());
        Seniority seniority = Seniority.fromString(filters.getSeniority());
        double performanceScoreBelow = filters.getPerformanceScoreBelow();
        double performanceScoreAbove = filters.getPerformanceScoreAbove();

        if (expertiseArea != null) {
            results.add(new ExpertiseAreaSpecification(expertiseArea));
        }

        if (seniority != null) {
            results.add(new SenioritySpecification(seniority));
        }

        if (performanceScoreBelow != 0) {
            results.add(new PerformanceScoreSpecification(performanceScoreBelow,
                    SpecificationFlag.BELOW));
        }

        if (performanceScoreAbove != 0) {
            results.add(new PerformanceScoreSpecification(performanceScoreAbove,
                    SpecificationFlag.ABOVE));
        }
        return results;
    }

    /**
     * Creates a conjunction specification for tickets from filters and user.
     * @param filters filter input
     * @param user current user
     * @return conjunction specification or null if none
     */
    public static ConjunctionSpecification<Ticket> createTicketConjunctionSpecification(
            final FiltersInput filters,
            final User user) {
        List<Specification<Ticket>> specifications =
                createTicketSpecifications(filters, user);
        if (specifications.isEmpty()) {
            return null;
        }
        return new ConjunctionSpecification<>(specifications);
    }

    /**
     * Creates a conjunction specification for developers from filters.
     * @param filters filter input
     * @return conjunction specification or null if none
     */
    public static ConjunctionSpecification<Developer> createDeveloperConjunctionSpecification(
            final FiltersInput filters) {
        List<Specification<Developer>> specifications =
                createDeveloperSpecifications(filters);
        if (specifications.isEmpty()) {
            return null;
        }
        return new ConjunctionSpecification<>(specifications);
    }
}
