package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.shared.application.query.ReadModel;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple.QueryHandler;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviews;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviewsQuery;

@RequiredArgsConstructor
public class SyncFindReviewsQueryHandler implements QueryHandler {

  private final FindReviews findReviews;

  @Override
  public ReadModel handle(Query query) {
    FindReviewsQuery findReviewsQuery =
        Optional.of(query)
            .filter(SyncFindReviewsQuery.class::isInstance)
            .map(SyncFindReviewsQuery.class::cast)
            .map(q -> new FindReviewsQuery(q.hotelId()))
            .orElseThrow();
    return findReviews.execute(findReviewsQuery);
  }
}
