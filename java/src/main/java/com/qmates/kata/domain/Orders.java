package com.qmates.kata.domain;

/** Pure helpers over {@link Order}. */
public final class Orders {

  private Orders() {}

  /** Sum of every line's quantity * unit price, in cents. */
  public static long subtotalCents(Order order) {
    return order.lines().stream()
        .mapToLong(line -> (long) line.quantity() * line.unitPriceCents())
        .sum();
  }
}
