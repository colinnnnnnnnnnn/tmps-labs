package com.tmps.lab1;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton Pattern:
 * Thread-safe global configuration and sequence tracker for the music record store.
 * Implemented using the Initialization-on-demand Holder idiom (Bill Pugh Singleton),
 * ensuring lazy, thread-safe, lock-free initialization guaranteed by JVM classloader semantics.
 */
public class RecordStoreConfig {

    private String storeName = "Vintage Groove Records";
    private String defaultCurrency = "USD";
    private double standardTaxRate = 0.20; // 20% VAT / Sales Tax
    private int bulkDiscountThreshold = 10; // 10 or more vinyl records for bulk discount
    private double bulkDiscountRate = 0.15; // 15% discount for bulk crates/orders

    private final AtomicLong sequenceCounter = new AtomicLong(1000L);

    // Private constructor prevents external instantiation
    private RecordStoreConfig() {}

    private static class InstanceHolder {
        private static final RecordStoreConfig INSTANCE = new RecordStoreConfig();
    }

    public static RecordStoreConfig getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public double getStandardTaxRate() {
        return standardTaxRate;
    }

    public void setStandardTaxRate(double standardTaxRate) {
        this.standardTaxRate = standardTaxRate;
    }

    public int getBulkDiscountThreshold() {
        return bulkDiscountThreshold;
    }

    public void setBulkDiscountThreshold(int bulkDiscountThreshold) {
        this.bulkDiscountThreshold = bulkDiscountThreshold;
    }

    public double getBulkDiscountRate() {
        return bulkDiscountRate;
    }

    public void setBulkDiscountRate(double bulkDiscountRate) {
        this.bulkDiscountRate = bulkDiscountRate;
    }

    public long nextOrderId() {
        return sequenceCounter.incrementAndGet();
    }
}
