package com.tmps.lab2.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VinylBoxSet implements OrderItemComponent {
    private final String title;
    private final double discountPercent;
    private final List<OrderItemComponent> children = new ArrayList<>();

    public VinylBoxSet(String title, double discountPercent) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Box set title cannot be blank");
        }
        if (discountPercent < 0.0 || discountPercent > 100.0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        this.title = title.trim();
        this.discountPercent = discountPercent;
    }

    public void addItem(OrderItemComponent item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to box set");
        }
        children.add(item);
    }

    public boolean removeItem(OrderItemComponent item) {
        return children.remove(item);
    }

    public List<OrderItemComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String getTitle() {
        return title;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public double getPrice() {
        double subtotal = 0.0;
        for (OrderItemComponent item : children) {
            subtotal += item.getPrice();
        }
        return subtotal * (1.0 - (discountPercent / 100.0));
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (OrderItemComponent item : children) {
            count += item.getItemCount();
        }
        return count;
    }

    @Override
    public String getFormattedDetails(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "%s📦 [Box Set Bundle - %.0f%% OFF] \"%s\" (%d records, bundle price: %.2f USD):%n",
                indent, discountPercent, title, getItemCount(), getPrice()));
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).getFormattedDetails(indent + "    "));
            if (i < children.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
