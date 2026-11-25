package com.tmps.lab3.state;

import com.tmps.lab3.domain.RecordOrder;
import com.tmps.lab3.observer.OrderObserver;
import com.tmps.lab3.observer.OrderSubject;
import com.tmps.lab3.strategy.ShippingStrategy;
import com.tmps.lab3.strategy.StandardGroundShippingStrategy;

public class VinylOrderContext {
    private final RecordOrder order;
    private OrderState currentState;
    private ShippingStrategy shippingStrategy;
    private final OrderSubject subject = new OrderSubject();

    public VinylOrderContext(RecordOrder order, ShippingStrategy shippingStrategy) {
        if (order == null) {
            throw new IllegalArgumentException("RecordOrder cannot be null");
        }
        this.order = order;
        this.shippingStrategy = shippingStrategy != null ? shippingStrategy : new StandardGroundShippingStrategy();
        this.currentState = new PendingPaymentState();
    }

    public VinylOrderContext(RecordOrder order) {
        this(order, new StandardGroundShippingStrategy());
    }

    public RecordOrder getOrder() {
        return order;
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    public void setState(OrderState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("New state cannot be null");
        }
        OrderState oldState = this.currentState;
        this.currentState = newState;
        subject.notifyObservers(this, oldState, newState);
    }

    public ShippingStrategy getShippingStrategy() {
        return shippingStrategy;
    }

    public void setShippingStrategy(ShippingStrategy shippingStrategy) {
        if (shippingStrategy == null) {
            throw new IllegalArgumentException("Shipping strategy cannot be null");
        }
        this.shippingStrategy = shippingStrategy;
    }

    public double calculateShippingCost() {
        return shippingStrategy.calculateShippingCost(order);
    }

    public double calculateGrandTotal() {
        return order.getSubtotal() + calculateShippingCost();
    }

    public void attachObserver(OrderObserver observer) {
        subject.attach(observer);
    }

    public void detachObserver(OrderObserver observer) {
        subject.detach(observer);
    }

    public OrderSubject getSubject() {
        return subject;
    }

    // Context delegating methods to current state
    public void pay() {
        currentState.pay(this);
    }

    public void ship() {
        currentState.ship(this);
    }

    public void deliver() {
        currentState.deliver(this);
    }

    public void cancel() {
        currentState.cancel(this);
    }
}
