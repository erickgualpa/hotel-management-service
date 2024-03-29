package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotels.domain.Hotel;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.exception.ActionNotYetImplemented;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public final class PostgreSqlJpaHotelRepository implements AggregateRepository<Hotel> {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public Optional<Hotel> find(AggregateId id) {
        PersistenceHotel persistenceHotel = entityManager.find(PersistenceHotel.class, UUID.fromString(id.value()));
        return Optional.ofNullable(persistenceHotel)
                .map(this::mapResultIntoHotel);
    }

    private Hotel mapResultIntoHotel(PersistenceHotel persistenceHotel) {
        String name = persistenceHotel.getName();
        String description = persistenceHotel.getDescription();
        String location = persistenceHotel.getLocation();
        Integer totalPrice = persistenceHotel.getTotalPrice();
        String imageURL = persistenceHotel.getImageURL();
        Double averageRating =
                findReviewsByHotel
                        .apply(persistenceHotel)
                        .stream()
                        .mapToDouble(PersistenceReview::getRating)
                        .filter(Objects::nonNull)
                        .average()
                        .orElse(0.0);
        return new Hotel(
                persistenceHotel.getId().toString(),
                name,
                description,
                location,
                totalPrice,
                imageURL,
                averageRating
        );
    }

    @Override
    public void save(Hotel aggregate) {
        throw new ActionNotYetImplemented();
    }
}