import {OrderConfirmationService} from "./order-confirmation-service";

type FakeEmail = {
  to: string;
  subject: string;
  body: string;
}

export class TestableOrderConfirmationService extends OrderConfirmationService {
  private latestSentEmail: FakeEmail | null = null;

  protected override async dispatchEmail(recipientEmail: string, subject: string, body: string): Promise<void> {
    console.log(`Fake send mail subject [${subject}] to email address [${recipientEmail}]: \n${body}`);
    this.latestSentEmail = { to: recipientEmail, subject: subject, body: body };
  }

  getLatestSentEmail(): FakeEmail | null {
    return this.latestSentEmail;
  }
}
