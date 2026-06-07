# Exercise 01 — Extract Interface

## The technique

> **Extract Interface** — Create an interface that declares the methods you use
> on a concrete class, make that class implement the interface, and change your
> code to depend on the interface instead of the class.

This is one of the safest dependency-breaking techniques in *Working Effectively
with Legacy Code*. Once your code depends on an interface rather than a concrete
type, you can supply any implementation you like in a test — a fake, a stub, or
a spy — without dragging in the real collaborator's I/O, configuration, or
construction cost.

### When to use it

Reach for Extract Interface when:

- A class names a **concrete collaborator type** directly, and
- That collaborator is **painful to use in a test** — it talks to the network or
  a database, needs credentials, or is slow — and
- You only use a **small slice** of its behaviour.

The smell here is "I can't test this class without also standing up that whole
other class."

### Mechanics

1. Identify the concrete class your code depends on (`StripePaymentGateway`).
2. List only the methods your client actually calls on it (`charge`).
3. Declare a new interface with exactly those methods (e.g. `PaymentGateway`).
4. Make the concrete class implement the new interface
   (`class StripePaymentGateway implements PaymentGateway`). Add `@Override`.
5. Change the client's field/constructor parameter type to the interface.
6. In your test, write a small class implementing the interface and inject it.
7. Run the tests.

## This exercise

`OrderProcessor` charges an order through a `StripePaymentGateway`. The
processor names the **concrete** gateway type, so `OrderProcessorTest` is forced
to construct a real Stripe gateway — which immediately tries to make an HTTP
call and throws.

### Goal

Make the red test pass **without changing the behaviour of `OrderProcessor` and
without weakening the assertions**. Introduce an interface for the gateway so
the test can inject a fake that records the charge and returns a canned result.

### The pain to remove

- The test cannot avoid real network I/O.
- `OrderProcessor` is coupled to a specific vendor's class.

### Run it

```bash
mvn test -Dtest=OrderProcessorTest
```

> No solution is included. If you get stuck, re-read the Mechanics and ask:
> *what is the smallest interface `OrderProcessor` actually needs?*
