package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;

public class CreateHotel {

  private final AggregateRepository<Hotel> repository;
  private final EventBus eventBus;

  public CreateHotel(AggregateRepository<Hotel> repository, EventBus eventBus) {
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void execute(CreateHotelCommand command) {
    String hotelId = command.id();
    String hotelName = command.name();
    String hotelDescription = command.description();
    String hotelLocation = command.location();
    Integer hotelPrice = command.price();
    String hotelImageURL = command.imageURL();

    Optional<Hotel> existing = repository.find(new AggregateId(hotelId));

    if (existing.isPresent()) {
      return;
    }

    Hotel hotel =
        Hotel.create(
            hotelId, hotelName, hotelDescription, hotelLocation, hotelPrice, hotelImageURL);

    repository.save(hotel);
    eventBus.publish(hotel.pullDomainEvents());
  }
}
