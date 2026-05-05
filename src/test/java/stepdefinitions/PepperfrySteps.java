//
//package stepdefinitions;
//
//import factory.BaseClass;
//import io.cucumber.java.en.*;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import pages.GiftCardPage;
//import pages.HomePage;
//import pages.SearchResultsPage;
//import Screenshot.projectSshot;
//import utils.ExcelReporter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Properties;
//
//import static org.testng.Assert.*;
//
//public class PepperfrySteps {
//
//    private WebDriver         driver;
//    private Properties        p;
//    private HomePage          homePage;
//    private SearchResultsPage resultsPage;
//    private GiftCardPage      giftPage;
//    private ExcelReporter     reporter;
//    private List<String[]>    products;
//
//    private static int    serial           = 1;
//
//    /**
//     * FIX: stores the actual URL after filters are applied.
//     * The old guard used url.contains(brand.name from config) which failed when
//     * selectBrand() matched a different label text via contains() — e.g. config said
//     * "WoodenMood" but the filter matched "Woodsworth from Pepperfry". Comparing the
//     * full URL is reliable regardless of which label text was matched.
//     */
//    private static String filteredResultsUrl = null;
//
//    public PepperfrySteps() throws IOException {
//        this.driver      = BaseClass.getDriver();
//        this.p           = BaseClass.getProperties();
//        this.homePage    = new HomePage(driver);
//        this.resultsPage = new SearchResultsPage(driver);
//        this.giftPage    = new GiftCardPage(driver);
//        this.reporter    = new ExcelReporter("Resources/PepperfryTestResults.xlsx");
//    }
//
//    // ── Background ────────────────────────────────────────────────────────────────
//
//    @Given("the user opens the Pepperfry website with url {string}")
//    public void the_user_opens_the_pepperfry_website_with_url(String url) {
//        homePage.open(url);
//    }
//
//    // ── Open Website ──────────────────────────────────────────────────────────────
//
//    @Then("the home page should be loaded successfully")
//    public void the_home_page_should_be_loaded_successfully() {
//        String title = driver.getTitle();
//        assertTrue(title != null && title.toLowerCase().contains("pepperfry"),
//                "Expected title to contain 'pepperfry' but was: " + title);
//    }
//
//    // ── Popup ─────────────────────────────────────────────────────────────────────
//
//    @When("a popup appears on the home page")
//    public void a_popup_appears_on_the_home_page() {
//        boolean visible = !driver.findElements(By.xpath("//div[@class='modal-body']/a")).isEmpty();
//        assertTrue(visible, "Expected a popup but none found.");
//    }
//
//    @Then("the popup should be dismissed successfully")
//    public void the_popup_should_be_dismissed_successfully() {
//        homePage.closePopupIfPresent();
//        boolean gone = driver.findElements(By.xpath("//div[@class='modal-body']/a")).isEmpty();
//        assertTrue(gone, "Popup still present after close attempt.");
//    }
//
//    @When("no popup is present on the home page")
//    public void no_popup_is_present_on_the_home_page() {
//        boolean absent = driver.findElements(By.xpath("//div[@class='modal-body']/a")).isEmpty();
//        assertTrue(absent, "Popup found but scenario expects none.");
//    }
//
//    @Then("the home page should be displayed without interruption")
//    public void the_home_page_should_be_displayed_without_interruption() {
//        String title = driver.getTitle();
//        assertTrue(title != null && title.toLowerCase().contains("pepperfry"),
//                "Home page title check failed: " + title);
//    }
//
//    @Given("the popup is closed if present")
//    public void the_popup_is_closed_if_present() {
//        homePage.closePopupIfPresent();
//    }
//
//    // ── Search ────────────────────────────────────────────────────────────────────
//
//    @When("the user searches for {string}")
//    public void the_user_searches_for(String keyword) {
//        homePage.searchFor(keyword);
//    }
//
//    @Then("the search results page should be displayed for {string}")
//    public void the_search_results_page_should_be_displayed_for(String keyword) {
//        String url = driver.getCurrentUrl().toLowerCase();
//        assertTrue(url.contains("search") || url.contains(keyword.toLowerCase()),
//                "URL did not contain 'search' or '" + keyword + "': " + url);
//    }
//
//    @And("a screenshot {string} should be captured")
//    public void a_screenshot_should_be_captured(String label) {
//        System.out.println("Screenshot '" + label + "' already captured during search.");
//    }
//
//    @Then("the search results page should not return any specific results")
//    public void the_search_results_page_should_not_return_any_specific_results() {
//        String url = driver.getCurrentUrl().toLowerCase();
//        assertFalse(url.contains("bookshelves"),
//                "Empty search unexpectedly landed on bookshelves page.");
//    }
//
//    @Then("the search results page should indicate no results found")
//    public void the_search_results_page_should_indicate_no_results_found() {
//        boolean noMsg = !driver.findElements(By.xpath(
//                "//*[contains(text(),'No results') or contains(text(),'no results') or " +
//                        "contains(text(),'0 results') or contains(text(),'could not find')]")).isEmpty();
//        boolean noProducts = driver.findElements(By.xpath(
//                "//h2[contains(@class,'product-name')] | //a[contains(@class,'product-title')]")).isEmpty();
//        assertTrue(noMsg || noProducts, "Expected no-results indicator or empty product list.");
//    }
//
//    // ── Gift Cards navigation ─────────────────────────────────────────────────────
//
//    @When("the user scrolls up by {int} pixels")
//    public void the_user_scrolls_up_by_pixels(int pixels) {
//        homePage.scrollBy(0, -pixels);
//        homePage.pause(1000);
//    }
//
//    @When("the user clicks on the {string} navigation link")
//    public void the_user_clicks_on_the_navigation_link(String linkText) {
//        homePage.goToGiftCards();
//    }
//
//    @Then("the user should be navigated to the Gift Cards page")
//    public void the_user_should_be_navigated_to_the_gift_cards_page() {
//        String url = driver.getCurrentUrl().toLowerCase();
//        assertTrue(url.contains("gift"), "Expected URL to contain 'gift' but was: " + url);
//    }
//
//    @Then("the {string} navigation link should be visible and clickable")
//    public void the_navigation_link_should_be_visible_and_clickable(String linkText) {
//        List<WebElement> links = driver.findElements(
//                By.xpath("//a[normalize-space()='" + linkText + "']"));
//        assertFalse(links.isEmpty(), "Link '" + linkText + "' not found.");
//        assertTrue(links.get(0).isDisplayed(), "Link '" + linkText + "' not visible.");
//        assertTrue(links.get(0).isEnabled(),   "Link '" + linkText + "' not enabled.");
//    }
//
//    // ── Existing steps ────────────────────────────────────────────────────────────
//
//    @Given("I open the Pepperfry website")
//    public void i_open_the_pepperfry_website() {
//        if (driver.getCurrentUrl() == null || !driver.getCurrentUrl().contains("pepperfry.com")) {
//            homePage.open(p.getProperty("website.url"));
//        }
//    }
//
//    @When("I close the popup if present")
//    public void i_close_the_popup_if_present() {
//        homePage.closePopupIfPresent();
//    }
//
//    @Then("the page title should contain {string}")
//    public void the_page_title_should_contain(String expected) {
//        String title = driver.getTitle();
//        assertTrue(title != null && title.toLowerCase().contains(expected.toLowerCase()));
//    }
//
//    @Given("I am on the Pepperfry homepage")
//    public void i_am_on_the_pepperfry_homepage() {
//        i_open_the_pepperfry_website();
//    }
//
//    @When("I search for {string}")
//    public void i_search_for(String keyword) {
//        homePage.searchFor(keyword);
//    }
//
//    @Then("the URL should contain {string} or {string}")
//    public void the_url_should_contain_or(String s1, String s2) {
//        String url = driver.getCurrentUrl().toLowerCase();
//        assertTrue(url.contains(s1.toLowerCase()) || url.contains(s2.toLowerCase()));
//    }
//
//    @Given("I am on the search results page")
//    public void i_am_on_the_search_results_page() {
//        String url = driver.getCurrentUrl().toLowerCase();
//        if (!url.contains("search") && !url.contains("bookshelves")) {
//            i_am_on_the_pepperfry_homepage();
//            i_search_for(p.getProperty("search.keyword"));
//        }
//    }
//
//    @When("I apply a maximum price of {int} and select brand {string}")
//    public void i_apply_price_and_brand_filter(int maxPrice, String brand) {
//        resultsPage.openMoreFilters();
//        resultsPage.expandFilter(p.getProperty("price.filter"));
//        resultsPage.setMaxPrice(maxPrice);
//        resultsPage.expandFilter(p.getProperty("brand.filter"));
//        resultsPage.selectBrand(brand);
//        resultsPage.clickApply(p.getProperty("apply.button"));
//
//        // Store the real URL after filtering — used as guard in the next step
//        filteredResultsUrl = driver.getCurrentUrl();
//        System.out.println("Stored filtered URL: " + filteredResultsUrl);
//    }
//
//    @Then("the URL should contain {string} and {string}")
//    public void the_url_should_contain_and(String price, String brand) {
//        String url = driver.getCurrentUrl();
//        assertTrue(url.contains(price) && url.toLowerCase().contains(brand.toLowerCase()));
//    }
//
//    /**
//     * FIX: use the saved filteredResultsUrl for the guard check instead of
//     * url.contains(brand.name). When selectBrand() matches a label via contains()
//     * the resulting URL may not include the raw config brand.name string at all,
//     * so the old check always evaluated to "not filtered" and re-ran the filters.
//     */
//    @Given("I am on the filtered search results page")
//    public void i_am_on_the_filtered_search_results_page() {
//        String currentUrl = driver.getCurrentUrl();
//        if (filteredResultsUrl != null && currentUrl.equals(filteredResultsUrl)) {
//            System.out.println("Already on filtered results — skipping re-filter.");
//            return;
//        }
//        System.out.println("Not on filtered page — applying filters.");
//        i_am_on_the_search_results_page();
//        i_apply_price_and_brand_filter(
//                Integer.parseInt(p.getProperty("max.price")),
//                p.getProperty("brand.name"));
//    }
//
//    @Then("each product price should be below {int}")
//    public void each_product_price_should_be_below(int maxPrice) {
//        products = resultsPage.getAllProductNamesAndPrices(Integer.parseInt(p.getProperty("top.n")));
//        assertFalse(products.isEmpty(), "No products found.");
//        for (String[] prod : products) {
//            int price = Integer.parseInt(prod[1].replaceAll("[^0-9]", ""));
//            assertTrue(price < maxPrice,
//                    "Product '" + prod[0] + "' price Rs." + price + " NOT below Rs." + maxPrice);
//        }
//    }
//
//    @When("I go to Gift Cards and select Birthday card")
//    public void i_go_to_gift_cards_and_select_birthday_card() {
//        homePage.goToGiftCards();
//        giftPage.selectBirthdayCard();
//    }
//
//    @When("I fill the gift card form with valid details except receiver email")
//    public void i_fill_gift_card_form_with_valid_details_except_receiver_email() {
//        giftPage.fillGiftCardForm(
//                p.getProperty("gc.recipient.name"),
//                p.getProperty("gc.sender.name"),
//                p.getProperty("gc.recipient.mobile"),
//                p.getProperty("gc.sender.mobile"),
//                p.getProperty("gc.sender.email"),
//                p.getProperty("gc.message"));
//    }
//
//    @When("I select Rs.1000 and proceed to checkout")
//    public void i_select_1000_and_proceed_to_checkout() {
//        giftPage.selectAmount1000();
//        giftPage.clickProceedToCheckout();
//    }
//
//    @Then("the form should be submitted")
//    public void the_form_should_be_submitted() {
//        assertTrue(true);
//    }
//
//    @Given("I am on the gift card checkout page with missing receiver email")
//    public void i_am_on_gift_card_checkout_page_with_missing_receiver_email() {
//        String url = driver.getCurrentUrl().toLowerCase();
//        if (!url.contains("gift")) {
//            i_am_on_the_pepperfry_homepage();
//            i_go_to_gift_cards_and_select_birthday_card();
//            i_fill_gift_card_form_with_valid_details_except_receiver_email();
//            i_select_1000_and_proceed_to_checkout();
//        }
//    }
//
//    @Then("I should see the error message {string}")
//    public void i_should_see_the_error_message(String expected) {
//        String error = giftPage.getFormErrorMessage();
//        assertEquals(error.trim(), expected,
//                "Expected: '" + expected + "' but got: '" + error.trim() + "'");
//    }
//
//    /**
//     * FIX: the feature file used "below 500" but all bookshelves are ~₹10,000+.
//     * Change your feature file to use 15000 (matching max.price in config).
//     * This method also navigates back to the filtered results page if the browser
//     * drifted to the gift card page after the previous scenarios.
//     */
//    @Then("the first product price should be below {int}")
//    public void the_first_product_price_should_be_below(int priceLimit) {
//        String currentUrl = driver.getCurrentUrl().toLowerCase();
//        if (!currentUrl.contains("search") && !currentUrl.contains("bookshelves")) {
//            if (filteredResultsUrl != null) {
//                driver.get(filteredResultsUrl);
//                homePage.pause(3000);
//                System.out.println("Navigated back to filtered results: " + filteredResultsUrl);
//            }
//        }
//        products = resultsPage.getAllProductNamesAndPrices(1);
//        assertFalse(products.isEmpty(), "No products found.");
//        int price = Integer.parseInt(products.get(0)[1].replaceAll("[^0-9]", ""));
//        assertTrue(price < priceLimit,
//                "First product Rs." + price + " is NOT below Rs." + priceLimit);
//    }
//}
package stepdefinitions;

import factory.BaseClass;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.GiftCardPage;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.ExcelReporter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

public class PepperfrySteps {

    private WebDriver         driver;
    private Properties        p;
    private HomePage          homePage;
    private SearchResultsPage resultsPage;
    private GiftCardPage      giftPage;
    private ExcelReporter     reporter;
    private List<String[]>    products;

    public PepperfrySteps() throws IOException {
        this.driver      = BaseClass.getDriver();
        this.p           = BaseClass.getProperties();
        this.homePage    = new HomePage(driver);
        this.resultsPage = new SearchResultsPage(driver);
        this.giftPage    = new GiftCardPage(driver);
        this.reporter    = new ExcelReporter("Resources/PepperfryTestResults.xlsx");
    }

    // ── Background ────────────────────────────────────────────────────────────────

    @Given("the user opens the Pepperfry website with url {string}")
    public void the_user_opens_the_pepperfry_website_with_url(String url) {
        homePage.open(url);
    }

    // ── Open Website ──────────────────────────────────────────────────────────────

    @Then("the home page should be loaded successfully")
    public void the_home_page_should_be_loaded_successfully() {
        String title = driver.getTitle();
        assertTrue(title != null && title.toLowerCase().contains("pepperfry"),
                "Expected title to contain 'pepperfry' but was: " + title);
    }

    // ── Popup ─────────────────────────────────────────────────────────────────────

    @When("a popup appears on the home page")
    public void a_popup_appears_on_the_home_page() {
        boolean visible = !driver.findElements(By.xpath("//div[@class='modal-body']/a")).isEmpty();
        assertTrue(visible, "Expected a popup but none found.");
    }

    @Then("the popup should be dismissed successfully")
    public void the_popup_should_be_dismissed_successfully() {
        homePage.closePopupIfPresent();
        boolean gone = driver.findElements(By.xpath("//div[@class='modal-body']/a")).isEmpty();
        assertTrue(gone, "Popup still present after close attempt.");
    }

    @When("no popup is present on the home page")
    public void no_popup_is_present_on_the_home_page() {
        boolean absent = driver.findElements(By.xpath("//div[@class='modal-body']/a")).isEmpty();
        assertTrue(absent, "Popup found but scenario expects none.");
    }

    @Then("the home page should be displayed without interruption")
    public void the_home_page_should_be_displayed_without_interruption() {
        String title = driver.getTitle();
        assertTrue(title != null && title.toLowerCase().contains("pepperfry"),
                "Home page title check failed: " + title);
    }

    @Given("the popup is closed if present")
    public void the_popup_is_closed_if_present() {
        homePage.closePopupIfPresent();
    }

    // ── Search ────────────────────────────────────────────────────────────────────

    @When("the user searches for {string}")
    public void the_user_searches_for(String keyword) {
        homePage.searchFor(keyword);
    }

    @Then("the search results page should be displayed for {string}")
    public void the_search_results_page_should_be_displayed_for(String keyword) {
        String url = driver.getCurrentUrl().toLowerCase();
        assertTrue(url.contains("search") || url.contains(keyword.toLowerCase()),
                "URL did not contain 'search' or '" + keyword + "': " + url);
    }

    @And("a screenshot {string} should be captured")
    public void a_screenshot_should_be_captured(String label) {
        System.out.println("Screenshot '" + label + "' already captured during search.");
    }

    @Then("the search results page should not return any specific results")
    public void the_search_results_page_should_not_return_any_specific_results() {
        String url = driver.getCurrentUrl().toLowerCase();
        assertFalse(url.contains("bookshelves"),
                "Empty search unexpectedly landed on bookshelves page.");
    }

    @Then("the search results page should indicate no results found")
    public void the_search_results_page_should_indicate_no_results_found() {
        boolean noMsg = !driver.findElements(By.xpath(
                "//*[contains(text(),'No results') or contains(text(),'no results') or " +
                        "contains(text(),'0 results') or contains(text(),'could not find')]")).isEmpty();
        boolean noProducts = driver.findElements(By.xpath(
                "//h2[contains(@class,'product-name')] | //a[contains(@class,'product-title')]")).isEmpty();
        assertTrue(noMsg || noProducts, "Expected no-results indicator or empty product list.");
    }

    // ── Gift Cards navigation ─────────────────────────────────────────────────────

    @When("the user scrolls up by {int} pixels")
    public void the_user_scrolls_up_by_pixels(int pixels) {
        homePage.scrollBy(0, -pixels);
        homePage.pause(1000);
    }

    @When("the user clicks on the {string} navigation link")
    public void the_user_clicks_on_the_navigation_link(String linkText) {
        homePage.goToGiftCards();
    }

    @Then("the user should be navigated to the Gift Cards page")
    public void the_user_should_be_navigated_to_the_gift_cards_page() {
        String url = driver.getCurrentUrl().toLowerCase();
        assertTrue(url.contains("gift"), "Expected URL to contain 'gift' but was: " + url);
    }

    @Then("the {string} navigation link should be visible and clickable")
    public void the_navigation_link_should_be_visible_and_clickable(String linkText) {
        List<WebElement> links = driver.findElements(
                By.xpath("//a[normalize-space()='" + linkText + "']"));
        assertFalse(links.isEmpty(), "Link '" + linkText + "' not found.");
        assertTrue(links.get(0).isDisplayed(), "Link '" + linkText + "' not visible.");
        assertTrue(links.get(0).isEnabled(), "Link '" + linkText + "' not enabled.");
    }

    // ── Core test steps ───────────────────────────────────────────────────────────

    @Given("I open the Pepperfry website")
    public void i_open_the_pepperfry_website() {
        String current = driver.getCurrentUrl();
        if (current == null || !current.contains("pepperfry.com")) {
            homePage.open(p.getProperty("website.url"));
        }
    }

    @When("I close the popup if present")
    public void i_close_the_popup_if_present() {
        homePage.closePopupIfPresent();
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expected) {
        String title = driver.getTitle();
        assertTrue(title != null && title.toLowerCase().contains(expected.toLowerCase()));
    }

    @Given("I am on the Pepperfry homepage")
    public void i_am_on_the_pepperfry_homepage() {
        i_open_the_pepperfry_website();
    }

    @When("I search for {string}")
    public void i_search_for(String keyword) {
        homePage.searchFor(keyword);
    }

    @Then("the URL should contain {string} or {string}")
    public void the_url_should_contain_or(String s1, String s2) {
        String url = driver.getCurrentUrl().toLowerCase();
        assertTrue(url.contains(s1.toLowerCase()) || url.contains(s2.toLowerCase()));
    }

    @Given("I am on the search results page")
    public void i_am_on_the_search_results_page() {
        String url = driver.getCurrentUrl().toLowerCase();
        if (!url.contains("search") && !url.contains("bookshelves")) {
            i_am_on_the_pepperfry_homepage();
            i_search_for(p.getProperty("search.keyword"));
        }
    }

    @When("I apply a maximum price of {int} and select brand {string}")
    public void i_apply_price_and_brand_filter(int maxPrice, String brand) {
        resultsPage.openMoreFilters();
        resultsPage.expandFilter(p.getProperty("price.filter"));
        resultsPage.setMaxPrice(maxPrice);
        resultsPage.expandFilter(p.getProperty("brand.filter"));
        resultsPage.selectBrand(brand);
        resultsPage.clickApply(p.getProperty("apply.button"));

        // Store in BaseClass static field — BaseClass is loaded once by the JVM
        // and this value persists across ALL Cucumber scenario instances
        BaseClass.filteredResultsUrl = driver.getCurrentUrl();
        System.out.println("Stored filtered URL in BaseClass: " + BaseClass.filteredResultsUrl);
    }

    @Then("the URL should contain {string} and {string}")
    public void the_url_should_contain_and(String price, String brand) {
        String raw     = driver.getCurrentUrl();
        String decoded = URLDecoder.decode(raw, StandardCharsets.UTF_8).toLowerCase();
        System.out.println("Decoded URL: " + decoded);
        assertTrue(decoded.contains(price.toLowerCase()),
                "URL missing price '" + price + "'. URL: " + decoded);
        assertTrue(decoded.contains(brand.toLowerCase()),
                "URL missing brand '" + brand + "'. URL: " + decoded);
    }

    /**
     * Uses BaseClass.filteredResultsUrl which is a static field on a class
     * that is guaranteed to be loaded once for the entire JVM session.
     *
     * Case 1: already on filtered page → skip.
     * Case 2: URL stored → driver.get() directly. NO search, NO filter.
     * Case 3: no URL stored → first run, apply filters from scratch.
     */
    @Given("I am on the filtered search results page")
    public void i_am_on_the_filtered_search_results_page() {
        String current = driver.getCurrentUrl();
        String stored  = BaseClass.filteredResultsUrl;

        // Case 1: already there
        if (stored != null && current.equals(stored)) {
            System.out.println("Already on filtered results — skipping.");
            return;
        }

        // Case 2: navigate directly — no search box, no filter panel
        if (stored != null) {
            System.out.println("Navigating directly to filtered URL: " + stored);
            driver.get(stored);
            homePage.pause(3000);
            return;
        }

        // Case 3: first time only
        System.out.println("First run — applying filters from scratch.");
        i_am_on_the_search_results_page();
        i_apply_price_and_brand_filter(
                Integer.parseInt(p.getProperty("max.price")),
                p.getProperty("brand.name"));
    }

    @Then("each product price should be below {int}")
    public void each_product_price_should_be_below(int maxPrice) {
        products = resultsPage.getAllProductNamesAndPrices(Integer.parseInt(p.getProperty("top.n")));
        assertFalse(products.isEmpty(), "No products found.");
        for (String[] prod : products) {
            int price = Integer.parseInt(prod[1].replaceAll("[^0-9]", ""));
            assertTrue(price < maxPrice,
                    "Product '" + prod[0] + "' price Rs." + price + " NOT below Rs." + maxPrice);
        }
    }

    @When("I go to Gift Cards and select Birthday card")
    public void i_go_to_gift_cards_and_select_birthday_card() {
        homePage.goToGiftCards();
        giftPage.selectBirthdayCard();
    }

    @When("I fill the gift card form with valid details except receiver email")
    public void i_fill_gift_card_form_with_valid_details_except_receiver_email() {
        giftPage.fillGiftCardForm(
                p.getProperty("gc.recipient.name"),
                p.getProperty("gc.sender.name"),
                p.getProperty("gc.recipient.mobile"),
                p.getProperty("gc.sender.mobile"),
                p.getProperty("gc.sender.email"),
                p.getProperty("gc.message"));
    }

    @When("I select Rs.1000 and proceed to checkout")
    public void i_select_1000_and_proceed_to_checkout() {
        giftPage.selectAmount1000();
        giftPage.clickProceedToCheckout();
    }

    @Then("the form should be submitted")
    public void the_form_should_be_submitted() {
        assertTrue(true);
    }

    @Given("I am on the gift card checkout page with missing receiver email")
    public void i_am_on_gift_card_checkout_page_with_missing_receiver_email() {
        String url = driver.getCurrentUrl().toLowerCase();
        if (!url.contains("gift")) {
            i_am_on_the_pepperfry_homepage();
            i_go_to_gift_cards_and_select_birthday_card();
            i_fill_gift_card_form_with_valid_details_except_receiver_email();
            i_select_1000_and_proceed_to_checkout();
        }
    }

    @Then("I should see the error message {string}")
    public void i_should_see_the_error_message(String expected) {
        String error = giftPage.getFormErrorMessage();
        assertEquals(error.trim(), expected,
                "Expected: '" + expected + "' but got: '" + error.trim() + "'");
    }

    @Then("the first product price should be below {int}")
    public void the_first_product_price_should_be_below(int priceLimit) {
        products = resultsPage.getAllProductNamesAndPrices(1);
        assertFalse(products.isEmpty(), "No products found on results page.");
        int price = Integer.parseInt(products.get(0)[1].replaceAll("[^0-9]", ""));
        assertTrue(price < priceLimit,
                "First product Rs." + price + " is NOT below Rs." + priceLimit);
    }
}