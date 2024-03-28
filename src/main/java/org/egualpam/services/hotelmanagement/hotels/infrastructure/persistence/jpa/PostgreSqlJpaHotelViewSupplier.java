package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelCriteria;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;
import org.egualpam.services.hotelmanagement.domain.shared.UniqueId;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class PostgreSqlJpaHotelViewSupplier implements ViewSupplier<HotelView> {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaHotelViewSupplier(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public HotelView get(Criteria criteria) {
        // TODO: Avoid having optional access to the hotelId
        UniqueId hotelId = ((HotelCriteria) criteria).getHotelId().orElseThrow();
        PersistenceHotel persistenceHotel = entityManager.find(PersistenceHotel.class, hotelId.value());
        Optional<HotelView.Hotel> hotel = Optional.ofNullable(persistenceHotel).map(this::mapIntoViewHotel);
        return new HotelView(hotel);
    }

    private HotelView.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
        double averageRating = findReviewsByHotel
                .apply(persistenceHotel)
                .stream()
                .mapToDouble(PersistenceReview::getRating)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);
        return new HotelView.Hotel(
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
