package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public final class MyDriver {

    private static WebDriver INSTANCE;

    public static WebDriver doThis() {
        if (INSTANCE == null) {
            INSTANCE = new ChromeDriver();
            INSTANCE.manage().deleteAllCookies();
            INSTANCE.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
            INSTANCE.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            INSTANCE.manage().window().maximize();
        }
        return INSTANCE;
    }
}