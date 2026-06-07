# Exercise 02 — Parameterize Constructor

## The technique

> **Parameterize Constructor** — When a class creates one of its dependencies
> internally, add that dependency as a constructor parameter so callers (and
> tests) can supply their own. Keep the old behaviour by defaulting to the
> object that used to be created inline.

This breaks the dependency at the point where it is born. Instead of a class
secretly reaching out and building a collaborator, the collaborator is handed
to it. Tests can then pass a controlled substitute.

### When to use it

Reach for Parameterize Constructor when:

- A constructor (or field initializer) contains a hard-coded `new Something()`,
- That `Something` makes the class hard to test — it reads the clock, the file
  system, the environment, a random source, etc., and
- You want existing callers to keep working untouched.

The smell here is "this class decides for itself what to depend on, and I can't
talk it out of it."

### Mechanics (Java)

1. Find the internal construction
   (here: `private final SystemClock clock = new SystemClock();`).
2. Identify the type being constructed (`SystemClock`).
3. Add a constructor that takes that type and assigns the field.
4. **Java has no default parameter values**, so preserve existing callers with
   an *overloaded* no-arg constructor that delegates:
   ```java
   public DiscountCalculator() {
     this(new SystemClock());   // unchanged behaviour for everyone else
   }
   public DiscountCalculator(SystemClock clock) {
     this.clock = clock;
   }
   ```
   (The field can no longer be initialized inline — initialize it in the
   constructor and drop `final` only if you must; here you can keep it `final`.)
5. In the test, pass a controlled substitute. `SystemClock` is non-final with an
   overridable `now()`, so an anonymous subclass that returns a fixed `Instant`
   is enough.
6. Run the tests.

## This exercise

`DiscountCalculator` rejects expired coupons by comparing each coupon's
`expiresAt` against the current time, which it reads from a `SystemClock` that
it constructs **inside its own field initializer**. `DiscountCalculatorTest`
needs to pin "now" to a known instant to prove the expiry branch works — and it
can't.

### Goal

Make the red test pass by giving the calculator a **seam for time**. After your
change:

- A test can construct the calculator with a fixed clock and assert the expiry
  behaviour deterministically.
- Production callers that write `new DiscountCalculator()` must still get the
  real system clock — **no caller should have to change.**

### The pain to remove

- Time is hard-wired, so the time-dependent branch cannot be tested.

### Run it

```bash
mvn test -Dtest=DiscountCalculatorTest
```

> No solution is included. Hint to check your work: production code that writes
> `new DiscountCalculator()` should still compile and behave identically.
