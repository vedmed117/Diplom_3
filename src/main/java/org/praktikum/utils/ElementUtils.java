package org.praktikum.utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;

public class ElementUtils {

    @Step("Ожидание кликабельности элемента {0}")
    public static void waitForElementToBeClickable(WebDriver driver, By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Проверка видимости элемента {0}")
    public static void verifyElementIsVisible(WebDriver driver, By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    @Step("Ожидание, что элемент '{1}' станет видимым")
    public static void waitUntilElementIsVerifiedToBeVisible(WebDriver driver, By sectionLocator, String sectionName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(d -> isElementInViewport(driver, sectionLocator));
        boolean isVisible = isElementInViewport(driver, sectionLocator);

        if (isVisible) {
            System.out.println("Элемент '" + sectionName + "' уже виден пользователю.");
        } else {
            System.out.println("Элемент '" + sectionName + "' не был виден пользователю, ожидаем его появления.");
        }

        assertTrue("Элемент '" + sectionName + "' не виден пользователю", isVisible);
    }

    @Step("Клик по элементу '{1}'")
    public static void clickElement(WebDriver driver, By elementLocator, String elementName) {
        waitForElementToBeClickable(driver, elementLocator);
        driver.findElement(elementLocator).click();
        System.out.println("Клик по кнопке '" + elementName + "' выполнен.");
    }

    @Step("Проверка, что элемент видим пользователю")
    public static boolean isElementInViewport(WebDriver driver, By element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (Boolean) jsExecutor.executeScript(
                "var elem = arguments[0],                 " +
                        "  box = elem.getBoundingClientRect(),    " +
                        "  cx = box.left + box.width / 2,         " +
                        "  cy = box.top + box.height / 2,         " +
                        "  e = document.elementFromPoint(cx, cy); " +
                        "for (; e; e = e.parentElement) {         " +
                        "  if (e === elem)                        " +
                        "    return true;                         " +
                        "}                                        " +
                        "return false;",
                driver.findElement(element)
        );
    }

    @Step("Проверка видимости секции в области просмотра")
    public static void verifySectionVisible(WebDriver driver, By sectionLocator, String sectionName) {
        boolean isVisible = isElementInViewport(driver, sectionLocator);

        if (isVisible) {
            System.out.println("Элемент '" + sectionName + "' уже виден пользователю.");
        } else {
            System.out.println("Элемент '" + sectionName + "' не был виден пользователю, ожидаем его появления.");
        }

        assertTrue("Элемент '" + sectionName + "' не виден пользователю", isVisible);
    }
}
