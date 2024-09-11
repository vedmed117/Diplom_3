import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageobjects.ForgotPasswordPage;
import org.praktikum.pageobjects.MainPage;
import org.praktikum.pageobjects.LoginPage;
import org.praktikum.pageobjects.RegisterPage;
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
        driver = WebDriverFactory.initializeWebDriver();

        // Передаем нужные аргументы (email, password, name)
        userData = UserData.createTestUser(
                "testuser_" + System.currentTimeMillis() + "@yandex.ru",
                "ValidPassword123",
                "TestUser"
        );

        accessToken = userData.getAccessTokenFromResponse(userData.createNewUser());
        MainPage mainPage = new MainPage(driver);
        mainPage.navigateToMainPage();
    }

    @After
    @Step("Удаление тестового пользователя и закрытие WebDriver")
    @Description("Удаляет тестового пользователя через API и закрывает экземпляр WebDriver.")
    public void tearDown() {
        if (accessToken != null) {
            userData.deleteUser(accessToken);
        }
        WebDriverFactory.closeBrowser(driver);
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Личный кабинет' на главной странице")
    @Description("Тест проверяет, что пользователь может авторизоваться, нажав на кнопку 'Личный кабинет', и что токен доступа сохраняется в localStorage.")
    public void testLoginFromMainPageUsingPersonalCabinetButton() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        mainPage.waitForElementToBeClickable(mainPage.getPersonalCabinetButton());
        mainPage.clickElement(mainPage.getPersonalCabinetButton());
        userData.performLogin(driver, loginPage);
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Войти' на главной странице")
    @Description("Тест проверяет, что пользователь может авторизоваться, нажав на кнопку 'Войти', и что токен доступа сохраняется в localStorage.")
    public void testLoginFromMainPageUsingLoginButton() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        mainPage.waitForElementToBeClickable(mainPage.getLoginButton());
        mainPage.clickElement(mainPage.getLoginButton());
        userData.performLogin(driver, loginPage);
    }

    @Test
    @DisplayName("Авторизация через кнопку 'Войти' на странице регистрации")
    @Description("Тест проверяет, что пользователь может авторизоваться, нажав на кнопку 'Войти' на странице регистрации, и что токен доступа сохраняется в localStorage.")
    public void testLoginFromRegisterPage() throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        driver.get(registerPage.getUrl());
        registerPage.waitForElementToBeClickable(registerPage.getLoginButton());
        registerPage.clickElement(registerPage.getLoginButton());
        userData.performLogin(driver, loginPage);
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
        userData.performLogin(driver, loginPage);
    }


}
