
package stepdefinitions;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class Hooks {

    @BeforeAll
    public static void globalSetup() throws IOException {
        if (BaseClass.getDriver() == null) {
            WebDriver driver = BaseClass.initilizeBrowser();
            driver.manage().window().maximize();

            java.time.Duration pageLoad = java.time.Duration.ofSeconds(60);
            java.time.Duration script   = java.time.Duration.ofSeconds(30);
            driver.manage().timeouts().pageLoadTimeout(pageLoad);
            driver.manage().timeouts().scriptTimeout(script);

            System.out.println("Browser initialized in @BeforeAll.");
        }
    }

    @AfterAll
    public static void globalTearDown() {
        BaseClass.quitDriver();
        // Save Excel report after all tests
        try {
            if (stepdefinitions.PepperfrySteps.reporter != null) {
                stepdefinitions.PepperfrySteps.reporter.save();
            }
        } catch (Exception e) {
            System.err.println("Failed to save Excel report: " + e.getMessage());
        }
    }


    @AfterStep
    public void addScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver d = BaseClass.getDriver();
            if (d != null) {
                byte[] screenshot = ((TakesScreenshot) d).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            }
        }
    }
}