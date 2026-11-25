package com.tmps.lab3.strategy;

import com.tmps.lab3.domain.RecordOrder;

public class InternationalAirShippingStrategy implements ShippingStrategy {
    private static final double BASE_FEE = 35.00;
    private static final double PER_RECORD_FEE = 5.00;

    @Override
    public double calculateShippingCost(RecordOrder order) {
        if (order == null) return BASE_FEE;
        return BASE_FEE + (order.getItemCount() * PER_RECORD_FEE);
    }

    @Override
    public String getCarrierName() {
        return "Global Vinyl Air Express";
    }

    @Override
    public int getEstimatedDeliveryDays() {
        return 8;
    }
}
