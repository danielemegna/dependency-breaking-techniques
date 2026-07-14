package com.qmates.kata.extractinterface;

public class FakeChargePaymentGateway implements ChargePaymentGateway {
  private final ChargeResult fixedResult;

  public FakeChargePaymentGateway(ChargeResult fixedResult) {
    this.fixedResult = fixedResult;
  }

  @Override
  public ChargeResult charge(long amountCents, String paymentToken) {
    return this.fixedResult;
  }

}
