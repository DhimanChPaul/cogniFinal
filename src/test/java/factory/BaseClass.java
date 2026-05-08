//
//package factory;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//import java.time.Duration;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//public class BaseClass {
//
//    protected static WebDriver driver;
//    private static Properties  p;
//
//    public static WebDriver initilizeBrowser() {
//        if (driver == null) {
//            driver = new ChromeDriver();
//        }
//        return driver;
//    }
//
//    public static WebDriver getDriver() {
//        return driver;
//    }
//
//    public static void quitDriver() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//        }
//    }
//
//    /**
//     * FIX: replaced Windows-only "\\" path separator with File.separator so the
//     * config file is found correctly on macOS and Linux CI agents as well.
//     */
//    public static Properties getProperties() throws IOException {
//        if (p == null) {
//            String configPath = System.getProperty("user.dir")
//                    + "/src/test/resources/config.properties";
//            FileInputStream file = new FileInputStream(configPath);
//            p = new Properties();
//            p.load(file);
//            file.close();
//        }
//        return p;
//    }
//
//    // ── Instance members (used by page objects) ───────────────────────────────────
//
//    protected WebDriverWait      wait;
//    protected JavascriptExecutor js;
//    protected Actions            actions;
//
//    public BaseClass(WebDriver driver) {
//        BaseClass.driver = driver;
//        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(20));
//        this.js      = (JavascriptExecutor) driver;
//        this.actions = new Actions(driver);
//    }
//
//    public BaseClass() {}
//
//    // ── Wait helpers ──────────────────────────────────────────────────────────────
//
//    protected WebElement waitForVisibility(By locator) {
//        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//    }
//
//    protected WebElement waitForPresence(By locator) {
//        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//    }
//
//    protected WebElement waitForClickable(By locator) {
//        return wait.until(ExpectedConditions.elementToBeClickable(locator));
//    }
//
//    // ── JS helpers ────────────────────────────────────────────────────────────────
//
//    protected void jsClick(WebElement element) {
//        js.executeScript("arguments[0].click();", element);
//    }
//
//    protected void scrollIntoView(WebElement element) {
//        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
//    }
//
//    public void scrollBy(int x, int y) {
//        js.executeScript("window.scrollBy(" + x + "," + y + ");");
//    }
//
//    // ── Utility ───────────────────────────────────────────────────────────────────
//
//    public void pause(long millis) {
//        try {
//            Thread.sleep(millis);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//}
package factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseClass {

    protected static WebDriver driver;
    private static Properties  p;
    public static String filteredResultsUrl = null;

    public static WebDriver initilizeBrowser() {
        if (driver == null) {
            driver = new ChromeDriver();
        }
        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static Properties getProperties() throws IOException {
        if (p == null) {
            String configPath = System.getProperty("user.dir")
                    + "/src/test/resources/config.properties";
            FileInputStream file = new FileInputStream(configPath);
            p = new Properties();
            p.load(file);
            file.close();
        }
        return p;
    }

    protected WebDriverWait      wait;
    protected JavascriptExecutor js;
    protected Actions            actions;

    public BaseClass(WebDriver driver) {
        BaseClass.driver = driver;
        this.wait= new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js= (JavascriptExecutor) driver;
        this.actions= new Actions(driver);
    }

    public BaseClass() {}

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void jsClick(WebElement element) {

        js.executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    public void scrollBy(int x, int y) {
        js.executeScript("window.scrollBy(" + x + "," + y + ");");
    }

    public void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}