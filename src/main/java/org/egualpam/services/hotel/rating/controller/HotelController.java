package org.egualpam.services.hotel.rating.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    @PostMapping(value = "/query")
    public List<Object> queryHotels() {
        return Collections.emptyList();
    }
}
