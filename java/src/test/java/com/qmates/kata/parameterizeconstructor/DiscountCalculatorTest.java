package com.qmates.kata.parameterizeconstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.qmates.kata.domain.Coupon;
import com.qmates.kata.domain.Customer;
import com.qmates.kata.domain.Order;
import com.qmates.kata.domain.OrderLine;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

class DiscountCalculatorTest {

  private static Order order() {
    return new Order(
        "order-2",
        new Customer("cus-2", "linus@example.com", "Linus"),
        List.of(new OrderLine("MUG-1", "Mug", 4, 1000)),
        "tok_visa",
        null,
        "US",
        1200);
  }

  // This test is RED on purpose.
  //
  // We want to verify the "expired coupon" branch deterministically. The coupon
  // below expires on 2099-01-01. To prove it is treated as expired, the test
  // must run "as if" the current time were the year 2100 — but the calculator
  // builds its own SystemClock() and is locked to the real clock (today). With
  // a real clock, a 2099 coupon still looks valid, so the discount is applied
  // and this assertion fails.
  //
  // YOUR TASK: apply PARAMETERIZE CONSTRUCTOR (see README.md) so a fixed clock
  // can be injected, then drive the test from a controlled "now". Do not change
  // the production default behaviour for callers that pass no clock.
  @Test
  void rejects_a_coupon_that_has_expired_relative_to_the_current_time() {
    Instant fixedNow = Instant.parse("2100-06-01T00:00:00Z");
    Coupon coupon = new Coupon("SAVE10", 10, Instant.parse("2099-01-01T00:00:00Z"));
    SystemClock clock = new FixedClock(fixedNow);
    DiscountCalculator calculator = new DiscountCalculator(clock);

    PricedOrder priced = calculator.price(order(), coupon);

    assertEquals("coupon expired", priced.rejectedReason());
    assertEquals(0, priced.discountCents());
    assertEquals(4000, priced.subtotalCents());
    assertEquals(4000, priced.totalCents());
  }

  // Already green and time-independent: a missing coupon is always a no-op.
  @Test
  void applies_no_discount_when_there_is_no_coupon() {
    DiscountCalculator calculator = new DiscountCalculator();

    PricedOrder priced = calculator.price(order(), null);

    assertEquals(0, priced.discountCents());
    assertEquals(4000, priced.subtotalCents());
    assertEquals(4000, priced.totalCents());
    assertNull(priced.rejectedReason());
  }

  @Test
  void applies_discount_with_non_expired_coupon() {
    Instant fixedNow = Instant.parse("2026-07-14T08:00:00Z");
    Coupon coupon = new Coupon("SAVE10", 10, Instant.parse("2099-01-01T00:00:00Z"));
    SystemClock clock = new FixedClock(fixedNow);
    DiscountCalculator calculator = new DiscountCalculator(clock);

    PricedOrder priced = calculator.price(order(), coupon);

    assertEquals(400, priced.discountCents());
    assertEquals(4000, priced.subtotalCents());
    assertEquals(4000 - 400, priced.totalCents());
    assertNull(priced.rejectedReason());
  }

}
