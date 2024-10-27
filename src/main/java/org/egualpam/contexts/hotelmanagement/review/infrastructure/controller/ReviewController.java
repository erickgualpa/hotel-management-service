package org.egualpam.contexts.hotelmanagement.review.infrastructure.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviewsQuery;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.exceptions.InvalidRating;
import org.egualpam.contexts.hotelmanagement.review.domain.exceptions.ReviewAlreadyExists;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.application.command.CommandBus;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.InvalidUniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;
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

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public final class ReviewController {

  private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

  private final CommandBus commandBus;
  private final QueryBus queryBus;

  @GetMapping
  public ResponseEntity<GetReviewsResponse> findReviews(@RequestParam String hotelId) {
    Query findReviewsQuery = new FindReviewsQuery(hotelId);

    final ManyReviews manyReviews;
    try {
      manyReviews = (ManyReviews) queryBus.publish(findReviewsQuery);
    } catch (Exception e) {
      logger.error(
          String.format(
              "An error occurred while processing the request with hotel id: [%s]", hotelId),
          e);
      return ResponseEntity.internalServerError().build();
    }

    return ResponseEntity.ok(mapIntoResponse(manyReviews.reviews()));
  }

  private GetReviewsResponse mapIntoResponse(List<ManyReviews.Review> reviews) {
    return new GetReviewsResponse(
        reviews.stream().map(r -> new GetReviewsResponse.Review(r.rating(), r.comment())).toList());
  }

  @PostMapping(path = "/{reviewId}")
  public ResponseEntity<Void> createReview(
      @PathVariable String reviewId, @RequestBody CreateReviewRequest createReviewRequest) {
    Command createReviewCommand =
        new CreateReviewCommand(
            reviewId,
            createReviewRequest.hotelId(),
            createReviewRequest.rating(),
            createReviewRequest.comment());

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
              createReviewRequest, reviewId),
          e);
      return ResponseEntity.internalServerError().build();
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping(path = "/{reviewId}")
  public ResponseEntity<Void> updateReview(
      @PathVariable String reviewId, @RequestBody UpdateReviewRequest updateReviewRequest) {
    Command updateReviewCommand = new UpdateReviewCommand(reviewId, updateReviewRequest.comment());

    try {
      commandBus.publish(updateReviewCommand);
    } catch (InvalidUniqueId e) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      logger.error(
          String.format(
              "An error occurred while processing the request [%s] given [reviewId=%s]",
              updateReviewRequest, reviewId),
          e);
      return ResponseEntity.internalServerError().build();
    }

    return ResponseEntity.noContent().build();
  }
}
