package com.tmps.lab3.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RecordOrderBuilder {
    private static final AtomicLong ID_GEN = new AtomicLong(3000L);

    private long id = ID_GEN.incrementAndGet();
    private String customerName = "Vinyl Enthusiast";
    private final List<VinylRecord> items = new ArrayList<>();
    private String shippingAddress = "Main St, Chisinau";

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

    public RecordOrderBuilder addItem(VinylRecord record) {
        if (record != null) {
            this.items.add(record);
        }
        return this;
    }

    public RecordOrderBuilder addItem(String title, String artist, double price) {
        this.items.add(new VinylRecord(title, artist, price));
        return this;
    }

    public RecordOrderBuilder withAddress(String address) {
        if (address != null && !address.trim().isEmpty()) {
            this.shippingAddress = address.trim();
        }
        return this;
    }

    public RecordOrder build() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must contain at least one vinyl record");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalStateException("Customer name cannot be empty");
        }
        return new RecordOrder(id, customerName, items, shippingAddress);
    }
}
