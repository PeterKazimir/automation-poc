package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import static driver.MyDriver.doThis;

public class SearchForm {

    private static final By FLIGHT_TYPE_DROPDOWN = By.cssSelector(".zEiP-formContainer .zcIg > div:first-child");
    private static final By ROUNDTRIP_DROPDOWN_BUTTON = By.cssSelector(".zEiP-formContainer .zcIg div[role = 'button']:contains('Hin&zurück')");
    private static final By ROUNDTRIP_OPTION = By.cssSelector("li[aria-label='Hin&zurück']");

    private static final By DEPARTURE_AIRPORT_INPUT = By.cssSelector("[aria-label = 'Eingabe Abflughafen']");
    private static final By DEPARTURE_AIRPORT_REMOVE_BUTTON = By.cssSelector("[aria-label = 'Eingabe Abflughafen'] div[role = 'list'] div[role = 'button']");
    private static final By DEPARTURE_AIRPORT_INPUT_WHEN_ACTIVE = By.cssSelector("input[placeholder = 'Von?']");

    private static final By DESTINATION_AIRPORT_INPUT = By.cssSelector("[aria-label = 'Eingabe Flugziel']");
    private static final By DESTINATION_AIRPORT_INPUT_WHEN_ACTIVE = By.cssSelector("input[placeholder = 'Nach?']");

    private static final String DROPDOWN_ITEM_FORMAT_XPATH = "//div[contains(@class,'airportCode') and contains(text(),'%s')]";

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

    public static void selectDepartureAirport(String airportCode) {
        cleanDefaultDepartureAirport();
        doThis().findElement(DEPARTURE_AIRPORT_INPUT_WHEN_ACTIVE).sendKeys(airportCode);
        doThis().findElement(By.xpath(String.format(DROPDOWN_ITEM_FORMAT_XPATH, airportCode))).click();
    }

    private static void cleanDefaultDepartureAirport() {
        try {
            doThis().findElements(DEPARTURE_AIRPORT_REMOVE_BUTTON).stream().iterator().next().click();
        } catch (NoSuchElementException e) {
            // This is okay
        }
    }

    public static void selectDestinationAirport(String airportCode) {
        doThis().findElement(DESTINATION_AIRPORT_INPUT_WHEN_ACTIVE).sendKeys(airportCode);
        doThis().findElement(By.xpath(String.format(DROPDOWN_ITEM_FORMAT_XPATH, airportCode))).click();
    }
}
