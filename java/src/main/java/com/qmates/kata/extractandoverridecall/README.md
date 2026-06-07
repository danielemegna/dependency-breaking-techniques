# Exercise 04 — Extract and Override Call

## The technique

> **Extract and Override Call** — Wrap a hard-to-test call (a static method, a
> global function, a singleton access) in a new protected method on the class.
> Then, in a test, subclass the class and override that method to do something
> observable instead.

This is the technique of choice when the troublesome dependency is **a bare
call**, not an object you hold a reference to. You can't inject a static method
through a constructor or hide it behind an interface without first giving it a
home you can override. Extracting the call creates exactly that override point —
a *seam* you can take advantage of by subclassing.

### When to use it

Reach for Extract and Override Call when:

- A method makes a **direct call** to a `static` method, a global function, or a
  singleton, and
- That call does something untestable (sends email, writes a file, hits a
  service), and
- The call is **not** reachable through a field or parameter you could swap.

The smell here is "the thing I need to stub isn't an object I own a reference
to — it's just a call sitting in the middle of my method."

### Mechanics (Java)

1. Identify the offending call
   (here: `EmailTransport.sendEmail(to, subject, body)`).
2. Create a new `protected` method on the class whose body is exactly that call,
   taking the same arguments:
   ```java
   protected void dispatchEmail(String to, String subject, String body) {
     EmailTransport.sendEmail(to, subject, body);
   }
   ```
3. Replace the inline call with a call to your new method (`dispatchEmail(...)`).
4. Confirm behaviour is unchanged — production still calls the real transport.
5. In the test, define a subclass that **overrides** the protected method
   (mark it `@Override`) to record its arguments instead of performing the real
   call. A small nested static class in the test is a fine home for it.
6. Drive the test through an instance of the subclass.
7. Run the tests.

> Note: `protected` members are visible to subclasses, which is all this
> technique needs. Keep the class and the method non-`final` so they can be
> overridden.

## This exercise

`OrderConfirmationService.confirm()` builds a confirmation email and sends it by
calling the static `EmailTransport.sendEmail` directly.
`OrderConfirmationServiceTest` wants to assert who the email went to and what
its subject was — but the real transport throws, and there is no seam to observe
the call.

### Goal

Make the red test pass by creating an **override point** for the email-sending
call, then exercising the service through a testing subclass that captures the
outgoing message. **Do not change the email's recipient, subject, or body, and
do not weaken the assertions.**

### The pain to remove

- A bare static call cannot be observed or stubbed.
- The only way to run `confirm()` today is to attempt a real send.

### Run it

```bash
mvn test -Dtest=OrderConfirmationServiceTest
```

> No solution is included. Remember: the goal is the smallest seam that lets a
> subclass take over the call — not to rewrite how the email is composed.
