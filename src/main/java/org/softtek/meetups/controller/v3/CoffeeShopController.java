package org.softtek.meetups.controller.v3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.sse.SseEventSink;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.softtek.meetups.dao.CoffeeOrderRequest;
import org.softtek.meetups.dao.CoffeeOrderResponse;
import org.softtek.meetups.model.CoffeeStatus;
import org.softtek.meetups.service.CoffeeService;

import java.util.concurrent.atomic.AtomicReference;

import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.time.Duration;

@ApplicationScoped
@Path("/api/v3")
public class CoffeeShopController {

    @Inject
    Logger logger;

    @Inject
    CoffeeService coffeeService;

    @POST
    @Path("/request")
    @RolesAllowed({"Customers"})
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void request(CoffeeOrderRequest request, @Context Sse sse, @Context SseEventSink eventSink) {
        AtomicReference<CoffeeStatus> coffeeStatus = new AtomicReference<>(null);
        Uni.createFrom()
            // create the new order as uni
            .item(coffeeService.requestCoffee(request.coffeeType, request.customerName))
            // produce an item every second transforming the new order item into a multi
            .onItem().transformToMulti(coffeeOrder ->
                Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                    .onItem().transform(ignored -> new CoffeeOrderResponse(coffeeOrder)))
            // filter items with a predicate avoiding duplicates
            .select().when(coffeeOrderResponse ->
                Uni.createFrom().item(coffeeStatus.get() == null ||
                    coffeeStatus.get() != coffeeOrderResponse.getCoffeeStatus()))
            // subscribe to uni transformed into multi
            .subscribe().with(coffeeOrderResponse -> {
                // log and save actual coffee order status
                logger.infof("Sending event for order '%s' with status '%s'", coffeeOrderResponse.getRequestId(),
                    coffeeOrderResponse.getCoffeeStatus());
                coffeeStatus.set(coffeeOrderResponse.getCoffeeStatus());
                // sink the server event with the new coffee status
                try {
                    eventSink.send(sse.newEvent(coffeeOrderResponse.toJsonString()));
                    // close connection if the coffee is ready
                    if (coffeeStatus.get() == CoffeeStatus.Ready)
                        eventSink.close();
                } catch (Exception e) {
                    // close connection on any error
                    logger.warnf("Ouch! Something went wrong while sending event for order '%s', " +
                            "caught '%s', cause '%s'", coffeeOrderResponse.getRequestId(), e.getMessage(), e.getCause());
                    eventSink.close();
                }
            });
    }

}
