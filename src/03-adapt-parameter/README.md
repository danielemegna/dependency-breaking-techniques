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
  class), and
- That type is **heavyweight to build** in a test (streams, sockets, request
  contexts), and
- The method really only needs a **few fields** from it.

The smell here is "to test five lines of arithmetic, I have to fake an entire
HTTP request."

### Mechanics

1. Look at what the method actually reads from the awkward parameter.
2. Define a new, minimal type describing just those needs (e.g.
   `ShippingRequest`).
3. Change the method to take the new type. Keep the production entry point
   working by adding an **adapter** that builds the new type from the real
   object (e.g. `fromIncomingMessage(req): Promise<ShippingRequest>`).
4. The adapter is where the messy extraction (header reads, body streaming)
   now lives — and it stays thin, so it needs little testing.
5. In the test, build the minimal type directly with a plain object.
6. Run the tests.

## This exercise

`ShippingController.quote()` (`shipping-controller.ts`) takes a Node
`IncomingMessage`, reads a header, and **streams the request body** to find the
package weight. The rate calculation we want to verify is trivial, but the test
in `shipping-controller.test.ts` can only hand the method a plain object — which
has no `.on()` stream method, so the call blows up.

### Goal

Make the red tests pass so the rate arithmetic can be tested with an ordinary,
hand-built request value. The messy job of reading headers and draining the body
stream should move into an adapter that maps a real `IncomingMessage` onto your
new request type. **Do not weaken the assertions or the rate formula.**

### The pain to remove

- The method's parameter type forces tests to fake a streaming socket.
- Trivial business logic is trapped behind framework machinery.

### Run it

```bash
npm run test:03
```

> No solution is included. Ask yourself: what is the *smallest* description of a
> request that `quote()` truly needs?
