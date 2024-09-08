package org.praktikum.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;

public class UserData {
    private String email;
    private String password;
    private String name;

    public UserData() {
    }

    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Response createNewUser() {
        return RestAssured.given()
                .contentType(JSON)
                .body(this)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/register");
    }

    public String getAccessTokenFromResponse(Response response) {
        return response.jsonPath().getString("accessToken");
    }

    public Response deleteUser(String accessToken) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .when()
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user")
                .then()
                .statusCode(202)  // Ожидаем статус 202
                .extract()
                .response();
    }
}
