package com.qmates.kata.adaptparameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class ShippingControllerTest {

  // These tests are RED on purpose.
  //
  // We want to test the rate arithmetic with a plain, hand-built request — no
  // servlet container, no mocking framework. But `quote()` demands a jakarta
  // `HttpServletRequest`, which is a ~50-method interface we cannot reasonably
  // implement inline. Passing `null` below just proves the point: the very
  // first call (`request.getHeader(...)`) throws a NullPointerException.
  //
  // YOUR TASK: apply ADAPT PARAMETER (see README.md). Introduce a small request
  // type that carries only what the quote logic needs (destination + weight),
  // make `quote()` accept that type, and provide an adapter that maps a real
  // HttpServletRequest onto it. Then replace `null` below with a plain,
  // hand-built request value and make the assertions pass.

  @Test
  void quotes_a_domestic_shipment_from_a_plain_request() throws Exception {
    ShippingController controller = new ShippingController();

    ShippingRequest request = new ShippingRequest("US", 2000);
    ShippingQuote quote = controller.quote(request);

    // base 500 + 2kg * 250 = 1000, no international surcharge
    assertEquals(1000, quote.costCents());
    assertEquals("US", quote.destinationCountry());
    assertEquals(2000, quote.weightGrams());
  }

  @Test
  void adds_the_international_surcharge_for_non_us_destinations() throws Exception {
    ShippingController controller = new ShippingController();

    ShippingRequest request = new ShippingRequest("IT", 1000);
    ShippingQuote quote = controller.quote(request);

    // base 500 + 1kg * 250 = 750, + 1500 international = 2250
    assertEquals(2250, quote.costCents());
    assertEquals("IT", quote.destinationCountry());
    assertEquals(1000, quote.weightGrams());
  }

}
