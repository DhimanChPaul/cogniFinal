
package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import listeners.ExtentReportListener;
import org.testng.annotations.Listeners;

/**
 * FIX: added @Listeners so ExtentReportListener fires its onStart / onFinish
 * lifecycle methods when the suite runs via Cucumber + TestNG.
 * Without this annotation the listener was never registered and no HTML report
 * was ever written, even though the listener class itself was correct.
 */
@Listeners(ExtentReportListener.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue     = {"stepdefinitions"},
        plugin   = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports/cucumber.json"   // optional: useful for CI
        },
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}