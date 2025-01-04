package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.support.TransactionTemplate;

class UpdateHotelRatingIT extends AbstractIntegrationTest {

  private static final String CREATE_HOTEL_REQUEST =
      """
                {
                    "id": "%s",
                    "name": "%s",
                    "description": "%s",
                    "location": "%s",
                    "price": %d,
                    "imageURL": "%s"
                }
                """;

  @Autowired private TransactionTemplate transactionTemplate;

  @Autowired private Clock clock;
  @Autowired private UniqueIdSupplier uniqueIdSupplier;
  @MockitoSpyBean private AggregateRepository<Hotel> repository;
  @Autowired private ReviewIsAlreadyProcessed reviewIsAlreadyProcessed;
  @Autowired private EventBus eventBus;

  private UpdateHotelRating testee;

  @BeforeEach
  void setUp() {
    testee =
        new UpdateHotelRating(
            clock, uniqueIdSupplier, repository, reviewIsAlreadyProcessed, eventBus);
  }

  @Test
  void notSetReviewIdAsProcessed_whenRepositorySaveFails() throws Exception {
    UUID hotelId = randomUUID();
    EntityId reviewId = new EntityId(UniqueId.get().value());

    createHotel(hotelId);

    doThrow(new RuntimeException()).when(repository).save(any(Hotel.class));

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(hotelId.toString(), reviewId.value(), 3);

    try {
      transactionTemplate.executeWithoutResult(ts -> testee.execute(command));
    } catch (RuntimeException ignored) {
    }

    transactionTemplate.executeWithoutResult(
        ts -> assertFalse(reviewIsAlreadyProcessed.with(reviewId)));
  }

  private void createHotel(UUID hotelId) throws Exception {
    String request =
        String.format(
            CREATE_HOTEL_REQUEST,
            hotelId,
            randomAlphabetic(5),
            randomAlphabetic(10),
            randomAlphabetic(10),
            100,
            "www." + randomAlphabetic(5) + ".com");

    mockMvc
        .perform(post("/v1/hotels").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());
  }
}
