
package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import listeners.ExtentReportListener;
import org.testng.annotations.Listeners;


@Listeners(ExtentReportListener.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue     = {"stepdefinitions"},
        plugin   = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports/cucumber.json"
        },
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}