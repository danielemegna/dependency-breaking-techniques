import { orderSubtotalCents, type Order } from "../domain.js";
import type ChargePaymentGateway from "./charge-payment-gateway";

export interface ProcessResult {
  status: "paid" | "declined";
  chargeId?: string;
  reason?: string;
}

/**
 * Turns a checked-out order into a payment.
 *
 * The logic here is what we actually want to test: empty orders are rejected,
 * the subtotal is charged, a successful charge yields a "paid" result, and a
 * decline yields a "declined" result.
 *
 * The obstacle: the collaborator is named by its *concrete* type,
 * `StripePaymentGateway`. To exercise `process()` we are forced to construct a
 * real Stripe gateway, which reaches for the network. There is no narrow type
 * we can implement with a test double.
 */
export class OrderProcessor {
  constructor(private readonly gateway: ChargePaymentGateway) { }

  async process(order: Order): Promise<ProcessResult> {
    if (order.lines.length === 0) {
      return { status: "declined", reason: "empty order" };
    }

    const amount = orderSubtotalCents(order);
    const result = await this.gateway.charge(amount, order.paymentToken);

    if (result.success) {
      return { status: "paid", chargeId: result.chargeId };
    }
    return { status: "declined", reason: result.declineReason };
  }
}
