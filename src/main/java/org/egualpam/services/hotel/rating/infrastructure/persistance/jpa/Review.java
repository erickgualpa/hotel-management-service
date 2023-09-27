package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    private UUID id;
    private Integer rating;
    private String comment;
    private UUID hotel_id;

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

    public UUID getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(UUID hotel_id) {
        this.hotel_id = hotel_id;
    }
}
