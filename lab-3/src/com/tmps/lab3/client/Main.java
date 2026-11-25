package com.tmps.lab3.client;

import com.tmps.lab3.command.*;
import com.tmps.lab3.domain.RecordOrder;
import com.tmps.lab3.domain.RecordOrderBuilder;
import com.tmps.lab3.observer.AuditLoggerObserver;
import com.tmps.lab3.observer.CustomerNotificationObserver;
import com.tmps.lab3.observer.WarehouseInventoryObserver;
import com.tmps.lab3.state.VinylOrderContext;
import com.tmps.lab3.strategy.ExpressCourierShippingStrategy;
import com.tmps.lab3.strategy.InternationalAirShippingStrategy;
import com.tmps.lab3.strategy.ShippingStrategy;
import com.tmps.lab3.strategy.StandardGroundShippingStrategy;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   TMPS Laboratory Work 3: Behavioral Design Patterns Demonstration");
        System.out.println("             Domain: Vintage Groove Records (Fulfillment System)");
        System.out.println("================================================================================\n");

        // -------------------------------------------------------------------------
        // 0. CREATIONAL REUSE: Assembling Order with Builder
        // -------------------------------------------------------------------------
        System.out.println(">>> [0] REUSING CREATIONAL BUILDER: Constructing Vinyl Order");
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Herbie Hancock")
                .addItem("Head Hunters", "Herbie Hancock", 42.00)
                .addItem("Maiden Voyage", "Herbie Hancock", 38.00)
                .addItem("Takin' Off", "Herbie Hancock", 35.00)
                .withAddress("Blue Note Blvd, New York, NY")
                .build();

        System.out.printf("  Created Order #%d for %s (%d records, subtotal: $%.2f)%n%n",
                order.getId(), order.getCustomerName(), order.getItemCount(), order.getSubtotal());

        // Context initial state: PENDING_PAYMENT with default Standard Ground Shipping
        VinylOrderContext context = new VinylOrderContext(order, new StandardGroundShippingStrategy());

        // -------------------------------------------------------------------------
        // 1. STRATEGY PATTERN DEMONSTRATION (Swappable at runtime)
        // -------------------------------------------------------------------------
        System.out.println(">>> [1] BEHAVIORAL PATTERN: STRATEGY (Interchangeable Shipping Calculations)");
        ShippingStrategy ground = new StandardGroundShippingStrategy();
        ShippingStrategy express = new ExpressCourierShippingStrategy();
        ShippingStrategy air = new InternationalAirShippingStrategy();

        System.out.printf(Locale.US, "  - Strategy 1 (%s): Shipping = $%.2f (Est: %d days) -> Grand Total = $%.2f%n",
                ground.getCarrierName(), ground.calculateShippingCost(order), ground.getEstimatedDeliveryDays(),
                order.getSubtotal() + ground.calculateShippingCost(order));

        System.out.printf(Locale.US, "  - Strategy 2 (%s): Shipping = $%.2f (Est: %d days) -> Grand Total = $%.2f%n",
                express.getCarrierName(), express.calculateShippingCost(order), express.getEstimatedDeliveryDays(),
                order.getSubtotal() + express.calculateShippingCost(order));

        System.out.printf(Locale.US, "  - Strategy 3 (%s): Shipping = $%.2f (Est: %d days) -> Grand Total = $%.2f%n",
                air.getCarrierName(), air.calculateShippingCost(order), air.getEstimatedDeliveryDays(),
                order.getSubtotal() + air.calculateShippingCost(order));

        // Swapping strategy on context at runtime
        context.setShippingStrategy(express);
        System.out.printf(Locale.US, "  => Active Strategy selected for order: %s (Total: $%.2f)%n%n",
                context.getShippingStrategy().getCarrierName(), context.calculateGrandTotal());

        // -------------------------------------------------------------------------
        // 2. OBSERVER PATTERN DEMONSTRATION (Dynamic Subscription & Broadcasts)
        // -------------------------------------------------------------------------
        System.out.println(">>> [2] BEHAVIORAL PATTERN: OBSERVER (Dynamic Event Notification)");
        CustomerNotificationObserver customerObserver = new CustomerNotificationObserver();
        WarehouseInventoryObserver warehouseObserver = new WarehouseInventoryObserver();
        AuditLoggerObserver auditObserver = new AuditLoggerObserver();

        // Attach all observers
        context.attachObserver(customerObserver);
        context.attachObserver(warehouseObserver);
        context.attachObserver(auditObserver);

        System.out.printf("  Attached %d active observers to Order #%d.%n%n",
                context.getSubject().getObservers().size(), order.getId());

        // -------------------------------------------------------------------------
        // 3. STATE PATTERN DEMONSTRATION (Encapsulated State Transitions & Guards)
        // -------------------------------------------------------------------------
        System.out.println(">>> [3] BEHAVIORAL PATTERN: STATE (Lifecycle State Encapsulation)");
        System.out.println("  Current Initial State: " + context.getCurrentState().getStateName());

        // Attempting an illegal transition: shipping before paying
        System.out.println("  Attempting invalid transition: Shipping directly from PENDING_PAYMENT...");
        try {
            context.ship();
        } catch (IllegalStateException ex) {
            System.out.println("  [STATE GUARD ENFORCED] Rejected: " + ex.getMessage());
        }

        // Valid transition: pay -> moves to PROCESSING
        System.out.println("  Executing valid transition: pay()...");
        context.pay();
        System.out.println("  Current State: " + context.getCurrentState().getStateName());
        System.out.println("  -> Customer Alert: " + customerObserver.getLastNotification());
        System.out.println("  -> Warehouse Action: " + warehouseObserver.getLastAction());
        System.out.println();

        // Valid transition: ship -> moves to SHIPPED
        System.out.println("  Executing valid transition: ship()...");
        context.ship();
        System.out.println("  Current State: " + context.getCurrentState().getStateName());
        System.out.println("  -> Customer Alert: " + customerObserver.getLastNotification());
        System.out.println("  -> Warehouse Action: " + warehouseObserver.getLastAction());
        System.out.println();

        // -------------------------------------------------------------------------
        // 4. COMMAND PATTERN DEMONSTRATION (Encapsulated Requests, Undo & History)
        // -------------------------------------------------------------------------
        System.out.println(">>> [4] BEHAVIORAL PATTERN: COMMAND (Undoable Invocations & Invoker History)");
        System.out.println("Creating fresh Order #2 to showcase complete Command Invoker pipeline with Undo/Redo:");

        RecordOrder order2 = new RecordOrderBuilder()
                .withCustomer("Nina Simone")
                .addItem("Little Girl Blue", "Nina Simone", 39.00)
                .addItem("Pastel Blues", "Nina Simone", 41.00)
                .build();

        VinylOrderContext context2 = new VinylOrderContext(order2, ground);
        context2.attachObserver(customerObserver);
        context2.attachObserver(warehouseObserver);
        context2.attachObserver(auditObserver);

        OrderCommandInvoker invoker = new OrderCommandInvoker();

        // Command 1: Pay
        OrderCommand payCmd = new PayOrderCommand(context2);
        System.out.println("  Invoker executing: " + payCmd.getDescription());
        invoker.executeCommand(payCmd);
        System.out.println("    State after command: " + context2.getCurrentState().getStateName());

        // Command 2: Change shipping strategy
        OrderCommand changeShippingCmd = new ChangeShippingStrategyCommand(context2, air);
        System.out.println("  Invoker executing: " + changeShippingCmd.getDescription());
        invoker.executeCommand(changeShippingCmd);
        System.out.printf(Locale.US, "    Carrier after command: %s (Total: $%.2f)%n",
                context2.getShippingStrategy().getCarrierName(), context2.calculateGrandTotal());

        // Command 3: Dispatch
        OrderCommand dispatchCmd = new DispatchOrderCommand(context2);
        System.out.println("  Invoker executing: " + dispatchCmd.getDescription());
        invoker.executeCommand(dispatchCmd);
        System.out.println("    State after command: " + context2.getCurrentState().getStateName());

        // Performing UNDO
        System.out.println("\n  >> Triggering UNDO operation on Invoker...");
        invoker.undo(); // Undoes Dispatch
        System.out.println("    State rolled back to: " + context2.getCurrentState().getStateName());

        System.out.println("  >> Triggering second UNDO operation on Invoker...");
        invoker.undo(); // Undoes Shipping Strategy Change
        System.out.printf(Locale.US, "    Carrier rolled back to: %s (Total: $%.2f)%n",
                context2.getShippingStrategy().getCarrierName(), context2.calculateGrandTotal());

        // Performing REDO
        System.out.println("\n  >> Triggering REDO operation on Invoker...");
        invoker.redo(); // Redoes Shipping Strategy Change
        System.out.printf(Locale.US, "    Carrier restored to: %s (Total: $%.2f)%n",
                context2.getShippingStrategy().getCarrierName(), context2.calculateGrandTotal());

        System.out.println("\n  Command Invoker Execution Log:");
        for (String log : invoker.getExecutionLog()) {
            System.out.println("    - " + log);
        }

        System.out.println("\n================================================================================");
        System.out.println("   All 4 Behavioral Design Patterns successfully demonstrated!");
        System.out.println("================================================================================");
    }
}
