package org.softtek.meetups.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.softtek.meetups.data.CoffeeOrder;
import org.softtek.meetups.model.CoffeeStatus;
import org.softtek.meetups.model.CoffeeType;

import java.io.Serializable;
import java.util.UUID;

public class CoffeeOrderResponse implements Serializable {

    private UUID requestId;
    private CoffeeType coffeeType;
    private CoffeeStatus coffeeStatus;
    private String customerName;

    @JsonProperty("request")
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty("coffee")
    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    @JsonProperty("status")
    public CoffeeStatus getCoffeeStatus() {
        return coffeeStatus;
    }

    @JsonProperty("customer")
    public String getCustomerName() {
        return customerName;
    }

    public CoffeeOrderResponse(CoffeeOrder coffeeOrder) {
        this.requestId = coffeeOrder.getRequestId();
        this.coffeeType = coffeeOrder.getCoffeeType();
        this.coffeeStatus = coffeeOrder.getCoffeeStatus();
        this.customerName = coffeeOrder.getCustomerName();
    }

    public String toJsonString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
