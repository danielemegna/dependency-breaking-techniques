package com.qmates.kata.extractinterface;

/**
 * Outcome of a payment charge.
 *
 * @param chargeId      provider-side identifier, when successful.
 * @param declineReason human-readable failure reason, when unsuccessful.
 */
public record ChargeResult(boolean success, String chargeId, String declineReason) {

  public static ChargeResult succeeded(String chargeId) {
    return new ChargeResult(true, chargeId, null);
  }

  public static ChargeResult declined(String reason) {
    return new ChargeResult(false, null, reason);
  }
}
