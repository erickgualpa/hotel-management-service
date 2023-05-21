package org.egualpam.services.hotel.rating.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Location {

    private final String identifier;
    private final String name;
}
