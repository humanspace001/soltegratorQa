package com.slotegrator.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;


class RequestHelper {

    private static RequestSpecBuilder requestSpecBuilder;

    static RequestSpecBuilder getRequestSpecificationBuilder() {
        createRequestSpecification();
        return requestSpecBuilder;
    }

    private static void createRequestSpecification() {
        requestSpecBuilder = new RequestSpecBuilder()
                .setBaseUri(Constants1.BASE_URI.getValue())
                .setBasePath(Constants1.BASE_PATH.getValue())
                .addHeader("Accept", ContentType.JSON.getAcceptHeader());
    }

    static Response auth(String grantTypeValue, Map authParams) {
        createRequestSpecification();

        RequestSpecification authParamsSpecification = requestSpecBuilder
                .addParams(authParams)
                .build();

        return given().urlEncodingEnabled(true)
                .auth().preemptive()
                .basic(Constants1.BASIC_AUTH_TOKEN.getValue(), "")
                .param("grant_type", grantTypeValue)
                .spec(authParamsSpecification)
                .when()
                .log().all()
                .post(Constants1.OAUTH2.getValue());
    }
}