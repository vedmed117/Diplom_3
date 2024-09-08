import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageObjects.LoginPage;
import org.praktikum.pageObjects.MainPage;
import org.praktikum.pageObjects.ProfilePage;
import org.praktikum.utils.BaseURL;
import org.praktikum.utils.UserData;
import org.praktikum.utils.WebDriverFactory;

import static org.junit.Assert.assertTrue;

public class ProfileTest {

    private WebDriver driver;
    private UserData userData;
    private String accessToken;
    private String baseUrl;

    @Before
    @Step("Настройка WebDriver и создание тестового пользователя")
    @Description("Инициализация WebDriver, создание тестового пользователя через API и вход на главную страницу.")
    public void setUp() {
        driver = WebDriverFactory.createYandexDriver(); // Инициализация через WebDriverFactory
        baseUrl = BaseURL.getBaseUrl(); // Получение URL из класса BaseURL
        userData = createTestUser();
        accessToken = getAccessToken(userData);
        navigateToMainPageAndLogin();
    }

    @After
    @Step("Завершение теста: удаление тестового пользователя и закрытие браузера")
    @Description("Удаление тестового пользователя через API и закрытие браузера.")
    public void tearDown() {
        if (accessToken != null) {
            deleteTestUser();
        }
        closeBrowser();
    }

    @Test
    @DisplayName("Проверка перехода в личный кабинет")
    @Description("Тест проверяет, что пользователь может перейти в личный кабинет, нажав на кнопку 'Личный кабинет', и проверяет наличие кнопки 'Выход'.")
    public void testTransitionToPersonalCabinet() throws InterruptedException {
        ProfilePage profilePage = new ProfilePage(driver);

        // Ожидание кликабельности кнопки "Выход"
        profilePage.waitForElementToBeClickable(profilePage.getLogoutButton());

        // Проверяем, что кнопка "Выход" отображается
        assertTrue("Кнопка 'Выход' не отображена", profilePage.isLogoutButtonDisplayed());
    }

    @Test
    @DisplayName("Проверка перехода в 'Конструктор' из личного кабинета")
    @Description("Тест проверяет переход из личного кабинета на главную страницу, нажав на кнопку 'Конструктор'.")
    public void testTransitionToConstructorFromProfile() throws InterruptedException {
        ProfilePage profilePage = new ProfilePage(driver);

        profilePage.waitForElementToBeClickable(profilePage.getLogoutButton());
        assertTrue("Кнопка 'Выход' не отображена", profilePage.isLogoutButtonDisplayed());

        profilePage.waitForElementToBeClickable(profilePage.getConstructorButton());
        profilePage.clickElement(profilePage.getConstructorButton());

        assertTrue("Мы не на главной странице после перехода", driver.getCurrentUrl().equals(baseUrl));
    }

    @Test
    @DisplayName("Проверка перехода на главную страницу по клику на логотип 'Stellar Burgers'")
    @Description("Тест проверяет переход с личного кабинета на главную страницу по клику на логотип 'Stellar Burgers'.")
    public void testTransitionToMainPageByStellarBurgersLogo() throws InterruptedException {
        ProfilePage profilePage = new ProfilePage(driver);

        profilePage.waitForElementToBeClickable(profilePage.getLogoutButton());
        assertTrue("Кнопка 'Выход' не отображена", profilePage.isLogoutButtonDisplayed());

        profilePage.clickElement(profilePage.getStellarBurgersLogo());

        assertTrue("Мы не на главной странице после перехода", driver.getCurrentUrl().equals(baseUrl));
    }

    @Test
    @DisplayName("Проверка выхода из аккаунта через Личный кабинет")
    @Description("Тест проверяет, что пользователь может выйти из аккаунта, нажав на кнопку 'Выход', и что токен в локальном хранилище становится пустым.")
    public void testLogoutFromPersonalCabinet() throws InterruptedException {
        ProfilePage profilePage = new ProfilePage(driver);

        // Ожидание кликабельности кнопки "Выход"
        profilePage.waitForElementToBeClickable(profilePage.getLogoutButton());

        // Клик по кнопке "Выход"
        profilePage.clickElement(profilePage.getLogoutButton());

        // Ждем 2 секунды
        waitForSeconds(2);

        // Проверка, что токен в localStorage пустой
        String localStorageToken = getTokenFromLocalStorage();
        assertTrue("Токен в localStorage не был удален", localStorageToken == null || localStorageToken.isEmpty());
    }

    @Step("Получение токена из localStorage")
    private String getTokenFromLocalStorage() {
        return (String) ((JavascriptExecutor) driver).executeScript("return localStorage.getItem('accessToken');");
    }

    @Step("Ожидание в течение нескольких секунд")
    private void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Остальные методы для создания, удаления тестовых пользователей и авторизации
    @Step("Создание тестового пользователя")
    private UserData createTestUser() {
        RestAssured.baseURI = baseUrl + "api";
        return new UserData(
                "testuser_" + System.currentTimeMillis() + "@yandex.ru",
                "ValidPassword123",
                "TestUser"
        );
    }

    @Step("Получение AccessToken")
    private String getAccessToken(UserData userData) {
        Response response = userData.createNewUser();
        return userData.getAccessTokenFromResponse(response);
    }

    @Step("Переход на главную страницу и авторизация")
    private void navigateToMainPageAndLogin() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);

        // Нажимаем кнопку "Войти в аккаунт"
        mainPage.waitForElementToBeClickable(mainPage.getLoginButton());
        mainPage.clickElement(mainPage.getLoginButton());

        // Вводим данные для входа
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(userData.getEmail());
        loginPage.enterPassword(userData.getPassword());

        // Нажимаем кнопку "Войти"
        loginPage.waitForElementToBeClickable(loginPage.getLoginButton());
        loginPage.clickElement(loginPage.getLoginButton());

        // Переход в личный кабинет
        mainPage.waitForElementToBeClickable(mainPage.getPersonalCabinetButton());
        mainPage.clickElement(mainPage.getPersonalCabinetButton());
    }

    @Step("Удаление тестового пользователя")
    private void deleteTestUser() {
        if (accessToken != null) {
            RestAssured.given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("/auth/user")
                    .then()
                    .statusCode(202);
        }
    }

    @Step("Закрытие браузера")
    private void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
