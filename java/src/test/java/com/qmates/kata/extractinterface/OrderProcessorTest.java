package com.qmates.kata.extractinterface;

import com.qmates.kata.domain.Customer;
import com.qmates.kata.domain.Order;
import com.qmates.kata.domain.OrderLine;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessorTest {

  private static Order orderWith(List<OrderLine> lines) {
    return new Order(
        "order-1",
        new Customer("cus-1", "ada@example.com", "Ada"),
        lines,
        "tok_visa",
        null,
        "US",
        800);
  }

  // This test is RED on purpose.
  //
  // It can only build the gateway as a real `StripePaymentGateway`, which
  // reaches for the network and throws. There is no abstraction we can stand a
  // test double behind.
  //
  // YOUR TASK: apply EXTRACT INTERFACE (see README.md) so that you can replace
  // the gateway below with an injected fake, then make the assertions pass. Do
  // not weaken the assertions — break the dependency instead.
  @Test
  void charges_the_order_subtotal_and_reports_the_order_as_paid() {
    ChargePaymentGateway gateway = new FakeChargePaymentGateway(
        ChargeResult.succeeded("charge-id")
    );
    OrderProcessor processor = new OrderProcessor(gateway);

    ProcessResult result = processor.process(
        orderWith(List.of(new OrderLine("BOOK-1", "Refactoring", 2, 3500)))
    );

    assertEquals(ProcessResult.Status.PAID, result.status());
    assertEquals("charge-id", result.chargeId());
    assertNull(result.reason());
  }

  // Already green: no collaborator is touched on this path. Keep it green while
  // you refactor.
  @Test
  void declines_an_empty_order_without_touching_the_gateway() {
    ChargePaymentGateway gateway = new FakeChargePaymentGateway(ChargeResult.succeeded("any"));
    OrderProcessor processor = new OrderProcessor(gateway);

    ProcessResult result = processor.process(orderWith(List.of()));

    assertEquals(ProcessResult.Status.DECLINED, result.status());
    assertEquals("empty order", result.reason());
    assertNull(result.chargeId());
  }

  @Test
  void declines_an_order_on_gateway_error() {
    ChargePaymentGateway gateway = new FakeChargePaymentGateway(
        ChargeResult.declined("insufficient-funds")
    );
    OrderProcessor processor = new OrderProcessor(gateway);

    ProcessResult result = processor.process(
        orderWith(List.of(new OrderLine("BOOK-1", "Refactoring", 2, 3500)))
    );

    assertEquals(ProcessResult.Status.DECLINED, result.status());
    assertEquals("insufficient-funds", result.reason());
    assertNull(result.chargeId());
  }

}
