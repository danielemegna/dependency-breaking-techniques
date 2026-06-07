import { SystemClock } from "./system-clock.js";
import { orderSubtotalCents, type Cents, type Coupon, type Order } from "../domain.js";

export interface PricedOrder {
  subtotalCents: Cents;
  discountCents: Cents;
  totalCents: Cents;
  /** Why the coupon was not applied, if it was rejected. */
  rejectedReason?: string;
}

/**
 * Applies a coupon to an order, honouring the coupon's expiry.
 *
 * The behaviour we want to test is time-dependent: an expired coupon must be
 * rejected and a still-valid one must be applied. But `this.clock` is built
 * with `new SystemClock()` right here in the constructor, so the calculator is
 * hard-wired to "now". A test cannot pin the clock to a known instant, which
 * makes the expiry branch effectively untestable.
 */
export class DiscountCalculator {
  private readonly clock = new SystemClock();

  price(order: Order, coupon: Coupon | undefined): PricedOrder {
    const subtotalCents = orderSubtotalCents(order);

    if (!coupon) {
      return { subtotalCents, discountCents: 0, totalCents: subtotalCents };
    }

    if (coupon.expiresAt.getTime() <= this.clock.now().getTime()) {
      return {
        subtotalCents,
        discountCents: 0,
        totalCents: subtotalCents,
        rejectedReason: "coupon expired",
      };
    }

    const discountCents = Math.round((subtotalCents * coupon.percentOff) / 100);
    return {
      subtotalCents,
      discountCents,
      totalCents: subtotalCents - discountCents,
    };
  }
}
