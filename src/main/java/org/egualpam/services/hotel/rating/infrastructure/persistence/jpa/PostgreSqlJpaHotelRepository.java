package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;
import org.egualpam.services.hotel.rating.infrastructure.persistence.HotelDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgreSqlJpaHotelRepository extends HotelRepository {

    private final EntityManager entityManager;
    private final HotelCriteriaQueryBuilder hotelCriteriaQueryBuilder;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.hotelCriteriaQueryBuilder = new HotelCriteriaQueryBuilder(entityManager);
    }

    @Override
    public List<Hotel> findHotels(Optional<Location> location,
                                  Optional<Price> minPrice,
                                  Optional<Price> maxPrice) {

        CriteriaQuery<HotelDto> criteriaQuery = hotelCriteriaQueryBuilder.buildFrom(location, minPrice, maxPrice);

        List<Hotel> hotels = entityManager.createQuery(criteriaQuery).getResultList()
                .stream()
                .map(
                        hotelDto ->
                                mapIntoEntity(
                                        hotelDto.id().toString(),
                                        hotelDto.name(),
                                        hotelDto.description(),
                                        hotelDto.location(),
                                        hotelDto.totalPrice(),
                                        hotelDto.imageURL()
                                )
                )
                .toList();

        hotels.forEach(
                h -> {
                    Query query = entityManager
                            .createNativeQuery("""
                                            SELECT r.id, r.rating, r.comment, r.hotel_id
                                            FROM reviews r
                                            WHERE r.hotel_id = :hotel_id
                                            """,
                                    Review.class
                            )
                            .setParameter(
                                    "hotel_id",
                                    UUID.fromString(h.getIdentifier().value())
                            );

                    List<Review> reviews = query.getResultList();

                    h.addReviews(
                            reviews.stream()
                                    .map(
                                            r ->
                                                    new org.egualpam.services.hotel.rating.domain.reviews.Review(
                                                            new Identifier(r.getId().toString()),
                                                            new Identifier(r.getHotelId().toString()),
                                                            new Rating(r.getRating()),
                                                            new Comment(r.getComment())
                                                    )
                                    )
                                    .toList()
                    );
                }
        );

        return hotels;
    }
}