package com.tmps.lab2.decorator;

import java.util.Locale;

public class UltrasonicCleaningDecorator extends OrderInvoiceDecorator {
    private static final double FEE_PER_RECORD = 4.00;
    private final int recordCount;

    public UltrasonicCleaningDecorator(OrderInvoice wrappedInvoice, int recordCount) {
        super(wrappedInvoice);
        if (recordCount < 1) {
            throw new IllegalArgumentException("Record count must be at least 1");
        }
        this.recordCount = recordCount;
    }

    public double getCleaningFee() {
        return recordCount * FEE_PER_RECORD;
    }

    public int getRecordCount() {
        return recordCount;
    }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + getCleaningFee();
    }

    @Override
    public String getInvoiceDetails() {
        return super.getInvoiceDetails() +
                String.format(Locale.US, " + [Add-On Service] Ultrasonic Deep Cleaning & Archival Sleeves (%d records @ $%.2f): +%8.2f USD%n",
                        recordCount, FEE_PER_RECORD, getCleaningFee());
    }
}
