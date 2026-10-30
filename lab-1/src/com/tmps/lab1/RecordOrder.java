package com.tmps.lab1;

import java.util.Collections;
import java.util.List;

/**
 * Immutable domain aggregate representing a complete order placed at the record store.
 */
public final class RecordOrder {
    private final long id;
    private final String customerName;
    private final List<RecordOrderItem> items;
    private final String shippingAddress;
    private final boolean isPriority;

    public RecordOrder(long id, String customerName, List<RecordOrderItem> items, String shippingAddress, boolean isPriority) {
        this.id = id;
        this.customerName = customerName;
        this.items = items != null ? Collections.unmodifiableList(items) : Collections.emptyList();
        this.shippingAddress = shippingAddress;
        this.isPriority = isPriority;
    }

    public long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<RecordOrderItem> getItems() {
        return items;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public int getTotalQuantity() {
        int total = 0;
        for (RecordOrderItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getRawSubtotal() {
        double subtotal = 0.0;
        for (RecordOrderItem item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    @Override
    public String toString() {
        return "RecordOrder{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", itemsCount=" + items.size() +
                ", totalQuantity=" + getTotalQuantity() +
                ", rawSubtotal=" + getRawSubtotal() +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", isPriority=" + isPriority +
                '}';
    }
}
