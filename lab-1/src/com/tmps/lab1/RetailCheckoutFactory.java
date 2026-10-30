package com.tmps.lab1;

/**
 * Concrete Factory for standard consumer vinyl retail transactions.
 */
public class RetailCheckoutFactory implements CheckoutFactory {
    @Override
    public PricingStrategy createPricingStrategy() {
        return new StandardPricingStrategy();
    }

    @Override
    public ReceiptPrinter createReceiptPrinter() {
        return new StandardReceiptPrinter();
    }
}
