package com.qmates.kata.parameterizeconstructor;

import java.time.Instant;

/**
 * Provides the current wall-clock time.
 *
 * <p>In production we obviously want the real time. The trouble is <em>where</em>
 * it gets constructed: {@link DiscountCalculator} builds one of these inside its
 * own field initializer, so a test has no way to say "pretend it is some other
 * moment".
 *
 * <p>This class is intentionally non-final and its {@link #now()} method is
 * overridable.
 */
public class SystemClock {

  public Instant now() {
    return Instant.now();
  }
}
