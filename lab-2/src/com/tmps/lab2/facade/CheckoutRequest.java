package com.tmps.lab2.facade;

import com.tmps.lab2.composite.OrderItemComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class CheckoutRequest {
    private final String customerName;
    private final String shippingAddress;
    private final boolean isPriority;
    private final List<OrderItemComponent> items;
    private final Set<InvoiceAddOn> addOns;
    private final String giftMessage;

    public CheckoutRequest(String customerName, String shippingAddress, boolean isPriority,
                           List<OrderItemComponent> items, Set<InvoiceAddOn> addOns, String giftMessage) {
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.isPriority = isPriority;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.addOns = addOns != null ? EnumSet.copyOf(addOns) : EnumSet.noneOf(InvoiceAddOn.class);
        this.giftMessage = giftMessage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public List<OrderItemComponent> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Set<InvoiceAddOn> getAddOns() {
        return Collections.unmodifiableSet(addOns);
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public static class Builder {
        private String customerName = "Record Enthusiast";
        private String shippingAddress = "In-store Pickup";
        private boolean isPriority = false;
        private final List<OrderItemComponent> items = new ArrayList<>();
        private final Set<InvoiceAddOn> addOns = EnumSet.noneOf(InvoiceAddOn.class);
        private String giftMessage = "Happy Listening!";

        public Builder customer(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder destination(String shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder priority(boolean isPriority) {
            this.isPriority = isPriority;
            return this;
        }

        public Builder addItem(OrderItemComponent item) {
            if (item != null) this.items.add(item);
            return this;
        }

        public Builder addOn(InvoiceAddOn addOn) {
            if (addOn != null) this.addOns.add(addOn);
            return this;
        }

        public Builder giftMessage(String msg) {
            this.giftMessage = msg;
            return this;
        }

        public CheckoutRequest build() {
            return new CheckoutRequest(customerName, shippingAddress, isPriority, items, addOns, giftMessage);
        }
    }
}
