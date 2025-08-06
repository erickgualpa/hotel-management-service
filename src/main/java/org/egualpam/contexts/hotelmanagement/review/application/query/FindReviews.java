package org.egualpam.contexts.hotelmanagement.review.application.query;

import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;

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
