import type { ChargeResult } from "./stripe-payment-gateway";
import type { Cents } from "../domain";
import type ChargePaymentGateway from "./charge-payment-gateway";

export default class FakeChargePaymentGateway implements ChargePaymentGateway {
  private readonly fixedChargeResult: ChargeResult;

  constructor(fixedChargeResult: ChargeResult) {
    this.fixedChargeResult = fixedChargeResult;
  }

  charge(amountCents: Cents, paymentToken: string): Promise<ChargeResult> {
    return Promise.resolve(this.fixedChargeResult);
  }
}
