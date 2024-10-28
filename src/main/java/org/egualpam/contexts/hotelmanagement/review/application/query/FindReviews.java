package org.egualpam.contexts.hotelmanagement.review.application.query;

import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;

public class FindReviews {

  private final ReadModelSupplier<ManyReviews> readModelSupplier;

  public FindReviews(ReadModelSupplier<ManyReviews> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public ManyReviews execute(FindReviewsQuery query) {
    String hotelId = query.hotelId();
    Criteria criteria = new ReviewCriteria(hotelId);
    return readModelSupplier.get(criteria);
  }
}
