package org.softtek.meetups.model;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public enum CoffeeStatus {
    Received,
    Preparation,
    Ready
}
