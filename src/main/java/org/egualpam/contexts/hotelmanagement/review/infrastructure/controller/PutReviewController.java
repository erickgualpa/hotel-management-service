package org.egualpam.contexts.hotelmanagement.review.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncUpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.application.command.CommandBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.InvalidUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger logger = LoggerFactory.getLogger(PutReviewController.class);

  private final CommandBus commandBus;

  @PutMapping(path = "/{reviewId}")
  public ResponseEntity<Void> updateReview(
      @PathVariable String reviewId, @RequestBody UpdateReviewRequest updateReviewRequest) {
    Command updateReviewCommand =
        new SyncUpdateReviewCommand(reviewId, updateReviewRequest.comment());

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
