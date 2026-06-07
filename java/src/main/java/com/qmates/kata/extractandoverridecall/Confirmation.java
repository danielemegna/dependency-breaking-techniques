package com.qmates.kata.extractandoverridecall;

/** A record of a sent order confirmation. */
public record Confirmation(String orderId, String emailedTo, long totalCents) {}
