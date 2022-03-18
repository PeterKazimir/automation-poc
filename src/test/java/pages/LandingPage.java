package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import static driver.MyDriver.doThis;
public class LandingPage {

    private static final String BASE_URL = "https://kayak.ch";

    private static final By REJECT_COOKIES = By.cssSelector("button[aria-label = 'Nein, danke']");
    private static final By FLIGHT_TYPE_DROPDOWN = By.cssSelector(".zEiP-formContainer .zcIg > div:first-child");
    private static final By ROUNDTRIP_DROPDOWN_BUTTON = By.cssSelector(".zEiP-formContainer .zcIg div[role = 'button']:contains('Hin&zurück')");
    private static final By ROUNDTRIP_OPTION = By.cssSelector("li[aria-label='Hin&zurück']");

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

    public static void selectOfferType(String type) {
        switch (type) {
            case "roundtrip":
                selectRoundtripType();
                break;
            default:
                throw new IllegalArgumentException("Unexpected type of offer: " + type);
        }
    }

    private static void selectRoundtripType() {

        try {
            doThis().findElement(ROUNDTRIP_DROPDOWN_BUTTON);
        } catch (NoSuchElementException e) {
            doThis().findElement(FLIGHT_TYPE_DROPDOWN).click();
            doThis().findElement(ROUNDTRIP_OPTION).click();
        }
    }
}