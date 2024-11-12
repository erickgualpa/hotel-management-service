package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public record SyncFindReviewsQuery(String hotelId) implements Query {}
