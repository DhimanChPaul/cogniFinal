package tests;

import config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.GiftCardPage;
import pages.HomePage;
import pages.SearchResultsPage;

public class PepperfryTest {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // ── Page Object Instances ────────────────────────────────────────────────
        HomePage          homePage    = new HomePage(driver);
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        GiftCardPage      giftPage    = new GiftCardPage(driver);

        try {

            // ── STEP 1 : Open site ───────────────────────────────────────────────
            homePage.open(TestConfig.WEBSITE_URL);

            // ── STEP 2 : Close popup ─────────────────────────────────────────────
            homePage.closePopupIfPresent();

            // ── STEP 3 : Search ──────────────────────────────────────────────────
            homePage.searchFor(TestConfig.SEARCH_KEYWORD);

            // ── STEP 4 : Open filters ────────────────────────────────────────────
            resultsPage.openMoreFilters();

            // ── STEP 5 : Apply Price filter ──────────────────────────────────────
            resultsPage.expandFilter(TestConfig.PRICE_FILTER);
            resultsPage.setMaxPrice(TestConfig.MAX_PRICE);

            // ── STEP 6 : Apply Brand filter ──────────────────────────────────────
            resultsPage.expandFilter(TestConfig.BRAND_FILTER);
            resultsPage.selectBrand(TestConfig.BRAND_NAME);

            // ── STEP 7 : Click Apply ─────────────────────────────────────────────
            resultsPage.clickApply(TestConfig.APPLY_BUTTON);
            System.out.println("All filters applied.");

            // ── STEP 8 : Display top products ────────────────────────────────────
            resultsPage.displayTopProducts(TestConfig.TOP_N, TestConfig.MAX_PRICE);
            System.out.println("Current URL: " + driver.getCurrentUrl());

            // ── STEP 9 : Navigate to Gift Cards ──────────────────────────────────
            homePage.goToGiftCards();

            // ── STEP 10 : Select Birthday card ───────────────────────────────────
            giftPage.selectBirthdayCard();

            // ── STEP 11 : Fill the gift card form ────────────────────────────────
            giftPage.fillGiftCardForm(
                    TestConfig.GC_RECIPIENT_NAME,
                    TestConfig.GC_SENDER_NAME,
                    TestConfig.GC_RECIPIENT_MOBILE,
                    TestConfig.GC_SENDER_MOBILE,
                    TestConfig.GC_SENDER_EMAIL,
                    TestConfig.GC_MESSAGE
            );

            // ── STEP 12 : Select ₹1000 denomination ─────────────────────────────
            giftPage.selectAmount1000();

            // ── STEP 13 : Proceed to checkout ────────────────────────────────────
            giftPage.clickProceedToCheckout();

            // ── STEP 14 : Read validation error ──────────────────────────────────
            String error = giftPage.getFormErrorMessage();
            System.out.println("Validation message: " + error);

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}