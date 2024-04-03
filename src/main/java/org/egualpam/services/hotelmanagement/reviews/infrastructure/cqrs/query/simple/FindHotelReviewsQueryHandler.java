package org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.reviews.application.query.MultipleReviewsView;
import org.egualpam.services.hotelmanagement.reviews.domain.ReviewCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.View;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindHotelReviewsQueryHandler implements QueryHandler<FindHotelReviewsQuery> {

    private final ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier;

    @Override
    public View handle(FindHotelReviewsQuery query) {
        return multipleReviewsViewSupplier.get(
                new ReviewCriteria(query.getHotelIdentifier())
        );
    }
}
