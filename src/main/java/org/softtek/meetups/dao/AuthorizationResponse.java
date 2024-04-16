package org.softtek.meetups.dao;

public class AuthorizationResponse {

    private String auth_token;

    public String getAuth_token() {
        return this.auth_token;
    }

    public AuthorizationResponse(String authorization) {
        this.auth_token = authorization;
    }
}