package com.qmates.kata.extractandoverridecall;

import com.qmates.kata.domain.Order;
import com.qmates.kata.domain.Orders;

/**
 * Sends the customer their order confirmation and returns a record of it.
 *
 * <p>The logic we want to test: the confirmation goes to the customer's email,
 * the subject names the order, and the recorded total matches the subtotal.
 *
 * <p>The obstacle: {@link #confirm(Order)} calls the static method
 * {@code EmailTransport.sendEmail(...)} <em>directly</em>. There is no object to
 * swap out and no constructor parameter to intercept — the dependency is baked
 * into the body of the method as a bare static call. A test therefore hits the
 * real transport, which throws.
 */
public class OrderConfirmationService {

  public Confirmation confirm(Order order) {
    long total = Orders.subtotalCents(order);
    String subject = "Your order " + order.id() + " is confirmed";
    String body =
        String.join(
            "\n",
            "Hi " + order.customer().name() + ",",
            "",
            "Thanks for your order " + order.id() + ".",
            "Total charged: " + String.format("%.2f", total / 100.0) + " USD.");
    String recipientEmailAddress = order.customer().email();

    sendEmail(recipientEmailAddress, subject, body);

    return new Confirmation(order.id(), recipientEmailAddress, total);
  }

  protected void sendEmail(String recipientEmailAddress, String subject, String body) {
    EmailTransport.sendEmail(recipientEmailAddress, subject, body);
  }

}
