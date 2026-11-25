package com.tmps.lab2.proxy;

import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.UserSession;

public interface OrderFulfillmentService {
    FulfillmentResult fulfillOrder(RecordOrder order, OrderInvoice invoice, UserSession session);
}
