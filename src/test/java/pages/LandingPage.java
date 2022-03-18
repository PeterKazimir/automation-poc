package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import static driver.MyDriver.doThis;
public class LandingPage {

    private static final String BASE_URL = "https://kayak.ch";

    private static final By REJECT_COOKIES = By.cssSelector("button[aria-label = 'Nein, danke']");

    public static void open() {
        doThis().get(BASE_URL);
    }

    public static void rejectCookies() {
        try {
            doThis().findElement(REJECT_COOKIES).click();
        } catch (NoSuchElementException e) {
            // This is fine
        }
    }
}