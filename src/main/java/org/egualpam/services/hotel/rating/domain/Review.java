package org.egualpam.services.hotel.rating.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class Review {

    private final String identifier;
    private final Integer rating;
    private final String comment;
}
