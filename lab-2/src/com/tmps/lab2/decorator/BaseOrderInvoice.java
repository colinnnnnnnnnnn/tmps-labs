package com.tmps.lab2.decorator;

import com.tmps.lab2.composite.OrderItemComponent;
import com.tmps.lab2.domain.RecordOrder;

import java.util.Locale;

public class BaseOrderInvoice implements OrderInvoice {
    private final RecordOrder order;
    private final double taxRate;

    public BaseOrderInvoice(RecordOrder order, double taxRate) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (taxRate < 0.0) {
            throw new IllegalArgumentException("Tax rate cannot be negative");
        }
        this.order = order;
        this.taxRate = taxRate;
    }

    public BaseOrderInvoice(RecordOrder order) {
        this(order, 0.10);
    }

    public RecordOrder getOrder() {
        return order;
    }

    public double getTaxRate() {
        return taxRate;
    }

    @Override
    public double calculateTotal() {
        double subtotal = order.getRawSubtotal();
        double tax = subtotal * taxRate;
        return subtotal + tax;
    }

    @Override
    public String getInvoiceDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "=== VINTAGE GROOVE INVOICE #%d ===%n", order.getId()));
        sb.append(String.format("Customer: %s%n", order.getCustomerName()));
        sb.append(String.format("Delivery Destination: %s%n", order.getShippingAddress()));
        sb.append(String.format("Priority Dispatch: %s%n", order.isPriority() ? "YES [Expedited Courier]" : "Standard Ground"));
        sb.append("Order Items:\n");
        for (OrderItemComponent item : order.getItems()) {
            sb.append(item.getFormattedDetails("  ")).append("\n");
        }
        double subtotal = order.getRawSubtotal();
        double tax = subtotal * taxRate;
        sb.append(String.format(Locale.US, "--------------------------------------------------%n"));
        sb.append(String.format(Locale.US, "Base Merchandise Subtotal : %8.2f USD (%d records)%n", subtotal, order.getTotalRecordCount()));
        sb.append(String.format(Locale.US, "Applicable Sales Tax (%.0f%%)  : %8.2f USD%n", taxRate * 100, tax));
        return sb.toString();
    }
}
