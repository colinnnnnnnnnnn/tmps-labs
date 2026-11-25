package com.tmps.lab3.command;

import com.tmps.lab3.state.VinylOrderContext;
import com.tmps.lab3.strategy.ShippingStrategy;

public class ChangeShippingStrategyCommand implements OrderCommand {
    private final VinylOrderContext context;
    private final ShippingStrategy newStrategy;
    private ShippingStrategy previousStrategy;

    public ChangeShippingStrategyCommand(VinylOrderContext context, ShippingStrategy newStrategy) {
        if (context == null || newStrategy == null) {
            throw new IllegalArgumentException("Context and new shipping strategy cannot be null");
        }
        this.context = context;
        this.newStrategy = newStrategy;
    }

    @Override
    public void execute() {
        this.previousStrategy = context.getShippingStrategy();
        context.setShippingStrategy(newStrategy);
    }

    @Override
    public void undo() {
        if (previousStrategy != null) {
            context.setShippingStrategy(previousStrategy);
        }
    }

    @Override
    public String getDescription() {
        return String.format("Change shipping method for Order #%d to %s",
                context.getOrder().getId(), newStrategy.getCarrierName());
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
