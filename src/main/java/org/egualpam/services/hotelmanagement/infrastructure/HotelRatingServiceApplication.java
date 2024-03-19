package org.egualpam.services.hotelmanagement.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "org.egualpam.services.hotelmanagement.infrastructure.configuration",
                "org.egualpam.services.hotelmanagement.infrastructure.controller"
        }
)
public class HotelRatingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelRatingServiceApplication.class, args);
    }

}
