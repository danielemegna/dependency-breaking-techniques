package com.qmates.kata.adaptparameter;

/** A computed shipping quote. */
public record ShippingQuote(String destinationCountry, int weightGrams, long costCents) {}
