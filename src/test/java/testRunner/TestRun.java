package testRunner;

import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
    @CucumberOptions(features= {".//Features/login.feature"},  glue="stepDefinition")
    public class TestRun {

    }

