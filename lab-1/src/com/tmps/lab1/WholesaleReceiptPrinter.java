package com.tmps.lab1;

import java.util.Locale;

/**
 * Wholesale B2B commercial invoice printer for bulk vinyl record distribution,
 * with detailed breakdown of volume discounts, tax, and shipping destination.
 */
public class WholesaleReceiptPrinter implements ReceiptPrinter {
    private final String currency;

    public WholesaleReceiptPrinter() {
        this(RecordStoreConfig.getInstance().getDefaultCurrency());
    }

    public WholesaleReceiptPrinter(String currency) {
        this.currency = currency;
    }

    @Override
    public void print(RecordOrder order, double finalPrice) {
        RecordStoreConfig config = RecordStoreConfig.getInstance();
        double gross = order.getRawSubtotal();
        boolean qualifiesForDiscount = order.getTotalQuantity() >= config.getBulkDiscountThreshold();
        double discountAmount = qualifiesForDiscount ? gross * config.getBulkDiscountRate() : 0.0;
        double taxableSubtotal = gross - discountAmount;
        double taxAmount = finalPrice - taxableSubtotal;

        System.out.println("COMMERCIAL INVOICE (B2B)");
        System.out.println("Issuer: " + config.getStoreName());
        System.out.println("Invoice Ref: INV-" + order.getId());
        System.out.println("Client: " + order.getCustomerName());
        System.out.println("Shipping Destination: " + (order.getShippingAddress() != null ? order.getShippingAddress() : "Self-pickup"));
        System.out.println("Total Vinyl Units: " + order.getTotalQuantity());
        System.out.println("Line Items:");
        for (RecordOrderItem item : order.getItems()) {
            String paddedName = String.format("%-35s", item.getName());
            String paddedQty = String.format("%-4d", item.getQuantity());
            System.out.printf(Locale.US, "  [VINYL] %s Qty: %s Unit: %.2f %s%n",
                    paddedName, paddedQty, item.getUnitPrice(), currency);
        }
        System.out.println("Accounting Summary:");
        System.out.printf(Locale.US, "  Gross Subtotal:     %.2f %s%n", gross, currency);
        if (qualifiesForDiscount) {
            System.out.printf(Locale.US, "  Bulk Discount (%d%%): -%.2f %s%n",
                    (int) (config.getBulkDiscountRate() * 100), discountAmount, currency);
        }
        System.out.printf(Locale.US, "  Tax (VAT %d%%):       +%.2f %s%n",
                (int) (config.getStandardTaxRate() * 100), taxAmount, currency);
        System.out.printf(Locale.US, "  Final Invoice Due:  %.2f %s%n%n", finalPrice, currency);
    }
}
