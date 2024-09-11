package org.praktikum.utils;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WebDriverFactory {

    @Step("Инициализация WebDriver")
    public static WebDriver initializeWebDriver() {
        String browser = getBrowser();  // Получаем название браузера

        switch (browser.toLowerCase()) {
            case "chrome":
                return createChromeDriver();
            case "yandex":
                return createYandexDriver();
            default:
                throw new IllegalArgumentException("Неизвестный браузер: " + browser);
        }
    }

    @Step("Закрытие браузера")
    public static void closeBrowser(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }

    private static String getBrowser() {
        String browser = System.getProperty("browser");
        if (browser != null) {
            return browser;
        }

        browser = System.getenv("BROWSER");
        if (browser != null) {
            return browser;
        }

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            browser = properties.getProperty("browser", "chrome"); // По умолчанию chrome
        } catch (IOException e) {
            e.printStackTrace();
        }

        return browser != null ? browser : "chrome";  // Chrome по умолчанию
    }

    public static WebDriver createYandexDriver() {
        Properties properties = loadProperties();

        String yandexDriverPath = properties.getProperty("yandex.driver.path");
        String yandexBrowserPath = properties.getProperty("yandex.browser.path");

        System.setProperty("webdriver.chrome.driver", yandexDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.setBinary(yandexBrowserPath);

        return new ChromeDriver(options);
    }

    public static WebDriver createChromeDriver() {
        Properties properties = loadProperties();

        String chromeDriverPath = properties.getProperty("chrome.driver.path");

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();

        return new ChromeDriver(options);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
