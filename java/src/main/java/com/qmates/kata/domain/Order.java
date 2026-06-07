package com.qmates.kata.domain;

import java.util.List;

/**
 * A checked-out order.
 *
 * <p>The same domain is shared across all four exercises, so the legacy
 * snippets feel like slices of one real application. You should not need to
 * change anything in this package to complete the exercises.
 *
 * @param couponCode optional coupon code applied at checkout; may be {@code null}.
 */
public record Order(
    String id,
    Customer customer,
    List<OrderLine> lines,
    String paymentToken,
    String couponCode,
    String destinationCountry,
    int weightGrams) {}
