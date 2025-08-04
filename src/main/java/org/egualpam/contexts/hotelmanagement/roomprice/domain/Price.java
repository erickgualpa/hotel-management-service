package org.egualpam.contexts.hotelmanagement.roomprice.domain;

public record Price(String amount, String currency) {

  public Price updateAmount(String newAmount) {
    return new Price(newAmount, this.currency);
  }

  private enum AllowedCurrency {
    EUR;
  }

  public static Price of(String amount) {
    return new Price(amount, AllowedCurrency.EUR.name());
  }
}
