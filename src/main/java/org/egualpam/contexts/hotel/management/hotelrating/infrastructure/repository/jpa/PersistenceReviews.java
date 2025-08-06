package org.egualpam.contexts.hotel.management.hotelrating.infrastructure.repository.jpa;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Set;

@JsonSerialize
record PersistenceReviews(Set<String> reviews) {}
