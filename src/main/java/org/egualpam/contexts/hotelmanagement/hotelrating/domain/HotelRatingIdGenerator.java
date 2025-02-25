package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public interface HotelRatingIdGenerator {
  AggregateId generate(UniqueId hotelId);
}
