package org.egualpam.contexts.hotelmanagement.review.application.query;

import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;

public class FindReviews {

  private final ReadModelSupplier<ReviewCriteria, ManyReviews> readModelSupplier;

  public FindReviews(ReadModelSupplier<ReviewCriteria, ManyReviews> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public ManyReviews execute(FindReviewsQuery query) {
    String hotelId = query.hotelId();
    ReviewCriteria criteria = new ReviewCriteria(hotelId);
    return readModelSupplier.get(criteria);
  }
}
