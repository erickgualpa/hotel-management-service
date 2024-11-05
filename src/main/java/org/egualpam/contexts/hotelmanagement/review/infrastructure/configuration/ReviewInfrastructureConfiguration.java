package org.egualpam.contexts.hotelmanagement.review.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviews;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviewsQuery;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.CreateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.UpdateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple.FindReviewsQueryHandler;
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
        .withHandler(
            CreateReviewCommand.class,
            new CreateReviewCommandHandler(transactionTemplate, createReview))
        .withHandler(
            UpdateReviewCommand.class,
            new UpdateReviewCommandHandler(transactionTemplate, updateReview));
  }

  @Bean
  public SimpleQueryBusConfiguration reviewsSimpleQueryBusConfiguration(FindReviews findReviews) {
    return new SimpleQueryBusConfiguration()
        .withHandler(FindReviewsQuery.class, new FindReviewsQueryHandler(findReviews));
  }

  @Bean
  public InternalEventBus springInternalEventBus(
      ApplicationEventPublisher applicationEventPublisher) {
    return new SpringInternalEventBus(applicationEventPublisher);
  }
}
