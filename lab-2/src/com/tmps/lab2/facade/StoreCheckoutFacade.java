package com.tmps.lab2.facade;

import com.tmps.lab2.composite.OrderItemComponent;
import com.tmps.lab2.decorator.BaseOrderInvoice;
import com.tmps.lab2.decorator.GiftWrapDecorator;
import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.decorator.UltrasonicCleaningDecorator;
import com.tmps.lab2.decorator.VinylInsuranceDecorator;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.RecordOrderBuilder;
import com.tmps.lab2.domain.UserSession;
import com.tmps.lab2.proxy.FulfillmentResult;
import com.tmps.lab2.proxy.OrderFulfillmentService;

public class StoreCheckoutFacade implements CheckoutFacade {
    private final OrderFulfillmentService fulfillmentService;

    public StoreCheckoutFacade(OrderFulfillmentService fulfillmentService) {
        if (fulfillmentService == null) {
            throw new IllegalArgumentException("Fulfillment service cannot be null");
        }
        this.fulfillmentService = fulfillmentService;
    }

    public OrderFulfillmentService getFulfillmentService() {
        return fulfillmentService;
    }

    @Override
    public CheckoutResponse processCheckout(CheckoutRequest request, UserSession session) {
        if (request == null) {
            throw new IllegalArgumentException("Checkout request cannot be null");
        }

        // Subsystem Step 1: Creational construction via RecordOrderBuilder
        RecordOrderBuilder builder = new RecordOrderBuilder()
                .withCustomer(request.getCustomerName())
                .withShippingAddress(request.getShippingAddress())
                .asPriority(request.isPriority());

        for (OrderItemComponent item : request.getItems()) {
            builder.addItem(item);
        }

        RecordOrder order = builder.build();

        // Subsystem Step 2: Dynamic Invoice decoration via Decorators
        OrderInvoice invoice = new BaseOrderInvoice(order);

        if (request.getAddOns().contains(InvoiceAddOn.ULTRASONIC_CLEANING)) {
            invoice = new UltrasonicCleaningDecorator(invoice, order.getTotalRecordCount());
        }
        if (request.getAddOns().contains(InvoiceAddOn.TRANSIT_INSURANCE)) {
            invoice = new VinylInsuranceDecorator(invoice);
        }
        if (request.getAddOns().contains(InvoiceAddOn.GIFT_WRAPPING)) {
            invoice = new GiftWrapDecorator(invoice, request.getGiftMessage());
        }

        // Subsystem Step 3: Authorization & Fulfillment dispatch via Proxy
        FulfillmentResult fulfillmentResult = fulfillmentService.fulfillOrder(order, invoice, session);

        // Subsystem Step 4: Package subsystem results into unified client response
        return new CheckoutResponse(
                fulfillmentResult.isSuccess(),
                order.getId(),
                fulfillmentResult.getFinalAmount(),
                fulfillmentResult.getTrackingNumber(),
                fulfillmentResult.getReceipt(),
                fulfillmentResult.getMessage()
        );
    }
}
