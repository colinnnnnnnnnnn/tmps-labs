package com.tmps.lab3.strategy;

import com.tmps.lab3.domain.RecordOrder;

public class ExpressCourierShippingStrategy implements ShippingStrategy {
    private static final double BASE_FEE = 15.00;
    private static final double PER_RECORD_FEE = 2.50;

    @Override
    public double calculateShippingCost(RecordOrder order) {
        if (order == null) return BASE_FEE;
        return BASE_FEE + (order.getItemCount() * PER_RECORD_FEE);
    }

    @Override
    public String getCarrierName() {
        return "Sonic Courier Express";
    }

    @Override
    public int getEstimatedDeliveryDays() {
        return 1;
    }
}
