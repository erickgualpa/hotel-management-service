package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

record Average(Double value) {
  Integer calculateRatingSumFor(ReviewsCount reviewsCount) {
    return (int) (value * reviewsCount.value());
  }
}
