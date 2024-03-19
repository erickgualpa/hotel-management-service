package org.egualpam.services.hotelmanagement.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.hotels.HotelsView;
import org.egualpam.services.hotelmanagement.application.shared.Query;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.domain.hotels.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple.FindHotelQuery;
import org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple.FindHotelsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    private final QueryBus queryBus;

    @GetMapping(value = "/{hotelId}")
    public ResponseEntity<GetHotelResponse> getHotel(@PathVariable String hotelId) {
        Query findHotelQuery = new FindHotelQuery(hotelId);

        final HotelView hotelView;
        try {
            hotelView = (HotelView) queryBus.publish(findHotelQuery);
        } catch (Exception e) {
            logger.error(
                    String.format("An error occurred while processing the request [hotelId=%s]", hotelId),
                    e
            );
            return ResponseEntity
                    .internalServerError()
                    .build();
        }

        if (hotelView.hotel().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HotelView.Hotel viewHotel = hotelView.hotel().get();

        GetHotelResponse.Hotel hotel = new GetHotelResponse.Hotel(
                viewHotel.identifier(),
                viewHotel.name(),
                viewHotel.description(),
                viewHotel.location(),
                viewHotel.totalPrice(),
                viewHotel.imageURL(),
                viewHotel.averageRating()
        );

        return ResponseEntity.ok(new GetHotelResponse(hotel));
    }

    @PostMapping(value = "/query")
    public ResponseEntity<QueryHotelResponse> queryHotels(@RequestBody QueryHotelRequest request) {
        Optional<String> location = Optional.ofNullable(request.location());
        Optional<Integer> minPrice =
                Optional.ofNullable(request.priceRange())
                        .map(QueryHotelRequest.PriceRange::begin);
        Optional<Integer> maxPrice =
                Optional.ofNullable(request.priceRange())
                        .map(QueryHotelRequest.PriceRange::end);

        Query findHotelsQuery = new FindHotelsQuery(
                location,
                minPrice,
                maxPrice
        );

        final HotelsView hotelsView;
        try {
            hotelsView = (HotelsView) queryBus.publish(findHotelsQuery);
        } catch (PriceRangeValuesSwapped e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(
                    String.format("An error occurred while processing the request [%s]", request),
                    e
            );
            return ResponseEntity
                    .internalServerError()
                    .build();
        }

        List<QueryHotelResponse.Hotel> hotels =
                hotelsView.hotels().stream()
                        .map(
                                h -> new QueryHotelResponse.Hotel(
                                        h.identifier(),
                                        h.name(),
                                        h.description(),
                                        h.location(),
                                        h.totalPrice(),
                                        h.imageURL(),
                                        h.averageRating()))
                        .toList();

        return ResponseEntity.ok(new QueryHotelResponse(hotels));
    }
}
