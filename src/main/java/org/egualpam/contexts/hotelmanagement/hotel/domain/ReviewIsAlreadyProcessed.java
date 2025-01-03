package org.egualpam.contexts.hotelmanagement.hotel.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;

public interface ReviewIsAlreadyProcessed {
  boolean with(EntityId reviewId);
}
