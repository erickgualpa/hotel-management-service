package org.egualpam.contexts.hotelmanagement.review.infrastructure.controller;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.QueryBus;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple.SyncFindReviewsQuery;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public final class SearchReviewsController {

  private final Logger logger = getLogger(this.getClass());
  private final QueryBus queryBus;

  @GetMapping
  public ResponseEntity<GetReviewsResponse> searchReviews(@RequestParam String hotelId) {
    Query findReviewsQuery = new SyncFindReviewsQuery(hotelId);

    final ManyReviews manyReviews;
    try {
      manyReviews = (ManyReviews) queryBus.publish(findReviewsQuery);
    } catch (RuntimeException e) {
      logger.error(
          format("An error occurred while processing the request with hotel id: [%s]", hotelId), e);
      return internalServerError().build();
    }

    return ok(mapIntoResponse(manyReviews.reviews()));
  }

  private GetReviewsResponse mapIntoResponse(List<ManyReviews.Review> reviews) {
    return new GetReviewsResponse(
        reviews.stream().map(r -> new GetReviewsResponse.Review(r.rating(), r.comment())).toList());
  }
}
