import type { IncomingMessage } from "node:http";
import type { Cents } from "../domain.js";

export interface ShippingQuote {
  destinationCountry: string;
  weightGrams: number;
  costCents: Cents;
}

export interface ShippingRequest {
  headers: Record<string, any>,
  body: string
}

/**
 * Produces a shipping quote for an incoming HTTP request.
 *
 * The interesting logic is the rate calculation: a flat base fee plus a
 * per-kilogram rate, with a surcharge for international destinations. We would
 * love to unit-test that arithmetic.
 *
 * The obstacle is the parameter type. `quote()` accepts a raw Node
 * `IncomingMessage`: it reads a header and then *streams* the request body via
 * `req.on('data' | 'end')`. Constructing a believable `IncomingMessage` in a
 * unit test means faking a readable socket stream — far more machinery than the
 * rate logic deserves.
 */
export class ShippingController {
  private readonly baseFeeCents = 500;
  private readonly perKgCents = 250;
  private readonly internationalSurchargeCents = 1500;

  async quote(req: IncomingMessage): Promise<ShippingQuote> {
    const body = await this.readBody(req);
    return this.quoteOnShippingRequest({
      headers: req.headers,
      body: body,
    })
  }

  async quoteOnShippingRequest(req: ShippingRequest): Promise<ShippingQuote> {
    const destinationCountry = (req.headers["x-destination-country"] as string | undefined) ?? "US";
    const { weightGrams } = JSON.parse(req.body) as { weightGrams: number };

    const kilos = weightGrams / 1000;
    let costCents = this.baseFeeCents + Math.round(kilos * this.perKgCents);
    if (destinationCountry !== "US") {
      costCents += this.internationalSurchargeCents;
    }

    return { destinationCountry, weightGrams, costCents };
  }

  private readBody(req: IncomingMessage): Promise<string> {
    return new Promise((resolve, reject) => {
      let raw = "";
      req.on("data", (chunk) => {
        raw += chunk;
      });
      req.on("end", () => resolve(raw));
      req.on("error", reject);
    });
  }
}
