package com.tmps.lab2.proxy;

import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.UserSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StandardOrderFulfillmentService implements OrderFulfillmentService {

    @Override
    public FulfillmentResult fulfillOrder(RecordOrder order, OrderInvoice invoice, UserSession session) {
        if (order == null || invoice == null) {
            throw new IllegalArgumentException("Order and invoice cannot be null");
        }

        double total = invoice.calculateTotal();
        LocalDateTime now = LocalDateTime.now();
        String dispatchType = order.isPriority() ? "EXPRESS-AIR" : "GROUND";
        String trackingId = String.format("VG-%s-%d-%d", dispatchType, order.getId(), System.currentTimeMillis() % 10000);

        StringBuilder receipt = new StringBuilder();
        receipt.append(invoice.getInvoiceDetails());
        receipt.append(String.format(Locale.US, "--------------------------------------------------%n"));
        receipt.append(String.format(Locale.US, "TOTAL CHARGED (Tax & Add-Ons) : %8.2f USD%n", total));
        receipt.append(String.format("Payment Clearance Status      : AUTHORIZED & SETTLED%n"));
        receipt.append(String.format("Carrier Tracking Identifier   : %s%n", trackingId));
        receipt.append(String.format("Processed By User             : %s [%s]%n",
                session != null ? session.getUsername() : "System",
                session != null ? session.getRole() : "UNKNOWN"));
        receipt.append(String.format("Fulfillment Timestamp         : %s%n",
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        receipt.append("==================================================");

        return new FulfillmentResult(
                true,
                trackingId,
                receipt.toString(),
                total,
                now,
                String.format("Order #%d fulfilled successfully. Dispatched via %s.", order.getId(), dispatchType)
        );
    }
}
