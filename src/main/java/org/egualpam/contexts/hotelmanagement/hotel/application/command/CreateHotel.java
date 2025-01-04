package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelAlreadyExists;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;

public class CreateHotel {

  private final Clock clock;
  private final UniqueIdSupplier uniqueIdSupplier;
  private final AggregateRepository<Hotel> repository;
  private final EventBus eventBus;

  public CreateHotel(
      Clock clock,
      UniqueIdSupplier uniqueIdSupplier,
      AggregateRepository<Hotel> repository,
      EventBus eventBus) {
    this.clock = clock;
    this.uniqueIdSupplier = uniqueIdSupplier;
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void execute(CreateHotelCommand command) {
    final String hotelId = command.id();
    final String hotelName = command.name();
    final String hotelDescription = command.description();
    final String hotelLocation = command.location();
    final Integer hotelPrice = command.price();
    final String hotelImageURL = command.imageURL();

    final Hotel hotel;
    try {
      hotel =
          Hotel.create(
              hotelId,
              hotelName,
              hotelDescription,
              hotelLocation,
              hotelPrice,
              hotelImageURL,
              uniqueIdSupplier,
              repository,
              clock);
    } catch (HotelAlreadyExists e) {
      return;
    }

    repository.save(hotel);
    eventBus.publish(hotel.pullDomainEvents());
  }
}
