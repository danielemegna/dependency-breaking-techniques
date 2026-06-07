# Dependency-Breaking Techniques — Java Katas

Four small, independent exercises for practicing the dependency-breaking
techniques from Michael Feathers' *Working Effectively with Legacy Code*.

Each exercise gives you a realistic slice of a fictional **online store
backend** that has a hard-to-test dependency baked in, plus a **failing test**
you must turn green — not by weakening the test, but by *breaking the
dependency* with the technique that exercise teaches.

> These katas assume you're comfortable with Java but new to legacy-code
> refactoring. **No solutions are included** — that's the point.

## Stack

- **Java 21**, **Maven**, **JUnit 5 (Jupiter)**.
- Exercise 03 uses `jakarta.servlet-api` (provided scope) on purpose, as its
  hard-to-construct dependency.

## The exercises

| # | Package | Technique | The dependency you'll break |
|---|---------|-----------|------------------------------|
| 01 | [`extractinterface`](src/main/java/com/qmates/kata/extractinterface) | **Extract Interface** | A concrete `StripePaymentGateway` that does real HTTP |
| 02 | [`parameterizeconstructor`](src/main/java/com/qmates/kata/parameterizeconstructor) | **Parameterize Constructor** | A `SystemClock` built inside the field initializer |
| 03 | [`adaptparameter`](src/main/java/com/qmates/kata/adaptparameter) | **Adapt Parameter** | A method that takes a `jakarta` `HttpServletRequest` |
| 04 | [`extractandoverridecall`](src/main/java/com/qmates/kata/extractandoverridecall) | **Extract and Override Call** | A direct call to the static `EmailTransport.sendEmail` |

Each package has its own `README.md` with the technique's definition, when to
use it, Java-specific mechanics, and the goal for that exercise. The exercises
are fully independent — do them in any order, though the table is a gentle
difficulty ramp.

## Running the tests

```bash
mvn test                                  # run all exercises (expect failures until you refactor)

mvn test -Dtest=OrderProcessorTest               # exercise 01
mvn test -Dtest=DiscountCalculatorTest           # exercise 02
mvn test -Dtest=ShippingControllerTest           # exercise 03
mvn test -Dtest=OrderConfirmationServiceTest     # exercise 04
```

When you first run `mvn test`, several tests will **fail** — that's by design.
Each failing test has a comment block explaining the pain and pointing you at
the technique to apply. Your job is to introduce the right *seam* so you can
substitute a test double, then make the test pass without changing the behaviour
of the production code or softening the assertions.

## How to approach a kata

1. Run that exercise's test and read the failure.
2. Read the package `README.md` — especially the **Mechanics**.
3. Find the seam: where does the hard dependency enter the code?
4. Apply the technique to create a substitution point.
5. Update the test to use a test double, then make it green.
6. Re-read your production code: did its real behaviour stay the same?

## A note on the shared domain

`com.qmates.kata.domain` holds the common types (`Order`, `Coupon`, `Customer`,
…) used across all four exercises. You should not need to modify it to complete
any kata.

## Reference

Michael C. Feathers, *Working Effectively with Legacy Code* (2004) — the
chapters on sensing/separation and the dependency-breaking technique catalog.
