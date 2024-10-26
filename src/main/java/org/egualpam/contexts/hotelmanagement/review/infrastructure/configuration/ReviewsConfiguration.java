package org.egualpam.contexts.hotelmanagement.review.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviewsQuery;
import org.egualpam.contexts.hotelmanagement.review.application.query.MultipleReviewsView;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.CreateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple.UpdateReviewCommandHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.query.simple.FindReviewsQueryHandler;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.persistence.jpa.PostgreSqlJpaMultipleReviewsViewSupplier;
import org.egualpam.contexts.hotelmanagement.review.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewsConfiguration {

  @Bean
  public AggregateRepository<Review> reviewRepository(EntityManager entityManager) {
    return new PostgreSqlJpaReviewRepository(entityManager);
  }

  @Bean
  public ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier(
      EntityManager entityManager) {
    return new PostgreSqlJpaMultipleReviewsViewSupplier(entityManager);
  }

  @Bean
  public SimpleCommandBusConfiguration reviewsSimpleCommandBusConfiguration(
      AggregateRepository<Review> reviewRepository, EventBus eventBus) {
    return new SimpleCommandBusConfiguration()
        .withHandler(
            CreateReviewCommand.class, new CreateReviewCommandHandler(reviewRepository, eventBus))
        .withHandler(
            UpdateReviewCommand.class, new UpdateReviewCommandHandler(reviewRepository, eventBus));
  }

  @Bean
  public SimpleQueryBusConfiguration reviewsSimpleQueryBusConfiguration(
      ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier) {
    return new SimpleQueryBusConfiguration()
        .withHandler(
            FindReviewsQuery.class, new FindReviewsQueryHandler(multipleReviewsViewSupplier));
  }
}
