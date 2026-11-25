package com.tmps.lab2.facade;

import com.tmps.lab2.domain.UserSession;

public interface CheckoutFacade {
    CheckoutResponse processCheckout(CheckoutRequest request, UserSession session);
}
