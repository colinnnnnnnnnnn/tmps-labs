package com.tmps.lab3.state;

public class CancelledState implements OrderState {
    private final String cancellationReason;

    public CancelledState(String cancellationReason) {
        this.cancellationReason = cancellationReason != null ? cancellationReason : "Cancelled by user";
    }

    public CancelledState() {
        this("Cancelled by user");
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    @Override
    public void pay(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot pay for a cancelled order.");
    }

    @Override
    public void ship(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot ship a cancelled order.");
    }

    @Override
    public void deliver(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot deliver a cancelled order.");
    }

    @Override
    public void cancel(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Order is already in CANCELLED state.");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }

    @Override
    public String toString() {
        return String.format("%s (Reason: %s)", getStateName(), cancellationReason);
    }
}
