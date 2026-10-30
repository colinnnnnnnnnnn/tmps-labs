package com.tmps.lab1;

/**
 * Concrete Factory for B2B wholesale record transactions with bulk volume incentives.
 */
public class WholesaleCheckoutFactory implements CheckoutFactory {
    @Override
    public PricingStrategy createPricingStrategy() {
        return new BulkDiscountPricingStrategy();
    }

    @Override
    public ReceiptPrinter createReceiptPrinter() {
        return new WholesaleReceiptPrinter();
    }
}
