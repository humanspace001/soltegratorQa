package com.slotegrator.api;

public enum Constants1 {
    BASIC_AUTH_TOKEN("front_2d6b0a8391742f5d789d7d915755e09e"),
    BASE_URI("http://test-api.d6.dev.devcaz.com"),
    BASE_PATH("/v2"),
    OAUTH2("/oauth2/token"),
    PLAYERS("/players");

    private String value;

    Constants1(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
