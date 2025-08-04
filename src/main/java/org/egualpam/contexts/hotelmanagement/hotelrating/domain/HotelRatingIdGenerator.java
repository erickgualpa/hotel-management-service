package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;

public interface HotelRatingIdGenerator {
  AggregateId generate(AggregateId hotelId);
}
