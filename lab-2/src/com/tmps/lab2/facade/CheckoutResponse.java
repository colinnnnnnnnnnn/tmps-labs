package com.tmps.lab2.facade;

public class CheckoutResponse {
    private final boolean success;
    private final long orderId;
    private final double totalAmount;
    private final String trackingNumber;
    private final String receipt;
    private final String statusMessage;

    public CheckoutResponse(boolean success, long orderId, double totalAmount,
                            String trackingNumber, String receipt, String statusMessage) {
        this.success = success;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.trackingNumber = trackingNumber;
        this.receipt = receipt;
        this.statusMessage = statusMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getOrderId() {
        return orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getReceipt() {
        return receipt;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public String toString() {
        return String.format("CheckoutResponse [Order #%d | Total: %.2f USD | Tracking: %s | Status: %s]",
                orderId, totalAmount, trackingNumber, statusMessage);
    }
}
