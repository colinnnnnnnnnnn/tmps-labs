package com.tmps.lab3.state;

public class ShippedState implements OrderState {

    @Override
    public void pay(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Order is already paid and in transit.");
    }

    @Override
    public void ship(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Order has already been dispatched with carrier.");
    }

    @Override
    public void deliver(VinylOrderContext context) {
        // Valid transition: Shipped -> Delivered
        context.setState(new DeliveredState());
    }

    @Override
    public void cancel(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot cancel an order that has already been dispatched with courier.");
    }

    @Override
    public String getStateName() {
        return "SHIPPED";
    }

    @Override
    public String toString() {
        return getStateName();
    }
}
