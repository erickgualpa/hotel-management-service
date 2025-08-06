package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.RoomType;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPriceIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateRoomPriceShould {

  @Captor private ArgumentCaptor<RoomPrice> roomPriceCaptor;
  @Mock private RoomPriceIdGenerator roomPriceIdGenerator;
  @Mock private AggregateRepository<RoomPrice> repository;

  private UpdateRoomPrice testee;

  @BeforeEach
  void setUp() {
    testee = new UpdateRoomPrice(roomPriceIdGenerator, repository);
  }

  @Test
  void createRoomPriceWhenNotExists() {
    String roomPriceId = randomUUID().toString();
    String hotelId = randomUUID().toString();
    String roomType = "S";
    String priceAmount = "100.00";

    when(roomPriceIdGenerator.get(new AggregateId(hotelId), RoomType.S))
        .thenReturn(new AggregateId(roomPriceId));
    when(repository.find(new AggregateId(roomPriceId))).thenReturn(Optional.empty());

    UpdateRoomPriceCommand command = new UpdateRoomPriceCommand(hotelId, roomType, priceAmount);

    testee.execute(command);

    verify(repository).save(roomPriceCaptor.capture());
    assertThat(roomPriceCaptor.getValue())
        .satisfies(
            saved -> {
              assertThat(saved.id().value()).isEqualTo(roomPriceId);
              assertThat(saved.hotelId()).isEqualTo(hotelId);
              assertThat(saved.roomType()).isEqualTo(roomType);
              assertThat(saved.price().amount()).isEqualTo("100.00");
              assertThat(saved.price().currency()).isEqualTo("EUR");
            });
  }

  @Test
  void updateRoomPrice() {
    String roomPriceId = randomUUID().toString();
    String hotelId = randomUUID().toString();
    String roomType = "S";

    String currentPriceAmount = "100.00";
    String newPriceAmount = "80.00";

    RoomPrice existing = RoomPrice.load(roomPriceId, hotelId, roomType, currentPriceAmount);

    when(roomPriceIdGenerator.get(new AggregateId(hotelId), RoomType.S))
        .thenReturn(new AggregateId(roomPriceId));
    when(repository.find(new AggregateId(roomPriceId))).thenReturn(Optional.of(existing));

    UpdateRoomPriceCommand command = new UpdateRoomPriceCommand(hotelId, roomType, newPriceAmount);

    testee.execute(command);

    verify(repository).save(roomPriceCaptor.capture());
    assertThat(roomPriceCaptor.getValue())
        .satisfies(
            saved -> {
              assertThat(saved.price().amount()).isEqualTo(newPriceAmount);
              assertThat(saved.price().currency()).isEqualTo("EUR");
            });
  }
}
