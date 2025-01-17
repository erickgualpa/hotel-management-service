package org.egualpam.contexts.hotelmanagement.review.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReview;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviews;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncCreateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncCreateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncUpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.SyncUpdateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple.SyncFindReviewsQuery;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple.SyncFindReviewsQueryHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.readmodelsupplier.JpaManyReviewsReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.repository.JpaReviewRepository;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEventBus;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring.SpringInternalEventBus;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EntityScan("org.egualpam.contexts.hotelmanagement.review.infrastructure.repository")
public class ReviewInfrastructureConfiguration {

  @Bean
  public AggregateRepository<Review> reviewRepository(EntityManager entityManager) {
    return new JpaReviewRepository(entityManager);
  }

  @Bean
  public ReadModelSupplier<ReviewCriteria, ManyReviews> manyReviewsReadModelSupplier(
      EntityManager entityManager) {
    return new JpaManyReviewsReadModelSupplier(entityManager);
  }

  @Bean
  public SimpleCommandBusConfiguration reviewsSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate,
      CreateReview createReview,
      UpdateReview updateReview) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncCreateReviewCommand.class,
            new SyncCreateReviewCommandHandler(transactionTemplate, createReview))
        .handling(
            SyncUpdateReviewCommand.class,
            new SyncUpdateReviewCommandHandler(transactionTemplate, updateReview));
  }

  @Bean
  public SimpleQueryBusConfiguration reviewsSimpleQueryBusConfiguration(FindReviews findReviews) {
    return new SimpleQueryBusConfiguration()
        .handling(SyncFindReviewsQuery.class, new SyncFindReviewsQueryHandler(findReviews));
  }

  @Bean
  public InternalEventBus springInternalEventBus(
      ApplicationEventPublisher applicationEventPublisher) {
    return new SpringInternalEventBus(applicationEventPublisher);
  }
}
