package com.tmps.lab2.decorator;

import java.util.Locale;

public class VinylInsuranceDecorator extends OrderInvoiceDecorator {
    private static final double FLAT_FEE = 7.50;
    private final String policyNumber;

    public VinylInsuranceDecorator(OrderInvoice wrappedInvoice, String policyNumber) {
        super(wrappedInvoice);
        this.policyNumber = (policyNumber == null || policyNumber.trim().isEmpty())
                ? "VG-INS-STANDARD"
                : policyNumber.trim();
    }

    public VinylInsuranceDecorator(OrderInvoice wrappedInvoice) {
        this(wrappedInvoice, "VG-INS-" + (System.currentTimeMillis() % 100000L));
    }

    public double getInsuranceFee() {
        return FLAT_FEE;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + getInsuranceFee();
    }

    @Override
    public String getInvoiceDetails() {
        return super.getInvoiceDetails() +
                String.format(Locale.US, " + [Add-On Service] Collector Damage/Loss Transit Insurance [Policy #%s]: +%8.2f USD%n",
                        policyNumber, getInsuranceFee());
    }
}
