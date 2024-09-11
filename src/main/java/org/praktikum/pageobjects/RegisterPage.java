package org.praktikum.pageobjects;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.praktikum.utils.BaseURL;

public class RegisterPage {
    private WebDriver driver;
    private String url = BaseURL.getBaseUrl() + "/register";

    private By nameField = By.xpath("//label[text()='Имя']/following-sibling::input");
    private By emailField = By.xpath("//label[text()='Email']/following-sibling::input");
    private By passwordField = By.xpath("//input[@name='Пароль']");

    private By registerButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Зарегистрироваться']");
    private By loginButton = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");

    private By passwordErrorMessage = By.xpath("//p[@class='input__error text_type_main-default' and text()='Некорректный пароль']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание кликабельности элемента {0}")
    @Description("Ожидает, когда элемент на странице регистрации станет кликабельным.")
    @DisplayName("Ожидание кликабельности элемента")
    public void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Клик по элементу {0}")
    @Description("Производит клик по указанному элементу на странице регистрации.")
    @DisplayName("Клик по элементу")
    public void clickElement(By element) {
        driver.findElement(element).click();
    }

    @Step("Ввод имени {0}")
    @Description("Вводит имя пользователя на странице регистрации.")
    @DisplayName("Ввод имени")
    public void enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    @Step("Ввод email {0}")
    @Description("Вводит email пользователя на странице регистрации.")
    @DisplayName("Ввод email")
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    @Step("Ввод пароля")
    @Description("Вводит пароль пользователя на странице регистрации.")
    @DisplayName("Ввод пароля")
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    @Step("Проверка отображения сообщения 'Некорректный пароль'")
    @Description("Проверяет, отображается ли сообщение об ошибке 'Некорректный пароль' на странице регистрации.")
    @DisplayName("Проверка ошибки пароля")
    public boolean isPasswordErrorMessageDisplayed() {
        return driver.findElement(passwordErrorMessage).isDisplayed();
    }

    @Step("Получение URL страницы регистрации")
    @Description("Получает URL страницы регистрации.")
    @DisplayName("Получение URL")
    public String getUrl() {
        return url;
    }

    @Step("Установка URL {0}")
    @Description("Устанавливает URL страницы регистрации.")
    @DisplayName("Установка URL")
    public void setUrl(String url) {
        this.url = url;
    }

    public By getRegisterButton() {
        return registerButton;
    }

    public By getLoginButton() {
        return loginButton;
    }

    public By getPasswordErrorMessage() {
        return passwordErrorMessage;
    }
}
