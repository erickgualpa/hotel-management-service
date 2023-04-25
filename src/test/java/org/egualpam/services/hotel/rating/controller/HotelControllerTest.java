package org.egualpam.services.hotel.rating.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryIsAcceptedSuccessfully() {
        assertNotNull(mockMvc);
    }
}
