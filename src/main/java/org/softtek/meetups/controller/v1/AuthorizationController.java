package org.softtek.meetups.controller.v1;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;
import org.softtek.meetups.dao.AuthorizationRequest;
import org.softtek.meetups.dao.AuthorizationResponse;
import org.softtek.meetups.exception.UnauthorizedException;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@Path("/api/v1")
public class AuthorizationController {

    @Inject
    Logger logger;

    @POST
    @PermitAll
    @Path("/authorize")
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking
    public Response authorize(AuthorizationRequest authorizationRequest) {
        try {
            Set<String> authorizedGroups = new HashSet<>();
            // TODO - perform real username/password validation
            if (authorizationRequest.username.equals("customer") &&
                authorizationRequest.password.equals("customer")) {
                logger.infof("Customer '%s' accepted, issuing token", authorizationRequest.username);
                authorizedGroups.add("Customers");
            } else if (authorizationRequest.username.equals("staff") &&
                authorizationRequest.password.equals("staff")) {
                logger.infof("Staff '%s' accepted, issuing token", authorizationRequest.username);
                authorizedGroups.add("Staff");
            } else {
                throw new UnauthorizedException();
            }
            // issue a one-hour authorization token
            Instant instantIssuedAt = Instant.now();
            String authorization_token = Jwt.issuer("http://localhost")
                .upn(String.format("%s@localhost", authorizationRequest.username))
                .expiresAt(instantIssuedAt.plusSeconds(3600))
                .audience("http://localhost")
                .issuedAt(instantIssuedAt)
                .groups(authorizedGroups)
                .sign();
            // successful JWT response
            return Response.ok(new AuthorizationResponse(authorization_token)).build();
        } catch (UnauthorizedException e) {
            logger.warnf("User '%s' not authorized", authorizationRequest.username);
        } catch (Exception e) {
            logger.error("Caught '%s' during jwt sign in due to '%s'", e.getMessage(), e.getCause());
        }
        // unauthorized reply
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
