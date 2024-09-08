import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.praktikum.pageObjects.MainPage;
import org.praktikum.utils.WebDriverFactory;

public class MainPageConstructorTest {

    private WebDriver driver;
    private MainPage mainPage;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createYandexDriver();
        mainPage = new MainPage(driver);
        driver.get("https://stellarburgers.nomoreparties.site/");
    }

    @Test
    @DisplayName("Тестирование кнопок конструктора")
    @Description("Последовательно кликаем на кнопки: Начинки, Соусы, Булки,. Проверяем видимость элементов.")
    public void testConstructorButtons() {
        waitForSeconds(2);

        clickFillingsButton();
        verifyFillingsVisible();
        waitForSeconds(2);

        clickSaucesButton();
        verifySaucesVisible();
        waitForSeconds(2);

        clickBunsButton();
        verifyBunsVisible();
        waitForSeconds(2);

        clickFillingsButton();
        verifyFillingsVisible();
        waitForSeconds(2);

        clickSaucesButton();
        verifySaucesVisible();
    }

    @Step("Ожидание кликабельности элемента {0}")
    private void waitForElementToBeClickable(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Проверка видимости элемента {0}")
    private void verifyElementIsVisible(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    @Step("Клик по кнопке Начинки")
    private void clickFillingsButton() {
        waitForElementToBeClickable(mainPage.getFillingsButton());
        mainPage.clickElement(mainPage.getFillingsButton());
    }

    @Step("Клик по кнопке Соусы")
    private void clickSaucesButton() {
        waitForElementToBeClickable(mainPage.getSaucesButton());
        mainPage.clickElement(mainPage.getSaucesButton());
    }

    @Step("Клик по кнопке Булки")
    private void clickBunsButton() {
        waitForElementToBeClickable(mainPage.getBunsButton());
        mainPage.clickElement(mainPage.getBunsButton());
    }

    @Step("Проверка видимости элемента Булки")
    private void verifyBunsVisible() {
        verifyElementIsVisible(mainPage.getBulkaElement());
    }

    @Step("Проверка видимости элемента Соусы")
    private void verifySaucesVisible() {
        verifyElementIsVisible(mainPage.getSauceElement());
    }

    @Step("Проверка видимости элемента Начинки")
    private void verifyFillingsVisible() {
        verifyElementIsVisible(mainPage.getFillingsElement());
    }

    private void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
