import { HttpClient } from "./http-client.js";
import type { Cents } from "../domain.js";
import type ChargePaymentGateway from "./charge-payment-gateway";

export interface ChargeResult {
  success: boolean;
  /** Provider-side identifier for the charge, when successful. */
  chargeId?: string;
  /** Human-readable failure reason, when unsuccessful. */
  declineReason?: string;
}

/**
 * Concrete payment gateway that talks to Stripe over HTTP.
 *
 * Notice the shape of the problem: this is a single, heavyweight, I/O-bound
 * class. It needs an API key, builds a real `HttpClient`, and posts to a live
 * endpoint. There is no abstraction between `OrderProcessor` and this class —
 * `OrderProcessor` names this concrete type directly.
 */
export class StripePaymentGateway implements ChargePaymentGateway {
  private readonly http = new HttpClient();
  private readonly baseUrl = "https://api.stripe.com/v1";

  constructor(private readonly apiKey: string) {
    if (!apiKey) {
      throw new Error("StripePaymentGateway requires an API key.");
    }
  }

  async charge(amountCents: Cents, paymentToken: string): Promise<ChargeResult> {
    const response = (await this.http.post(`${this.baseUrl}/charges`, {
      amount: amountCents,
      currency: "usd",
      source: paymentToken,
      key: this.apiKey,
    })) as { id: string };

    return { success: true, chargeId: response.id };
  }

  /** Issues a refund for a previously successful charge. */
  async refund(chargeId: string): Promise<void> {
    await this.http.post(`${this.baseUrl}/refunds`, { charge: chargeId });
  }
}
