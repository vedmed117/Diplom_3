import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageObjects.RegisterPage;
import org.praktikum.utils.UserData;
import org.praktikum.utils.WebDriverFactory;
import org.praktikum.utils.BaseURL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RegistrationTest {

    private WebDriver driver;
    private UserData userData;
    private String accessToken;
    private String baseUrl;

    @Before
    @Step("Настройка WebDriver и инициализация тестовых данных")
    public void setUp() {
        initializeWebDriver();
        baseUrl = BaseURL.getBaseUrl(); // Получаем базовый URL из класса BaseUrl
    }

    @After
    @Step("Завершение: удаление тестового пользователя и закрытие браузера")
    public void tearDown() {
        if (accessToken != null) {
            deleteTestUser();
        }
        closeBrowser();
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    @Description("Этот тест проверяет, что пользователь может успешно зарегистрироваться через страницу регистрации и подтверждает регистрацию через API.")
    @Step("Выполнение теста успешной регистрации пользователя")
    public void testSuccessfulRegistration() {
        RegisterPage registerPage = new RegisterPage(driver);

        // Создаем пользователя с валидными данными
        userData = createTestUserWithValidData();

        // Переход на страницу регистрации
        driver.get(registerPage.getUrl());

        // Ввод данных для регистрации
        registerPage.enterName(userData.getName());
        registerPage.enterEmail(userData.getEmail());
        registerPage.enterPassword(userData.getPassword());

        // Клик на кнопку "Зарегистрироваться"
        registerPage.waitForElementToBeClickable(registerPage.getRegisterButton());
        registerPage.clickElement(registerPage.getRegisterButton());

        // Проверка успешной регистрации через API: Логинимся и проверяем наличие токена
        loginAndVerifyUser();
    }

    @Test
    @DisplayName("Регистрация с некорректным паролем отображает 'Некорректный пароль' и проверяет, что пользователь не зарегистрирован")
    @Description("Этот тест проверяет, что при регистрации с паролем менее шести символов отображается ошибка 'Некорректный пароль', и пользователь не был зарегистрирован через API.")
    @Step("Выполнение теста регистрации с некорректным паролем")
    public void testRegistrationWithInvalidPassword() {
        RegisterPage registerPage = new RegisterPage(driver);

        // Создаем пользователя с некорректным паролем
        userData = createTestUserWithInvalidPassword();

        // Переход на страницу регистрации
        driver.get(registerPage.getUrl());

        // Ввод данных для регистрации с некорректным паролем (один символ)
        registerPage.enterName(userData.getName());
        registerPage.enterEmail(userData.getEmail());
        registerPage.enterPassword(userData.getPassword());

        // Клик на кнопку "Зарегистрироваться"
        registerPage.waitForElementToBeClickable(registerPage.getRegisterButton());
        registerPage.clickElement(registerPage.getRegisterButton());

        // Проверяем, что отображается ошибка "Некорректный пароль"
        assertTrue("Сообщение 'Некорректный пароль' не отображено", registerPage.isPasswordErrorMessageDisplayed());

        // Проверяем через API, что пользователь не был зарегистрирован
        Response response = loginUserWithInvalidData();
        assertEquals(401, response.getStatusCode());
        assertEquals("email or password are incorrect", response.jsonPath().getString("message"));
    }

    @Step("Инициализация WebDriver через WebDriverFactory")
    private void initializeWebDriver() {
        driver = WebDriverFactory.createYandexDriver(); // Используем фабрику для создания драйвера
    }

    @Step("Создание тестового пользователя с валидными данными")
    private UserData createTestUserWithValidData() {
        return new UserData(
                "testuser_" + System.currentTimeMillis() + "@yandex.ru",
                "ValidPassword123",
                "TestUser"
        );
    }

    @Step("Создание тестового пользователя с некорректным паролем")
    private UserData createTestUserWithInvalidPassword() {
        return new UserData(
                "testuser_invalid_" + System.currentTimeMillis() + "@yandex.ru",
                "1", // Некорректный пароль, меньше 6 символов
                "TestUser"
        );
    }

    @Step("Логин и проверка успешной регистрации через API")
    private void loginAndVerifyUser() {
        RestAssured.baseURI = baseUrl + "api"; // Используем baseUrl из BaseUrl
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(userData)
                .when()
                .post("/auth/login");

        // Проверяем, что токен был успешно получен, что подтверждает успешную регистрацию
        accessToken = response.jsonPath().getString("accessToken");
        assertNotNull("Не удалось получить accessToken, регистрация не удалась", accessToken);
    }

    @Step("Логин и проверка, что пользователь не был зарегистрирован через API")
    private Response loginUserWithInvalidData() {
        RestAssured.baseURI = baseUrl + "api"; // Используем baseUrl из BaseUrl
        return RestAssured.given()
                .contentType("application/json")
                .body(userData)
                .when()
                .post("/auth/login");
    }

    @Step("Удаление тестового пользователя через API")
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
