package org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.reviews.application.query.FindReviewsQuery;
import org.egualpam.services.hotelmanagement.reviews.application.query.MultipleReviewsView;
import org.egualpam.services.hotelmanagement.reviews.domain.ReviewCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.View;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindReviewsQueryHandler implements QueryHandler {

    private final ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier;

    @Override
    public View handle(Query query) {
        FindReviewsQuery findReviewsQuery = (FindReviewsQuery) query;
        return multipleReviewsViewSupplier.get(
                new ReviewCriteria(findReviewsQuery.getHotelIdentifier())
        );
    }

    @Override
    public Class<? extends Query> type() {
        return FindReviewsQuery.class;
    }
}
