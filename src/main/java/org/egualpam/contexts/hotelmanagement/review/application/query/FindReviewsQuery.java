package org.egualpam.contexts.hotelmanagement.review.application.query;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public record FindReviewsQuery(String hotelId) implements Query {}
