# Exercise 02 — Parameterize Constructor

## The technique

> **Parameterize Constructor** — When a class creates one of its dependencies
> internally, add that dependency as a constructor parameter so callers (and
> tests) can supply their own. Keep the old behaviour by defaulting the
> parameter to the object that used to be created inline.

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

### Mechanics

1. Find the internal construction (here: `private readonly clock = new SystemClock()`).
2. Identify the type of the thing being constructed.
3. Add a constructor parameter of that type.
4. Give the parameter a **default value** equal to the original expression, so
   callers that pass nothing behave exactly as before. (TypeScript supports
   default parameter values directly.)
5. Assign the parameter to the field instead of constructing inline.
6. In the test, pass a controlled substitute (a fixed clock).
7. Run the tests.

## This exercise

`DiscountCalculator` (`discount-calculator.ts`) rejects expired coupons by
comparing each coupon's `expiresAt` against the current time. It obtains the
time from a `SystemClock` that it constructs **inside its own field
initializer**. The test in `discount-calculator.test.ts` needs to pin "now" to
a known instant to prove the expiry branch works — and it can't.

### Goal

Make the red test pass by giving the calculator a **seam for time**. After your
change:

- A test can construct the calculator with a fixed clock and assert the expiry
  behaviour deterministically.
- Production callers that construct `new DiscountCalculator()` with no arguments
  must still get the real system clock — **no caller should have to change.**

### The pain to remove

- Time is hard-wired, so the time-dependent branch cannot be tested.

### Run it

```bash
npm run test:02
```

> No solution is included. Hint to check your work: production code that writes
> `new DiscountCalculator()` should still compile and behave identically.
