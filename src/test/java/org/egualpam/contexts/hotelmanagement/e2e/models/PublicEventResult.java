package org.egualpam.contexts.hotelmanagement.e2e.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PublicEventResult(String type, String aggregateId, Instant occurredOn) {}
