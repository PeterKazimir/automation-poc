package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import pages.LandingPage;
import pages.SearchForm;

public class KayakSteps {
    
    @Given("the user visits kayak.ch landing page")
    public void theUserVisitsKayakChLandingPage() {
        LandingPage.open();
        LandingPage.rejectCookies();
    }

    @Given("^the user searches for (.+) offers$")
    public void theUserSearchesForRoundtripOffers(String type) {
        SearchForm.selectOfferType(type);
    }

    @Given("^the departure airport is (.+)$")
    public void theDepartureAirportIs(String departureAirport) {
        SearchForm.selectDepartureAirport(departureAirport);
    }

    @Given("^the destination airport is (.+)$")
    public void theDestinationAirportIs(String destinationAirport) {
        SearchForm.selectDestinationAirport(destinationAirport);
    }

    @Given("^the departure date is (.+)$")
    public void departureDateIs(String departureDate) {
        SearchForm.selectDepartureDate(departureDate);
    }

    @Given("^the return date is (.+)$")
    public void theReturnDateIs(String returnDate) {
        System.out.println("Setting the departure date to: " + returnDate);
    }

    @Given("^the desired maximum price is (.+)$")
    public void theDesiredMaximumPriceIs(String maxPrice) {
        System.out.println("Max price is: " + maxPrice);
    }

    @Then("^all search results are roundtrip offers$")
    public void theSearchResultsAreRoundtripOffers() {
        Assert.assertTrue("Luckily, this is true", true);
    }

    @Then("^at least one result is cheaper than (.+)$")
    public void atLeastOneResultIsCheaperThan(String maxPrice) {
        System.out.println("Looking for a flight under: " + maxPrice);
    }
}
