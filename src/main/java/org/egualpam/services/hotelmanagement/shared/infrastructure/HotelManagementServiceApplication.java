package org.egualpam.services.hotelmanagement.shared.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "org.egualpam.services.hotelmanagement.shared.infrastructure.configuration",
                "org.egualpam.services.hotelmanagement.hotels.infrastructure.controller",
                "org.egualpam.services.hotelmanagement.reviews.infrastructure.controller"
        }
)
public class HotelManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelManagementServiceApplication.class, args);
    }
}
