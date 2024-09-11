package org.praktikum.utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.praktikum.pageobjects.LoginPage;
import org.praktikum.pageobjects.MainPage;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    private Map<String, Object> loginResult;
    private int loginResultStatusCode;
    private String loginResultAccessToken;

    @Step("Создание нового тестового пользователя с параметрами: {email}, {password}, {name}")
    public static UserData createTestUser(String email, String password, String name) {
        return new UserData(email, password, name);
    }

    @Step("Создание нового пользователя через API")
    public Response createNewUser() {
        return RestAssured.given()
                .contentType(JSON)
                .body(this)
                .when()
                .post(BaseURL.getBaseUrl()+API_URI.getRegisterUri()); //"https://stellarburgers.nomoreparties.site/api/auth/register");
    }

    @Step("Получение токена доступа для тестового пользователя")
    public String getAccessTokenFromResponse(Response response) {
        return response.jsonPath().getString("accessToken");
    }

    @Step("Удаление пользователя через API")
    public void deleteUser(String accessToken) {
        if (accessToken != null) {
            RestAssured.given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(BaseURL.getBaseUrl()+API_URI.getUserUri())
                    .then()
                    .statusCode(202);
        }
    }
    @Step("Логин пользователя и возврат статус-кода и токена")
    public void loginUser(String baseUrl) {
        Response response = io.restassured.RestAssured.given()
                .contentType("application/json")
                .body(this)
                .when()
                .post(baseUrl + "api/auth/login");

        loginResultStatusCode = response.getStatusCode();
        loginResultAccessToken = response.jsonPath().getString("accessToken");
        loginResult = new HashMap<>();
        loginResult.put("statusCode", loginResultStatusCode);
        loginResult.put("accessToken", loginResultAccessToken);
    }

    public int getLoginResultStatusCode() {
        return loginResultStatusCode;
    }

    public String getLoginResultAccessToken() {
        return loginResultAccessToken;
    }
    @Step("Явное ожидание токена из localStorage WebDriver")
    public String waitForTokenInLocalStorage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return localStorage.getItem('accessToken')") != null
        );
        return getTokenFromLocalStorage(driver);
    }
    @Step("Получение токена из localStorage WebDriver")
    public String getTokenFromLocalStorage(WebDriver driver) {
        return (String) ((JavascriptExecutor) driver).executeScript("return localStorage.getItem('accessToken');");
    }
    @Step("Авторизация пользователя")
    public void performLogin(WebDriver driver, LoginPage loginPage) {
        MainPage mainPage = new MainPage(driver);
        loginPage.enterEmail(getEmail());
        loginPage.enterPassword(getPassword());
        loginPage.waitForElementToBeClickable(loginPage.getLoginButton());
        loginPage.clickElement(loginPage.getLoginButton());
        mainPage.waitForElementToBeClickable(mainPage.getPersonalCabinetButton());
        String localStorageToken = waitForTokenInLocalStorage(driver);
        assertNotNull("Токен в localStorage не найден", localStorageToken);
    }

}