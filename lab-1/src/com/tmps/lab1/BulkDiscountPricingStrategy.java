package com.tmps.lab1;

/**
 * Wholesale bulk discount pricing: applies percentage discount if total quantity exceeds threshold,
 * then adds tax.
 */
public class BulkDiscountPricingStrategy implements PricingStrategy {
    private final int threshold;
    private final double discountRate;
    private final double taxRate;

    public BulkDiscountPricingStrategy() {
        this(
                RecordStoreConfig.getInstance().getBulkDiscountThreshold(),
                RecordStoreConfig.getInstance().getBulkDiscountRate(),
                RecordStoreConfig.getInstance().getStandardTaxRate()
        );
    }

    public BulkDiscountPricingStrategy(int threshold, double discountRate, double taxRate) {
        this.threshold = threshold;
        this.discountRate = discountRate;
        this.taxRate = taxRate;
    }

    @Override
    public double calculateTotal(RecordOrder order) {
        double baseTotal = order.getRawSubtotal();
        double discountedSubtotal = order.getTotalQuantity() >= threshold
                ? baseTotal * (1.0 - discountRate)
                : baseTotal;
        return discountedSubtotal * (1.0 + taxRate);
    }

    public int getThreshold() {
        return threshold;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public double getTaxRate() {
        return taxRate;
    }
}
