package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.controller;

public record UpdateRoomPriceRequest(String hotelId, String roomType, Price price) {
  record Price(String amount) {}

  public String priceAmount() {
    return this.price.amount;
  }
}
