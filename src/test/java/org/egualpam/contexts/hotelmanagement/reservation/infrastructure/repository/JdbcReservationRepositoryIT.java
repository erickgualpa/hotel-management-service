package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.repository;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.DateRange;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.RoomTestRepository;
import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

class JdbcReservationRepositoryIT extends AbstractIntegrationTest {

  @Autowired private HotelTestRepository hotelTestRepository;
  @Autowired private RoomTestRepository roomTestRepository;

  @Autowired JdbcClient jdbcClient;

  private AggregateRepository<Reservation> testee;

  @BeforeEach
  void setUp() {
    testee = new JdbcReservationRepository(jdbcClient);
  }

  @Test
  void findShouldReturnEmptyWhenReservationNotExists() {
    String reservationId = randomUUID().toString();
    Optional<Reservation> result = testee.find(new AggregateId(reservationId));
    assertThat(result).isEmpty();
  }

  @Test
  void findShouldReturnExistingWhenReservationIsAlreadySaved() {
    UUID hotelId = randomUUID();

    String roomId = randomUUID().toString();
    String roomType = "S";

    String reservationId = randomUUID().toString();
    String reservedFrom = "2025-11-25";
    String reservedTo = "2025-11-27";

    Reservation reservation = Reservation.load(reservationId, roomId, reservedFrom, reservedTo);

    createHotel(hotelId);
    createRoom(roomId, roomType, hotelId.toString());
    testee.save(reservation);

    Optional<Reservation> result = testee.find(new AggregateId(reservationId));

    assertThat(result)
        .get()
        .satisfies(
            existing -> {
              assertThat(existing.id().value()).isEqualTo(reservationId);
              assertThat(existing.roomId()).isEqualTo(roomId);
              assertThat(existing.reservationDateRange())
                  .isEqualTo(new DateRange(reservedFrom, reservedTo));
            });
  }

  private void createHotel(UUID hotelId) {
    String hotelName = randomAlphabetic(5);
    String hotelDescription = randomAlphabetic(10);
    String hotelLocation = randomAlphabetic(10);
    Integer hotelPrice = 100;
    String hotelImageURL = "www." + randomAlphabetic(5) + ".com";
    hotelTestRepository.insertHotel(
        hotelId, hotelName, hotelDescription, hotelLocation, hotelPrice, hotelImageURL);
  }

  private void createRoom(String roomId, String roomType, String hotelId) {
    roomTestRepository.insertRoom(roomId, roomType, hotelId);
  }
}
