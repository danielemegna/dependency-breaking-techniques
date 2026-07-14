import {OrderConfirmationService} from "./order-confirmation-service";

export class TestableOrderConfirmationService extends OrderConfirmationService {

  protected override async dispatchEmail(recipientEmail: string, subject: string, body: string): Promise<void> {
    console.log(`Fake send mail subject [${subject}] to email address [${recipientEmail}]: \n${body}`);
  }

}
