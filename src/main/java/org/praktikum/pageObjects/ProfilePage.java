package org.praktikum.pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilePage {

    private WebDriver driver;

    private final By constructorButton = By.xpath("//p[contains(text(), 'Конструктор')]");
    private final By stellarBurgersLogo = By.cssSelector("body > div > div > header > nav > div > a > svg");

    private final By logoutButton = By.xpath("//button[text()='Выход']");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Ожидание кликабельности элемента: {element}")
    public void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Клик по элементу: {element}")
    public void clickElement(By element) {
        driver.findElement(element).click();
    }

    @Step("Получение локатора кнопки 'Конструктор'")
    public By getConstructorButton() {
        return constructorButton;
    }

    @Step("Получение локатора логотипа 'Stellar Burgers'")
    public By getStellarBurgersLogo() {
        return stellarBurgersLogo;
    }

    @Step("Получение локатора кнопки 'Выход'")
    public By getLogoutButton() {
        return logoutButton;
    }

    @Step("Проверка отображения кнопки 'Выход'")
    public boolean isLogoutButtonDisplayed() {
        return driver.findElement(logoutButton).isDisplayed();
    }
}
