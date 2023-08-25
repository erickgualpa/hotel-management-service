package org.egualpam.services.hotel.rating.infrastructure.persistance;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
public class StaticRatedHotelRepository implements RatedHotelRepository {

    // TODO: Uncomment once amended
    /*private final StaticHotelRepository hotelRepository;
    private final StaticReviewRepository reviewRepository;*/

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
        /*List<RatedHotel> hotels =
                hotelRepository.findHotelsMatchingQuery(query).stream()
                        .map(
                                h ->
                                        new RatedHotel(
                                                h.getIdentifier(),
                                                h.getName(),
                                                h.getDescription(),
                                                new Location(
                                                        h.getLocation().getIdentifier(),
                                                        h.getLocation().getName()),
                                                h.getTotalPrice(),
                                                h.getImageURL()))
                        .collect(Collectors.toList());
        hotels.forEach(h -> h.addReviews(findReviewsMatchingHotel(h)));
        return hotels;*/
        return Collections.emptyList();
    }

    /*private List<Review> findReviewsMatchingHotel(RatedHotel h) {
        return reviewRepository.findReviewsMatchingHotelIdentifier(h.getIdentifier()).stream()
                .map(r -> new Review(r.getIdentifier(), r.getRating(), r.getComment()))
                .collect(Collectors.toList());
    }*/
}
