package com.tmps.lab3.observer;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

public interface OrderObserver {
    void onOrderStateChanged(VinylOrderContext context, OrderState oldState, OrderState newState);
}
