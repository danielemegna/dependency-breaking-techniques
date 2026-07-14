package com.qmates.kata.extractandoverridecall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.qmates.kata.domain.Customer;
import com.qmates.kata.domain.Order;
import com.qmates.kata.domain.OrderLine;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderConfirmationServiceTest {

  private static Order order() {
    return new Order(
        "order-4",
        new Customer("cus-4", "grace@example.com", "Grace"),
        List.of(new OrderLine("PEN-1", "Pen", 3, 150)),
        "tok_visa",
        null,
        "US",
        50);
  }

  // This test is RED on purpose.
  //
  // `confirm()` calls the static `EmailTransport.sendEmail` directly, and the
  // real transport throws rather than send for real. There is no object or
  // constructor parameter to intercept the call, so we cannot observe what was
  // sent — and the throw fails the test before we can assert anything.
  //
  // YOUR TASK: apply EXTRACT AND OVERRIDE CALL (see README.md). Extract the
  // `EmailTransport.sendEmail(...)` call into a `protected` method on the
  // service, then create a testing subclass (below or nested) that overrides it
  // to capture the arguments instead of sending. Drive `confirm()` through that
  // subclass and assert against what was captured. Do not change the email's
  // content or weaken the assertions.
  @Test
  void emails_the_customer_a_confirmation_naming_the_order() {
    OrderConfirmationService service = new TestableOrderConfirmationService();

    Confirmation confirmation = service.confirm(order());

    // The returned record (does not require observing the email):
    assertEquals("grace@example.com", confirmation.emailedTo());
    assertEquals(450, confirmation.totalCents());
    assertEquals("order-4", confirmation.orderId());

    // Once you have a seam, capture the outgoing email in your testing subclass
    // and assert, for example:
    //
    //   assertEquals("grace@example.com", captured.to());
    //   assertTrue(captured.subject().contains("order-4"));
  }
}
