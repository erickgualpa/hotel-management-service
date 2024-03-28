package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotels.application.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostgreSqlJpaSingleHotelViewSupplierIT extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void returnViewWithEmptyOptional_whenHotelIdNotMatchesAnyHotel() {
        final ViewSupplier<SingleHotelView> testee = new PostgreSqlJpaHotelViewSupplier(entityManager);
        String hotelId = randomUUID().toString();
        Criteria criteria = new HotelCriteria(hotelId);

        SingleHotelView result = testee.get(criteria);

        assertNotNull(result);
        assertThat(result.hotel()).isEmpty();
    }
}