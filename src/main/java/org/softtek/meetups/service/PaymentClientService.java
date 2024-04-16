package org.softtek.meetups.service;

import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.softtek.meetups.dao.PaymentResponse;

@RegisterRestClient(baseUri = "https://dummyjson.com")
public interface PaymentClientService {

    @PUT
    @Path("products/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    PaymentResponse paymentRequest(@PathParam("id") Integer requestId);

}
