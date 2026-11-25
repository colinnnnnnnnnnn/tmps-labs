package com.tmps.lab2.test;

import com.tmps.lab2.composite.SingleRecordItem;
import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.UserSession;
import com.tmps.lab2.facade.CheckoutFacade;
import com.tmps.lab2.facade.CheckoutRequest;
import com.tmps.lab2.facade.CheckoutResponse;
import com.tmps.lab2.facade.InvoiceAddOn;
import com.tmps.lab2.facade.StoreCheckoutFacade;
import com.tmps.lab2.proxy.FulfillmentResult;
import com.tmps.lab2.proxy.OrderFulfillmentService;
import com.tmps.lab2.proxy.SecuredOrderFulfillmentProxy;
import com.tmps.lab2.proxy.StandardOrderFulfillmentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class FacadePatternTest {

    // Spy / Mock Subsystem Service to inspect subsystem call order and parameter delegation
    private static class SubsystemFulfillmentSpy implements OrderFulfillmentService {
        private final List<String> callTrace = new ArrayList<>();
        private RecordOrder receivedOrder;
        private OrderInvoice receivedInvoice;
        private UserSession receivedSession;

        @Override
        public FulfillmentResult fulfillOrder(RecordOrder order, OrderInvoice invoice, UserSession session) {
            callTrace.add("fulfillOrder");
            this.receivedOrder = order;
            this.receivedInvoice = invoice;
            this.receivedSession = session;

            return new FulfillmentResult(
                    true,
                    "SPY-TRACK-12345",
                    "SPY-RECEIPT-OK",
                    invoice.calculateTotal(),
                    LocalDateTime.now(),
                    "Fulfillment mock succeeded"
            );
        }

        public List<String> getCallTrace() {
            return callTrace;
        }

        public RecordOrder getReceivedOrder() {
            return receivedOrder;
        }

        public OrderInvoice getReceivedInvoice() {
            return receivedInvoice;
        }

        public UserSession getReceivedSession() {
            return receivedSession;
        }
    }

    /**
     * Requirement B Test:
     * The facade calls the subsystem in the right order (verified with a spy/mock).
     */
    public void testFacadeCallsSubsystemInRightOrder() {
        SubsystemFulfillmentSpy spy = new SubsystemFulfillmentSpy();
        StoreCheckoutFacade facade = new StoreCheckoutFacade(spy);

        CheckoutRequest request = new CheckoutRequest.Builder()
                .customer("Sonny Rollins")
                .destination("Bridge St, New York, NY")
                .priority(true)
                .addItem(new SingleRecordItem("Saxophone Colossus", "Sonny Rollins", 38.00))
                .addOn(InvoiceAddOn.ULTRASONIC_CLEANING)
                .addOn(InvoiceAddOn.TRANSIT_INSURANCE)
                .build();

        UserSession session = UserSession.customer("sonny_vip");

        CheckoutResponse response = facade.processCheckout(request, session);

        // Verify the spy was invoked exactly once
        if (spy.getCallTrace().size() != 1 || !spy.getCallTrace().get(0).equals("fulfillOrder")) {
            throw new AssertionError("Facade did not delegate to fulfillment service in proper order: " + spy.getCallTrace());
        }

        // Verify that the facade constructed the Order correctly via Builder
        RecordOrder order = spy.getReceivedOrder();
        if (order == null || !order.getCustomerName().equals("Sonny Rollins") || !order.isPriority()) {
            throw new AssertionError("Facade failed to assemble RecordOrder properly via Builder");
        }

        // Verify that the facade applied the requested decorators to the invoice
        OrderInvoice invoice = spy.getReceivedInvoice();
        String invoiceDetails = invoice.getInvoiceDetails();
        if (!invoiceDetails.contains("Ultrasonic Deep Cleaning") || !invoiceDetails.contains("Collector Damage/Loss Transit Insurance")) {
            throw new AssertionError("Facade failed to apply requested decorators to invoice");
        }

        // Verify user session was passed through
        if (spy.getReceivedSession() != session) {
            throw new AssertionError("Facade altered or failed to deliver user session");
        }

        // Verify response mapping
        if (!response.isSuccess() || !response.getTrackingNumber().equals("SPY-TRACK-12345")) {
            throw new AssertionError("Facade failed to map subsystem result to CheckoutResponse");
        }
    }

    /**
     * Requirement B Test:
     * End-to-end integration through Facade -> Proxy -> Real Subject & Decorators & Composite.
     */
    public void testFacadeEndToEndCheckoutFlow() {
        OrderFulfillmentService realService = new StandardOrderFulfillmentService();
        SecuredOrderFulfillmentProxy proxy = new SecuredOrderFulfillmentProxy(realService);
        CheckoutFacade facade = new StoreCheckoutFacade(proxy);

        CheckoutRequest request = new CheckoutRequest.Builder()
                .customer("Charles Mingus")
                .destination("Greenwich Village, NY")
                .priority(false)
                .addItem(new SingleRecordItem("Mingus Ah Um", "Charles Mingus", 42.00))
                .addOn(InvoiceAddOn.GIFT_WRAPPING)
                .giftMessage("To Mingus with love")
                .build();

        UserSession session = UserSession.customer("mingus_fan");

        CheckoutResponse response = facade.processCheckout(request, session);

        if (!response.isSuccess()) {
            throw new AssertionError("End-to-end checkout through Facade failed");
        }
        if (response.getTrackingNumber() == null || !response.getTrackingNumber().startsWith("VG-GROUND-")) {
            throw new AssertionError("Expected ground tracking number, got: " + response.getTrackingNumber());
        }
        if (!response.getReceipt().contains("To Mingus with love")) {
            throw new AssertionError("Receipt missing gift wrap message");
        }
    }
}
