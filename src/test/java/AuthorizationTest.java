import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageObjects.ForgotPasswordPage;
import org.praktikum.pageObjects.MainPage;
import org.praktikum.pageObjects.LoginPage;
import org.praktikum.pageObjects.RegisterPage;
import org.praktikum.utils.BaseURL;
import org.praktikum.utils.UserData;
import org.praktikum.utils.WebDriverFactory;

import static org.junit.Assert.assertNotNull;

public class AuthorizationTest {

    private WebDriver driver;
    private String accessToken;
    private UserData userData;

    @Before
    @Step("Настройка WebDriver и создание тестового пользователя")
    @Description("Инициализация WebDriver, создание тестового пользователя через API и переход на главную страницу.")
    public void setUp() {
        initializeWebDriver();
        userData = createTestUser();
        accessToken = getAccessToken(userData);
        navigateToMainPage();
    }

    @After
    @Step("Завершение теста: удаление тестового пользователя и закрытие WebDriver")
    @Description("Удаляет тестового пользователя через API и закрывает экземпляр WebDriver.")
    public void tearDown() {
        deleteTestUser();
        closeBrowser();
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Личный кабинет' на главной странице")
    @Description("Тест проверяет, что пользователь может авторизоваться, нажав на кнопку 'Личный кабинет', и что токен доступа сохраняется в localStorage.")
    public void testLoginFromMainPageUsingPersonalCabinetButton() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        performLoginFromPersonalCabinet(mainPage);
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Войти' на главной странице")
    @Description("Тест проверяет, что пользователь может авторизоваться, нажав на кнопку 'Войти', и что токен доступа сохраняется в localStorage.")
    public void testLoginFromMainPageUsingLoginButton() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        performLoginFromLoginButton(mainPage);
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Войти' на странице регистрации")
    @Description("Тест проверяет, что пользователь может авторизоваться, нажав на кнопку 'Войти' на странице регистрации, и что токен доступа сохраняется в localStorage.")
    public void testLoginFromRegisterPage() throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(driver);
        driver.get(registerPage.getUrl());

        registerPage.waitForElementToBeClickable(registerPage.getLoginButton());
        registerPage.clickElement(registerPage.getLoginButton());

        Thread.sleep(5000);

        LoginPage loginPage = new LoginPage(driver);
        performLogin(loginPage);
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Войти' на странице восстановления пароля")
    @Description("Тест проверяет, что пользователь может авторизоваться, нажав на кнопку 'Войти' на странице восстановления пароля, и что токен доступа сохраняется в localStorage.")
    public void testLoginFromForgotPasswordPage() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        driver.get(loginPage.getUrl());

        loginPage.waitForElementToBeClickable(loginPage.getForgotPasswordButton());
        loginPage.clickElement(loginPage.getForgotPasswordButton());

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);

        forgotPasswordPage.waitForElementToBeClickable(forgotPasswordPage.getLoginButton());
        forgotPasswordPage.clickElement(forgotPasswordPage.getLoginButton());

        Thread.sleep(5000);

        performLogin(loginPage);
    }

    @Step("Инициализация WebDriver")
    private void initializeWebDriver() {
        driver = WebDriverFactory.createYandexDriver();
    }

    @Step("Создание тестового пользователя")
    private UserData createTestUser() {
        RestAssured.baseURI = BaseURL.getBaseUrl() + "api";
        return new UserData(
                "testuser_" + System.currentTimeMillis() + "@yandex.ru",
                "ValidPassword123",
                "TestUser"
        );
    }

    @Step("Получение токена доступа для тестового пользователя")
    private String getAccessToken(UserData userData) {
        Response response = userData.createNewUser();
        return userData.getAccessTokenFromResponse(response);
    }

    @Step("Переход на главную страницу")
    private void navigateToMainPage() {
        driver.get(BaseURL.getBaseUrl());
    }

    @Step("Удаление тестового пользователя")
    private void deleteTestUser() {
        if (accessToken != null) {
            userData.deleteUser(accessToken);
        }
    }

    @Step("Закрытие браузера")
    private void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Step("Получение токена из localStorage")
    private String getTokenFromLocalStorage() {
        return (String) ((JavascriptExecutor) driver).executeScript("return localStorage.getItem('accessToken');");
    }

    @Step("Авторизация через кнопку 'Личный кабинет'")
    private void performLoginFromPersonalCabinet(MainPage mainPage) throws InterruptedException {
        mainPage.waitForElementToBeClickable(mainPage.getPersonalCabinetButton());
        mainPage.clickElement(mainPage.getPersonalCabinetButton());
        Thread.sleep(5000);
        LoginPage loginPage = new LoginPage(driver);
        performLogin(loginPage);
    }

    @Step("Авторизация через кнопку 'Войти'")
    private void performLoginFromLoginButton(MainPage mainPage) throws InterruptedException {
        mainPage.waitForElementToBeClickable(mainPage.getLoginButton());
        mainPage.clickElement(mainPage.getLoginButton());
        Thread.sleep(5000);
        LoginPage loginPage = new LoginPage(driver);
        performLogin(loginPage);
    }

    @Step("Авторизация пользователя")
    private void performLogin(LoginPage loginPage) throws InterruptedException {
        loginPage.enterEmail(userData.getEmail());
        loginPage.enterPassword(userData.getPassword());
        loginPage.waitForElementToBeClickable(loginPage.getLoginButton());
        loginPage.clickElement(loginPage.getLoginButton());
        Thread.sleep(5000);
        String localStorageToken = getTokenFromLocalStorage();
        assertNotNull("Токен в localStorage не найден", localStorageToken);
    }
}
