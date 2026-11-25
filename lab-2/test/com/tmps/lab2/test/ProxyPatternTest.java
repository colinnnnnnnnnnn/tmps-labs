package com.tmps.lab2.test;

import com.tmps.lab2.composite.SingleRecordItem;
import com.tmps.lab2.decorator.BaseOrderInvoice;
import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.domain.AccessDeniedException;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.RecordOrderBuilder;
import com.tmps.lab2.domain.UserSession;
import com.tmps.lab2.proxy.FulfillmentResult;
import com.tmps.lab2.proxy.OrderFulfillmentService;
import com.tmps.lab2.proxy.SecuredOrderFulfillmentProxy;
import com.tmps.lab2.proxy.StandardOrderFulfillmentService;

public class ProxyPatternTest {

    /**
     * Requirement B Test:
     * A Proxy blocks unauthorized access and passes authorized calls through unchanged.
     */
    public void testProxyBlocksUnauthorizedAccessAndPassesAuthorized() {
        OrderFulfillmentService realService = new StandardOrderFulfillmentService();
        SecuredOrderFulfillmentProxy proxy = new SecuredOrderFulfillmentProxy(realService);

        RecordOrder standardOrder = new RecordOrderBuilder()
                .withCustomer("Art Blakey")
                .addItem(new SingleRecordItem("Moanin'", "Art Blakey", 35.00))
                .build();
        OrderInvoice standardInvoice = new BaseOrderInvoice(standardOrder);

        // 1. Block unauthenticated guest
        boolean guestBlocked = false;
        try {
            proxy.fulfillOrder(standardOrder, standardInvoice, UserSession.guest());
        } catch (AccessDeniedException ex) {
            guestBlocked = true;
        }
        if (!guestBlocked) {
            throw new AssertionError("Proxy failed to block unauthenticated guest user");
        }

        // 2. Block regular customer attempting high-value wholesale transaction (> $1000)
        RecordOrder highValueOrder = new RecordOrderBuilder()
                .withCustomer("Bulk Dealer")
                .addItem(new SingleRecordItem("Rare Acetate Test Pressing", "Miles Davis", 1200.00))
                .build();
        OrderInvoice highValueInvoice = new BaseOrderInvoice(highValueOrder);

        boolean highValueBlocked = false;
        try {
            proxy.fulfillOrder(highValueOrder, highValueInvoice, UserSession.customer("dealer123"));
        } catch (AccessDeniedException ex) {
            highValueBlocked = true;
        }
        if (!highValueBlocked) {
            throw new AssertionError("Proxy failed to block high-value transaction from regular customer");
        }

        // 3. Pass authorized customer through for standard order
        FulfillmentResult customerResult = proxy.fulfillOrder(standardOrder, standardInvoice, UserSession.customer("art_collector"));
        if (customerResult == null || !customerResult.isSuccess()) {
            throw new AssertionError("Proxy failed to pass authorized customer call through");
        }
        if (Math.abs(customerResult.getFinalAmount() - standardInvoice.calculateTotal()) > 0.001) {
            throw new AssertionError("Proxy altered final amount unexpectedly");
        }

        // 4. Pass authorized staff through for high-value order
        FulfillmentResult staffResult = proxy.fulfillOrder(highValueOrder, highValueInvoice, UserSession.staff("store_manager"));
        if (staffResult == null || !staffResult.isSuccess()) {
            throw new AssertionError("Proxy failed to pass authorized staff call for high-value order");
        }
    }

    /**
     * Requirement B Test:
     * Proxy accurately records security audit log entries for both granted and denied operations.
     */
    public void testProxyAuditLogRecordsBothDeniedAndGrantedAttempts() {
        OrderFulfillmentService realService = new StandardOrderFulfillmentService();
        SecuredOrderFulfillmentProxy proxy = new SecuredOrderFulfillmentProxy(realService);

        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Audited User")
                .addItem(new SingleRecordItem("Blue Train", "John Coltrane", 40.00))
                .build();
        OrderInvoice invoice = new BaseOrderInvoice(order);

        // Perform one denied call
        try {
            proxy.fulfillOrder(order, invoice, null);
        } catch (AccessDeniedException ignored) {}

        // Perform one granted call
        proxy.fulfillOrder(order, invoice, UserSession.customer("audited_customer"));

        if (proxy.getAuditLog().size() < 2) {
            throw new AssertionError("Audit log expected at least 2 entries, got: " + proxy.getAuditLog().size());
        }

        boolean foundDenied = false;
        boolean foundGranted = false;

        for (String entry : proxy.getAuditLog()) {
            if (entry.contains("[SECURITY DENIED")) foundDenied = true;
            if (entry.contains("[SECURITY GRANTED")) foundGranted = true;
        }

        if (!foundDenied) {
            throw new AssertionError("Audit log does not record denied security event");
        }
        if (!foundGranted) {
            throw new AssertionError("Audit log does not record granted security event");
        }
    }
}
