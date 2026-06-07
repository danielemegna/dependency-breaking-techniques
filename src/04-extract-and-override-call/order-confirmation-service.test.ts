import { describe, expect, it } from "vitest";
import { OrderConfirmationService } from "./order-confirmation-service.js";
import type { Order } from "../domain.js";

function buildOrder(): Order {
  return {
    id: "order-4",
    customer: { id: "cus-4", email: "grace@example.com", name: "Grace" },
    lines: [
      { sku: "PEN-1", description: "Pen", quantity: 3, unitPriceCents: 150 },
    ],
    paymentToken: "tok_visa",
    destinationCountry: "US",
    weightGrams: 50,
  };
}

describe("OrderConfirmationService", () => {
  // This test is RED on purpose.
  //
  // `confirm()` calls the module-level `sendEmail` directly, and the real
  // transport throws rather than send for real. There is no object or
  // constructor parameter to intercept the call, so we cannot observe what was
  // sent — and the throw fails the test.
  //
  // YOUR TASK: apply EXTRACT AND OVERRIDE CALL (see README.md). Extract the
  // `sendEmail(...)` call into a protected method, then create a testing
  // subclass here that overrides it to capture the arguments instead of
  // sending. Then assert against what was captured. Do not change the email's
  // content or weaken the assertions.
  it("emails the customer a confirmation naming the order", async () => {
    const service = new OrderConfirmationService();

    const confirmation = await service.confirm(buildOrder());

    // Returned record (this part does not require the email to be observed):
    expect(confirmation.emailedTo).toBe("grace@example.com");
    expect(confirmation.totalCents).toBe(450);

    // These assertions require you to capture the outgoing email. Replace the
    // service above with your testing subclass and assert on the captured call:
    //
    //   expect(captured.to).toBe("grace@example.com");
    //   expect(captured.subject).toContain("order-4");
    //
    // The placeholders below are intentionally failing until you do that.
    const captured: { to: string; subject: string } | undefined = undefined;
    expect(captured).toBeDefined();
    expect(captured!.subject).toContain("order-4");
  });
});
