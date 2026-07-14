package com.qmates.kata.extractinterface;

import java.util.Map;

/**
 * Concrete payment gateway that talks to Stripe over HTTP.
 *
 * <p>Notice the shape of the problem: this is a single, heavyweight, I/O-bound
 * class. It needs an API key, builds a real {@link HttpClient}, and posts to a
 * live endpoint. There is no abstraction between {@link OrderProcessor} and
 * this class — {@code OrderProcessor} names this concrete type directly.
 */
public class StripePaymentGateway implements ChargePaymentGateway {

  private final HttpClient http = new HttpClient();
  private final String baseUrl = "https://api.stripe.com/v1";
  private final String apiKey;

  public StripePaymentGateway(String apiKey) {
    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalArgumentException("StripePaymentGateway requires an API key.");
    }
    this.apiKey = apiKey;
  }

  @Override
  public ChargeResult charge(long amountCents, String paymentToken) {
    Map<String, Object> payload =
        Map.of(
            "amount", amountCents,
            "currency", "usd",
            "source", paymentToken,
            "key", apiKey);

    Map<String, Object> response = http.post(baseUrl + "/charges", payload);
    return ChargeResult.succeeded((String) response.get("id"));
  }

  /** Issues a refund for a previously successful charge. */
  public void refund(String chargeId) {
    http.post(baseUrl + "/refunds", Map.of("charge", chargeId));
  }
}
