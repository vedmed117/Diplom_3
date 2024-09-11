package org.praktikum.pageobjects;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.praktikum.utils.BaseURL;

public class LoginPage {
    private WebDriver driver;
    private String url = BaseURL.getBaseUrl() + "/login";

    private By emailField = By.xpath("//input[@name='name']");
    private By passwordField = By.xpath("//input[@name='Пароль']");
    private By loginButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Войти']");
    private By forgotPasswordButton = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Восстановить пароль']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание кликабельности элемента {0}")
    @Description("Ожидает, пока элемент станет кликабельным на странице входа.")
    @DisplayName("Ожидание кликабельности элемента")
    public void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Клик на элемент {0}")
    @Description("Выполняет клик по указанному элементу на странице входа.")
    @DisplayName("Клик на элемент")
    public void clickElement(By element) {
        driver.findElement(element).click();
    }

    @Step("Ввод email {0}")
    @Description("Вводит email пользователя на странице входа.")
    @DisplayName("Ввод email")
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    @Step("Ввод пароля")
    @Description("Вводит пароль пользователя на странице входа.")
    @DisplayName("Ввод пароля")
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    @Step("Получение URL страницы входа")
    @Description("Возвращает URL страницы входа.")
    @DisplayName("Получение URL")
    public String getUrl() {
        return url;
    }

    @Step("Установка URL на {0}")
    @Description("Устанавливает URL для страницы входа.")
    @DisplayName("Установка URL")
    public void setUrl(String url) {
        this.url = url;
    }

    public By getLoginButton() {
        return loginButton;
    }

    public By getForgotPasswordButton() {
        return forgotPasswordButton;
    }
}
