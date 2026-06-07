# Dependency-Breaking Techniques — Katas

Hands-on katas for practicing the **dependency-breaking techniques** from
Michael Feathers' *Working Effectively with Legacy Code*.

Legacy code is, in Feathers' phrase, *code without tests*. The reason it so
often has no tests is rarely the logic itself — it's the **dependencies**: a
class reaches out to the network, the clock, the file system, or a framework
object, and you can't exercise it in isolation. A *dependency-breaking
technique* is a small, safe change that introduces a **seam** — a place where
you can swap the awkward collaborator for a test double — so the logic finally
becomes testable.

Each kata hands you a realistic slice of a fictional **online store backend**
with one such dependency baked in, plus a **failing test**. Your job is to turn
that test green by *breaking the dependency* — never by weakening the test.

## Language tracks

The **same four exercises**, over the **same domain**, are implemented per
language so you can practice in the stack you prefer. Pick one and follow its
own `README.md` for setup details:

| Language | Folder | Stack | Install & run all |
|----------|--------|-------|-------------------|
| TypeScript | [`typescript/`](typescript) | Vitest, strict TypeScript | `cd typescript && npm install && npm test` |
| Java | [`java/`](java) | Java 21, Maven, JUnit 5 | `cd java && mvn test` |

On a fresh checkout the suite is **mostly red on purpose** — each failing test
marks an exercise waiting for you. (As a baseline, both tracks start with 5
failing and 2 passing tests.)

## The four techniques

Each exercise teaches exactly one technique. Read the per-exercise `README.md`
inside a track for the full Feathers-style writeup (definition, when to use it,
step-by-step mechanics).

| # | Technique | The smell it addresses | In this kata |
|---|-----------|------------------------|--------------|
| 01 | **Extract Interface** | A class names a *concrete* collaborator that's painful to use in a test (network, DB, credentials). | A processor charges through a concrete payment gateway that does real HTTP. |
| 02 | **Parameterize Constructor** | A class *builds its own* dependency inside the constructor, so you can't substitute it. | A calculator constructs its own clock, so time-dependent logic can't be pinned. |
| 03 | **Adapt Parameter** | A method takes a *framework type you don't own* that is heavyweight to construct in a test. | A controller takes a raw HTTP request and streams its body. |
| 04 | **Extract and Override Call** | A method makes a *bare static/global call* — no object to inject or stub. | A service sends mail via a direct static call to the transport. |

The first three create a seam by **substitution** (inject a different object);
the fourth creates one by **subclassing** (override the call). Extract Interface
and Adapt Parameter both replace an awkward *type* — the difference is whether
you own that type (Extract Interface) or not (Adapt Parameter).

## The shared domain

Every exercise operates on slices of one small online-store model — `Order`,
`OrderLine`, `Customer`, `Coupon`, with money as whole **cents** — so the
snippets read like parts of a real application rather than disconnected toys.
You should not need to modify the domain types to complete any kata.

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

## Who this is for

Developers comfortable with the language of their chosen track but new to
legacy-code refactoring.

## Reference

Michael C. Feathers, *Working Effectively with Legacy Code* (2004) — the
chapters on sensing/separation and the dependency-breaking technique catalog.
