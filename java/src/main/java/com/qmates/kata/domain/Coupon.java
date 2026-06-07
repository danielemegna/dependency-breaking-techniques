package com.qmates.kata.domain;

import java.time.Instant;

/**
 * A discount coupon.
 *
 * @param percentOff percentage off, 0..100.
 * @param expiresAt  the coupon is invalid on or after this instant.
 */
public record Coupon(String code, int percentOff, Instant expiresAt) {}
