package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.HotelDto;
import org.egualpam.services.hotel.rating.infrastructure.persistance.ReviewDto;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

public class PostgreSqlJpaHotelRepository extends HotelRepository {

    private final EntityManager entityManager;
    private final HotelCriteriaQueryBuilder hotelCriteriaQueryBuilder;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.hotelCriteriaQueryBuilder = new HotelCriteriaQueryBuilder(entityManager);
    }

    @Override
    public List<Hotel> findHotelsMatchingQuery(HotelQuery hotelQuery) {

        CriteriaQuery<HotelDto> criteriaQuery = hotelCriteriaQueryBuilder.buildFrom(hotelQuery);

        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(
                        hotelDto ->
                                buildEntity(
                                        hotelDto.id().toString(),
                                        hotelDto.name(),
                                        hotelDto.description(),
                                        hotelDto.location(),
                                        hotelDto.totalPrice(),
                                        hotelDto.imageURL(),
                                        findReviewsByHotelId(hotelDto.id()).stream()
                                                .map(review ->
                                                        buildReviewEntity(
                                                                review.identifier(),
                                                                review.rating(),
                                                                review.comment()))
                                                .collect(Collectors.toList()))
                )
                .collect(Collectors.toList());
    }

    private List<ReviewDto> findReviewsByHotelId(Long hotelId) {
        List<Review> reviews =
                entityManager.createNativeQuery("""
                                        SELECT r.id, r.rating, r.comment, r.hotel_id
                                        FROM reviews r
                                        WHERE r.hotel_id = :hotel_id
                                        """,
                                Review.class)
                        .setParameter("hotel_id", hotelId)
                        .getResultList();

        return reviews.stream()
                .map(review ->
                        new ReviewDto(
                                review.getId().toString(),
                                review.getRating(),
                                review.getComment()))
                .collect(Collectors.toList());
    }
}