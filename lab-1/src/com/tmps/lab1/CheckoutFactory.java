package com.tmps.lab1;

/**
 * Abstract Factory Pattern:
 * Declares factory methods for creating families of related checkout components
 * (pricing strategies and receipt printers) without coupling clients to concrete classes.
 */
public interface CheckoutFactory {
    PricingStrategy createPricingStrategy();
    ReceiptPrinter createReceiptPrinter();
}
