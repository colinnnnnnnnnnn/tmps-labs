package com.tmps.lab2.domain;

import com.tmps.lab2.composite.OrderItemComponent;
import com.tmps.lab2.composite.SingleRecordItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RecordOrderBuilder {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(2000L);

    private long id = ID_GENERATOR.incrementAndGet();
    private String customerName = "Walk-in Collector";
    private final List<OrderItemComponent> items = new ArrayList<>();
    private String shippingAddress = "In-store Pickup";
    private boolean isPriority = false;

    public RecordOrderBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public RecordOrderBuilder withCustomer(String customerName) {
        if (customerName != null && !customerName.trim().isEmpty()) {
            this.customerName = customerName.trim();
        }
        return this;
    }

    public RecordOrderBuilder addItem(OrderItemComponent item) {
        if (item != null) {
            this.items.add(item);
        }
        return this;
    }

    public RecordOrderBuilder addSingleRecord(String title, String artist, double price) {
        this.items.add(new SingleRecordItem(title, artist, price));
        return this;
    }

    public RecordOrderBuilder withShippingAddress(String shippingAddress) {
        if (shippingAddress != null && !shippingAddress.trim().isEmpty()) {
            this.shippingAddress = shippingAddress.trim();
        }
        return this;
    }

    public RecordOrderBuilder asPriority(boolean isPriority) {
        this.isPriority = isPriority;
        return this;
    }

    public RecordOrder build() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must contain at least one item");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalStateException("Customer name cannot be empty");
        }
        return new RecordOrder(id, customerName, items, shippingAddress, isPriority);
    }
}
