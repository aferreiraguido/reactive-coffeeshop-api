package org.softtek.meetups.dao;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthorizationRequest {

    public String username;
    public String password;

}