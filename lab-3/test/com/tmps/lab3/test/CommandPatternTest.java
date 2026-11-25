package com.tmps.lab3.test;

import com.tmps.lab3.command.*;
import com.tmps.lab3.domain.RecordOrder;
import com.tmps.lab3.domain.RecordOrderBuilder;
import com.tmps.lab3.state.VinylOrderContext;
import com.tmps.lab3.strategy.ExpressCourierShippingStrategy;
import com.tmps.lab3.strategy.StandardGroundShippingStrategy;

public class CommandPatternTest {

    /**
     * Requirement B Test:
     * Executing a Command and then undoing it restores the original state and strategy.
     */
    public void testExecutingCommandAndUndoingItRestoresOriginalState() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Billie Holiday")
                .addItem("Lady in Satin", "Billie Holiday", 45.00)
                .build();

        VinylOrderContext context = new VinylOrderContext(order, new StandardGroundShippingStrategy());
        OrderCommandInvoker invoker = new OrderCommandInvoker();

        // Initial state is PENDING_PAYMENT
        if (!context.getCurrentState().getStateName().equals("PENDING_PAYMENT")) {
            throw new AssertionError("Initial state must be PENDING_PAYMENT");
        }

        // Execute PayOrderCommand
        OrderCommand payCommand = new PayOrderCommand(context);
        invoker.executeCommand(payCommand);

        if (!context.getCurrentState().getStateName().equals("PROCESSING")) {
            throw new AssertionError("State after pay command execution should be PROCESSING, got: " + context.getCurrentState().getStateName());
        }

        // Undo PayOrderCommand
        boolean undoSuccess = invoker.undo();
        if (!undoSuccess) {
            throw new AssertionError("Invoker failed to execute undo");
        }

        if (!context.getCurrentState().getStateName().equals("PENDING_PAYMENT")) {
            throw new AssertionError("State after undo should be restored to PENDING_PAYMENT, got: " + context.getCurrentState().getStateName());
        }
    }

    /**
     * Requirement B Test:
     * Command Redo and History tracking functions correctly across multiple commands.
     */
    public void testCommandRedoAndHistoryTracking() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Ella Fitzgerald")
                .addItem("Ella and Louis", "Ella Fitzgerald", 50.00)
                .build();

        VinylOrderContext context = new VinylOrderContext(order, new StandardGroundShippingStrategy());
        OrderCommandInvoker invoker = new OrderCommandInvoker();

        OrderCommand payCmd = new PayOrderCommand(context);
        OrderCommand changeStrategyCmd = new ChangeShippingStrategyCommand(context, new ExpressCourierShippingStrategy());
        OrderCommand dispatchCmd = new DispatchOrderCommand(context);

        invoker.executeCommand(payCmd);
        invoker.executeCommand(changeStrategyCmd);
        invoker.executeCommand(dispatchCmd);

        if (!context.getCurrentState().getStateName().equals("SHIPPED")) {
            throw new AssertionError("State after all commands should be SHIPPED, got: " + context.getCurrentState().getStateName());
        }
        if (invoker.getUndoHistory().size() != 3) {
            throw new AssertionError("Undo history should contain 3 commands, got: " + invoker.getUndoHistory().size());
        }

        // Undo Dispatch
        invoker.undo();
        if (!context.getCurrentState().getStateName().equals("PROCESSING")) {
            throw new AssertionError("State after undoing dispatch should be PROCESSING, got: " + context.getCurrentState().getStateName());
        }

        // Redo Dispatch
        boolean redoSuccess = invoker.redo();
        if (!redoSuccess) {
            throw new AssertionError("Invoker redo failed");
        }
        if (!context.getCurrentState().getStateName().equals("SHIPPED")) {
            throw new AssertionError("State after redoing dispatch should be restored to SHIPPED, got: " + context.getCurrentState().getStateName());
        }

        // Check execution audit log
        if (invoker.getExecutionLog().size() < 5) {
            throw new AssertionError("Execution log entries missing: " + invoker.getExecutionLog());
        }
    }
}
