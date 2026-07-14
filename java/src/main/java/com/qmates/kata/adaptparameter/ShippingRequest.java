package com.qmates.kata.adaptparameter;

import java.io.IOException;

public interface ShippingRequest {
    String destinationCountry();
    int weightInGrams() throws IOException;
}
