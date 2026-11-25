package com.tmps.lab3.observer;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderSubject {
    private final List<OrderObserver> observers = new ArrayList<>();

    public void attach(OrderObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(VinylOrderContext context, OrderState oldState, OrderState newState) {
        // Defensive copy to prevent concurrent modification during notification
        List<OrderObserver> snapshot = new ArrayList<>(observers);
        for (OrderObserver observer : snapshot) {
            observer.onOrderStateChanged(context, oldState, newState);
        }
    }

    public List<OrderObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }
}
