/**
 * Provides the current wall-clock time.
 *
 * In production we obviously want the real time. The trouble is *where* it gets
 * constructed: `DiscountCalculator` builds one of these inside its own
 * constructor, so a test has no way to say "pretend it is some other moment".
 */
export class SystemClock {
  now(): Date {
    return new Date();
  }
}
