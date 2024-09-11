package org.praktikum.pageobjects;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.praktikum.utils.BaseURL;

public class ForgotPasswordPage {
    private WebDriver driver;
    private String url = BaseURL.getBaseUrl()+ "/forgot-password";//"https://stellarburgers.nomoreparties.site/forgot-password";

    private By loginButton = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание кликабельности элемента {0}")
    @Description("Ожидает, пока элемент станет кликабельным на странице восстановления пароля.")
    @DisplayName("Ожидание кликабельности элемента")
    public void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Клик на элемент {0}")
    @Description("Выполняет клик по указанному элементу на странице восстановления пароля.")
    @DisplayName("Клик на элемент")
    public void clickElement(By element) {
        driver.findElement(element).click();
    }

    @Step("Получение URL страницы восстановления пароля")
    @Description("Возвращает URL страницы восстановления пароля.")
    @DisplayName("Получение URL")
    public String getUrl() {
        return url;
    }

    @Step("Установка URL на {0}")
    @Description("Устанавливает URL для страницы восстановления пароля.")
    @DisplayName("Установка URL")
    public void setUrl(String url) {
        this.url = url;
    }

    public By getLoginButton() {
        return loginButton;
    }
}
