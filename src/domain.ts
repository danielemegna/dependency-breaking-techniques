/**
 * Shared domain model for the fictional "online store" backend.
 *
 * All four exercises operate on slices of this same domain, so the legacy
 * snippets feel like parts of one real application rather than disconnected
 * toys. You should not need to change anything in this file to complete the
 * exercises.
 */

/** Money is represented as a whole number of cents to avoid float rounding. */
export type Cents = number;

export interface Customer {
  id: string;
  email: string;
  name: string;
}

export interface OrderLine {
  sku: string;
  description: string;
  quantity: number;
  unitPriceCents: Cents;
}

export interface Order {
  id: string;
  customer: Customer;
  lines: OrderLine[];
  /** Payment token captured at checkout (e.g. from a card form). */
  paymentToken: string;
  /** Optional coupon code the customer applied at checkout. */
  couponCode?: string;
  /** Destination country code, used for shipping quotes. */
  destinationCountry: string;
  /** Total package weight in grams, used for shipping quotes. */
  weightGrams: number;
}

export interface Coupon {
  code: string;
  /** Percentage off, expressed as 0..100. */
  percentOff: number;
  /** The coupon is invalid on or after this instant. */
  expiresAt: Date;
}

/** Sum of every line's quantity * unit price. */
export function orderSubtotalCents(order: Order): Cents {
  return order.lines.reduce(
    (total, line) => total + line.quantity * line.unitPriceCents,
    0,
  );
}
