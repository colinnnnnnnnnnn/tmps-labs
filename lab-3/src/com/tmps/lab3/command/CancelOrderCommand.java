package com.tmps.lab3.command;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

public class CancelOrderCommand implements OrderCommand {
    private final VinylOrderContext context;
    private OrderState previousState;

    public CancelOrderCommand(VinylOrderContext context) {
        if (context == null) {
            throw new IllegalArgumentException("VinylOrderContext receiver cannot be null");
        }
        this.context = context;
    }

    @Override
    public void execute() {
        this.previousState = context.getCurrentState();
        context.cancel();
    }

    @Override
    public void undo() {
        if (previousState != null) {
            context.setState(previousState);
        }
    }

    @Override
    public String getDescription() {
        return String.format("Cancel Order #%d", context.getOrder().getId());
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
