package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Hooks {

    WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://www.google.com");
    }

    @After
    public void tearDown() {
        driver.close();
    }
}