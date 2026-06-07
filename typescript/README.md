# Dependency-Breaking Techniques — TypeScript Katas

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

> These katas assume you're comfortable with TypeScript but new to legacy-code
> refactoring. **No solutions are included** — that's the point.

> This kata also exists in other languages. See the [repository root
> README](../README.md) for the full list — but you don't need it: everything to
> work the TypeScript track is below.

## The exercises

| # | Folder | Technique | The dependency you'll break |
|---|--------|-----------|------------------------------|
| 01 | [`src/01-extract-interface`](src/01-extract-interface) | **Extract Interface** | A concrete `StripePaymentGateway` that does real HTTP |
| 02 | [`src/02-parameterize-constructor`](src/02-parameterize-constructor) | **Parameterize Constructor** | A `SystemClock` built inside the constructor |
| 03 | [`src/03-adapt-parameter`](src/03-adapt-parameter) | **Adapt Parameter** | A method that takes a Node `IncomingMessage` |
| 04 | [`src/04-extract-and-override-call`](src/04-extract-and-override-call) | **Extract and Override Call** | A direct call to a module-level `sendEmail` |

Each folder has its own `README.md` with the technique's definition, when to use
it, step-by-step mechanics, and the goal for that exercise. The exercises are
fully independent — do them in any order, though the table above is a gentle
difficulty ramp.

### What each technique addresses

| Technique | The smell it addresses |
|-----------|------------------------|
| **Extract Interface** | A class names a *concrete* collaborator that's painful to use in a test (network, DB, credentials). |
| **Parameterize Constructor** | A class *builds its own* dependency inside the constructor, so you can't substitute it. |
| **Adapt Parameter** | A method takes a *framework type you don't own* that is heavyweight to construct in a test. |
| **Extract and Override Call** | A method makes a *bare module/global call* — no object to inject or stub. |

The first three create a seam by **substitution** (inject a different object);
the fourth creates one by **subclassing** (override the call). Extract Interface
and Adapt Parameter both replace an awkward *type* — the difference is whether
you own that type (Extract Interface) or not (Adapt Parameter).

## Setup

```bash
npm install
```

## Running the tests

```bash
npm test            # run all exercises (expect failures until you refactor)
npm run test:watch  # watch mode

npm run test:01     # run a single exercise
npm run test:02
npm run test:03
npm run test:04

npm run typecheck   # type-check the whole project with tsc
```

When you first run `npm test`, several tests will **fail** — that's by design
(as a baseline, 5 fail and 2 pass). Each failing test has a comment block
explaining the pain and pointing you at the technique to apply. Your job is to
introduce the right *seam* so you can substitute a test double, then make the
test pass without changing the behaviour of the production code or softening the
assertions.

## How to work a kata

1. Run that exercise's test and read the failure.
2. Read the exercise `README.md` — especially the **Mechanics**.
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

`src/domain.ts` holds the common types (`Order`, `Coupon`, `Customer`, …) used
across all four exercises, with money as whole **cents**. The snippets read like
parts of one real application rather than disconnected toys. You should not need
to modify it to complete any kata.

## Reference

Michael C. Feathers, *Working Effectively with Legacy Code* (2004) — the
chapters on sensing/separation and the dependency-breaking technique catalog.
