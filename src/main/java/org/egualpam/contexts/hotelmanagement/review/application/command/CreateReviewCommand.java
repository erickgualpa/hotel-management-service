package org.egualpam.contexts.hotelmanagement.review.application.command;

import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;

public record CreateReviewCommand(
    String reviewIdentifier, String hotelIdentifier, Integer rating, String comment)
    implements Command {}
