package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class JpaOneHotelReadModelSupplier implements ReadModelSupplier<OneHotel> {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final EntityManager entityManager;
  private final WebClient imageServiceClient;

  public JpaOneHotelReadModelSupplier(EntityManager entityManager, WebClient imageServiceClient) {
    this.entityManager = entityManager;
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
    String imageURL =
        Optional.ofNullable(persistenceHotel.getImageURL())
            .orElseGet(() -> retrieveImageURL(persistenceHotel.getId()).orElse(null));

    String query =
        """
        SELECT avg_value
        FROM hotel_average_rating
        WHERE hotel_id=:hotelId
        """;

    BigDecimal hotelAverageRating;
    try {
      hotelAverageRating =
          (BigDecimal)
              entityManager
                  .createNativeQuery(query)
                  .setParameter("hotelId", persistenceHotel.getId())
                  .getSingleResult();
    } catch (NoResultException e) {
      hotelAverageRating = BigDecimal.ZERO;
    }

    return new OneHotel.Hotel(
        persistenceHotel.getId().toString(),
        persistenceHotel.getName(),
        persistenceHotel.getDescription(),
        persistenceHotel.getLocation(),
        persistenceHotel.getPrice(),
        imageURL,
        hotelAverageRating.doubleValue());
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

  public record HotelAverageRating(Double averageRating) {}
}
