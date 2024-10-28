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
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.CreateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.UpdateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple.FindReviewsQueryHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.readmodelsupplier.PostgreSqlJpaManyReviewsReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.repository.PostgreSqlJpaReviewRepository;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEventBus;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring.SpringInternalEventBus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewInfrastructureConfiguration {

  @Bean
  public AggregateRepository<Review> reviewRepository(EntityManager entityManager) {
    return new PostgreSqlJpaReviewRepository(entityManager);
  }

  @Bean
  public ReadModelSupplier<ManyReviews> manyReviewsReadModelSupplier(EntityManager entityManager) {
    return new PostgreSqlJpaManyReviewsReadModelSupplier(entityManager);
  }

  @Bean
  public SimpleCommandBusConfiguration reviewsSimpleCommandBusConfiguration(
      CreateReview createReview, UpdateReview updateReview) {
    return new SimpleCommandBusConfiguration()
        .withHandler(CreateReviewCommand.class, new CreateReviewCommandHandler(createReview))
        .withHandler(UpdateReviewCommand.class, new UpdateReviewCommandHandler(updateReview));
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