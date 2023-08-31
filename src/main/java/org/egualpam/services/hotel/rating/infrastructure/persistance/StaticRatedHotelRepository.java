package org.egualpam.services.hotel.rating.infrastructure.persistance;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.domain.Review;

import java.util.List;


@RequiredArgsConstructor
public class StaticRatedHotelRepository implements RatedHotelRepository {

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {

        RatedHotel fakeRatedHotel = new RatedHotel(
                "some-hotel-identifier",
                "some-hotel-name",
                "some-hotel-description",
                new Location(
                        "some-location-identifier",
                        "some-location-name"),
                100,
                "some-image-url");

        fakeRatedHotel.addReviews(
                List.of(
                        new Review(
                                "some-review-identifier",
                                3,
                                "some-review-comment"
                        )
                )
        );

        return List.of(fakeRatedHotel);
    }
}
