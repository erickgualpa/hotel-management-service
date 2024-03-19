package org.egualpam.services.hotelmanagement.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "org.egualpam.services.hotelmanagement.infrastructure.configuration",
                "org.egualpam.services.hotelmanagement.infrastructure.controller"
        }
)
public class HotelManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelManagementServiceApplication.class, args);
    }
}
