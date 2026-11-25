package com.tmps.lab2.decorator;

import java.util.Locale;

public class GiftWrapDecorator extends OrderInvoiceDecorator {
    private static final double FLAT_FEE = 5.00;
    private final String giftMessage;

    public GiftWrapDecorator(OrderInvoice wrappedInvoice, String giftMessage) {
        super(wrappedInvoice);
        this.giftMessage = (giftMessage == null || giftMessage.trim().isEmpty())
                ? "Happy Listening!"
                : giftMessage.trim();
    }

    public GiftWrapDecorator(OrderInvoice wrappedInvoice) {
        this(wrappedInvoice, "Happy Listening!");
    }

    public double getGiftWrapFee() {
        return FLAT_FEE;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + getGiftWrapFee();
    }

    @Override
    public String getInvoiceDetails() {
        return super.getInvoiceDetails() +
                String.format(Locale.US, " + [Add-On Service] Luxury Japanese Obi & Gift Ribbon (Card: \"%s\"): +%8.2f USD%n",
                        giftMessage, getGiftWrapFee());
    }
}
