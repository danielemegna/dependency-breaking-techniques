package com.qmates.kata.adaptparameter;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WrapperShippingRequest implements ShippingRequest {

    private static final Pattern WEIGHT_EXTRACT_REGEX = Pattern.compile("\"weightGrams\"\\s*:\\s*(\\d+)");
    private final HttpServletRequest servletRequest;

    public WrapperShippingRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    @Override
    public String destinationCountry() {
        return servletRequest.getHeader("X-Destination-Country");
    }

    @Override
    public int weightInGrams() throws IOException {
        return parseWeight(readBody(this.servletRequest));
    }

    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder raw = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                raw.append(line);
            }
        }
        return raw.toString();
    }

    private int parseWeight(String jsonBody) {
        Matcher matcher = WEIGHT_EXTRACT_REGEX.matcher(jsonBody);
        if (!matcher.find()) {
            throw new IllegalArgumentException("request body is missing weightGrams");
        }
        return Integer.parseInt(matcher.group(1));
    }
}
