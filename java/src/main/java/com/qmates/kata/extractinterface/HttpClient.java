package com.qmates.kata.extractinterface;

import java.util.Map;

/**
 * A tiny HTTP client. In the real application this wraps a real HTTP library
 * and talks to live third-party services over the network.
 *
 * <p>For this kata it deliberately refuses to make a real call, so that any
 * test that accidentally reaches the network fails loudly and instantly
 * instead of hanging or hitting Stripe for real.
 */
public class HttpClient {

  public Map<String, Object> post(String url, Map<String, Object> body) {
    throw new IllegalStateException(
        "HttpClient.post tried to make a real network call to " + url
            + ". Real I/O is not available in tests — you need a seam to substitute a fake."
            + " (payload had " + body.size() + " fields)");
  }
}
