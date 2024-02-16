package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;

public final class CreateReview implements InternalCommand {

    private final String reviewIdentifier;
    private final String hotelIdentifier;
    private final Integer rating;
    private final String comment;

    private final ReviewRepository reviewRepository;

    public CreateReview(
            String reviewIdentifier,
            String hotelIdentifier,
            Integer rating,
            String comment,
            ReviewRepository reviewRepository) {
        this.reviewIdentifier = reviewIdentifier;
        this.hotelIdentifier = hotelIdentifier;
        this.rating = rating;
        this.comment = comment;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void execute() {
        Review review = new Review(
                new Identifier(reviewIdentifier),
                new Identifier(hotelIdentifier),
                new Rating(rating),
                new Comment(comment)
        );
        reviewRepository.save(review);
    }
}
