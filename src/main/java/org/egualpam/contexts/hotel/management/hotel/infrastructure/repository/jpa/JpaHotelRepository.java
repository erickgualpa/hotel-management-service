package org.egualpam.contexts.hotel.management.hotel.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotel.management.hotel.domain.Hotel;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.jpa.PersistenceHotel;

public final class JpaHotelRepository implements AggregateRepository<Hotel> {

  private final EntityManager entityManager;

  public JpaHotelRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Hotel> find(AggregateId id) {
    UUID hotelId = UUID.fromString(id.value());
    PersistenceHotel persistenceHotel = entityManager.find(PersistenceHotel.class, hotelId);
    return Optional.ofNullable(persistenceHotel).map(this::mapResultIntoHotel);
  }

  private Hotel mapResultIntoHotel(PersistenceHotel persistenceHotel) {
    UUID hotelId = persistenceHotel.getId();
    String name = persistenceHotel.getName();
    String description = persistenceHotel.getDescription();
    String location = persistenceHotel.getLocation();
    Integer price = persistenceHotel.getPrice();
    String imageURL = persistenceHotel.getImageURL();

    return Hotel.load(hotelId.toString(), name, description, location, price, imageURL);
  }

  @Override
  public void save(Hotel hotel) {
    PersistenceHotel persistenceHotel = new PersistenceHotel();
    UUID hotelId = UUID.fromString(hotel.id().value());
    persistenceHotel.setId(hotelId);
    persistenceHotel.setName(hotel.name());
    persistenceHotel.setDescription(hotel.description());
    persistenceHotel.setLocation(hotel.location());
    persistenceHotel.setPrice(hotel.price());
    persistenceHotel.setImageURL(hotel.imageURL());

    entityManager.merge(persistenceHotel);

    entityManager.flush();
  }
}
