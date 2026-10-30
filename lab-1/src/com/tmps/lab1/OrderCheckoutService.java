package com.tmps.lab1;

/**
 * High-level checkout orchestrator.
 * Collaborates with [CheckoutFactory] (Abstract Factory) to produce cohesive
 * pricing and printing strategies for the given record order.
 */
public class OrderCheckoutService {
    private final CheckoutFactory factory;

    public OrderCheckoutService(CheckoutFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("CheckoutFactory cannot be null");
        }
        this.factory = factory;
    }

    public double processCheckout(RecordOrder order) {
        PricingStrategy pricingStrategy = factory.createPricingStrategy();
        ReceiptPrinter printer = factory.createReceiptPrinter();

        double total = pricingStrategy.calculateTotal(order);
        printer.print(order, total);
        return total;
    }
}
