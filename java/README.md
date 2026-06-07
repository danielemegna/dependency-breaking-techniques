# Dependency-Breaking Techniques — Java Katas

Four small, independent exercises for practicing the **dependency-breaking
techniques** from Michael Feathers' *Working Effectively with Legacy Code*.

Legacy code is, in Feathers' phrase, *code without tests*. The reason it so
often has no tests is rarely the logic itself — it's the **dependencies**: a
class reaches out to the network, the clock, the file system, or a framework
object, and you can't exercise it in isolation. A *dependency-breaking
technique* is a small, safe change that introduces a **seam** — a place where
you can swap the awkward collaborator for a test double — so the logic finally
becomes testable.

Each exercise gives you a realistic slice of a fictional **online store
backend** that has one such dependency baked in, plus a **failing test** you
must turn green — not by weakening the test, but by *breaking the dependency*
with the technique that exercise teaches.

> These katas assume you're comfortable with Java but new to legacy-code
> refactoring. **No solutions are included** — that's the point.

> This kata also exists in other languages. See the [repository root
> README](../README.md) for the full list — but you don't need it: everything to
> work the Java track is below.

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

### What each technique addresses

| Technique | The smell it addresses |
|-----------|------------------------|
| **Extract Interface** | A class names a *concrete* collaborator that's painful to use in a test (network, DB, credentials). |
| **Parameterize Constructor** | A class *builds its own* dependency inside the constructor, so you can't substitute it. |
| **Adapt Parameter** | A method takes a *framework type you don't own* that is heavyweight to construct in a test. |
| **Extract and Override Call** | A method makes a *bare static/global call* — no object to inject or stub. |

The first three create a seam by **substitution** (inject a different object);
the fourth creates one by **subclassing** (override the call). Extract Interface
and Adapt Parameter both replace an awkward *type* — the difference is whether
you own that type (Extract Interface) or not (Adapt Parameter).

## Running the tests

```bash
mvn test                                  # run all exercises (expect failures until you refactor)

mvn test -Dtest=OrderProcessorTest               # exercise 01
mvn test -Dtest=DiscountCalculatorTest           # exercise 02
mvn test -Dtest=ShippingControllerTest           # exercise 03
mvn test -Dtest=OrderConfirmationServiceTest     # exercise 04
```

When you first run `mvn test`, several tests will **fail** — that's by design
(as a baseline, 5 fail and 2 pass). Each failing test has a comment block
explaining the pain and pointing you at the technique to apply. Your job is to
introduce the right *seam* so you can substitute a test double, then make the
test pass without changing the behaviour of the production code or softening the
assertions.

## How to work a kata

1. Run that exercise's test and read the failure.
2. Read the package `README.md` — especially the **Mechanics**.
3. Find the seam: where does the hard dependency enter the code?
4. Apply the technique to create a substitution point.
5. Update the test to use a test double, then make it pass.
6. Re-read your production code: did its real behaviour stay the same?

## Ground rules

- **Don't weaken the test.** Break the dependency instead; the assertions stay.
- **Don't change production behaviour.** Existing callers must keep working —
  a seam adds a way to substitute, it doesn't alter the default path.
- **No solutions are included.** Getting stuck and re-reading the mechanics is
  the exercise.

## The shared domain

`com.qmates.kata.domain` holds the common types (`Order`, `Coupon`, `Customer`,
…) used across all four exercises, with money as whole **cents**. The snippets
read like parts of one real application rather than disconnected toys. You
should not need to modify it to complete any kata.

## Reference

Michael C. Feathers, *Working Effectively with Legacy Code* (2004) — the
chapters on sensing/separation and the dependency-breaking technique catalog.
