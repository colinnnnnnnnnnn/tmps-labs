package com.tmps.lab3.observer;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarehouseInventoryObserver implements OrderObserver {
    private final List<String> inventoryActions = new ArrayList<>();

    @Override
    public void onOrderStateChanged(VinylOrderContext context, OrderState oldState, OrderState newState) {
        String action;
        switch (newState.getStateName()) {
            case "PROCESSING":
                action = String.format("RESERVE: Allocated %d vinyl disc(s) for Order #%d from shelf inventory.",
                        context.getOrder().getItemCount(), context.getOrder().getId());
                break;
            case "SHIPPED":
                action = String.format("DISPATCH: Handed Order #%d over to carrier '%s'.",
                        context.getOrder().getId(), context.getShippingStrategy().getCarrierName());
                break;
            case "CANCELLED":
                action = String.format("RESTOCK: Restocked %d vinyl disc(s) for cancelled Order #%d.",
                        context.getOrder().getItemCount(), context.getOrder().getId());
                break;
            case "DELIVERED":
                action = String.format("ARCHIVE: Order #%d fulfillment marked completed.", context.getOrder().getId());
                break;
            default:
                action = String.format("INFO: Order #%d status updated to %s.", context.getOrder().getId(), newState.getStateName());
                break;
        }
        inventoryActions.add(action);
    }

    public List<String> getInventoryActions() {
        return Collections.unmodifiableList(inventoryActions);
    }

    public String getLastAction() {
        if (inventoryActions.isEmpty()) return null;
        return inventoryActions.get(inventoryActions.size() - 1);
    }
}
