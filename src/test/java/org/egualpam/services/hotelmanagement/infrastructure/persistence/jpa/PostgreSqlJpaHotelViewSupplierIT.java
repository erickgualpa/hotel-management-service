package org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelCriteria;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;
import org.egualpam.services.hotelmanagement.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostgreSqlJpaHotelViewSupplierIT extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void returnViewWithEmptyOptional_whenHotelIdNotMatchesAnyHotel() {
        final ViewSupplier<HotelView> testee = new PostgreSqlJpaHotelViewSupplier(entityManager);
        AggregateId hotelId = new AggregateId(randomUUID());
        Criteria criteria = new HotelCriteria(hotelId);

        HotelView result = testee.get(criteria);

        assertNotNull(result);
        assertThat(result.hotel()).isEmpty();
    }
}