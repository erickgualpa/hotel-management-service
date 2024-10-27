package org.egualpam.contexts.hotelmanagement.review.application.command;

import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;

public record UpdateReviewCommand(String reviewIdentifier, String comment) implements Command {}
