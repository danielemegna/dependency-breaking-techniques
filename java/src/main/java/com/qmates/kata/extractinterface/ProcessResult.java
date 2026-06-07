package com.qmates.kata.extractinterface;

/** Outcome of processing an order for payment. */
public record ProcessResult(Status status, String chargeId, String reason) {

  public enum Status {
    PAID,
    DECLINED
  }

  public static ProcessResult paid(String chargeId) {
    return new ProcessResult(Status.PAID, chargeId, null);
  }

  public static ProcessResult declined(String reason) {
    return new ProcessResult(Status.DECLINED, null, reason);
  }
}
