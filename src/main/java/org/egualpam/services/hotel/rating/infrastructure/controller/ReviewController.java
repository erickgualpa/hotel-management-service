package org.egualpam.services.hotel.rating.infrastructure.controller;


import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.reviews.FindReviews;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final FindReviews findReviews;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> findReviewsByHotelIdentifier(@RequestParam String hotelIdentifier) {
        return ResponseEntity.ok(
                findReviews.findByHotelIdentifier(hotelIdentifier)
        );
    }
}
