package org.egualpam.contexts.hotelmanagement.review.infrastructure.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple.SyncFindReviewsQuery;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.QueryBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public final class SearchReviewsController {

  private static final Logger logger = LoggerFactory.getLogger(SearchReviewsController.class);

  private final QueryBus queryBus;

  @GetMapping
  public ResponseEntity<GetReviewsResponse> searchReviews(@RequestParam String hotelId) {
    Query findReviewsQuery = new SyncFindReviewsQuery(hotelId);

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
}
