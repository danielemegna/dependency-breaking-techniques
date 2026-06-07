# Exercise 03 — Adapt Parameter

## The technique

> **Adapt Parameter** — When a method takes a parameter whose type is awkward to
> construct or fake in a test, introduce a simpler, narrower type that exposes
> only what the method needs. Wrap (adapt) the awkward real object behind that
> type in production, and pass a plain implementation in tests.

Sometimes you cannot use Extract Interface, because the troublesome type is one
you do not own (a framework or platform class) — you can't make it implement
your interface. Adapt Parameter sidesteps that: you define your own small type
and translate the foreign object into it at the boundary.

### When to use it

Reach for Adapt Parameter when:

- A method signature mentions a **type you cannot modify** (a framework/runtime
  class such as `HttpServletRequest`), and
- That type is **heavyweight to build** in a test (servlet containers, request
  contexts, ~50-method interfaces), and
- The method really only needs a **few fields** from it.

The smell here is "to test five lines of arithmetic, I have to fake an entire
HTTP request."

### Mechanics (Java)

1. Look at what the method actually reads from the awkward parameter (here: one
   header and the request body).
2. Define a new, minimal type describing just those needs — a `record` is ideal:
   ```java
   public record ShippingRequest(String destinationCountry, int weightGrams) {}
   ```
3. Change `quote()` to take the new type. Keep the production entry point
   working by adding an **adapter** that builds the new type from the real
   object, e.g. a static `ShippingRequest.fromServletRequest(HttpServletRequest)`
   or an overloaded `quote(HttpServletRequest)` that adapts and delegates.
4. The adapter is where the messy extraction (header read, body reading) now
   lives — and it stays thin, so it needs little testing.
5. In the test, build the minimal type directly with a plain value.
6. Run the tests.

## This exercise

`ShippingController.quote()` takes a `jakarta.servlet.http.HttpServletRequest`,
reads a header, and reads the request body to find the package weight. The rate
calculation we want to verify is trivial, but `ShippingControllerTest` has no
sane way to build an `HttpServletRequest` — so it passes `null`, and the call
blows up with a `NullPointerException`.

### Goal

Make the red tests pass so the rate arithmetic can be tested with an ordinary,
hand-built request value. The messy job of reading the header and the body
should move into an adapter that maps a real `HttpServletRequest` onto your new
request type. **Do not weaken the assertions or the rate formula.**

### The pain to remove

- The method's parameter type forces tests to fake a servlet request.
- Trivial business logic is trapped behind framework machinery.

### Run it

```bash
mvn test -Dtest=ShippingControllerTest
```

> No solution is included. Ask yourself: what is the *smallest* description of a
> request that `quote()` truly needs?
