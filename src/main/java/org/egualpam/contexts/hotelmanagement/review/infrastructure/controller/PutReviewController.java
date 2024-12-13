package org.egualpam.contexts.hotelmanagement.review.infrastructure.controller;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.noContent;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncUpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.shared.domain.InvalidUniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public final class PutReviewController {

  private final Logger logger = getLogger(this.getClass());
  private final CommandBus commandBus;

  @PutMapping(path = "/{reviewId}")
  public ResponseEntity<Void> updateReview(
      @PathVariable String reviewId, @RequestBody UpdateReviewRequest updateReviewRequest) {
    Command updateReviewCommand =
        new SyncUpdateReviewCommand(reviewId, updateReviewRequest.comment());

    try {
      commandBus.publish(updateReviewCommand);
    } catch (InvalidUniqueId e) {
      return badRequest().build();
    } catch (RuntimeException e) {
      logger.error(
          format(
              "An error occurred while processing the request [%s] given [reviewId=%s]",
              updateReviewRequest, reviewId),
          e);
      return internalServerError().build();
    }

    return noContent().build();
  }
}
