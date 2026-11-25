package com.tmps.lab2.domain;

import com.tmps.lab2.composite.OrderItemComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordOrder {
    private final long id;
    private final String customerName;
    private final List<OrderItemComponent> items;
    private final String shippingAddress;
    private final boolean isPriority;

    RecordOrder(long id, String customerName, List<OrderItemComponent> items,
                String shippingAddress, boolean isPriority) {
        this.id = id;
        this.customerName = customerName;
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        this.shippingAddress = shippingAddress;
        this.isPriority = isPriority;
    }

    public long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<OrderItemComponent> getItems() {
        return items;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public double getRawSubtotal() {
        double sum = 0.0;
        for (OrderItemComponent item : items) {
            sum += item.getPrice();
        }
        return sum;
    }

    public int getTotalRecordCount() {
        int total = 0;
        for (OrderItemComponent item : items) {
            total += item.getItemCount();
        }
        return total;
    }
}
