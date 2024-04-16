package org.softtek.meetups.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.softtek.meetups.model.CoffeeType;

public class CoffeeOrderRequest {

    @JsonProperty("coffee")
    public CoffeeType coffeeType;

    @JsonProperty("customer")
    public String customerName;

}
