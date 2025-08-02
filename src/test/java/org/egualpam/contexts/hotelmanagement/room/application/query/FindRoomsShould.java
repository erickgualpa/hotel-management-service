package org.egualpam.contexts.hotelmanagement.room.application.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindRoomsShould {

  @Captor private ArgumentCaptor<RoomCriteria> roomCriteriaCaptor;
  @Mock private ReadModelSupplier<RoomCriteria, ManyRooms> readModelSupplier;

  private FindRooms testee;

  @BeforeEach
  void setUp() {
    testee = new FindRooms(readModelSupplier);
  }

  @Test
  void findRooms() {
    String availableFrom = "2025-11-25";
    String availableTo = "2025-11-27";
    FindRoomsQuery query = new FindRoomsQuery(availableFrom, availableTo);

    when(readModelSupplier.get(roomCriteriaCaptor.capture())).thenReturn(new ManyRooms(List.of()));

    ManyRooms result = testee.execute(query);

    assertThat(roomCriteriaCaptor.getValue())
        .satisfies(
            roomCriteria -> {
              assertThat(roomCriteria.getAvailableFrom()).isEqualTo(availableFrom);
              assertThat(roomCriteria.getAvailableTo()).isEqualTo(availableTo);
            });

    assertNotNull(result);
    assertThat(result.rooms()).isEmpty();
  }
}
