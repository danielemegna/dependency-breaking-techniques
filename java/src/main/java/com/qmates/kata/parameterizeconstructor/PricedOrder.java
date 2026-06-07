package com.qmates.kata.parameterizeconstructor;

/**
 * Result of pricing an order with a (possibly expired or absent) coupon.
 *
 * @param rejectedReason why the coupon was not applied, or {@code null}.
 */
public record PricedOrder(
    long subtotalCents, long discountCents, long totalCents, String rejectedReason) {

  public static PricedOrder of(long subtotalCents, long discountCents) {
    return new PricedOrder(subtotalCents, discountCents, subtotalCents - discountCents, null);
  }

  public static PricedOrder rejected(long subtotalCents, String reason) {
    return new PricedOrder(subtotalCents, 0, subtotalCents, reason);
  }
}
