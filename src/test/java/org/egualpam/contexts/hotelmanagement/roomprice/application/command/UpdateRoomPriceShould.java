package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPriceIdGenerator;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.RoomType;
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
  void roomPriceShouldBeUpdated() {
    String roomPriceId = randomUUID().toString();
    String hotelId = randomUUID().toString();
    String roomType = "S";
    String priceAmount = "100.00";

    when(roomPriceIdGenerator.get(new AggregateId(hotelId), RoomType.S)).thenReturn(roomPriceId);

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
}
