import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageobjects.LoginPage;
import org.praktikum.pageobjects.MainPage;
import org.praktikum.pageobjects.ProfilePage;
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
        driver = WebDriverFactory.initializeWebDriver();
        baseUrl = BaseURL.getBaseUrl();
        userData = UserData.createTestUser(
                "testuser_" + System.currentTimeMillis() + "@yandex.ru",
                "ValidPassword123",
                "TestUser");
        accessToken = userData.getAccessTokenFromResponse(userData.createNewUser());
        navigateToMainPageAndLogin();
    }

    @After
    @Step("Завершение теста: удаление тестового пользователя и закрытие браузера")
    @Description("Удаление тестового пользователя через API и закрытие браузера.")
    public void tearDown() {
        if (accessToken != null) {
            userData.deleteUser(accessToken);
        }
        WebDriverFactory.closeBrowser(driver);
    }

    @Test
    @DisplayName("Проверка перехода в личный кабинет")
    @Description("Тест проверяет, что пользователь может перейти в личный кабинет, нажав на кнопку 'Личный кабинет', и проверяет наличие кнопки 'Выход'.")
    public void testTransitionToPersonalCabinet() throws InterruptedException {
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForElementToBeClickable(profilePage.getLogoutButton());
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
        assertTrue("Мы не на главной странице после перехода", driver.getCurrentUrl().equals(BaseURL.getBaseUrlWithSlash()));
    }

    @Test
    @DisplayName("Проверка перехода на главную страницу по клику на логотип 'Stellar Burgers'")
    @Description("Тест проверяет переход с личного кабинета на главную страницу по клику на логотип 'Stellar Burgers'.")
    public void testTransitionToMainPageByStellarBurgersLogo() throws InterruptedException {
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForElementToBeClickable(profilePage.getLogoutButton());
        assertTrue("Кнопка 'Выход' не отображена", profilePage.isLogoutButtonDisplayed());
        profilePage.waitForElementToBeClickable(profilePage.getStellarBurgersLogo());
        profilePage.clickElement(profilePage.getStellarBurgersLogo());
        assertTrue("Мы не на главной странице после перехода", driver.getCurrentUrl().equals(BaseURL.getBaseUrlWithSlash()));
    }

    @Test
    @DisplayName("Проверка выхода из аккаунта через Личный кабинет")
    @Description("Тест проверяет, что пользователь может выйти из аккаунта, нажав на кнопку 'Выход', и что токен в локальном хранилище становится пустым.")
    public void testLogoutFromPersonalCabinet() throws InterruptedException {
        ProfilePage profilePage = new ProfilePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        profilePage.waitForElementToBeClickable(profilePage.getLogoutButton());
        profilePage.clickElement(profilePage.getLogoutButton());
        loginPage.waitForElementToBeClickable(loginPage.getLoginButton());
        assertTrue("Кнопка 'Войти' не отображена", driver.findElement(loginPage.getLoginButton()).isDisplayed());
        String localStorageToken = userData.getTokenFromLocalStorage(driver);
        assertTrue("Токен в localStorage не был удален", localStorageToken == null || localStorageToken.isEmpty());
    }

    @Step("Переход на главную страницу и авторизация")
    private void navigateToMainPageAndLogin() {
        MainPage mainPage = new MainPage(driver);
        mainPage.navigateToMainPage();
        mainPage.waitForElementToBeClickable(mainPage.getLoginButton());
        mainPage.clickElement(mainPage.getLoginButton());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(userData.getEmail());
        loginPage.enterPassword(userData.getPassword());
        loginPage.waitForElementToBeClickable(loginPage.getLoginButton());
        loginPage.clickElement(loginPage.getLoginButton());
        mainPage.waitForElementToBeClickable(mainPage.getPersonalCabinetButton());
        mainPage.clickElement(mainPage.getPersonalCabinetButton());
    }
}
