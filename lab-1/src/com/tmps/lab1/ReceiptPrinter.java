package com.tmps.lab1;

/**
 * Strategy interface for record order receipt/invoice printing.
 */
@FunctionalInterface
public interface ReceiptPrinter {
    void print(RecordOrder order, double finalPrice);
}
