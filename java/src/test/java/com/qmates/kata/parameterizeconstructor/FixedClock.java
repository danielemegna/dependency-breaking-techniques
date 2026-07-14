package com.qmates.kata.parameterizeconstructor;

import java.time.Instant;

public class FixedClock extends SystemClock {
  private final Instant fixedNow;

  public FixedClock(Instant fixedNow) {
    this.fixedNow = fixedNow;
  }

  @Override
  public Instant now() {
    return this.fixedNow;
  }

}
