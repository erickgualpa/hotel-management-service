package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;

public record SyncCreateReviewCommand(
    String reviewIdentifier, String hotelIdentifier, Integer rating, String comment)
    implements Command {}
