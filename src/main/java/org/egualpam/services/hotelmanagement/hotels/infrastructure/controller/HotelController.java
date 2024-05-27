package org.egualpam.services.hotelmanagement.hotels.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.hotels.application.query.FindHotelQuery;
import org.egualpam.services.hotelmanagement.hotels.application.query.FindHotelsQuery;
import org.egualpam.services.hotelmanagement.hotels.application.query.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.application.query.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.exceptions.PriceRangeValuesSwapped;
import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.services.hotelmanagement.shared.domain.exceptions.InvalidUniqueId;
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

        final SingleHotelView singleHotelView;
        try {
            singleHotelView = (SingleHotelView) queryBus.publish(findHotelQuery);
        } catch (InvalidUniqueId e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error(
                    String.format("An error occurred while processing the request [hotelId=%s]", hotelId),
                    e
            );
            return ResponseEntity.internalServerError().build();
        }

        return singleHotelView.hotel()
                .map(hotel -> ResponseEntity.ok(mapIntoResponse(hotel)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private GetHotelResponse mapIntoResponse(SingleHotelView.Hotel viewHotel) {
        return new GetHotelResponse(
                new GetHotelResponse.Hotel(
                        viewHotel.identifier(),
                        viewHotel.name(),
                        viewHotel.description(),
                        viewHotel.location(),
                        viewHotel.totalPrice(),
                        viewHotel.imageURL(),
                        viewHotel.averageRating()
                )
        );
    }

    @PostMapping(value = "/query")
    public ResponseEntity<QueryHotelResponse> queryHotels(@RequestBody QueryHotelRequest request) {
        Query findHotelsQuery = new FindHotelsQuery(
                request.location(),
                Optional.ofNullable(request.priceRange()).map(QueryHotelRequest.PriceRange::begin).orElse(null),
                Optional.ofNullable(request.priceRange()).map(QueryHotelRequest.PriceRange::end).orElse(null)
        );

        final MultipleHotelsView multipleHotelsView;
        try {
            multipleHotelsView = (MultipleHotelsView) queryBus.publish(findHotelsQuery);
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
        return ResponseEntity.ok(mapIntoResponse(multipleHotelsView.hotels()));
    }

    private QueryHotelResponse mapIntoResponse(List<MultipleHotelsView.Hotel> hotels) {
        return new QueryHotelResponse(hotels.stream()
                .map(
                        h -> new QueryHotelResponse.Hotel(
                                h.identifier(),
                                h.name(),
                                h.description(),
                                h.location(),
                                h.totalPrice(),
                                h.imageURL(),
                                h.averageRating()))
                .toList());
    }
}
