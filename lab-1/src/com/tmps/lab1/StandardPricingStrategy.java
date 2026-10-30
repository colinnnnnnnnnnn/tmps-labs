package com.tmps.lab1;

/**
 * Standard retail pricing: calculates raw subtotal + standard tax (VAT) from RecordStoreConfig.
 */
public class StandardPricingStrategy implements PricingStrategy {
    private final double taxRate;

    public StandardPricingStrategy() {
        this(RecordStoreConfig.getInstance().getStandardTaxRate());
    }

    public StandardPricingStrategy(double taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public double calculateTotal(RecordOrder order) {
        double subtotal = order.getRawSubtotal();
        return subtotal * (1.0 + taxRate);
    }

    public double getTaxRate() {
        return taxRate;
    }
}
