package org.softtek.meetups.controller.v1;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;

import org.softtek.meetups.dao.CoffeeOrderResponse;
import org.softtek.meetups.dao.CoffeeOrderRequest;
import org.softtek.meetups.service.CoffeeService;
import org.softtek.meetups.data.CoffeeOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("/api/v1")
public class CoffeeShopController {

    @Inject
    CoffeeService coffeeService;

    @POST
    @Path("/request")
    @RolesAllowed({"Customers"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response request(CoffeeOrderRequest request) {
        CoffeeOrder coffeeOrder = coffeeService.requestCoffee(request.coffeeType, request.customerName);
        return Response.accepted(new CoffeeOrderResponse(coffeeOrder)).build();
    }

    @GET
    @Path("/status/{request-id}")
    @RolesAllowed({"Customers"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response status(@PathParam(value = "request-id") UUID requestId) {
        CoffeeOrder coffeeOrder = coffeeService.getCoffeeOrder(requestId);
        return Response.ok(new CoffeeOrderResponse(coffeeOrder)).build();
    }

    @PUT
    @Path("/progress/{request-id}")
    @RolesAllowed({"Staff"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response progress(@PathParam(value = "request-id") UUID requestId) {
        CoffeeOrder coffeeOrder = coffeeService.progressCoffeeOrder(requestId);
        return Response.ok(new CoffeeOrderResponse(coffeeOrder)).build();
    }

    @GET
    @Path("/orders")
    @RolesAllowed({"Staff"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<CoffeeOrderResponse> orders() {
        List<CoffeeOrderResponse> response = new ArrayList<>();
        List<CoffeeOrder> requests = coffeeService.getCoffeeRequests();
        requests.forEach(coffeeOrder -> response.add(new CoffeeOrderResponse(coffeeOrder)));
        return response;
    }

}
