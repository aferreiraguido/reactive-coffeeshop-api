package org.softtek.meetups.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.softtek.meetups.dao.PaymentResponse;
import org.softtek.meetups.data.CoffeeOrder;

import org.jboss.logging.Logger;

@ApplicationScoped
public class PaymentService {

    @Inject
    Logger logger;

    @RestClient
    PaymentClientService paymentService;

    public PaymentResponse payCoffee(CoffeeOrder coffeeOrder) {
        // we are simulating here a payment gateway using the ordinal of the CoffeeType enum
        // to get a product from http://dummyjson.com and get the price attribute
        PaymentResponse paymentResponse = paymentService.paymentRequest(coffeeOrder.getCoffeeType().ordinal());
        logger.infof("Request '%s' paid '%.2f' with %.2f%% discounted price",
            coffeeOrder.getRequestId(), paymentResponse.price, paymentResponse.discountPercentage);
        return paymentResponse;
    }

}
