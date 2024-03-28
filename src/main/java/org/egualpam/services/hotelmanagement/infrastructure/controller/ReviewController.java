package org.egualpam.services.hotelmanagement.infrastructure.controller;


import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.application.reviews.ReviewsView;
import org.egualpam.services.hotelmanagement.application.shared.Command;
import org.egualpam.services.hotelmanagement.application.shared.CommandBus;
import org.egualpam.services.hotelmanagement.application.shared.Query;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.InvalidRating;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.ReviewAlreadyExists;
import org.egualpam.services.hotelmanagement.domain.shared.exception.InvalidUniqueId;
import org.egualpam.services.hotelmanagement.domain.shared.exception.RequiredPropertyIsMissing;
import org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple.CreateReviewCommand;
import org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple.FindHotelReviewsQuery;
import org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple.UpdateReviewCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public final class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @GetMapping
    public ResponseEntity<GetReviewsResponse> findReviews(@RequestParam String hotelId) {
        Query findHotelReviewsQuery = new FindHotelReviewsQuery(hotelId);

        final ReviewsView reviewsView;
        try {
            reviewsView = (ReviewsView) queryBus.publish(findHotelReviewsQuery);
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "An error occurred while processing the request with hotel id: [%s]",
                            hotelId
                    ),
                    e
            );
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(mapIntoResponse(reviewsView.reviews()));
    }

    private GetReviewsResponse mapIntoResponse(List<ReviewsView.Review> reviews) {
        return new GetReviewsResponse(
                reviews.stream()
                        .map(r ->
                                new GetReviewsResponse.Review(
                                        r.rating(),
                                        r.comment()))
                        .toList()
        );
    }

    @PostMapping(path = "/{reviewId}")
    public ResponseEntity<Void> createReview(@PathVariable String reviewId,
                                             @RequestBody CreateReviewRequest createReviewRequest) {
        Command createReviewCommand = new CreateReviewCommand(
                reviewId,
                createReviewRequest.hotelId(),
                createReviewRequest.rating(),
                createReviewRequest.comment()
        );

        try {
            commandBus.publish(createReviewCommand);
        } catch (RequiredPropertyIsMissing | InvalidUniqueId | InvalidRating e) {
            return ResponseEntity.badRequest().build();
        } catch (ReviewAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "An error occurred while processing the request [%s] given [reviewId=%s]",
                            createReviewRequest,
                            reviewId
                    ),
                    e
            );
            return ResponseEntity.internalServerError().build();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable String reviewId,
                                             @RequestBody UpdateReviewRequest updateReviewRequest) {
        Command updateReviewCommand = new UpdateReviewCommand(
                reviewId,
                updateReviewRequest.comment()
        );

        try {
            commandBus.publish(updateReviewCommand);
        } catch (InvalidUniqueId e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "An error occurred while processing the request [%s] given [reviewId=%s]",
                            updateReviewRequest,
                            reviewId
                    ),
                    e
            );
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.noContent().build();
    }
}
