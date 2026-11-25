package com.tmps.lab2.proxy;

import java.time.LocalDateTime;

public class FulfillmentResult {
    private final boolean success;
    private final String trackingNumber;
    private final String receipt;
    private final double finalAmount;
    private final LocalDateTime timestamp;
    private final String message;

    public FulfillmentResult(boolean success, String trackingNumber, String receipt,
                             double finalAmount, LocalDateTime timestamp, String message) {
        this.success = success;
        this.trackingNumber = trackingNumber;
        this.receipt = receipt;
        this.finalAmount = finalAmount;
        this.timestamp = timestamp;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getReceipt() {
        return receipt;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
