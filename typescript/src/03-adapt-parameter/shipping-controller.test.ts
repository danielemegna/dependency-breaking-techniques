import { describe, expect, it } from "vitest";
import { ShippingController, type ShippingRequest } from "./shipping-controller.js";

describe("ShippingController", () => {
  // This test is RED on purpose.
  //
  // We want to test the rate arithmetic with a plain, hand-built request — no
  // sockets, no streams. But `quote()` demands a Node `IncomingMessage` and
  // calls `req.on('data' | 'end')` on it. The plain object below has no `.on`
  // method, so the call throws "req.on is not a function".
  //
  // YOUR TASK: apply ADAPT PARAMETER (see README.md). Introduce a small request
  // type that carries only what the quote logic needs, make `quote()` (or a new
  // method) accept that type, and provide an adapter that maps a real
  // IncomingMessage onto it. Then this plain object will be enough.
  it("quotes a domestic shipment from a plain request", async () => {
    const controller = new ShippingController();

    // A simple stand-in for an HTTP request. No streams involved.
    const request: ShippingRequest = {
      headers: { "x-destination-country": "US" },
      body: JSON.stringify({ weightGrams: 2000 }),
    };

    // NOTE: this line will not type-check / will throw until you adapt the
    // parameter. Once you do, pass `request` (or an adapted form of it).
    const quote = await controller.quoteOnShippingRequest(request);

    // base 500 + 2kg * 250 = 1000 -> 1000, no international surcharge
    expect(quote.costCents).toBe(1000);
    expect(quote.destinationCountry).toBe("US");
  });

  it("adds the international surcharge for non-US destinations", async () => {
    const controller = new ShippingController();

    const request = {
      headers: { "x-destination-country": "IT" },
      body: JSON.stringify({ weightGrams: 1000 }),
    };

    const quote = await controller.quoteOnShippingRequest(request as never);

    // base 500 + 1kg * 250 = 750, + 1500 international = 2250
    expect(quote.costCents).toBe(2250);
    expect(quote.destinationCountry).toBe("IT");
  });
});
