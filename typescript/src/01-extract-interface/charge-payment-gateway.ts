import type { Cents } from "../domain";
import type { ChargeResult } from "./stripe-payment-gateway";

export default interface ChargePaymentGateway {
  charge(amountCents: Cents, paymentToken: string): Promise<ChargeResult>;
}
