package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateRoomPriceShould {

  @Mock private AggregateRepository<RoomPrice> repository;
  private UpdateRoomPrice testee;

  @BeforeEach
  void setUp() {
    testee = new UpdateRoomPrice(repository);
  }

  @Test
  void roomPriceShouldBeUpdated() {
    String roomPriceId = UUID.randomUUID().toString();
    String hotelId = UUID.randomUUID().toString();
    String roomType = "S";

    UpdateRoomPriceCommand command = new UpdateRoomPriceCommand(roomPriceId, hotelId, roomType);

    testee.execute(command);

    verify(repository).save(any());
  }
}
