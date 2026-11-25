package com.tmps.lab3.test;

import com.tmps.lab3.domain.RecordOrder;
import com.tmps.lab3.domain.RecordOrderBuilder;
import com.tmps.lab3.observer.AuditLoggerObserver;
import com.tmps.lab3.observer.CustomerNotificationObserver;
import com.tmps.lab3.observer.WarehouseInventoryObserver;
import com.tmps.lab3.state.VinylOrderContext;

public class ObserverPatternTest {

    /**
     * Requirement B Test:
     * All subscribed Observers are notified of state transitions.
     */
    public void testAllSubscribedObserversAreNotifiedOfStateChanges() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Grant Green")
                .addItem("Idle Moments", "Grant Green", 42.00)
                .build();

        VinylOrderContext context = new VinylOrderContext(order);

        CustomerNotificationObserver customerObserver = new CustomerNotificationObserver();
        WarehouseInventoryObserver warehouseObserver = new WarehouseInventoryObserver();
        AuditLoggerObserver auditObserver = new AuditLoggerObserver();

        context.attachObserver(customerObserver);
        context.attachObserver(warehouseObserver);
        context.attachObserver(auditObserver);

        // Advance state from PENDING_PAYMENT to PROCESSING
        context.pay();

        if (customerObserver.getNotificationLog().size() != 1) {
            throw new AssertionError("Customer observer did not receive state change notification");
        }
        if (!customerObserver.getLastNotification().contains("[PENDING_PAYMENT] to [PROCESSING]")) {
            throw new AssertionError("Customer notification content mismatch: " + customerObserver.getLastNotification());
        }

        if (warehouseObserver.getInventoryActions().size() != 1) {
            throw new AssertionError("Warehouse observer did not receive state change event");
        }
        if (!warehouseObserver.getLastAction().contains("RESERVE: Allocated 1 vinyl disc(s)")) {
            throw new AssertionError("Warehouse action mismatch: " + warehouseObserver.getLastAction());
        }

        if (auditObserver.getAuditTrail().size() != 1) {
            throw new AssertionError("Audit logger did not record state change event");
        }
    }

    /**
     * Requirement B & Advanced Requirement A Test:
     * Observers can unsubscribe at runtime, and unsubscribed observers receive no further updates.
     */
    public void testUnsubscribedObserversDoNotReceiveFurtherNotifications() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Freddie Hubbard")
                .addItem("Open Sesame", "Freddie Hubbard", 36.00)
                .build();

        VinylOrderContext context = new VinylOrderContext(order);

        CustomerNotificationObserver customerObserver = new CustomerNotificationObserver();
        WarehouseInventoryObserver warehouseObserver = new WarehouseInventoryObserver();

        context.attachObserver(customerObserver);
        context.attachObserver(warehouseObserver);

        // First transition: both observers notified
        context.pay();
        if (customerObserver.getNotificationLog().size() != 1 || warehouseObserver.getInventoryActions().size() != 1) {
            throw new AssertionError("Initial notification delivery failed");
        }

        // Unsubscribe customerObserver at runtime
        context.detachObserver(customerObserver);

        // Second transition: only warehouseObserver should receive updates
        context.ship();

        if (customerObserver.getNotificationLog().size() != 1) {
            throw new AssertionError("Detached customer observer unexpectedly received notification after unsubscription!");
        }

        if (warehouseObserver.getInventoryActions().size() != 2) {
            throw new AssertionError("Active warehouse observer failed to receive second notification");
        }
        if (!warehouseObserver.getLastAction().contains("DISPATCH: Handed Order")) {
            throw new AssertionError("Warehouse action mismatch on dispatch: " + warehouseObserver.getLastAction());
        }
    }
}
