package org.praktikum.pageobjects;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.praktikum.utils.BaseURL;

public class MainPage {
    private WebDriver driver;
    private String url = BaseURL.getBaseUrlWithSlash(); //"https://stellarburgers.nomoreparties.site/";

    private By personalCabinetButton = By.xpath("//p[contains(@class, 'AppHeader_header__linkText__3q_va') and text()='Личный Кабинет']");
    private By loginButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Войти в аккаунт']");
    private By bunsButton = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Булки']");
    private By fillingsButton = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Начинки']");
    private By saucesButton = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Соусы']");
    private final By bulkaSection = By.xpath("//h2[contains(@class, 'text_type_main-medium') and text()='Булки']");

    private final By sauceSection = By.xpath("//h2[contains(@class, 'text_type_main-medium') and text()='Соусы']");
    private final By fillingsSection = By.xpath("//h2[contains(@class, 'text_type_main-medium') and text()='Начинки']");


    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание кликабельности элемента {0}")
    @DisplayName("Ожидание кликабельности элемента")
    public void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Клик по элементу {0}")
    @DisplayName("Клик по элементу")
    public void clickElement(By element) {
        driver.findElement(element).click();
    }

    @Step("Получение URL")
    @DisplayName("Получение URL")
    public String getUrl() {
        return url;
    }

    @Step("Установка URL: {0}")
    @DisplayName("Установка URL")
    public void setUrl(String url) {
        this.url = url;
    }

    @Step("Переход на главную страницу")
    @DisplayName("Переход на главную страницу")
    public void navigateToMainPage() {
        driver.get(BaseURL.getBaseUrl());
    }

    @Step("Проверка, что мы на главной странице")
    public boolean isOnMainPage() {
        return driver.getCurrentUrl().equals(url);
    }

    public By getPersonalCabinetButton() {
        return personalCabinetButton;
    }

    public By getLoginButton() {
        return loginButton;
    }

    public By getBunsButton() {
        return bunsButton;
    }

    public By getFillingsButton() {
        return fillingsButton;
    }

    public By getSaucesButton() {
        return saucesButton;
    }

    public By getBulkaSection() {
        return bulkaSection;
    }

    public By getSauceSection() {
        return sauceSection;
    }

    public By getFillingsSection() {
        return fillingsSection;
    }
}
