/**
 * A tiny HTTP client. In the real application this wraps `fetch` and talks to
 * live third-party services over the network.
 *
 * For this kata it deliberately refuses to make a real call, so that any test
 * that accidentally reaches the network fails loudly and instantly instead of
 * hanging or hitting Stripe for real.
 */
export class HttpClient {
  async post(url: string, _body: unknown): Promise<unknown> {
    throw new Error(
      `HttpClient.post tried to make a real network call to ${url}. ` +
        `Real I/O is not available in tests — you need a seam to substitute a fake.`,
    );
  }
}
