package main.tickets;

import lombok.Getter;
import main.tickets.actions.Action;
import main.globals.ticketenums.BusinessValue;

public final class UIFeedbackTicket extends Ticket {
    private final String uiElementId;
    @Getter
    private final BusinessValue businessValue;
    @Getter
    private final int usabilityScore;

    // Optional fields
    private final String screenshotUrl;
    private final String suggestedFix;

    private UIFeedbackTicket(final UIFeedbackTicketBuilder builder) {
        super(builder);
        this.uiElementId = builder.uiElementId;
        this.businessValue = builder.businessValue;
        this.usabilityScore = builder.usabilityScore;
        this.screenshotUrl = builder.screenshotUrl;
        this.suggestedFix = builder.suggestedFix;
    }

    /**
     * Builder class for UIFeedbackTicket.
     */
    public static class UIFeedbackTicketBuilder extends Ticket.Builder<UIFeedbackTicketBuilder> {
        private String uiElementId;
        private BusinessValue businessValue;
        private int usabilityScore;

        // Optional fields
        private String screenshotUrl = "";
        private String suggestedFix = "";

        /**
         * Set UI element identifier related to the feedback.
         * @param uiElementIdValue ui element id
         * @return builder instance
         */
        public UIFeedbackTicketBuilder setUiElementId(final String uiElementIdValue) {
            this.uiElementId = uiElementIdValue;
            return this;
        }

        /**
         * Set the business value for this feedback ticket.
         * @param businessValueValue business value
         * @return builder instance
         */
        public UIFeedbackTicketBuilder setBusinessValue(final BusinessValue businessValueValue) {
            this.businessValue = businessValueValue;
            return this;
        }

        /**
         * Set the usability score.
         * @param usabilityScoreValue usability score
         * @return builder instance
         */
        public UIFeedbackTicketBuilder setUsabilityScore(final int usabilityScoreValue) {
            this.usabilityScore = usabilityScoreValue;
            return this;
        }

        /**
         * Set the screenshot URL for the feedback.
         * @param screenshotUrlValue screenshot URL
         * @return builder instance
         */
        public UIFeedbackTicketBuilder setScreenshotUrl(final String screenshotUrlValue) {
            this.screenshotUrl = screenshotUrlValue;
            return this;
        }

        /**
         * Set the suggested fix text.
         * @param suggestedFixValue suggested fix
         * @return builder instance
         */
        public UIFeedbackTicketBuilder setSuggestedFix(final String suggestedFixValue) {
            this.suggestedFix = suggestedFixValue;
            return this;
        }

        /**
         * Return the concrete builder instance.
         * @return builder instance
         */
        @Override
        protected UIFeedbackTicketBuilder self() {
            return this;
        }

        /**
         * Build a UIFeedbackTicket from the accumulated values.
         * @return new UIFeedbackTicket
         */
        @Override
        public UIFeedbackTicket build() {
            return new UIFeedbackTicket(this);
        }
    }

    /**
     * Create a deep copy of the UIFeedbackTicket.
     * @return a deep copy of the ticket
     */
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
