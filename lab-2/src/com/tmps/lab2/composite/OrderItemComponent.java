package com.tmps.lab2.composite;

public interface OrderItemComponent {
    String getTitle();
    double getPrice();
    int getItemCount();
    String getFormattedDetails(String indent);
}
