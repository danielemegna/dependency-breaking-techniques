package com.qmates.kata.extractandoverridecall;

public class TestableOrderConfirmationService extends OrderConfirmationService {

  private FakeEmail latestSentEmail;

  @Override
  protected void sendEmail(String recipientEmailAddress, String subject, String body) {
    System.out.printf("Fake send mail subject [%s] to email address [%s]: \n%s", subject, recipientEmailAddress, body);
    this.latestSentEmail = new FakeEmail(recipientEmailAddress, subject, body);
  }

  FakeEmail getLatestSentEmail() {
    return this.latestSentEmail;
  }

  record FakeEmail(String recipientEmailAddress, String subject, String body) { }

}
