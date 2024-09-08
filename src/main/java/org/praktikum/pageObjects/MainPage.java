package org.praktikum.pageObjects;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private WebDriver driver;
    private String url = "https://stellarburgers.nomoreparties.site/";

    private By personalCabinetButton = By.xpath("//p[contains(@class, 'AppHeader_header__linkText__3q_va') and text()='Личный Кабинет']");
    private By loginButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Войти в аккаунт']");
    private By bunsButton = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Булки']");
    private By fillingsButton = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Начинки']");
    private By saucesButton = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Соусы']");
    private final By bulkaElement = By.xpath("//img[@alt='Флюоресцентная булка R2-D3']");
    private final By sauceElement = By.xpath("//img[@alt='Соус Spicy-X']");
    private final By fillingsElement = By.xpath("//img[@alt='Мясо бессмертных моллюсков Protostomia']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание кликабельности элемента {0}")
    @Description("Ожидает, пока элемент не станет кликабельным на главной странице.")
    @DisplayName("Ожидание кликабельности элемента")
    public void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Клик по элементу {0}")
    @Description("Кликает по указанному элементу на главной странице.")
    @DisplayName("Клик по элементу")
    public void clickElement(By element) {
        driver.findElement(element).click();
    }

    @Step("Получение URL")
    @Description("Возвращает текущий URL главной страницы.")
    @DisplayName("Получение URL")
    public String getUrl() {
        return url;
    }

    @Step("Установка URL: {0}")
    @Description("Устанавливает URL главной страницы.")
    @DisplayName("Установка URL")
    public void setUrl(String url) {
        this.url = url;
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

    public By getBulkaElement() {
        return bulkaElement;
    }

    public By getSauceElement() {
        return sauceElement;
    }
    public By getFillingsElement() {
        return fillingsElement;
    }
}
