package org.egualpam.contexts.hotel.customer.hotel.infrastructure.readmodelsupplier.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotel.customer.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotel.customer.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.shared.jpa.hotelaveragerating.GetHotelAverageRating;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.shared.jpa.hotelaveragerating.HotelAverageRating;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class JpaOneHotelReadModelSupplier
    implements ReadModelSupplier<UniqueHotelCriteria, OneHotel> {

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
  public OneHotel get(UniqueHotelCriteria criteria) {
    UniqueId hotelId = criteria.hotelId();

    String sql =
        """
        SELECT id, name, description, location, price, image_url
        FROM hotels
        WHERE id=:hotelId;
        """;

    PersistenceHotel persistenceHotel;
    try {
      persistenceHotel =
          (PersistenceHotel)
              entityManager
                  .createNativeQuery(sql, PersistenceHotel.class)
                  .setParameter("hotelId", UUID.fromString(hotelId.value()))
                  .getSingleResult();
    } catch (NoResultException e) {
      persistenceHotel = null;
    }

    Optional<OneHotel.Hotel> hotel =
        Optional.ofNullable(persistenceHotel).map(this::mapIntoViewHotel);

    return new OneHotel(hotel);
  }

  private OneHotel.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
    UUID hotelId = persistenceHotel.id();

    String imageURL =
        Optional.ofNullable(persistenceHotel.imageURL())
            .orElseGet(() -> retrieveImageURL(hotelId).orElse(null));

    HotelAverageRating hotelAverageRating = getHotelAverageRating.using(hotelId);

    return new OneHotel.Hotel(
        hotelId.toString(),
        persistenceHotel.name(),
        persistenceHotel.description(),
        persistenceHotel.location(),
        persistenceHotel.price().intValue(),
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

  public record PersistenceHotel(
      UUID id, String name, String description, String location, Long price, String imageURL) {}
}
