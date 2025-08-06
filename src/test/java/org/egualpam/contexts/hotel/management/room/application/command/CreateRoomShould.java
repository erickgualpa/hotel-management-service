package org.egualpam.contexts.hotel.management.room.application.command;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.egualpam.contexts.hotel.management.room.domain.Room;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRoomShould {

  @Captor private ArgumentCaptor<Room> roomCaptor;
  @Mock private AggregateRepository<Room> repository;
  private CreateRoom testee;

  @BeforeEach
  void setUp() {
    testee = new CreateRoom(repository);
  }

  @Test
  void createRoom() {
    String roomId = randomUUID().toString();
    String roomType = "M";
    String hotelId = randomUUID().toString();

    testee.execute(new CreateRoomCommand(roomId, roomType, hotelId));

    verify(repository).save(roomCaptor.capture());
    Room saved = roomCaptor.getValue();
    assertThat(saved.id().value()).isEqualTo(roomId);
    assertThat(saved.roomType()).isEqualTo(roomType);
    assertThat(saved.hotelId()).isEqualTo(hotelId);
  }
}
