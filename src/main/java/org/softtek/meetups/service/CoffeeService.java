package org.softtek.meetups.service;

import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.softtek.meetups.data.CoffeeOrder;
import org.softtek.meetups.model.CoffeeStatus;
import org.softtek.meetups.model.CoffeeType;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CoffeeService {

    ConcurrentHashMap<UUID, CoffeeOrder> coffeeRequests = new ConcurrentHashMap<>();

    @Inject
    Logger logger;

    public CoffeeOrder requestCoffee(CoffeeType coffeeType, String customerName) {
        CoffeeOrder coffeeOrder = new CoffeeOrder(coffeeType, customerName);
        coffeeRequests.put(coffeeOrder.getRequestId(), coffeeOrder);
        logger.infof("Coffee order '%s' received to make '%s' for '%s'", coffeeOrder.getRequestId(),
            coffeeType, customerName);
        return coffeeOrder;
    }

    public CoffeeOrder getCoffeeOrder(UUID requestId) {
        CoffeeOrder coffeeOrder = coffeeRequests.get(requestId);
        return coffeeOrder == null ? null : coffeeOrder;
    }

    public CoffeeOrder progressCoffeeOrder(UUID requestId) {
        CoffeeOrder coffeeOrder = coffeeRequests.get(requestId);
        if (coffeeOrder == null) return null;
        else {
            switch (coffeeOrder.getCoffeeStatus()) {
                case CoffeeStatus.Received -> coffeeOrder.setCoffeeStatus(CoffeeStatus.Preparation);
                case CoffeeStatus.Preparation -> coffeeOrder.setCoffeeStatus(CoffeeStatus.Ready);
            }
            logger.infof("Coffee order '%s' is now '%s' for '%s'", coffeeOrder.getRequestId(),
                coffeeOrder.getCoffeeStatus(), coffeeOrder.getCustomerName());
            return coffeeOrder;
        }
    }

    public List<CoffeeOrder> getCoffeeRequests() {
        List<CoffeeOrder> orders = new ArrayList<>();
        coffeeRequests.forEach((requestId, coffeeOrder) -> orders.add(coffeeOrder));
        return orders;
    }

}
