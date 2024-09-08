package org.egualpam.services.hotelmanagement.hotel.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotel.application.query.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.services.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class PostgreSqlJpaSingleHotelViewSupplier implements ViewSupplier<SingleHotelView> {

    private static final Logger logger = LoggerFactory.getLogger(PostgreSqlJpaSingleHotelViewSupplier.class);
    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;
    private final WebClient imageServiceClient;

    public PostgreSqlJpaSingleHotelViewSupplier(
            EntityManager entityManager,
            WebClient imageServiceClient
    ) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
        this.imageServiceClient = imageServiceClient;
    }

    @Override
    public SingleHotelView get(Criteria criteria) {
        HotelCriteria hotelCriteria = (HotelCriteria) criteria;
        UniqueId hotelId = hotelCriteria.getHotelId().orElseThrow(RequiredPropertyIsMissing::new);
        PersistenceHotel persistenceHotel = entityManager.find(
                PersistenceHotel.class,
                UUID.fromString(hotelId.value())
        );
        Optional<SingleHotelView.Hotel> hotel = Optional.ofNullable(persistenceHotel).map(this::mapIntoViewHotel);
        return new SingleHotelView(hotel);
    }

    private SingleHotelView.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
        Double averageRating = findReviewsByHotel
                .apply(persistenceHotel)
                .stream()
                .mapToDouble(PersistenceReview::getRating)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);

        String imageURL = Optional.ofNullable(persistenceHotel.getImageURL())
                .orElseGet(
                        () -> retrieveImageURL(persistenceHotel.getId()).orElse(null)
                );

        return new SingleHotelView.Hotel(
                persistenceHotel.getId().toString(),
                persistenceHotel.getName(),
                persistenceHotel.getDescription(),
                persistenceHotel.getLocation(),
                persistenceHotel.getTotalPrice(),
                imageURL,
                averageRating
        );
    }

    private Optional<String> retrieveImageURL(UUID hotelId) {
        ImageServiceResponse response = null;
        try {
            response = imageServiceClient.get()
                    .uri("/v1/images/hotels/" + hotelId)
                    .retrieve()
                    .bodyToMono(ImageServiceResponse.class)
                    .block();
        } catch (Exception e) {
            logger.warn(String.format("Unable to retrieve imageURL given hotelId [%s]", hotelId), e);
        }

        return Optional.ofNullable(response).map(ImageServiceResponse::imageURL);
    }

    public record ImageServiceResponse(String imageURL) {
    }
}
