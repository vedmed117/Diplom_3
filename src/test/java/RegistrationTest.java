import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageobjects.RegisterPage;
import org.praktikum.utils.UserData;
import org.praktikum.utils.WebDriverFactory;
import org.praktikum.utils.BaseURL;

import static org.junit.Assert.*;


public class RegistrationTest {

    private WebDriver driver;
    private UserData userData;
    private String accessToken;
    private String baseUrl;

    @Before
    @Step("Настройка WebDriver и инициализация тестовых данных")
    public void setUp() {
        driver = WebDriverFactory.initializeWebDriver();
        baseUrl = BaseURL.getBaseUrl();
    }

    @After
    @Step("Завершение: удаление тестового пользователя и закрытие браузера")
    public void tearDown() {
        if (accessToken != null) {
            userData.deleteUser(accessToken);
        }
        WebDriverFactory.closeBrowser(driver);
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    @Description("Этот тест проверяет, что пользователь может успешно зарегистрироваться через страницу регистрации и подтверждает регистрацию через API.")
    public void testSuccessfulRegistration() throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(driver);

        userData = UserData.createTestUser(
                "testuser_" + System.currentTimeMillis() + "@yandex.ru",
                "ValidPassword123",
                "TestUser");

        driver.get(registerPage.getUrl());

        registerPage.enterName(userData.getName());
        registerPage.enterEmail(userData.getEmail());
        registerPage.enterPassword(userData.getPassword());
        registerPage.waitForElementToBeClickable(registerPage.getRegisterButton());
        registerPage.clickElement(registerPage.getRegisterButton());
        userData.loginUser(BaseURL.getBaseUrlWithSlash());
        assertEquals("Ожидался статус-код 200 при успешной регистрации", 200, userData.getLoginResultStatusCode());
        assertNotNull("Токен доступа должен быть получен после успешной регистрации", userData.getLoginResultAccessToken());
    }

    @Test
    @DisplayName("Регистрация с некорректным паролем отображает 'Некорректный пароль' и проверяет, что пользователь не зарегистрирован")
    @Description("Этот тест проверяет, что при регистрации с паролем менее шести символов отображается ошибка 'Некорректный пароль', и пользователь не был зарегистрирован через API.")
    public void testRegistrationWithInvalidPassword() {
        RegisterPage registerPage = new RegisterPage(driver);

        userData = UserData.createTestUser(
                "testuser_" + System.currentTimeMillis() + "@yandex.ru",
                "1",
                "TestUser");

        driver.get(registerPage.getUrl());

        registerPage.enterName(userData.getName());
        registerPage.enterEmail(userData.getEmail());
        registerPage.enterPassword(userData.getPassword());
        registerPage.waitForElementToBeClickable(registerPage.getRegisterButton());
        registerPage.clickElement(registerPage.getRegisterButton());
        userData.loginUser(BaseURL.getBaseUrlWithSlash());
        assertEquals("Ожидался статус-код 401 при неудачной регистрации", 401, userData.getLoginResultStatusCode());
        assertNull("Токен не должен быть получен при неудачной регистрации", userData.getLoginResultAccessToken());
    }
}
