package com.tmps.lab3.state;

public class PendingPaymentState implements OrderState {

    @Override
    public void pay(VinylOrderContext context) {
        // Valid transition: Pending -> Processing
        context.setState(new ProcessingState());
    }

    @Override
    public void ship(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot ship an order in PENDING_PAYMENT state. Payment must be processed first.");
    }

    @Override
    public void deliver(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot deliver an unpaid order.");
    }

    @Override
    public void cancel(VinylOrderContext context) {
        // Valid transition: Pending -> Cancelled
        context.setState(new CancelledState("Order cancelled by customer before payment."));
    }

    @Override
    public String getStateName() {
        return "PENDING_PAYMENT";
    }

    @Override
    public String toString() {
        return getStateName();
    }
}
