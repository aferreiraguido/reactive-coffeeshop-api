package org.softtek.meetups;

import org.softtek.meetups.wrappers.AuthorizationRequestBuilder;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
public class AuthorizationUnitTest {

    @Test
    void customerAuthorization() {
        given()
            .contentType(ContentType.JSON)
            .body(new AuthorizationRequestBuilder().username("customer").password("customer").build())
            .when()
            .post("/api/v1/authorize")
            .then()
            .statusCode(200);
    }

    @Test
    void staffAuthorization() {
        given()
            .contentType(ContentType.JSON)
            .body(new AuthorizationRequestBuilder().username("staff").password("staff").build())
            .when()
            .post("/api/v1/authorize")
            .then()
            .statusCode(200);
    }

}