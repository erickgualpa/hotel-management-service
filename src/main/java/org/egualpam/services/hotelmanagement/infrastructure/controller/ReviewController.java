package org.egualpam.services.hotelmanagement.infrastructure.controller;


import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.application.reviews.ReviewsView;
import org.egualpam.services.hotelmanagement.application.shared.Command;
import org.egualpam.services.hotelmanagement.application.shared.CommandBus;
import org.egualpam.services.hotelmanagement.application.shared.Query;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.InvalidRating;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.ReviewAlreadyExists;
import org.egualpam.services.hotelmanagement.domain.shared.exception.InvalidIdentifier;
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
    public ResponseEntity<GetReviewsResponse> findReviews(@RequestParam String hotelIdentifier) {
        Query findHotelReviewsQuery = new FindHotelReviewsQuery(hotelIdentifier);

        final ReviewsView reviewsView;
        try {
            reviewsView = (ReviewsView) queryBus.publish(findHotelReviewsQuery);
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "An error occurred while processing the request with hotel identifier: [%s]",
                            hotelIdentifier
                    ),
                    e
            );
            return ResponseEntity
                    .internalServerError()
                    .build();
        }

        List<GetReviewsResponse.Review> reviews =
                reviewsView.reviews().stream()
                        .map(r ->
                                new GetReviewsResponse.Review(
                                        r.rating(),
                                        r.comment()))
                        .toList();

        return ResponseEntity.ok(new GetReviewsResponse(reviews));
    }

    @PostMapping(path = "/{reviewIdentifier}")
    public ResponseEntity<Void> createReview(@PathVariable String reviewIdentifier,
                                             @RequestBody CreateReviewRequest createReviewRequest) {
        Command createReviewCommand = new CreateReviewCommand(
                reviewIdentifier,
                createReviewRequest.hotelIdentifier(),
                createReviewRequest.rating(),
                createReviewRequest.comment()
        );

        try {
            commandBus.publish(createReviewCommand);
        } catch (InvalidIdentifier | InvalidRating e) {
            return ResponseEntity.badRequest().build();
        } catch (ReviewAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "An error occurred while processing the request [%s] given [reviewId=%s]",
                            createReviewRequest,
                            reviewIdentifier
                    ),
                    e
            );
            return ResponseEntity.internalServerError().build();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "/{reviewIdentifier}")
    public ResponseEntity<Void> updateReview(@PathVariable String reviewIdentifier,
                                             @RequestBody UpdateReviewRequest updateReviewRequest) {
        Command updateReviewCommand = new UpdateReviewCommand(
                reviewIdentifier,
                updateReviewRequest.comment()
        );

        try {
            commandBus.publish(updateReviewCommand);
        } catch (InvalidIdentifier e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(
                    String.format(
                            "An error occurred while processing the request [%s] given [reviewId=%s]",
                            updateReviewRequest,
                            reviewIdentifier
                    ),
                    e
            );
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.noContent().build();
    }
}
