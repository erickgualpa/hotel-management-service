package org.egualpam.services.hotel.rating.infrastructure.persistance.staticstorage;

import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.domain.Review;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StaticRatedHotelRepository implements RatedHotelRepository {

    private final StaticHotelRepository hotelRepository;
    private final StaticReviewRepository reviewRepository;

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
        List<RatedHotel> hotels =
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
        return hotels;
    }

    private List<Review> findReviewsMatchingHotel(RatedHotel h) {
        return reviewRepository.findReviewsMatchingHotelIdentifier(h.getIdentifier()).stream()
                .map(r -> new Review(r.getIdentifier(), r.getRating(), r.getComment()))
                .collect(Collectors.toList());
    }
}
