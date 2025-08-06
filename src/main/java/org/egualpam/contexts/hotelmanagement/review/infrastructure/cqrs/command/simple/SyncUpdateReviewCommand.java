package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;

public record SyncUpdateReviewCommand(String reviewIdentifier, String comment) implements Command {}
