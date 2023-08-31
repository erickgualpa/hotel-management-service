package org.egualpam.services.hotel.rating;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
// TODO: Remove this annotation once database configuration is amended
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
class HotelRatingServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void applicationContextLoads() {
        assertNotNull(applicationContext);
    }
}
