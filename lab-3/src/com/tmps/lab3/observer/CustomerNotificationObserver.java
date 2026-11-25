package com.tmps.lab3.observer;

import com.tmps.lab3.state.OrderState;
import com.tmps.lab3.state.VinylOrderContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerNotificationObserver implements OrderObserver {
    private final List<String> notificationLog = new ArrayList<>();

    @Override
    public void onOrderStateChanged(VinylOrderContext context, OrderState oldState, OrderState newState) {
        String message = String.format("Dear %s, your vinyl order #%d has transitioned from [%s] to [%s].",
                context.getOrder().getCustomerName(),
                context.getOrder().getId(),
                oldState != null ? oldState.getStateName() : "INITIAL",
                newState.getStateName());
        notificationLog.add(message);
    }

    public List<String> getNotificationLog() {
        return Collections.unmodifiableList(notificationLog);
    }

    public String getLastNotification() {
        if (notificationLog.isEmpty()) return null;
        return notificationLog.get(notificationLog.size() - 1);
    }
}
