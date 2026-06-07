package com.qmates.kata.extractandoverridecall;

/**
 * Static email transport.
 *
 * <p>This is a {@code static} method, not an injected object — it is called
 * directly by classname. In production it would open an SMTP connection and
 * send a real message. Here it refuses to do real I/O so that any test reaching
 * it fails loudly.
 *
 * <p>Because it is a static call (not an object you hold a reference to), you
 * cannot replace it with Extract Interface or Parameterize Constructor as
 * cleanly — which is exactly what makes it a good fit for Extract and Override
 * Call.
 */
public final class EmailTransport {

  private EmailTransport() {}

  public static void sendEmail(String to, String subject, String body) {
    throw new IllegalStateException(
        "EmailTransport.sendEmail tried to send a real message to " + to
            + " (subject: \"" + subject + "\"). Real email delivery is not available in tests."
            + " body length=" + body.length());
  }
}
