package org.softtek.meetups.controller.v2;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.sse.SseEventSink;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.GET;

import org.softtek.meetups.dao.CoffeeOrderResponse;
import org.softtek.meetups.dao.CoffeeOrderRequest;
import org.softtek.meetups.service.PaymentService;
import org.softtek.meetups.service.CoffeeService;
import org.softtek.meetups.dao.PaymentResponse;
import org.softtek.meetups.model.CoffeeStatus;

import java.util.concurrent.atomic.AtomicReference;
import java.time.Duration;
import java.util.UUID;

import org.jboss.logging.Logger;

@ApplicationScoped
@Path("/api/v2")
public class CoffeeShopController {

    @Inject
    Logger logger;

    @Inject
    CoffeeService coffeeService;

    @Inject
    PaymentService paymentService;

    @GET
    @Path("/orders")
    @RolesAllowed({"Staff"})
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<CoffeeOrderResponse> orders() {
        return Multi.createFrom()
            .iterable(coffeeService.getCoffeeRequests())
            .onItem().transform(coffeeOrder -> new CoffeeOrderResponse(coffeeOrder));
    }

    @PUT
    @Path("/pay/{requestId}")
    @RolesAllowed({"Customers"})
    /* TODO - use virtual thread instead */
    public PaymentResponse pay(@PathParam(value = "requestId") UUID requestId) throws InterruptedException {
        return paymentService.payCoffee(coffeeService.getCoffeeOrder(requestId));
    }

}
