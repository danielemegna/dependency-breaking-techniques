package com.qmates.kata.parameterizeconstructor;

import com.qmates.kata.domain.Coupon;
import com.qmates.kata.domain.Order;
import com.qmates.kata.domain.Orders;

/**
 * Applies a coupon to an order, honouring the coupon's expiry.
 *
 * <p>The behaviour we want to test is time-dependent: an expired coupon must be
 * rejected and a still-valid one must be applied. But {@code clock} is built
 * with {@code new SystemClock()} right here in the field initializer, so the
 * calculator is hard-wired to "now". A test cannot pin the clock to a known
 * instant, which makes the expiry branch effectively untestable.
 */
public class DiscountCalculator {

  private final SystemClock clock = new SystemClock();

  public PricedOrder price(Order order, Coupon coupon) {
    long subtotal = Orders.subtotalCents(order);

    if (coupon == null) {
      return PricedOrder.of(subtotal, 0);
    }

    if (!coupon.expiresAt().isAfter(clock.now())) {
      return PricedOrder.rejected(subtotal, "coupon expired");
    }

    long discount = Math.round(subtotal * coupon.percentOff() / 100.0);
    return PricedOrder.of(subtotal, discount);
  }
}
