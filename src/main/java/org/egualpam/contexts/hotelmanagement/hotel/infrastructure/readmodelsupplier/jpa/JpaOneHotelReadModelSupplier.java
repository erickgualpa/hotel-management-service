package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa;

import jakarta.persistence.EntityManager;
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
  private final GetHotelAverageRating getHotelAverageRating;
  private final WebClient imageServiceClient;

  public JpaOneHotelReadModelSupplier(EntityManager entityManager, WebClient imageServiceClient) {
    this.entityManager = entityManager;
    this.getHotelAverageRating = new GetHotelAverageRating(entityManager);
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
    UUID hotelId = persistenceHotel.getId();

    String imageURL =
        Optional.ofNullable(persistenceHotel.getImageURL())
            .orElseGet(() -> retrieveImageURL(hotelId).orElse(null));

    HotelAverageRating hotelAverageRating = getHotelAverageRating.using(hotelId);

    return new OneHotel.Hotel(
        hotelId.toString(),
        persistenceHotel.getName(),
        persistenceHotel.getDescription(),
        persistenceHotel.getLocation(),
        persistenceHotel.getPrice(),
        imageURL,
        hotelAverageRating.value());
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
