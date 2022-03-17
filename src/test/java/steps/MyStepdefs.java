package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MyStepdefs {

    @Given("^the tests are executed$")
    public void theTestsAreExecuted() {
        System.out.println("This step run successfully");
    }

    @When("the browser opens at www.google.com")
    public void theBrowserOpensAtWwwGoogleCom() {
        System.out.println("This step also run successfully");
    }

    @Then("the Google search page is loaded")
    public void theGoogleSearchPageIsLoaded() {
        System.out.println("This step run successfully as well");
    }
}
