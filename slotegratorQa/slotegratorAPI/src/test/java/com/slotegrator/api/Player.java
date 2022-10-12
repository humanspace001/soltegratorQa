package com.slotegrator.api;

import com.github.javafaker.Faker;

import java.util.Base64;


public class Player {
    private final String name;
    private final String surname;
    private final String username;
    private final String password;
    private final String encodedPasswordString;
    private final String email;
    private final String currencyCode;
    private Integer id;
    private Integer country_id;
    private Integer timeZone_id;
    private String gender;
    private String birthdate;
    private Boolean bonuses_allowed;
    private Boolean is_verified;

    public Player() {
        Faker faker = new Faker();
        name = faker.name().firstName();
        surname = faker.name().lastName();
        username = name + surname + System.currentTimeMillis() % 9999;
        password = "1234568";
        encodedPasswordString = Base64.getEncoder().encodeToString(password.getBytes());
        email = username + "@gmail.com";
        currencyCode = "RUB";
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }

    public void setTimezone_id(Integer timezone_id) {
        this.timeZone_id = timezone_id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone_number(String phone_number) {
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setBonuses_allowed(Boolean bonuses_allowed) {
        this.bonuses_allowed = bonuses_allowed;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getEncodedPasswordString() {
        return encodedPasswordString;
    }

    public String getEmail() {
        return email;
    }

    public Integer getCountry_id() {
        return country_id;
    }

    public Integer getTimeZone_id() {
        return timeZone_id;
    }

    public void setTimeZone_id(Integer timeZone_id) {
        this.timeZone_id = timeZone_id;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public Boolean getBonuses_allowed() {
        return bonuses_allowed;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }
}
