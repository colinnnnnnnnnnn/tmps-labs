package com.tmps.lab3.state;

public class ProcessingState implements OrderState {

    @Override
    public void pay(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Order has already been paid and is currently being processed.");
    }

    @Override
    public void ship(VinylOrderContext context) {
        // Valid transition: Processing -> Shipped
        context.setState(new ShippedState());
    }

    @Override
    public void deliver(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot deliver an order while still processing in the warehouse.");
    }

    @Override
    public void cancel(VinylOrderContext context) {
        // Valid transition: Processing -> Cancelled (triggers refund)
        context.setState(new CancelledState("Order cancelled during processing; payment refunded."));
    }

    @Override
    public String getStateName() {
        return "PROCESSING";
    }

    @Override
    public String toString() {
        return getStateName();
    }
}
