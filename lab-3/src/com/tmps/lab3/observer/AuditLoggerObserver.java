package com.tmps.lab3.observer;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuditLoggerObserver implements OrderObserver {
    private final List<String> auditTrail = new ArrayList<>();

    @Override
    public void onOrderStateChanged(VinylOrderContext context, OrderState oldState, OrderState newState) {
        String entry = String.format("[AUDIT %s] Order #%d transition: [%s] -> [%s] (Subtotal: $%.2f, Shipping: $%.2f)",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                context.getOrder().getId(),
                oldState != null ? oldState.getStateName() : "NONE",
                newState.getStateName(),
                context.getOrder().getSubtotal(),
                context.calculateShippingCost());
        auditTrail.add(entry);
    }

    public List<String> getAuditTrail() {
        return Collections.unmodifiableList(auditTrail);
    }

    public String getLastAuditEntry() {
        if (auditTrail.isEmpty()) return null;
        return auditTrail.get(auditTrail.size() - 1);
    }
}
