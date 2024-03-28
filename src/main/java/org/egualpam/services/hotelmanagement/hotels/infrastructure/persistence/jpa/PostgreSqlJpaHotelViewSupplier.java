package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotels.application.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class PostgreSqlJpaHotelViewSupplier implements ViewSupplier<SingleHotelView> {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaHotelViewSupplier(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public SingleHotelView get(Criteria criteria) {
        // TODO: Avoid having optional access to the hotelId
        UniqueId hotelId = ((HotelCriteria) criteria).getHotelId().orElseThrow();
        PersistenceHotel persistenceHotel = entityManager.find(PersistenceHotel.class, hotelId.value());
        Optional<SingleHotelView.Hotel> hotel = Optional.ofNullable(persistenceHotel).map(this::mapIntoViewHotel);
        return new SingleHotelView(hotel);
    }

    private SingleHotelView.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
        double averageRating = findReviewsByHotel
                .apply(persistenceHotel)
                .stream()
                .mapToDouble(PersistenceReview::getRating)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);
        return new SingleHotelView.Hotel(
                persistenceHotel.getId().toString(),
                persistenceHotel.getName(),
                persistenceHotel.getDescription(),
                persistenceHotel.getLocation(),
                persistenceHotel.getTotalPrice(),
                persistenceHotel.getImageURL(),
                averageRating
        );
    }
}
