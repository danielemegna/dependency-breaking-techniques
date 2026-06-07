package com.qmates.kata.domain;

/** A single line in an order. Prices are whole cents to avoid float rounding. */
public record OrderLine(String sku, String description, int quantity, long unitPriceCents) {}
