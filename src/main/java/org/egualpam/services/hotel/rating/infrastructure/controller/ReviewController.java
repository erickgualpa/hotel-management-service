package org.egualpam.services.hotel.rating.infrastructure.controller;


import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.reviews.CreateReviewCommand;
import org.egualpam.services.hotel.rating.application.reviews.FindReviewsByHotelIdentifier;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.domain.shared.InvalidIdentifier;
import org.egualpam.services.hotel.rating.domain.shared.InvalidRating;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final FindReviewsByHotelIdentifier findReviewsByHotelIdentifier;
    private final CreateReview createReview;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> findReviewsByHotelIdentifier(@RequestParam String hotelIdentifier) {
        List<ReviewDto> reviews = findReviewsByHotelIdentifier.execute(hotelIdentifier);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping(path = "/{reviewIdentifier}")
    public ResponseEntity<Void> createReview(@PathVariable String reviewIdentifier,
                                             @RequestBody CreateReviewRequest createReviewRequest) {
        CreateReviewCommand createReviewCommand = new CreateReviewCommand(
                reviewIdentifier,
                createReviewRequest.hotelIdentifier(),
                createReviewRequest.rating(),
                createReviewRequest.comment()
        );

        try {
            createReview.execute(createReviewCommand);
        } catch (InvalidIdentifier | InvalidRating e) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
