package com.qmates.kata.adaptparameter;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Produces a shipping quote for an incoming HTTP request.
 *
 * <p>The interesting logic is the rate calculation: a flat base fee plus a
 * per-kilogram rate, with a surcharge for international destinations. We would
 * love to unit-test that arithmetic.
 *
 * <p>The obstacle is the parameter type. {@link #quote(HttpServletRequest)}
 * accepts a raw {@link HttpServletRequest}: it reads a header and then reads the
 * request body via {@code request.getReader()}. Constructing a believable
 * {@code HttpServletRequest} in a unit test means a servlet container or a
 * mocking framework — far more machinery than the rate logic deserves.
 */
public class ShippingController {

  private static final long BASE_FEE_CENTS = 500;
  private static final long PER_KG_CENTS = 250;
  private static final long INTERNATIONAL_SURCHARGE_CENTS = 1500;

  public ShippingQuote quote(HttpServletRequest request) throws IOException {
    ShippingRequest shippingRequest = new WrapperShippingRequest(request);
    return quote(shippingRequest);
  }

  public ShippingQuote quote(ShippingRequest request) throws IOException {
    String destinationCountry = request.destinationCountry();
    if (destinationCountry == null) {
      destinationCountry = "US";
    }

    int weightGrams = request.weightInGrams();

    double kilos = weightGrams / 1000.0;
    long costCents = BASE_FEE_CENTS + Math.round(kilos * PER_KG_CENTS);
    if (!destinationCountry.equals("US")) {
      costCents += INTERNATIONAL_SURCHARGE_CENTS;
    }

    return new ShippingQuote(destinationCountry, weightGrams, costCents);
  }

}
