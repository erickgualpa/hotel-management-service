package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import org.egualpam.services.hotelmanagement.shared.application.command.Command;

public final class UpdateReviewCommand implements Command {

    private final String reviewIdentifier;
    private final String comment;

    public UpdateReviewCommand(String reviewIdentifier, String comment) {
        this.reviewIdentifier = reviewIdentifier;
        this.comment = comment;
    }

    public String getReviewIdentifier() {
        return reviewIdentifier;
    }

    public String getComment() {
        return comment;
    }
}
