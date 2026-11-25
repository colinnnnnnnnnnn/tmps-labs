package com.tmps.lab3.state;

public interface OrderState {
    void pay(VinylOrderContext context);
    void ship(VinylOrderContext context);
    void deliver(VinylOrderContext context);
    void cancel(VinylOrderContext context);
    String getStateName();
}
