import { describe, expect, it } from "vitest";
import { OrderProcessor } from "./order-processor.js";
import type { Order } from "../domain.js";
import FakeChargePaymentGateway from "./fake-charge-payment-gateway";

function buildOrder(overrides: Partial<Order> = {}): Order {
  return {
    id: "order-1",
    customer: { id: "cus-1", email: "ada@example.com", name: "Ada" },
    lines: [
      { sku: "BOOK-1", description: "Refactoring", quantity: 2, unitPriceCents: 3500 },
    ],
    paymentToken: "tok_visa",
    destinationCountry: "US",
    weightGrams: 800,
    ...overrides,
  };
}

describe("OrderProcessor", () => {
  // This test is RED on purpose.
  //
  // It can only build the gateway as a real `StripePaymentGateway`, which
  // reaches for the network and throws. There is no abstraction we can stand a
  // test double behind.
  //
  // YOUR TASK: apply EXTRACT INTERFACE (see README.md) so that you can replace
  // the lines below with an injected fake gateway, then make the assertions
  // pass. Do not weaken the assertions — break the dependency instead.
  it("charges the order subtotal and reports the order as paid", async () => {
    const gateway = new FakeChargePaymentGateway({ success: true, chargeId: "charge-id", declineReason: undefined })
    const processor = new OrderProcessor(gateway);

    const result = await processor.process(buildOrder());

    expect(result.status).toBe("paid");
    expect(result.chargeId).toBe("charge-id");
    expect(result.reason).toBeUndefined();
  });

  // Already green: no collaborator is touched on this path. Keep it green while
  // you refactor.
  it("declines an empty order without touching the gateway", async () => {
    const gateway = new FakeChargePaymentGateway({ success: true, chargeId: "charge-id", declineReason: undefined })
    const processor = new OrderProcessor(gateway);

    const result = await processor.process(buildOrder({ lines: [] }));

    expect(result.status).toBe("declined");
    expect(result.reason).toBe("empty order");
    expect(result.chargeId).toBeUndefined();
  });

  it("declines an order on failure from gateway", async () => {
    const gateway = new FakeChargePaymentGateway({
      success: false,
      chargeId: undefined,
      declineReason: "insufficient-credit"
    })
    const processor = new OrderProcessor(gateway);

    const result = await processor.process(buildOrder());

    expect(result.status).toBe("declined");
    expect(result.reason).toBe("insufficient-credit");
    expect(result.chargeId).toBeUndefined();
  });

});
