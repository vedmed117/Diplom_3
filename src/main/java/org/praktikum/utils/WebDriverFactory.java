package org.praktikum.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory {

    public static WebDriver createYandexDriver() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\programming\\edgedriver_win64\\WebDriver\\bin\\yandexdriver.exe");


        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Users\\user\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe");

        return new ChromeDriver(options);
    }

    public static WebDriver createChromeDriver() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\programming\\edgedriver_win64\\WebDriver\\bin\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();

        return new ChromeDriver(options);
    }
}
