package com.tmps.lab1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder Pattern:
 * Provides a fluent interface to construct complex [RecordOrder] entities step-by-step
 * while enforcing domain invariants prior to instantiation.
 */
public class RecordOrderBuilder {
    private long id = RecordStoreConfig.getInstance().nextOrderId();
    private String customerName = "";
    private final List<RecordOrderItem> items = new ArrayList<>();
    private String shippingAddress = null;
    private boolean isPriority = false;

    public RecordOrderBuilder withCustomer(String name) {
        this.customerName = name != null ? name.trim() : "";
        return this;
    }

    public RecordOrderBuilder addItem(String name, int quantity, double unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero: " + quantity);
        }
        if (unitPrice < 0.0) {
            throw new IllegalArgumentException("Item unit price cannot be negative: " + unitPrice);
        }
        this.items.add(new RecordOrderItem(name, quantity, unitPrice));
        return this;
    }

    public RecordOrderBuilder withShippingAddress(String address) {
        this.shippingAddress = (address != null && !address.trim().isEmpty()) ? address.trim() : null;
        return this;
    }

    public RecordOrderBuilder asPriority(boolean priority) {
        this.isPriority = priority;
        return this;
    }

    public RecordOrder build() {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalStateException("Customer name is required to build a RecordOrder");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("RecordOrder must contain at least one RecordOrderItem");
        }

        return new RecordOrder(
                id,
                customerName,
                Collections.unmodifiableList(new ArrayList<>(items)),
                shippingAddress,
                isPriority
        );
    }
}
