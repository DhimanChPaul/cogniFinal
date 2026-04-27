package tests;

import config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GiftCardPage;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.ExcelReporter;
import java.time.Duration;
import java.util.List;

public class PepperfryTest {

    WebDriver         driver;
    HomePage          homePage;
    SearchResultsPage resultsPage;
    GiftCardPage      giftPage;
    ExcelReporter     reporter;
    int               serial = 1;

    @BeforeClass
    public void setUp() {
        reporter = new ExcelReporter("Resources/PepperfryTestResults.xlsx");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        homePage    = new HomePage(driver);
        resultsPage = new SearchResultsPage(driver);
        giftPage    = new GiftCardPage(driver);
    }

    @AfterClass
    public void tearDown() {
        if (reporter != null) {
            reporter.save();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void tc001_OpenWebsite() {
        try {
            homePage.open(TestConfig.getWebsiteUrl());
            homePage.closePopupIfPresent();
            String  title = driver.getTitle();
            boolean pass  = title != null && title.toLowerCase().contains("pepperfry");
            log("TC_001", "Open Website",
                    "Open pepperfry.com and close popup",
                    "Title contains 'Pepperfry'",
                    "Title: " + title,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_001", "Open Website",
                    "Open pepperfry.com and close popup",
                    "Title contains 'Pepperfry'",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 2)
    public void tc002_SearchBookshelves() {
        try {
            homePage.searchFor(TestConfig.getSearchKeyword());
            String  url  = driver.getCurrentUrl();
            boolean pass = url.toLowerCase().contains("search") ||
                    url.toLowerCase().contains("bookshelves");
            log("TC_002", "Search Bookshelves",
                    "Type 'Bookshelves' in search box and press ENTER",
                    "URL contains 'search' or 'bookshelves'",
                    "URL: " + url,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_002", "Search Bookshelves",
                    "Type 'Bookshelves' in search box and press ENTER",
                    "URL contains 'search' or 'bookshelves'",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 3)
    public void tc003_ApplyPriceAndBrandFilter() {
        try {
            resultsPage.openMoreFilters();
            resultsPage.expandFilter(TestConfig.getPriceFilter());
            resultsPage.setMaxPrice(TestConfig.getMaxPrice());
            resultsPage.expandFilter(TestConfig.getBrandFilter());
            resultsPage.selectBrand(TestConfig.getBrandName());
            resultsPage.clickApply(TestConfig.getApplyButton());
            String  url      = driver.getCurrentUrl();
            boolean hasPrice = url.contains(String.valueOf(TestConfig.getMaxPrice()));
            boolean hasBrand = url.toLowerCase().contains(TestConfig.getBrandName().toLowerCase());
            boolean pass     = hasPrice && hasBrand;
            log("TC_003", "Apply Price + Brand Filter",
                    "Set max price Rs." + TestConfig.getMaxPrice() +
                            " → Select brand '" + TestConfig.getBrandName() + "' → Click APPLY",
                    "URL contains Rs." + TestConfig.getMaxPrice() +
                            " and '" + TestConfig.getBrandName() + "'",
                    "URL: " + url,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_003", "Apply Price + Brand Filter",
                    "Set max price Rs." + TestConfig.getMaxPrice() +
                            " → Select brand '" + TestConfig.getBrandName() + "' → Click APPLY",
                    "URL contains Rs." + TestConfig.getMaxPrice() +
                            " and '" + TestConfig.getBrandName() + "'",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 4)
    public void tc004_VerifyAllProductPrices() {
        try {
            List<String[]> products = resultsPage.getAllProductNamesAndPrices(TestConfig.getTopN());

            if (products.isEmpty()) {
                log("TC_004", "Verify Product Price < Rs.15000",
                        "Check each product price is below Rs.15000",
                        "At least 1 product below Rs." + TestConfig.getMaxPrice(),
                        "No products found on page", "FAIL");
                return;
            }

            for (int i = 0; i < products.size(); i++) {
                String  tcId     = String.format("TC_%03d", serial);  // TC_004, TC_005, TC_006
                String  name     = products.get(i)[0];
                String  priceStr = products.get(i)[1].replaceAll("[^0-9]", "");
                int     price    = Integer.parseInt(priceStr);
                boolean pass     = price < TestConfig.getMaxPrice();

                log(tcId, "Verify Product Price < Rs.15000",
                        "Check price of: " + name,
                        "Price < Rs." + TestConfig.getMaxPrice(),
                        "Actual Price: Rs." + price,
                        pass ? "PASS" : "FAIL");
            }

        } catch (Exception e) {
            log("TC_004", "Verify Product Price < Rs.15000",
                    "Check each product price is below Rs.15000",
                    "All prices < Rs." + TestConfig.getMaxPrice(),
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 5)
    public void tc005_FillGiftCardForm() {
        try {
            homePage.goToGiftCards();
            giftPage.selectBirthdayCard();
            giftPage.fillGiftCardForm(
                    TestConfig.getGcRecipientName(), TestConfig.getGcSenderName(),
                    TestConfig.getGcRecipientMobile(), TestConfig.getGcSenderMobile(),
                    TestConfig.getGcSenderEmail(), TestConfig.getGcMessage());
            giftPage.selectAmount1000();
            giftPage.clickProceedToCheckout();
            log("TC_005", "Fill Gift Card Form",
                    "Go to Gift Cards → Birthday card → fill form → Rs.1000 → Proceed",
                    "Form filled and Proceed to Checkout clicked",
                    "Recipient: " + TestConfig.getGcRecipientName() +
                            " | Sender: " + TestConfig.getGcSenderName() +
                            " | Rs.1000 selected | Proceed clicked", "PASS");
        } catch (Exception e) {
            log("TC_005", "Fill Gift Card Form",
                    "Go to Gift Cards → Birthday card → fill form → Rs.1000 → Proceed",
                    "Form filled and Proceed to Checkout clicked",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 6)
    public void tc006_CaptureValidationError() {
        try {
            String  error    = giftPage.getFormErrorMessage();
            String  expected = "Receiver's Email ID Cannot Be Empty";
            boolean pass     = expected.equalsIgnoreCase(error.trim());
            log("TC_006", "Capture Validation Error",
                    "Read error shown when receiver email is missing",
                    expected,
                    error.isEmpty() ? "No error shown" : "Error: " + error,
                    pass ? "PASS" : "FAIL");
        } catch (Exception e) {
            log("TC_006", "Capture Validation Error",
                    "Read error shown when receiver email is missing",
                    "Receiver's Email ID Cannot Be Empty",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    @Test(priority = 7)
    public void tc007_VerifyPriceBelow500() {
        try {
            List<String[]> products = resultsPage.getAllProductNamesAndPrices(1);

            if (products.isEmpty()) {
                log("TC_007", "Verify Product Price < Rs.500",
                        "Check if first product price is below Rs.500",
                        "Price < Rs.500",
                        "No products found", "FAIL");
                return;
            }

            String  name     = products.get(0)[0];
            String  priceStr = products.get(0)[1].replaceAll("[^0-9]", "");
            int     price    = Integer.parseInt(priceStr);
            boolean pass     = price < 500;

            log("TC_007", "Verify Product Price < Rs.500",
                    "Check if '" + name + "' price is below Rs.500",
                    "Price < Rs.500",
                    "Actual Price: Rs." + price,
                    pass ? "PASS" : "FAIL");

        } catch (Exception e) {
            log("TC_007", "Verify Product Price < Rs.500",
                    "Check if first product price is below Rs.500",
                    "Price < Rs.500",
                    "Exception: " + e.getMessage(), "FAIL");
        }
    }

    private void log(String id, String name, String desc,
                    String expected, String actual, String status) {
        reporter.logResult(serial++, id, name, desc, expected, actual, status);
    }
}