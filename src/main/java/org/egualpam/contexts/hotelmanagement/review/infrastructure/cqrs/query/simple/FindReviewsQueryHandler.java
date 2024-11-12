package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviews;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviewsQuery;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindReviewsQueryHandler implements QueryHandler<SyncFindReviewsQuery> {

  private final FindReviews findReviews;

  @Override
  public ReadModel handle(SyncFindReviewsQuery query) {
    FindReviewsQuery findReviewsQuery = new FindReviewsQuery(query.hotelId());
    return findReviews.execute(findReviewsQuery);
  }
}
