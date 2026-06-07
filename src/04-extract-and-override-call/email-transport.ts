/**
 * Module-level email transport.
 *
 * This is a free function, not a class instance — it is imported and called
 * directly. In production it would open an SMTP connection and send a real
 * message. Here it refuses to do real I/O so that any test reaching it fails
 * loudly.
 *
 * Because it is a free function (not an injected object), you cannot replace it
 * with Extract Interface or Parameterize Constructor as cleanly — which is
 * exactly what makes it a good fit for Extract and Override Call.
 */
export async function sendEmail(
  to: string,
  subject: string,
  body: string,
): Promise<void> {
  throw new Error(
    `sendEmail tried to send a real message to ${to} (subject: "${subject}"). ` +
      `Real email delivery is not available in tests. body length=${body.length}`,
  );
}
