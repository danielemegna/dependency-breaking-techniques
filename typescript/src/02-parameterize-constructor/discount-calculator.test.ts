import { describe, expect, it } from "vitest";
import { DiscountCalculator } from "./discount-calculator.js";
import type { Coupon, Order } from "../domain.js";

function buildOrder(): Order {
  return {
    id: "order-2",
    customer: { id: "cus-2", email: "linus@example.com", name: "Linus" },
    lines: [
      { sku: "MUG-1", description: "Mug", quantity: 4, unitPriceCents: 1000 },
    ],
    paymentToken: "tok_visa",
    destinationCountry: "US",
    weightGrams: 1200,
  };
}

describe("DiscountCalculator", () => {
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
  it("rejects a coupon that has expired relative to the current time", () => {
    const fixedNow = new Date("2100-06-01T00:00:00Z");
    const coupon: Coupon = {
      code: "SAVE10",
      percentOff: 10,
      expiresAt: new Date("2099-01-01T00:00:00Z"),
    };

    // Once you have a seam, construct the calculator so that "now" === fixedNow.
    const calculator = new DiscountCalculator();
    void fixedNow; // currently unused — there is nowhere to inject it yet.

    const priced = calculator.price(buildOrder(), coupon);

    expect(priced.rejectedReason).toBe("coupon expired");
    expect(priced.discountCents).toBe(0);
    expect(priced.totalCents).toBe(4000);
  });

  // Already green and time-independent: a missing coupon is always a no-op.
  it("applies no discount when there is no coupon", () => {
    const calculator = new DiscountCalculator();

    const priced = calculator.price(buildOrder(), undefined);

    expect(priced.discountCents).toBe(0);
    expect(priced.totalCents).toBe(4000);
  });
});
