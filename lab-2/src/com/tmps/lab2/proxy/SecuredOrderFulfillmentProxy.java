package com.tmps.lab2.proxy;

import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.domain.AccessDeniedException;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.UserRole;
import com.tmps.lab2.domain.UserSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecuredOrderFulfillmentProxy implements OrderFulfillmentService {
    private final OrderFulfillmentService realService;
    private final List<String> auditLog = new ArrayList<>();

    public SecuredOrderFulfillmentProxy(OrderFulfillmentService realService) {
        if (realService == null) {
            throw new IllegalArgumentException("Real fulfillment service cannot be null");
        }
        this.realService = realService;
    }

    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }

    public OrderFulfillmentService getRealService() {
        return realService;
    }

    @Override
    public FulfillmentResult fulfillOrder(RecordOrder order, OrderInvoice invoice, UserSession session) {
        if (order == null || invoice == null) {
            throw new IllegalArgumentException("Order and invoice cannot be null");
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 1. Guard check: User authentication
        if (session == null || !session.isAuthenticated()) {
            String deniedMsg = String.format("[SECURITY DENIED %s] Anonymous/unauthenticated checkout blocked for Order #%d.",
                    timestamp, order.getId());
            auditLog.add(deniedMsg);
            throw new AccessDeniedException("Authentication required: Guest users cannot finalize transactional orders.");
        }

        // 2. Guard check: Priority dispatch restriction
        if (order.isPriority() && session.getRole() == UserRole.GUEST) {
            String deniedMsg = String.format("[SECURITY DENIED %s] User '%s' lacks privilege for priority courier dispatch on Order #%d.",
                    timestamp, session.getUsername(), order.getId());
            auditLog.add(deniedMsg);
            throw new AccessDeniedException("Access Denied: Priority expedited dispatch requires a verified user account.");
        }

        // 3. Guard check: High-value wholesale order threshold (> $1000 requires STAFF or ADMIN)
        double total = invoice.calculateTotal();
        if (total > 1000.0 && session.getRole() != UserRole.STAFF && session.getRole() != UserRole.ADMIN) {
            String deniedMsg = String.format("[SECURITY DENIED %s] High-value order (%.2f USD) for user '%s' requires STAFF or ADMIN approval.",
                    timestamp, total, session.getUsername());
            auditLog.add(deniedMsg);
            throw new AccessDeniedException(String.format(
                    "Access Denied: High-value transactions (%.2f USD > $1000.00) require STAFF or ADMIN authorization.", total));
        }

        // 4. Authorized: Record security grant and delegate transparently to real service
        String grantedMsg = String.format("[SECURITY GRANTED %s] User '%s' [%s] authorized for Order #%d (Total: %.2f USD).",
                timestamp, session.getUsername(), session.getRole(), order.getId(), total);
        auditLog.add(grantedMsg);

        return realService.fulfillOrder(order, invoice, session);
    }
}
