package org.egualpam.contexts.hotelmanagement.review.application.command;

import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;

public final class CreateReviewCommand implements Command {

  private final String reviewIdentifier;
  private final String hotelIdentifier;
  private final Integer rating;
  private final String comment;

  public CreateReviewCommand(
      String reviewIdentifier, String hotelIdentifier, Integer rating, String comment) {
    this.reviewIdentifier = reviewIdentifier;
    this.hotelIdentifier = hotelIdentifier;
    this.rating = rating;
    this.comment = comment;
  }

  public String getReviewIdentifier() {
    return reviewIdentifier;
  }

  public String getHotelIdentifier() {
    return hotelIdentifier;
  }

  public Integer getRating() {
    return rating;
  }

  public String getComment() {
    return comment;
  }
}
