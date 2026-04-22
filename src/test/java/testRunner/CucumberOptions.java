package testRunner;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

// intellij identified cucumber.class as -JUnit runner
@RunWith(Cucumber.class)
@CucumberOptions(features= {".//Features/login.feature"},  glue="stepDefinition")
public class TestRun {

}