package com.tmps.lab3.state;

public class DeliveredState implements OrderState {

    @Override
    public void pay(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Delivered order is completed and finalized.");
    }

    @Override
    public void ship(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Cannot ship an order that has already been delivered.");
    }

    @Override
    public void deliver(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Order has already been delivered.");
    }

    @Override
    public void cancel(VinylOrderContext context) {
        throw new IllegalStateException("Invalid State Transition: Delivered orders cannot be cancelled directly; please initiate returns/rma procedure.");
    }

    @Override
    public String getStateName() {
        return "DELIVERED";
    }

    @Override
    public String toString() {
        return getStateName();
    }
}
