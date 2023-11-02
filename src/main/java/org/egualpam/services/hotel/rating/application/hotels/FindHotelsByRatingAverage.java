package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FindHotelsByRatingAverage {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public FindHotelsByRatingAverage(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<HotelDto> execute(Filters filters) {
        Optional<Location> location = Optional.ofNullable(filters.location())
                .map(Location::new);

        Optional<Price> minPrice = Optional.ofNullable(filters.priceBegin())
                .map(Price::new);

        Optional<Price> maxPrice = Optional.ofNullable(filters.priceEnd())
                .map(
                        targetMaxPrice -> {
                            boolean isPriceRangeInvalid = minPrice
                                    .map(Price::value)
                                    .filter(targetMinPrice -> targetMinPrice > targetMaxPrice)
                                    .isPresent();

                            if (isPriceRangeInvalid) {
                                throw new InvalidPriceRange();
                            }

                            return new Price(targetMaxPrice);
                        }
                );

        List<Hotel> hotels = hotelRepository.findHotels(location, minPrice, maxPrice);

        hotels.forEach(this::decorateReviews);

        return hotels.stream()
                .sorted(Comparator.comparingDouble(Hotel::calculateRatingAverage).reversed())
                .map(this::mapIntoHotelDto)
                .toList();
    }

    private void decorateReviews(Hotel hotel) {
        hotel.addReviews(
                reviewRepository.findByHotelIdentifier(
                        new Identifier(hotel.getIdentifier())
                )
        );
    }

    private HotelDto mapIntoHotelDto(Hotel hotel) {
        return new HotelDto(
                hotel.getIdentifier(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getLocation(),
                hotel.getTotalPrice(),
                hotel.getImageURL(),
                hotel.getReviews().stream()
                        .map(review ->
                                new ReviewDto(
                                        review.getRating().value(),
                                        review.getComment().value()
                                )
                        )
                        .toList()
        );
    }
}