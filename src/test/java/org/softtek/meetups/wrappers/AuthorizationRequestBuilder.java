package org.softtek.meetups.wrappers;

import org.softtek.meetups.dao.AuthorizationRequest;

public class AuthorizationRequestBuilder {

    private AuthorizationRequest authorizationRequest = new AuthorizationRequest();

    public AuthorizationRequestBuilder username(String username) {
        authorizationRequest.username = username;
        return this;
    }

    public AuthorizationRequestBuilder password(String password) {
        authorizationRequest.password = password;
        return this;
    }

    public AuthorizationRequest build() {
        return authorizationRequest;
    }

}
