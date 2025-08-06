package org.egualpam.contexts.hotel.customer.review.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record UpdateReviewRequest(String comment) {}
