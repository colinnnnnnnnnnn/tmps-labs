package com.tmps.lab3.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordOrder {
    private final long id;
    private final String customerName;
    private final List<VinylRecord> items;
    private final String shippingAddress;

    public RecordOrder(long id, String customerName, List<VinylRecord> items, String shippingAddress) {
        this.id = id;
        this.customerName = customerName;
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        this.shippingAddress = shippingAddress;
    }

    public long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<VinylRecord> getItems() {
        return items;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public double getSubtotal() {
        double sum = 0.0;
        for (VinylRecord item : items) {
            sum += item.getPrice();
        }
        return sum;
    }

    public int getItemCount() {
        return items.size();
    }
}
