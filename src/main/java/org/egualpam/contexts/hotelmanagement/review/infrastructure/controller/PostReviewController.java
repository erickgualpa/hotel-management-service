package org.egualpam.contexts.hotelmanagement.review.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.domain.InvalidRating;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewAlreadyExists;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncCreateReviewCommand;
import org.egualpam.contexts.hotelmanagement.shared.domain.InvalidUniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public final class PostReviewController {

  private static final Logger logger = LoggerFactory.getLogger(PostReviewController.class);

  private final CommandBus commandBus;

  @PostMapping(path = "/{reviewId}")
  public ResponseEntity<Void> createReview(
      @PathVariable String reviewId, @RequestBody CreateReviewRequest createReviewRequest) {
    Command createReviewCommand =
        new SyncCreateReviewCommand(
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
}
