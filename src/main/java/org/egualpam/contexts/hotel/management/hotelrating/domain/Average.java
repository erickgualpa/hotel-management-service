package org.egualpam.contexts.hotel.management.hotelrating.domain;

record Average(Double value) {
  Integer calculateRatingSumFor(Integer reviewsCount) {
    return (int) (value * reviewsCount);
  }
}
