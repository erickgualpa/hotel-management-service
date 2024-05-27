package org.egualpam.services.hotelmanagement.e2e.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PublicEventResult(String type) {
}