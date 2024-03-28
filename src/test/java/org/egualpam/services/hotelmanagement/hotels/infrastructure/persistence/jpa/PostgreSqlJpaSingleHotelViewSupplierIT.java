package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotels.application.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.exception.RequiredPropertyIsMissing;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgreSqlJpaSingleHotelViewSupplierIT extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    private ViewSupplier<SingleHotelView> testee;

    @BeforeEach
    void setUp() {
        testee = new PostgreSqlJpaSingleHotelViewSupplier(entityManager);
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
}