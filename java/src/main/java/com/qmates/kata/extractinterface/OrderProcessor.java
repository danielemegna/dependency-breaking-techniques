package com.qmates.kata.extractinterface;

import com.qmates.kata.domain.Order;
import com.qmates.kata.domain.Orders;

/**
 * Turns a checked-out order into a payment.
 *
 * <p>The logic here is what we actually want to test: empty orders are
 * rejected, the subtotal is charged, a successful charge yields a PAID result,
 * and a decline yields a DECLINED result.
 *
 * <p>The obstacle: the collaborator is named by its <em>concrete</em> type,
 * {@link StripePaymentGateway}. To exercise {@link #process(Order)} we are
 * forced to construct a real Stripe gateway, which reaches for the network.
 * There is no narrow type we can implement with a test double.
 */
public class OrderProcessor {

  private final StripePaymentGateway gateway;

  public OrderProcessor(StripePaymentGateway gateway) {
    this.gateway = gateway;
  }

  public ProcessResult process(Order order) {
    if (order.lines().isEmpty()) {
      return ProcessResult.declined("empty order");
    }

    long amount = Orders.subtotalCents(order);
    ChargeResult result = gateway.charge(amount, order.paymentToken());

    if (result.success()) {
      return ProcessResult.paid(result.chargeId());
    }
    return ProcessResult.declined(result.declineReason());
  }
}
