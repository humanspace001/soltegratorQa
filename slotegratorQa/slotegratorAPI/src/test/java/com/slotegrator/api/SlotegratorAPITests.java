package com.slotegrator.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SlotegratorAPITests {

    private static String accessToken;
    private static String registrUserAccessToken;
    private static String registrUserRefreshToken;
    private static Player newPlayer = new Player();

    @Order(1)
    @Test
    @DisplayName("get guest Access Token")
    void postRequestToken200() {

        Map<String, String> params = new HashMap<>();
        params.put("scope", "guest:default");

        System.out.println("1. [REQUEST] get guest Access Token");
        Response response = RequestHelper.auth("client_credentials", params);

        System.out.println("1. [RESPONSE] get guest Access Token");
        response.then().log().body();
        accessToken = response.path("access_token");
        int statusCode = response.getStatusCode();

        assertThat(accessToken, is(notNullValue()));
        assertThat(accessToken, is(not(equalTo( "wrong token"))));
        assertEquals(200, statusCode, "Uncorrect status code returned! Should be 200!");
    }

    @Order(2)
    @Test
    @DisplayName("register a new Player")
    public void registerANewPlayer() {

        System.out.println("2. [REQUEST] register a new Player");

        Response registration = given().urlEncodingEnabled(true)
                .spec(RequestHelper.getRequestSpecificationBuilder().build())
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Authorization", "Bearer " + accessToken)
                .param("username", newPlayer.getUsername())
                .param("name", newPlayer.getName())
                .param("surname", newPlayer.getSurname())
                .param("password_change", newPlayer.getEncodedPasswordString())
                .param("password_repeat", newPlayer.getEncodedPasswordString())
                .param("email", newPlayer.getEmail())
                .param("currency_code", newPlayer.getCurrencyCode())
                .log().params()
                .when()
                .post(Constants1.PLAYERS.getValue());

        System.out.println("2. [RESPONSE] register a new Player");

        registration
                .then()
                .log().body()
                .assertThat()
                .statusCode(201)
                .body("username", equalTo(newPlayer.getUsername()))
                .body("email", equalTo(newPlayer.getEmail()))
                .body("name", equalTo(newPlayer.getName()))
                .body("surname", equalTo(newPlayer.getSurname()))
                .body(matchesJsonSchemaInClasspath("createNewUserResponseJSONSchema.json"));

        newPlayer.setId(registration.path("id"));
        newPlayer.setCountry_id(registration.path("country_id"));
        newPlayer.setTimezone_id(registration.path("timezone_id"));
        newPlayer.setGender(registration.path("gender"));
        newPlayer.setPhone_number(registration.path("phone_number"));
        newPlayer.setBirthdate(registration.path("birthdate"));
        newPlayer.setBonuses_allowed(registration.path("bonuses_allowed"));
        newPlayer.setIs_verified(registration.path("is_verified"));
    }

    @Order(3)
    @Test
    @DisplayName("authenticate as recently created Player")
    public void authAsCreatedPlayer() {

        Stream<String> forApiTest = Stream.of("client_id", "client_secret", "grant_type");
        Map<String, String> authParams = new HashMap<>();
        forApiTest.forEach(key -> authParams.put(key, "test"));
        Response response = RequestHelper.auth("client_credentials", authParams);
        response.then().log().body();
        accessToken = response.path("access_token");
        int statusCode = response.getStatusCode();
        assertThat(accessToken, is(notNullValue()));
        assertThat(accessToken, is(not(equalTo(""))));
        assertEquals(200, statusCode, "Uncorrect status code returned! Should be 200!");
    }

    @Order(4)
    @Test
    @DisplayName("get player self profile")
    public void getSelfPlayerProfile() {

        System.out.println("4. [REQUEST] get player self profile");

        Response playerProfile = given().urlEncodingEnabled(true)
                .spec(RequestHelper.getRequestSpecificationBuilder().build())
                .header("Authorization", "Bearer " + registrUserAccessToken)
                //.pathParam("userId", newPlayer.getId())
                .log().all()
                .when()
                .get(Constants1.PLAYERS + "/{userId}");

        System.out.println("4. [RESPONSE] get player self profile");

        playerProfile
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .body(matchesJsonSchemaInClasspath("getUserResponseJSONSchema.json"));

    }

    @Order(5)
    @Test
    @DisplayName("get another player profile")
    public void getOtherPlayerProfile() {

        System.out.println("5. [REQUEST] get another player profile");

        Response otherProfile = given().urlEncodingEnabled(true)
                .spec(RequestHelper.getRequestSpecificationBuilder().build())
                .header("Authorization", "Bearer " + registrUserAccessToken)
               // .pathParam("userId", newPlayer.getId() - 1)
                .log().all()
                .when()
                .get(Constants1.PLAYERS + "/{userId}");

        System.out.println("5. [RESPONSE] get another player profile");

        otherProfile
                .then()
                .log().body()
                .assertThat().statusCode(404);
    }
}
