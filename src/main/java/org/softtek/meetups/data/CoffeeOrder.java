package org.softtek.meetups.data;

import org.softtek.meetups.model.CoffeeStatus;
import org.softtek.meetups.model.CoffeeType;
import java.util.UUID;

public class CoffeeOrder {

    private UUID requestId;
    private CoffeeType coffeeType;
    private CoffeeStatus coffeeStatus;
    private String customerName;

    public UUID getRequestId() {
        return requestId;
    }

    public CoffeeStatus getCoffeeStatus() {
        return coffeeStatus;
    }

    public void setCoffeeStatus(CoffeeStatus coffeeStatus) {
        this.coffeeStatus = coffeeStatus;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public CoffeeOrder(CoffeeType coffeeType, String customerName) {
        this.requestId = UUID.randomUUID();
        this.coffeeType = coffeeType;
        this.coffeeStatus = CoffeeStatus.Received;
        this.customerName = customerName;
    }

}
