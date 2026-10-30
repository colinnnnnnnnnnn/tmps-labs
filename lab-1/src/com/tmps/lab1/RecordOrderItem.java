package com.tmps.lab1;

import java.util.Objects;

/**
 * Immutable domain entity representing a line item in a record store order.
 */
public final class RecordOrderItem {
    private final String name;
    private final int quantity;
    private final double unitPrice;

    public RecordOrderItem(String name, int quantity, double unitPrice) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Record item name cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero: " + quantity);
        }
        if (unitPrice < 0.0) {
            throw new IllegalArgumentException("Unit price cannot be negative: " + unitPrice);
        }
        this.name = name.trim();
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSubtotal() {
        return quantity * unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecordOrderItem)) return false;
        RecordOrderItem that = (RecordOrderItem) o;
        return quantity == that.quantity &&
                Double.compare(that.unitPrice, unitPrice) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, unitPrice);
    }

    @Override
    public String toString() {
        return "RecordOrderItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
