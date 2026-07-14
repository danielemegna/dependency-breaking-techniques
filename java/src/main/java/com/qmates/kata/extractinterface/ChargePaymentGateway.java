package com.qmates.kata.extractinterface;

public interface ChargePaymentGateway {
    ChargeResult charge(long amountCents, String paymentToken);
}
