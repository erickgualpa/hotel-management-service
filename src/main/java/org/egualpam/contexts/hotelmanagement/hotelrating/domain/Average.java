package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

record Average(Double value) {
  Integer calculateRatingSumFor(Integer reviewsCount) {
    return (int) (value * reviewsCount);
  }
}
