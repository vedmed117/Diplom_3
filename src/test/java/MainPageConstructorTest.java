import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageobjects.MainPage;
import org.praktikum.utils.BaseURL;
import org.praktikum.utils.ElementUtils;
import org.praktikum.utils.WebDriverFactory;

import static org.junit.Assert.assertTrue;

public class MainPageConstructorTest {

    private WebDriver driver;
    private MainPage mainPage;

    @Before
    @Step("Настройка WebDriver")
    public void setUp() {
        driver = WebDriverFactory.initializeWebDriver();
        mainPage = new MainPage(driver);
        driver.get(BaseURL.getBaseUrl());
    }

    @Test
    @DisplayName("Корректный переход к разделу Начинки")
    @Description("Тест ждет загрузки кнопки Начинки и проверяет возможность перехода к данной секции ")
    public void testCorrectMovingToFillingsSection() {
        // Клик по кнопке "Начинки"
        ElementUtils.clickElement(driver, mainPage.getFillingsButton(), "Начинки");
        // Ожидание, что элемент "Начинки" стал видимым
        ElementUtils.waitUntilElementIsVerifiedToBeVisible(driver, mainPage.getFillingsSection(), "Начинки");
    }
    @Test
    @DisplayName("Корректный переход к разделу Соусы")
    @Description("Тест ждет загрузки кнопки Соусы и проверяет возможность перехода к данной секции ")
    public void testCorrectMovingToSaucesSection() {
        // Клик по кнопке "Соусы"
        ElementUtils.clickElement(driver, mainPage.getSaucesButton(), "Соусы");
        // Ожидание, что элемент "Соусы" стал видимым
        ElementUtils.waitUntilElementIsVerifiedToBeVisible(driver, mainPage.getSauceSection(), "Соусы");
    }
    @Test
    @DisplayName("Корректный переход к разделу Булки")
    @Description("Тест ждет загрузки кнопки Булки и проверяет возможность перехода к данной секции ")
    public void testCorrectMovingToBunsSection() {
        // Клик по кнопке "Начинки"
        ElementUtils.clickElement(driver, mainPage.getFillingsButton(), "Начинки");
        // Ожидание, что элемент "Начинки" стал видимым
        ElementUtils.waitUntilElementIsVerifiedToBeVisible(driver, mainPage.getFillingsSection(), "Начинки");
        // Клик по кнопке "Булки"
        ElementUtils.clickElement(driver, mainPage.getBunsButton(), "Булки");
        // Ожидание, что элемент "Булки" стал видимым
        ElementUtils.waitUntilElementIsVerifiedToBeVisible(driver, mainPage.getBulkaSection(), "Булки");
    }

  @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
