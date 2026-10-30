package com.tmps.lab1;

/**
 * Strategy interface for record order pricing calculations.
 */
@FunctionalInterface
public interface PricingStrategy {
    double calculateTotal(RecordOrder order);
}
