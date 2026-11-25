package com.tmps.lab3.command;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

public class DispatchOrderCommand implements OrderCommand {
    private final VinylOrderContext context;
    private OrderState previousState;

    public DispatchOrderCommand(VinylOrderContext context) {
        if (context == null) {
            throw new IllegalArgumentException("VinylOrderContext receiver cannot be null");
        }
        this.context = context;
    }

    @Override
    public void execute() {
        this.previousState = context.getCurrentState();
        context.ship();
    }

    @Override
    public void undo() {
        if (previousState != null) {
            context.setState(previousState);
        }
    }

    @Override
    public String getDescription() {
        return String.format("Dispatch Order #%d via %s",
                context.getOrder().getId(), context.getShippingStrategy().getCarrierName());
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
