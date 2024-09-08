package org.egualpam.services.hotelmanagement.hotel.infrastructure.persistence.jpa;

import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.hotel.application.query.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// TODO: Review this tests requiring '@Transactional' and '@AutoConfigureTestEntityManager' that are increasing the duration of the build
@Transactional
@AutoConfigureTestEntityManager
class PostgreSqlJpaSingleHotelViewSupplierIT extends AbstractIntegrationTest {

    private static final String IMAGE_SERVICE_RESPONSE = """
            {
                "imageURL": "%s"
            }
            """;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private WebClient imageServiceClient;

    private ViewSupplier<SingleHotelView> testee;

    @BeforeEach
    void setUp() {
        testee = new PostgreSqlJpaSingleHotelViewSupplier(entityManager, imageServiceClient);
    }

    @Test
    void returnViewWithEmptyOptional_whenHotelIdNotMatchesAnyHotel() {
        String hotelId = randomUUID().toString();
        Criteria criteria = new HotelCriteria(hotelId);

        SingleHotelView result = testee.get(criteria);

        assertNotNull(result);
        assertThat(result.hotel()).isEmpty();
    }

    @Test
    void throwDomainException_whenHotelIdIsMissing() {
        String hotelId = null;
        Criteria criteria = new HotelCriteria(hotelId);
        assertThrows(RequiredPropertyIsMissing.class, () -> testee.get(criteria));
    }

    @Test
    void returnViewRetrievingImageURLFromImageService_whenIsNotPresentInDatabase() {
        UUID hotelId = randomUUID();
        String imageURL = "www." + randomAlphabetic(5) + ".com";

        PersistenceHotel persistenceHotelWithMissingImageURL = new PersistenceHotel(
                hotelId,
                randomAlphabetic(5),
                randomAlphabetic(5),
                randomAlphabetic(5),
                nextInt(100, 200),
                null
        );
        testEntityManager.persistAndFlush(persistenceHotelWithMissingImageURL);

        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/v1/images/hotels/" + hotelId))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(IMAGE_SERVICE_RESPONSE.formatted(imageURL))
                        )
        );

        SingleHotelView singleHotelView = testee.get(new HotelCriteria(hotelId.toString()));

        assertThat(singleHotelView.hotel()).isNotEmpty();
        assertThat(singleHotelView.hotel().get().imageURL()).isEqualTo(imageURL);
    }

    @Test
    void returnViewWithNoImageURL_whenIsNotPresentInDatabaseAndImageServiceFails() {
        UUID hotelId = randomUUID();

        PersistenceHotel persistenceHotelWithMissingImageURL = new PersistenceHotel(
                hotelId,
                randomAlphabetic(5),
                randomAlphabetic(5),
                randomAlphabetic(5),
                nextInt(100, 200),
                null
        );
        testEntityManager.persistAndFlush(persistenceHotelWithMissingImageURL);

        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/v1/images/hotels/" + hotelId))
                        .willReturn(aResponse().withStatus(404))
        );

        SingleHotelView singleHotelView = testee.get(new HotelCriteria(hotelId.toString()));

        assertThat(singleHotelView.hotel()).isNotEmpty();
        assertNull(singleHotelView.hotel().get().imageURL());
    }
}