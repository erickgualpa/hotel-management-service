package org.egualpam.services.hotel.rating.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "org.egualpam.services.hotel.rating.infrastructure.configuration",
                "org.egualpam.services.hotel.rating.infrastructure.controller"
        }
)
public class HotelRatingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelRatingServiceApplication.class, args);
    }

}
