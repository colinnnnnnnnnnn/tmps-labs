package com.tmps.lab1;

import java.util.Locale;

/**
 * Standard consumer retail receipt printer for vinyl record purchases.
 */
public class StandardReceiptPrinter implements ReceiptPrinter {
    private final String currency;

    public StandardReceiptPrinter() {
        this(RecordStoreConfig.getInstance().getDefaultCurrency());
    }

    public StandardReceiptPrinter(String currency) {
        this.currency = currency;
    }

    @Override
    public void print(RecordOrder order, double finalPrice) {
        RecordStoreConfig config = RecordStoreConfig.getInstance();
        System.out.println("RETAIL RECEIPT");
        System.out.println("Store: " + config.getStoreName());
        System.out.println("Order ID: #" + order.getId() + " | Customer: " + order.getCustomerName());
        System.out.println("Priority: " + (order.isPriority() ? "YES" : "NO"));
        System.out.println("Items:");
        for (RecordOrderItem item : order.getItems()) {
            System.out.printf(Locale.US, "  %s x%d @ %.2f %s = %.2f %s%n",
                    item.getName(), item.getQuantity(), item.getUnitPrice(), currency, item.getSubtotal(), currency);
        }
        System.out.printf(Locale.US, "Total (incl. tax): %.2f %s%n%n", finalPrice, currency);
    }
}
