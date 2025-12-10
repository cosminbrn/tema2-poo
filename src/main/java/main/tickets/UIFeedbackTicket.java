package main.tickets;

import lombok.Getter;
import main.tickets.actions.Action;
import main.tickets.enums.BusinessValue;

public class UIFeedbackTicket extends Ticket {
    private final String uiElementId;
    @Getter
    private final BusinessValue businessValue;
    @Getter
    private final int usabilityScore;

    // Optional fields
    private final String screenshotUrl;
    private final String suggestedFix;

    protected UIFeedbackTicket(UIFeedbackTicketBuilder builder) {
        super(builder);
        this.uiElementId = builder.uiElementId;
        this.businessValue = builder.businessValue;
        this.usabilityScore = builder.usabilityScore;
        this.screenshotUrl = builder.screenshotUrl;
        this.suggestedFix = builder.suggestedFix;
    }

    public static class UIFeedbackTicketBuilder extends Ticket.Builder<UIFeedbackTicketBuilder> {
        private String uiElementId;
        private BusinessValue businessValue;
        private int usabilityScore;

        // Optional fields
        private String screenshotUrl = "";
        private String suggestedFix = "";

        public UIFeedbackTicketBuilder setUiElementId(String uiElementId) {
            this.uiElementId = uiElementId;
            return this;
        }

        public UIFeedbackTicketBuilder setBusinessValue(BusinessValue businessValue) {
            this.businessValue = businessValue;
            return this;
        }

        public UIFeedbackTicketBuilder setUsabilityScore(int usabilityScore) {
            this.usabilityScore = usabilityScore;
            return this;
        }

        public UIFeedbackTicketBuilder setScreenshotUrl(String screenshotUrl) {
            this.screenshotUrl = screenshotUrl;
            return this;
        }

        public UIFeedbackTicketBuilder setSuggestedFix(String suggestedFix) {
            this.suggestedFix = suggestedFix;
            return this;
        }

        @Override
        protected UIFeedbackTicketBuilder self() {
            return this;
        }

        @Override
        public UIFeedbackTicket build() {
            return new UIFeedbackTicket(this);
        }
    }

    @Override
    public Ticket deepCopy() {
        UIFeedbackTicketBuilder b = new UIFeedbackTicketBuilder();
        UIFeedbackTicket copy = b.setId(getId()).setType(getType())
                .setTitle(getTitle())
                .setBusinessPriority(getBusinessPriority())
                .setStatus(getStatus())
                .setCreatedAt(getCreatedAt())
                .setAssignedAt(getAssignedAt())
                .setSolvedAt(getSolvedAt())
                .setAssignedTo(getAssignedTo())
                .setReportedBy(getReportedBy())
                .setDescription(getDescription() == null ? "" : getDescription())
                .setUiElementId(this.uiElementId)
                .setBusinessValue(this.businessValue)
                .setUsabilityScore(this.usabilityScore)
                .setScreenshotUrl(this.screenshotUrl)
                .setSuggestedFix(this.suggestedFix).build();

        if (getComments() != null) {
            for (Comment c : getComments()) {
                copy.addComment(c.author(), c.comment(), c.timestamp());
            }
        }

        if (getActions() != null) {
            for (Action a : getActions()) {
                copy.getActions().add(a.deepCopy());
            }
        }

        return copy;
    }
}