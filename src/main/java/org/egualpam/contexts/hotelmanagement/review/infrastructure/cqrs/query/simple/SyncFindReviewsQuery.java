package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;

public record SyncFindReviewsQuery(String hotelId) implements Query {}
