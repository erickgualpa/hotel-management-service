package org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "reviews")
public class PersistenceReview {

    @Id
    private UUID id;
    private Integer rating;
    private String comment;
    @Column(name = "hotel_id")
    private UUID hotelId;

    public PersistenceReview() {
    }

    public PersistenceReview(UUID id, Integer rating, String comment, UUID hotelId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.hotelId = hotelId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getHotelId() {
        return hotelId;
    }

    public void setHotelId(UUID hotelId) {
        this.hotelId = hotelId;
    }
}
