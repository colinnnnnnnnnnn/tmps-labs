package com.tmps.lab2.decorator;

public abstract class OrderInvoiceDecorator implements OrderInvoice {
    protected final OrderInvoice wrappedInvoice;

    public OrderInvoiceDecorator(OrderInvoice wrappedInvoice) {
        if (wrappedInvoice == null) {
            throw new IllegalArgumentException("Wrapped invoice cannot be null");
        }
        this.wrappedInvoice = wrappedInvoice;
    }

    public OrderInvoice getWrappedInvoice() {
        return wrappedInvoice;
    }

    @Override
    public double calculateTotal() {
        return wrappedInvoice.calculateTotal();
    }

    @Override
    public String getInvoiceDetails() {
        return wrappedInvoice.getInvoiceDetails();
    }
}
