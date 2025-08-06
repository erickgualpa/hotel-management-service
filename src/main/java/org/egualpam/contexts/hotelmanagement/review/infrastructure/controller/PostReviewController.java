package org.egualpam.contexts.hotelmanagement.review.infrastructure.controller;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.status;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.shared.domain.InvalidUniqueId;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.CommandBus;
import org.egualpam.contexts.hotelmanagement.review.domain.InvalidRating;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewAlreadyExists;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncCreateReviewCommand;
import org.slf4j.Logger;
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

  private final Logger logger = getLogger(this.getClass());
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
      return badRequest().build();
    } catch (ReviewAlreadyExists e) {
      return status(CONFLICT).build();
    } catch (RuntimeException e) {
      logger.error(
          format(
              "An error occurred while processing the request [%s] given [reviewId=%s]",
              createReviewRequest, reviewId),
          e);
      return internalServerError().build();
    }

    return status(CREATED).build();
  }
}
