package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.engine.Engine;
import main.fileio.CommandInput;
import main.fileio.ParamsInput;
import main.globals.ExpertiseArea;
import main.globals.TicketType;
import main.tickets.BugTicket;
import main.tickets.FeatureRequestTicket;
import main.tickets.Ticket;
import main.tickets.UIFeedbackTicket;
import main.tickets.enums.*;

import static main.globals.TicketType.*;
import static main.tickets.enums.Status.OPEN;

public class ReportTicketCommand extends Command {
    public ReportTicketCommand(CommandInput commandInput, ArrayNode output) {
        super(commandInput, output);
    }

    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();
        Engine engine = Engine.getInstance();
        ParamsInput params = commandInput.getParams();
        TicketType type = TicketType.valueOf(params.getType());

        if (type == BUG && commandInput.getParams().getReportedBy().isEmpty()) {
            addErrorOutput(commandInput, output, ErrorMessages.ANONYMOUS_REPORTING_ONLY_FOR_BUGS);
        }

        if (type == BUG) {
            Ticket ticket = new BugTicket.BugBuilder()
                    .setId(db.getNextTicketID())
                    .setType(BUG)
                    .setTitle(params.getTitle())
                    .setBusinessPriority(BusinessPriority.fromString(params.getBusinessPriority()))
                    .setStatus(OPEN)
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
                    .setId(db.getNextTicketID())
                    .setType(UI_FEEDBACK)
                    .setTitle(params.getTitle())
                    .setBusinessPriority(BusinessPriority.fromString(params.getBusinessPriority()))
                    .setStatus(OPEN)
                    .setExpertiseArea(ExpertiseArea.fromString(params.getExpertiseArea()))
                    .setDescription(params.getDescription())
                    .setReportedBy(params.getReportedBy())
                    .setUiElementId(params.getUiElementId())
                    .setBusinessValue(BusinessValue.fromString(params.getBusinessValue()))
                    .setUsabilityScore(params.getUsabilityScore())
                    .setScreenshotUrl(params.getScreenshotUrl())
                    .setSuggestedFix(params.getSuggestedFix())
                    .build();
            db.addTicket(ticket);
        } else if (type == FEATURE_REQUEST) {
            Ticket ticket = new FeatureRequestTicket.FeatureRequestBuilder()
                    .setTitle(params.getTitle())
                    .setBusinessPriority(BusinessPriority.fromString(params.getBusinessPriority()))
                    .setStatus(OPEN)
                    .setExpertiseArea(ExpertiseArea.fromString(params.getExpertiseArea()))
                    .setDescription(params.getDescription())
                    .setReportedBy(params.getReportedBy())
                    .setBusinessValue(BusinessValue.fromString(params.getBusinessValue()))
                    .setCustomerDemand(CustomerDemand.fromString(params.getCustomerDemand()))
                    .build();
            db.addTicket(ticket);
        } else {
            throw new IllegalArgumentException("Unsupported ticket type: " + type);
        }
    }
}
