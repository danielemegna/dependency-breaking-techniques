# Dependency-Breaking Techniques — Katas

Hands-on katas for practicing the dependency-breaking techniques from Michael
Feathers' *Working Effectively with Legacy Code*. Each exercise hands you a
realistic slice of a fictional **online store backend** with a hard-to-test
dependency baked in, plus a **failing test** you turn green by *breaking the
dependency* — never by weakening the test. **No solutions are included.**

The same four exercises are implemented per language, so you can practice in the
stack you prefer:

| Language | Folder | Stack | Run |
|----------|--------|-------|-----|
| TypeScript | [`typescript/`](typescript) | Vitest, strict TS | `cd typescript && npm install && npm test` |
| Java | [`java/`](java) | Java 21, Maven, JUnit 5 | `cd java && mvn test` |

Both tracks cover the same techniques:

1. **Extract Interface** — depend on an interface, not a concrete collaborator.
2. **Parameterize Constructor** — inject a dependency the class used to build itself.
3. **Adapt Parameter** — wrap an awkward framework parameter in a narrow type.
4. **Extract and Override Call** — wrap a bare static/global call so a subclass can override it.

Start with each track's own `README.md`, then work the per-exercise READMEs.

## Reference

Michael C. Feathers, *Working Effectively with Legacy Code* (2004).
