package com.tmps.lab3.test;

import com.tmps.lab3.domain.RecordOrder;
import com.tmps.lab3.domain.RecordOrderBuilder;
import com.tmps.lab3.state.VinylOrderContext;

public class StatePatternTest {

    /**
     * Requirement B Test:
     * State allows valid sequential transitions through the lifecycle.
     */
    public void testStateAllowsValidSequentialTransitions() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Dexter Gordon")
                .addItem("Go!", "Dexter Gordon", 36.00)
                .build();

        VinylOrderContext context = new VinylOrderContext(order);
        if (!context.getCurrentState().getStateName().equals("PENDING_PAYMENT")) {
            throw new AssertionError("Initial state must be PENDING_PAYMENT, got: " + context.getCurrentState().getStateName());
        }

        // Transition 1: pay() -> PROCESSING
        context.pay();
        if (!context.getCurrentState().getStateName().equals("PROCESSING")) {
            throw new AssertionError("State after pay() must be PROCESSING, got: " + context.getCurrentState().getStateName());
        }

        // Transition 2: ship() -> SHIPPED
        context.ship();
        if (!context.getCurrentState().getStateName().equals("SHIPPED")) {
            throw new AssertionError("State after ship() must be SHIPPED, got: " + context.getCurrentState().getStateName());
        }

        // Transition 3: deliver() -> DELIVERED
        context.deliver();
        if (!context.getCurrentState().getStateName().equals("DELIVERED")) {
            throw new AssertionError("State after deliver() must be DELIVERED, got: " + context.getCurrentState().getStateName());
        }
    }

    /**
     * Requirement B Test:
     * State rejects forbidden / invalid transitions according to business invariants.
     */
    public void testStateRejectsForbiddenTransitions() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Lee Morgan")
                .addItem("The Sidewinder", "Lee Morgan", 38.00)
                .build();

        VinylOrderContext context = new VinylOrderContext(order);

        // Guard 1: Cannot ship or deliver when in PENDING_PAYMENT
        boolean shipBlocked = false;
        try {
            context.ship();
        } catch (IllegalStateException ex) {
            shipBlocked = true;
        }
        if (!shipBlocked) {
            throw new AssertionError("State failed to reject shipping unpaid order");
        }

        boolean deliverBlocked = false;
        try {
            context.deliver();
        } catch (IllegalStateException ex) {
            deliverBlocked = true;
        }
        if (!deliverBlocked) {
            throw new AssertionError("State failed to reject delivering unpaid order");
        }

        // Advance to PROCESSING
        context.pay();

        // Guard 2: Cannot pay twice or deliver directly from PROCESSING
        boolean payAgainBlocked = false;
        try {
            context.pay();
        } catch (IllegalStateException ex) {
            payAgainBlocked = true;
        }
        if (!payAgainBlocked) {
            throw new AssertionError("State failed to reject duplicate payment");
        }

        // Advance to SHIPPED
        context.ship();

        // Guard 3: Cannot cancel once dispatched with courier
        boolean cancelShippedBlocked = false;
        try {
            context.cancel();
        } catch (IllegalStateException ex) {
            cancelShippedBlocked = true;
        }
        if (!cancelShippedBlocked) {
            throw new AssertionError("State failed to reject cancellation of shipped order");
        }
    }
}
