package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.domain.hotels.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.Query;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

    private final ObjectMapper objectMapper;
    private final QueryBus queryBus;

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

        final List<HotelDto> hotelsDto;
        try {
            String outcome = queryBus.publish(findHotelsQuery);
            hotelsDto = objectMapper.readerForListOf(HotelDto.class).readValue(outcome);
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
                hotelsDto.stream()
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
