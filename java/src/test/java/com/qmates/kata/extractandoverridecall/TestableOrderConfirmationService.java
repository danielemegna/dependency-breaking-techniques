package com.qmates.kata.extractandoverridecall;

public class TestableOrderConfirmationService extends OrderConfirmationService {

  @Override
  protected void sendEmail(String recipientEmailAddress, String subject, String body) {
    System.out.printf("Fake send mail subject [%s] to email address [%s]: \n%s", subject, recipientEmailAddress, body);
  }

}
