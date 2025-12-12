package main.command.commands.search;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.view.tickets.viewticketsstrategy.DeveloperTicketViewStrategy;
import main.command.commands.view.tickets.viewticketsstrategy.ManagerTicketViewStrategy;
import main.command.commands.view.tickets.viewticketsstrategy.TicketFilteringStrategy;
import main.fileio.FiltersInput;
import main.globals.Specification;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.userenums.Role;
import main.tickets.Ticket;
import main.users.Developer;
import main.users.Manager;
import main.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.App.MAPPER;
import static main.globals.commandenums.CommandType.SEARCH;

public class SearchCommand extends Command {
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();

        User currentUser = db.getUserByUsername(commandInput.getUsername());
        if (currentUser == null) {
            addErrorOutput(commandInput, output,
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            commandInput.getUsername()));
            return;
        }

        if (currentUser.getRole() == Role.DEVELOPER) {
            Developer dev = (Developer) currentUser;
            FiltersInput filters = commandInput.getFilters();
            List<Ticket> result = new ArrayList<>();

            if (!filters.getSearchType().equals("TICKET")) {
                throw new IllegalArgumentException("Search type must be TICKET for DEVELOPER");
            }
            Specification<Ticket> spec = SpecificationFactory.createTicketConjunctionSpecification(filters, dev);
            if (spec == null) {
                result = callViewTickets(dev);
                addOutput(commandInput, output, getViewTicketNode(result, filters.getKeywords()));
                return;
            }


            List<Ticket> allTickets = dev.getOpenTicketsFromAssignedMilestones();

            for (Ticket ticket : allTickets) {
                if (spec.isSatisfiedBy(ticket)) {
                    result.add(ticket);
                }
            }
            addOutput(commandInput, output, getViewTicketNode(result, filters.getKeywords()));
        } else if (currentUser.getRole() == Role.MANAGER) {
            Manager manager = (Manager) currentUser;
            FiltersInput filters = commandInput.getFilters();

            if (filters.getSearchType().equals("TICKET")) {
                List<Ticket> result = new ArrayList<>();
                Specification<Ticket> spec = SpecificationFactory.createTicketConjunctionSpecification(filters, manager);
                if (spec == null) {
                    result = callViewTickets(manager);
                    addOutput(commandInput, output, getTicketNode(result, filters.getKeywords()));
                    return;
                }
                List<Ticket> allTickets = db.getTickets();

                for (Ticket ticket : allTickets) {
                    if (spec.isSatisfiedBy(ticket)) {
                        result.add(ticket);
                    }
                }
                addOutput(commandInput, output, getTicketNode(result, filters.getKeywords()));
            } else if (filters.getSearchType().equals("DEVELOPER")) {
                List<Developer> result = new ArrayList<>();
                Specification<Developer> spec = SpecificationFactory.createDeveloperConjunctionSpecification(filters);
                if (spec == null) {
                    result = manager.getSubordinateDevelopers();
                    addOutput(commandInput, output, getDeveloperNode(result));
                    return;
                }
                List<Developer> allDevs = manager.getSubordinateDevelopers();

                for (Developer dev : allDevs) {
                    if (spec.isSatisfiedBy(dev)) {
                        result.add(dev);
                    }
                }
                addOutput(commandInput, output, getDeveloperNode(result));
            }
        } else {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    public void addOutput(final CommandInput input,
                          final ArrayNode output,
                          final ArrayNode results) {
        node.put("command", SEARCH.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.put("searchType", input.getFilters().getSearchType());
        node.set("results", results);

        output.add(node);
    }

    private ArrayNode getTicketNode(List<Ticket> tickets, List<String> keywords) {

        tickets.sort(Comparator.comparing(Ticket::getCreatedAt).thenComparingInt(Ticket::getId));
        ArrayNode results = MAPPER.createArrayNode();

        for (Ticket ticket : tickets) {
            List<String> matchingWords = getStrings(keywords, ticket);


            ObjectNode ticketNode = MAPPER.createObjectNode();
            ticketNode.put("id", ticket.getId());
            ticketNode.put("type", ticket.getType().getTypeName());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("businessPriority", ticket.getBusinessPriority().getLabel());
            ticketNode.put("status", ticket.getStatus().getStatusName());
            ticketNode.put("createdAt", ticket.getCreatedAt());
            ticketNode.put("solvedAt", ticket.getSolvedAt());
            ticketNode.put("reportedBy", ticket.getReportedBy());
            ticketNode.set("matchingWords", MAPPER.valueToTree(matchingWords));
            results.add(ticketNode);
        }
        return results;
    }

    private ArrayNode getViewTicketNode(List<Ticket> tickets, List<String> keywords) {

        tickets.sort(Comparator.comparing(Ticket::getCreatedAt).thenComparingInt(Ticket::getId));
        ArrayNode results = MAPPER.createArrayNode();

        for (Ticket ticket : tickets) {
            List<String> matchingWords = getStrings(keywords, ticket);


            ObjectNode ticketNode = MAPPER.createObjectNode();
            ticketNode.put("id", ticket.getId());
            ticketNode.put("type", ticket.getType().getTypeName());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("businessPriority", ticket.getBusinessPriority().getLabel());
            ticketNode.put("status", ticket.getStatus().getStatusName());
            ticketNode.put("createdAt", ticket.getCreatedAt());
            ticketNode.put("solvedAt", ticket.getSolvedAt());
            ticketNode.put("reportedBy", ticket.getReportedBy());
            results.add(ticketNode);
        }
        return results;
    }

    private static List<String> getStrings(List<String> keywords, Ticket ticket) {
        List<String> matchingWords = new ArrayList<>();
        String content = ticket.getTitle().toLowerCase();
        if (ticket.getDescription() != null) {
            content = content + ticket.getDescription().toLowerCase();
        }

        if (keywords != null && !keywords.isEmpty()) {
            for (String keyword : keywords) {
                if (content.contains(keyword.toLowerCase())) {
                    matchingWords.addAll(findFullWords(content, keyword));
                }
            }
        }
        return matchingWords;
    }

    private ArrayNode getDeveloperNode(List<Developer> developers) {
        ArrayNode results = MAPPER.createArrayNode();

        developers.sort(Comparator.comparing(Developer::getUsername));
        for (Developer dev : developers) {
            ObjectNode devNode = MAPPER.createObjectNode();
            devNode.put("username", dev.getUsername());
            devNode.put("expertiseArea", dev.getExpertiseArea().getName());
            devNode.put("seniority", dev.getSeniority().getName());
            devNode.put("performanceScore", dev.getPerformanceScore());
            devNode.put("hireDate", dev.getHireDate());

            results.add(devNode);
        }
        return results;
    }

    private List<Ticket> callViewTickets(User user) {
        if (user.getRole() == Role.DEVELOPER) {
            TicketFilteringStrategy strategy = new DeveloperTicketViewStrategy();
            return strategy.getTickets(user);
        } else {
            TicketFilteringStrategy strategy = new ManagerTicketViewStrategy();
            return strategy.getTickets(user);
        }
    }

    private static List<String> findFullWords(String text, String search) {
        List<String> matches = new ArrayList<>();

        String regex = "\\b\\w*" + Pattern.quote(search) + "\\w*\\b";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }
}
