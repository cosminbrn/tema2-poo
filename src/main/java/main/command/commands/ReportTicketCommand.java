package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.engine.Engine;
import main.fileio.CommandInput;
import main.fileio.ParamsInput;
import main.globals.userenums.ExpertiseArea;
import main.globals.ticketenums.TicketType;
import main.globals.ticketenums.*;
import main.tickets.BugTicket;
import main.tickets.FeatureRequestTicket;
import main.tickets.Ticket;
import main.tickets.UIFeedbackTicket;
import main.users.User;

import static main.globals.ticketenums.TicketType.*;
import static main.globals.WorkflowStage.TESTING;
import static main.globals.ticketenums.BusinessPriority.LOW;
import static main.globals.ticketenums.Status.OPEN;

/**
 * Command to report new tickets during testing stage.
 */
public class ReportTicketCommand extends Command {

    /**
     * Execute the report ticket command and add a new ticket to the database.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        Database db = Database.getInstance();
        ParamsInput params = commandInput.getParams();
        TicketType type = TicketType.valueOf(params.getType());

        if (type != BUG && commandInput.getParams().getReportedBy().isEmpty()) {
            addErrorOutput(commandInput, output,
                    ErrorMessages.ANONYMOUS_REPORTING_ONLY_FOR_BUGS.getErrorMessage());
            return;
        }

        if (Engine.getCurrentStage() != TESTING) {
            addErrorOutput(commandInput, output,
                    ErrorMessages.REPORT_ONLY_DURING_TESTING.getErrorMessage());
            return;
        }

        User currentUser = db.getUserByUsername(commandInput.getUsername());
        if (currentUser == null) {
            addErrorOutput(commandInput, output,
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            commandInput.getUsername()));
            return;
        }

        if (type == BUG) {
            BusinessPriority bp = BusinessPriority.fromString(params.getBusinessPriority());
            if (params.getReportedBy().isEmpty()) {
                bp = LOW;
            }
            Ticket ticket = new BugTicket.BugBuilder()
                    .setId(db.getNextTicketId())
                    .setType(BUG)
                    .setTitle(params.getTitle())
                    .setBusinessPriority(bp)
                    .setStatus(OPEN)
                    .setCreatedAt(commandInput.getTimestamp())
                    .setExpertiseArea(ExpertiseArea.fromString(params.getExpertiseArea()))
                    .setDescription(params.getDescription())
                    .setReportedBy(params.getReportedBy())
                    .setExpectedBehavior(params.getExpectedBehavior())
                    .setActualBehavior(params.getActualBehavior())
                    .setFrequency(Frequency.fromString(params.getFrequency()))
                    .setSeverity(Severity.fromString(params.getSeverity()))
                    .setEnvironment(params.getEnvironment())
                    .setErrorCode(params.getErrorCode())
                    .build();
            db.addTicket(ticket);
        } else if (type == UI_FEEDBACK) {
            Ticket ticket = new UIFeedbackTicket.UIFeedbackTicketBuilder()
                    .setId(db.getNextTicketId())
                    .setType(UI_FEEDBACK)
                    .setTitle(params.getTitle())
                    .setBusinessPriority(BusinessPriority.fromString(params.getBusinessPriority()))
                    .setStatus(OPEN)
                    .setExpertiseArea(ExpertiseArea.fromString(params.getExpertiseArea()))
                    .setDescription(params.getDescription())
                    .setReportedBy(params.getReportedBy())
                    .setCreatedAt(commandInput.getTimestamp())
                    .setUiElementId(params.getUiElementId())
                    .setBusinessValue(BusinessValue.fromString(params.getBusinessValue()))
                    .setUsabilityScore(params.getUsabilityScore())
                    .setScreenshotUrl(params.getScreenshotUrl())
                    .setSuggestedFix(params.getSuggestedFix())
                    .build();
            db.addTicket(ticket);
        } else if (type == FEATURE_REQUEST) {
            Ticket ticket = new FeatureRequestTicket.FeatureRequestBuilder()
                    .setId(db.getNextTicketId())
                    .setType(FEATURE_REQUEST)
                    .setTitle(params.getTitle())
                    .setBusinessPriority(BusinessPriority.fromString(params.getBusinessPriority()))
                    .setStatus(OPEN)
                    .setExpertiseArea(ExpertiseArea.fromString(params.getExpertiseArea()))
                    .setDescription(params.getDescription())
                    .setReportedBy(params.getReportedBy())
                    .setCreatedAt(commandInput.getTimestamp())
                    .setBusinessValue(BusinessValue.fromString(params.getBusinessValue()))
                    .setCustomerDemand(CustomerDemand.fromString(params.getCustomerDemand()))
                    .build();
            db.addTicket(ticket);
        } else {
            throw new IllegalArgumentException("Unsupported ticket type: " + type);
        }
    }
}
