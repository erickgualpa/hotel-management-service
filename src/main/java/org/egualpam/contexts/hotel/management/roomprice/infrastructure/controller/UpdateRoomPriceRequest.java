package org.egualpam.contexts.hotel.management.roomprice.infrastructure.controller;

public record UpdateRoomPriceRequest(String hotelId, String roomType, Price price) {
  record Price(String amount) {}

  public String priceAmount() {
    return this.price.amount;
  }
}
