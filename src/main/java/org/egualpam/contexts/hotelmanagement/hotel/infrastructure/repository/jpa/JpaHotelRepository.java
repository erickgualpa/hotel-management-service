package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa.PersistenceHotel;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;

public final class JpaHotelRepository implements AggregateRepository<Hotel> {

  private final EntityManager entityManager;

  public JpaHotelRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Hotel> find(AggregateId id) {
    PersistenceHotel persistenceHotel =
        entityManager.find(PersistenceHotel.class, UUID.fromString(id.value()));
    return Optional.ofNullable(persistenceHotel).map(this::mapResultIntoHotel);
  }

  private Hotel mapResultIntoHotel(PersistenceHotel persistenceHotel) {
    String name = persistenceHotel.getName();
    String description = persistenceHotel.getDescription();
    String location = persistenceHotel.getLocation();
    Integer price = persistenceHotel.getPrice();
    String imageURL = persistenceHotel.getImageURL();
    return new Hotel(
        persistenceHotel.getId().toString(), name, description, location, price, imageURL);
  }

  @Override
  public void save(Hotel hotel) {
    PersistenceHotel persistenceHotel = new PersistenceHotel();
    UUID hotelId = UUID.fromString(hotel.id().value());
    persistenceHotel.setId(hotelId);
    persistenceHotel.setName(hotel.name().value());
    persistenceHotel.setDescription(hotel.description().value());
    persistenceHotel.setLocation(hotel.location().value());
    persistenceHotel.setPrice(hotel.price().value());
    persistenceHotel.setImageURL(hotel.imageURL().value());

    entityManager.merge(persistenceHotel);
    entityManager.flush();
  }
}
