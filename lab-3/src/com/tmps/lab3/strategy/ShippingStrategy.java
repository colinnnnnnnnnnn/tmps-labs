package com.tmps.lab3.strategy;

import com.tmps.lab3.domain.RecordOrder;

public interface ShippingStrategy {
    double calculateShippingCost(RecordOrder order);
    String getCarrierName();
    int getEstimatedDeliveryDays();
}
