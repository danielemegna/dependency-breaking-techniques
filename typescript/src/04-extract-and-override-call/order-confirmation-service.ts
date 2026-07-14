import {sendEmail} from "./email-transport.js";
import {orderSubtotalCents, type Order} from "../domain.js";

export interface Confirmation {
  orderId: string;
  emailedTo: string;
  totalCents: number;
}

/**
 * Sends the customer their order confirmation and returns a record of it.
 *
 * The logic we want to test: the confirmation goes to the customer's email, the
 * subject names the order, and the recorded total matches the subtotal.
 *
 * The obstacle: `confirm()` calls the module-level `sendEmail` function
 * *directly*. There is no object to swap out and no constructor parameter to
 * intercept — the dependency is baked into the body of the method as a bare
 * call. The test below therefore hits the real transport, which throws.
 */
export class OrderConfirmationService {

  async confirm(order: Order): Promise<Confirmation> {
    const total = orderSubtotalCents(order);
    const subject = `Your order ${order.id} is confirmed`;
    const body = [
      `Hi ${order.customer.name},`,
      ``,
      `Thanks for your order ${order.id}.`,
      `Total charged: ${(total / 100).toFixed(2)} USD.`,
    ].join("\n");
    let customerEmail = order.customer.email;
    await this.dispatchEmail(customerEmail, subject, body);

    return {
      orderId: order.id,
      emailedTo: customerEmail,
      totalCents: total,
    };
  }

  protected async dispatchEmail(recipientEmail: string, subject: string, body: string) {
    await sendEmail(recipientEmail, subject, body);
  }
}
