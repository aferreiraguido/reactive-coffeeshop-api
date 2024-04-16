package org.softtek.meetups;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.softtek.meetups.data.CoffeeOrder;
import org.softtek.meetups.model.CoffeeStatus;
import org.softtek.meetups.model.CoffeeType;
import org.softtek.meetups.service.CoffeeService;

import jakarta.inject.Inject;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CoffeeServiceUnitTest {

    @Inject
    CoffeeService coffeeService;

    CoffeeOrder coffeeOrder;

    static Integer orderRequests = 0;

    @BeforeEach
    void coffeeOrderRequest() {
        coffeeOrder = coffeeService.requestCoffee(CoffeeType.Latte, "Alice");
        orderRequests++;
    }

    @Test
    void newCoffeeOrder() {
        assertAll(
            () -> assertNotNull(coffeeOrder),
            () -> assertNotNull(coffeeOrder.getRequestId()),
            () -> assertEquals(coffeeOrder.getCoffeeType(), CoffeeType.Latte),
            () -> assertEquals(coffeeOrder.getCoffeeStatus(), CoffeeStatus.Received),
            () -> assertEquals(coffeeOrder.getCustomerName(), "Alice"),
            () -> assertEquals(coffeeService.getCoffeeOrder(coffeeOrder.getRequestId()), coffeeOrder));
    }

    @Test
    void progressCoffeeOrder() {
        coffeeService.progressCoffeeOrder(coffeeOrder.getRequestId());
        assertEquals(coffeeOrder.getCoffeeStatus(), CoffeeStatus.Preparation);
    }

    @Test
    void coffeeOrderReady() {
        UUID requestId = coffeeOrder.getRequestId();
        coffeeService.progressCoffeeOrder(requestId);
        coffeeService.progressCoffeeOrder(requestId);
        assertEquals(coffeeOrder.getCoffeeStatus(), CoffeeStatus.Ready);
    }

    @Test
    void transitiveReadyOrderClosure() {
        UUID requestId = coffeeOrder.getRequestId();
        coffeeService.progressCoffeeOrder(requestId);
        coffeeService.progressCoffeeOrder(requestId);
        coffeeService.progressCoffeeOrder(requestId);
        assertEquals(coffeeOrder.getCoffeeStatus(), CoffeeStatus.Ready);
    }

    @Test
    void coffeeOrdersWithAddedOrders() {
        assertEquals(orderRequests, coffeeService.getCoffeeRequests().size());
    }

}
