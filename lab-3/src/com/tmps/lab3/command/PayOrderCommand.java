package com.tmps.lab3.command;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

public class PayOrderCommand implements OrderCommand {
    private final VinylOrderContext context;
    private OrderState previousState;

    public PayOrderCommand(VinylOrderContext context) {
        if (context == null) {
            throw new IllegalArgumentException("VinylOrderContext receiver cannot be null");
        }
        this.context = context;
    }

    @Override
    public void execute() {
        this.previousState = context.getCurrentState();
        context.pay();
    }

    @Override
    public void undo() {
        if (previousState != null) {
            context.setState(previousState);
        }
    }

    @Override
    public String getDescription() {
        return String.format("Process payment for Order #%d", context.getOrder().getId());
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
