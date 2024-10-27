package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class PostgreSqlJpaOneHotelReadModelSupplier implements ReadModelSupplier<OneHotel> {

  private static final Logger logger =
      LoggerFactory.getLogger(PostgreSqlJpaOneHotelReadModelSupplier.class);
  private final EntityManager entityManager;
  private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;
  private final WebClient imageServiceClient;

  public PostgreSqlJpaOneHotelReadModelSupplier(
      EntityManager entityManager, WebClient imageServiceClient) {
    this.entityManager = entityManager;
    this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    this.imageServiceClient = imageServiceClient;
  }

  @Override
  public OneHotel get(Criteria criteria) {
    HotelCriteria hotelCriteria = (HotelCriteria) criteria;
    UniqueId hotelId = hotelCriteria.getHotelId().orElseThrow(RequiredPropertyIsMissing::new);
    PersistenceHotel persistenceHotel =
        entityManager.find(PersistenceHotel.class, UUID.fromString(hotelId.value()));
    Optional<OneHotel.Hotel> hotel =
        Optional.ofNullable(persistenceHotel).map(this::mapIntoViewHotel);
    return new OneHotel(hotel);
  }

  private OneHotel.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
    Double averageRating =
        findReviewsByHotel.apply(persistenceHotel).stream()
            .mapToDouble(PersistenceReview::getRating)
            .filter(Objects::nonNull)
            .average()
            .orElse(0.0);

    String imageURL =
        Optional.ofNullable(persistenceHotel.getImageURL())
            .orElseGet(() -> retrieveImageURL(persistenceHotel.getId()).orElse(null));

    return new OneHotel.Hotel(
        persistenceHotel.getId().toString(),
        persistenceHotel.getName(),
        persistenceHotel.getDescription(),
        persistenceHotel.getLocation(),
        persistenceHotel.getPrice(),
        imageURL,
        averageRating);
  }

  private Optional<String> retrieveImageURL(UUID hotelId) {
    ImageServiceResponse response = null;
    try {
      response =
          imageServiceClient
              .get()
              .uri("/v1/images/hotels/" + hotelId)
              .retrieve()
              .bodyToMono(ImageServiceResponse.class)
              .block();
    } catch (Exception e) {
      logger.warn(String.format("Unable to retrieve imageURL given hotelId [%s]", hotelId), e);
    }

    return Optional.ofNullable(response).map(ImageServiceResponse::imageURL);
  }

  public record ImageServiceResponse(String imageURL) {}
}
