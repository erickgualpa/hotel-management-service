package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

class JpaOneHotelReadModelSupplierIT extends AbstractIntegrationTest {

  private static final String IMAGE_SERVICE_RESPONSE =
      """
      {
          "imageURL": "%s"
      }
      """;

  @Autowired private HotelTestRepository hotelTestRepository;

  @Autowired private EntityManager entityManager;

  @Autowired private WebClient imageServiceClient;

  private ReadModelSupplier<UniqueHotelCriteria, OneHotel> testee;

  @BeforeEach
  void setUp() {
    testee = new JpaOneHotelReadModelSupplier(entityManager, imageServiceClient);
  }

  @Test
  void returnViewWithEmptyOptional_whenHotelIdNotMatchesAnyHotel() {
    String hotelId = randomUUID().toString();
    UniqueHotelCriteria criteria = new UniqueHotelCriteria(hotelId);

    OneHotel result = testee.get(criteria);

    assertNotNull(result);
    assertThat(result.hotel()).isEmpty();
  }

  @Test
  void returnViewRetrievingImageURLFromImageService_whenIsNotPresentInDatabase() {
    UUID hotelId = randomUUID();
    String imageURL = "www." + randomAlphabetic(5) + ".com";

    // Hotel with no imageURL
    hotelTestRepository.insertHotel(
        hotelId,
        randomAlphabetic(5),
        randomAlphabetic(5),
        randomAlphabetic(5),
        nextInt(100, 200),
        null);

    wireMockServer.stubFor(
        WireMock.get(urlEqualTo("/v1/images/hotels/" + hotelId))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(IMAGE_SERVICE_RESPONSE.formatted(imageURL))));

    OneHotel result = testee.get(new UniqueHotelCriteria(hotelId.toString()));

    assertThat(result.hotel()).isNotEmpty();
    assertThat(result.hotel().get().imageURL()).isEqualTo(imageURL);
  }

  @Test
  void returnViewWithNoImageURL_whenIsNotPresentInDatabaseAndImageServiceFails() {
    UUID hotelId = randomUUID();

    // Hotel with no imageURL
    hotelTestRepository.insertHotel(
        hotelId,
        randomAlphabetic(5),
        randomAlphabetic(5),
        randomAlphabetic(5),
        nextInt(100, 200),
        null);

    wireMockServer.stubFor(
        WireMock.get(urlEqualTo("/v1/images/hotels/" + hotelId))
            .willReturn(aResponse().withStatus(404)));

    OneHotel result = testee.get(new UniqueHotelCriteria(hotelId.toString()));

    assertThat(result.hotel()).isNotEmpty();
    assertNull(result.hotel().get().imageURL());
  }
}
