package org.egualpam.services.hotel.rating.application.reviews;

public record CreateReviewCommand(String reviewIdentifier,
                                  String hotelIdentifier,
                                  Integer rating,
                                  String comment
) {
}
