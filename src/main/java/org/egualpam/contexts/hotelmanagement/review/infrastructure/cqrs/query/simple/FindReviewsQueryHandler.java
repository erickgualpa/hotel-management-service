package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviewsQuery;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindReviewsQueryHandler implements QueryHandler {

  private final ReadModelSupplier<ManyReviews> readModelSupplier;

  @Override
  public ReadModel handle(Query query) {
    FindReviewsQuery findReviewsQuery = (FindReviewsQuery) query;
    return readModelSupplier.get(new ReviewCriteria(findReviewsQuery.hotelId()));
  }
}
