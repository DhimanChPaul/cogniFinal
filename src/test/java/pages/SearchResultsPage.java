//
//package pages;
//
//import Screenshot.projectSshot;
//import factory.BaseClass;
//import org.openqa.selenium.*;
//import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.PageFactory;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchResultsPage extends BaseClass {
//
//    // ── Locators ─────────────────────────────────────────────────────────────────
//    @FindBy(xpath = "//span[contains(@class,'more-filter')]")
//    private WebElement moreFiltersBtn;
//
//    @FindBy(xpath = "//input[@formcontrolname='inputMax']")
//    private WebElement maxPriceInput;
//
//    @FindBy(xpath = "//h2[contains(@class,'product-name')] | //a[contains(@class,'product-title')]")
//    private List<WebElement> productNames;
//
//    @FindBy(xpath = "//span[contains(@class,'offer-price')] | //span[contains(@class,'price-display')]")
//    private List<WebElement> productPrices;
//
//    // Screenshot #3 fires only once per run
//    private boolean topProductsCaptured = false;
//
//    // ── Constructor ──────────────────────────────────────────────────────────────
//    public SearchResultsPage(WebDriver driver) {
//        super(driver);
//        PageFactory.initElements(driver, this);
//    }
//
//    // ── Actions ──────────────────────────────────────────────────────────────────
//
//    public void openMoreFilters() {
//        wait.until(ExpectedConditions.elementToBeClickable(moreFiltersBtn));
//        jsClick(moreFiltersBtn);
//        pause(2000);
//        System.out.println("More Filters panel opened.");
//    }
//
//    /**
//     * FIX: close the filter drawer/overlay before leaving the page.
//     * Call this after clickApply() so the drawer-overlay is gone
//     * before any subsequent navigation (e.g. GIFT CARDS link).
//     */
//    public void closeFilterDrawerIfOpen() {
//        try {
//            // Wait briefly for the overlay to disappear on its own after APPLY
//            wait.until(ExpectedConditions.invisibilityOfElementLocated(
//                    By.xpath("//div[contains(@class,'drawer-overlay')]")));
//            System.out.println("Filter drawer closed / overlay gone.");
//        } catch (Exception e) {
//            // Overlay still present — try clicking outside / pressing Escape
//            try {
//                actions.sendKeys(Keys.ESCAPE).perform();
//                pause(1000);
//                System.out.println("Dismissed filter drawer via Escape.");
//            } catch (Exception ignored) {
//                System.out.println("Could not dismiss filter drawer — proceeding anyway.");
//            }
//        }
//    }
//
//    public void expandFilter(String filterName) {
//        By locator = By.xpath(
//                String.format("//accordion-heading[normalize-space()='%s']", filterName));
//        WebElement filter = waitForPresence(locator);
//        scrollIntoView(filter);
//        pause(1000);
//        jsClick(filter);
//        pause(1500);
//        System.out.println("Expanded filter: " + filterName);
//    }
//
//    public void setMaxPrice(int maxPrice) {
//        wait.until(ExpectedConditions.visibilityOf(maxPriceInput));
//        scrollIntoView(maxPriceInput);
//        pause(1000);
//        actions.moveToElement(maxPriceInput)
//                .click()
//                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
//                .sendKeys(Keys.DELETE)
//                .sendKeys(String.valueOf(maxPrice))
//                .sendKeys(Keys.TAB)
//                .perform();
//        pause(1500);
//        System.out.println("Max price set to: ₹" + maxPrice);
//    }
//
//    public void selectBrand(String brandName) {
//        By byExactText    = By.xpath(String.format("//label[normalize-space(text())='%s']", brandName));
//        By bySpanText     = By.xpath(String.format("//label[.//span[normalize-space(text())='%s']]", brandName));
//        By byContainsText = By.xpath(String.format("//label[contains(normalize-space(.), '%s')]", brandName));
//
//        WebElement brand = null;
//
//        // Debug — print all label texts so we can see what's actually in the DOM
//        List<WebElement> allLabels = driver.findElements(By.xpath("//label"));
//        System.out.println("=== ALL LABELS IN DOM ===");
//        for (WebElement l : allLabels) {
//            System.out.println("  TEXT='" + l.getText() + "'  FOR='" + l.getAttribute("for") + "'");
//        }
//        System.out.println("=========================");
//
//        for (By locator : List.of(byExactText, bySpanText, byContainsText)) {
//            try {
//                brand = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//                System.out.println("Found brand with locator: " + locator);
//                break;
//            } catch (TimeoutException ignored) {
//                System.out.println("Locator not matched: " + locator);
//            }
//        }
//
//        if (brand == null) {
//            throw new RuntimeException("Brand '" + brandName + "' not found in filter panel. " +
//                    "Check if brand name is correct or if panel needs scrolling.");
//        }
//
//        scrollIntoView(brand);
//        pause(1000);
//        jsClick(brand);
//        pause(2000);
//        System.out.println("Selected brand: " + brandName);
//    }
//
//    public void clickApply(String buttonText) {
//        By locator = By.xpath(String.format("//button[normalize-space()='%s']", buttonText));
//        WebElement btn = waitForClickable(locator);
//        scrollIntoView(btn);
//        pause(500);
//        jsClick(btn);
//        pause(3000);
//        System.out.println("Clicked: " + buttonText);
//
//        // Wait for the drawer overlay to disappear after applying filter
//        // This prevents ElementClickInterceptedException on subsequent navigation
//        closeFilterDrawerIfOpen();
//
//        // Screenshot #2 — AFTER applying filter
//        projectSshot.capture(driver, "2_AfterFilter");
//    }
//
//    public void displayTopProducts(int count, int maxPrice) {
//        pause(5000);
//        scrollBy(0, 300);
//        pause(2000);
//
//        System.out.println("\n===============================================");
//        System.out.println("Total products below ₹" + maxPrice + ": " + productNames.size());
//        System.out.println("Displaying Top " + count + " products:");
//        System.out.println("===============================================\n");
//
//        int displayed = Math.min(count, productNames.size());
//        for (int i = 0; i < displayed; i++) {
//            String name  = productNames.get(i).getText();
//            String price = (i < productPrices.size()) ? productPrices.get(i).getText() : "Price N/A";
//            System.out.println((i + 1) + ". " + name);
//            System.out.println("   Price: " + price);
//            System.out.println("-----------------------------------------------");
//        }
//    }
//
//    public List<String[]> getAllProductNamesAndPrices(int count) {
//        pause(2000);
//        scrollBy(0, 300);
//        pause(1000);
//
//        List<String[]> products = new ArrayList<>();
//        System.out.println("Total products found: " + productNames.size());
//
//        int limit = Math.min(count, productNames.size());
//        for (int i = 0; i < limit; i++) {
//            String name  = productNames.get(i).getText();
//            String price = (i < productPrices.size()) ? productPrices.get(i).getText() : "0";
//            if (!name.isEmpty() && !price.isEmpty()) {
//                products.add(new String[]{name, price});
//                System.out.println("Product: " + name + " | Price: " + price);
//            }
//        }
//
//        // Screenshot #3 — only on first call
//        if (!topProductsCaptured) {
//            projectSshot.capture(driver, "3_TopProductsBelow");
//            topProductsCaptured = true;
//        }
//
//        return products;
//    }
//}
package pages;

import Screenshot.projectSshot;
import factory.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends BaseClass {

    @FindBy(xpath = "//span[contains(@class,'more-filter')]")
    private WebElement moreFiltersBtn;

    @FindBy(xpath = "//input[@formcontrolname='inputMax']")
    private WebElement maxPriceInput;

    @FindBy(xpath = "//h2[contains(@class,'product-name')] | //a[contains(@class,'product-title')]")
    private List<WebElement> productNames;

    @FindBy(xpath = "//span[contains(@class,'offer-price')] | //span[contains(@class,'price-display')]")
    private List<WebElement> productPrices;

    private boolean topProductsCaptured = false;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void openMoreFilters() {
        wait.until(ExpectedConditions.elementToBeClickable(moreFiltersBtn));
        jsClick(moreFiltersBtn);
        pause(2000);
        System.out.println("More Filters panel opened.");
    }

    public void closeFilterDrawerIfOpen() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'drawer-overlay')]")));
            System.out.println("Filter drawer overlay gone.");
        } catch (Exception e) {
            try {
                actions.sendKeys(Keys.ESCAPE).perform();
                pause(1000);
                System.out.println("Dismissed filter drawer via Escape.");
            } catch (Exception ignored) {
                System.out.println("Could not dismiss filter drawer.");
            }
        }
    }

    public void expandFilter(String filterName) {
        By locator = By.xpath(
                String.format("//accordion-heading[normalize-space()='%s']", filterName));
        WebElement filter = waitForPresence(locator);
        scrollIntoView(filter);
        pause(1000);
        jsClick(filter);
        pause(1500);
        System.out.println("Expanded filter: " + filterName);
    }

    public void setMaxPrice(int maxPrice) {
        wait.until(ExpectedConditions.visibilityOf(maxPriceInput));
        scrollIntoView(maxPriceInput);
        pause(1000);
        actions.moveToElement(maxPriceInput)
                .click()
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .sendKeys(String.valueOf(maxPrice))
                .sendKeys(Keys.TAB)
                .perform();
        pause(1500);
        System.out.println("Max price set to: ₹" + maxPrice);
    }

    /**
     * Selects a brand by EXACT name only — no contains().
     *
     * FIX: The brand list in the filter panel is a scrollable container.
     * WoodenMood may not be in the initial viewport, so the old code
     * fell through to contains('Woodsworth') and matched the wrong brand.
     *
     * Strategy:
     * 1. Try label[@for='brandName'] — fastest, matches FOR attribute exactly.
     * 2. Try label[normalize-space(text())='brandName'] — exact visible text.
     * 3. If not found, scroll the brand list container down in small steps
     *    and retry both locators after each scroll — up to 5 scroll attempts.
     * 4. Never use contains() — prevents accidental partial matches.
     */
    public void selectBrand(String brandName) {
        pause(1000);

        // The scrollable brand list container inside the filter panel
        By brandListContainer = By.xpath(
                "//accordion-group[.//accordion-heading[normalize-space()='Brand']]" +
                        "//div[contains(@class,'filter-scroll') or contains(@class,'brand-list') " +
                        "or contains(@class,'scroll')]");

        By byFor  = By.xpath(String.format("//label[@for='%s']", brandName));
        By byText = By.xpath(String.format("//label[normalize-space(text())='%s']", brandName));

        WebElement brand = findBrandElement(byFor, byText);

        // If not found in initial view, scroll the brand list and retry
        if (brand == null) {
            System.out.println("Brand not in initial view — scrolling brand list to find: " + brandName);

            // Try scrolling the brand container using JS
            List<WebElement> containers = driver.findElements(brandListContainer);
            WebElement scrollContainer = containers.isEmpty() ? null : containers.get(0);

            for (int attempt = 1; attempt <= 8; attempt++) {
                // Scroll the container if found, otherwise scroll the whole panel
                if (scrollContainer != null) {
                    js.executeScript("arguments[0].scrollTop += 150;", scrollContainer);
                } else {
                    // Scroll the filter panel itself
                    List<WebElement> panels = driver.findElements(
                            By.xpath("//div[contains(@class,'filter-panel') or " +
                                    "contains(@class,'more-filter') or " +
                                    "contains(@class,'sidebar')]"));
                    if (!panels.isEmpty()) {
                        js.executeScript("arguments[0].scrollTop += 150;", panels.get(0));
                    }
                }
                pause(500);

                brand = findBrandElement(byFor, byText);
                if (brand != null) {
                    System.out.println("Found brand after " + attempt + " scroll attempt(s).");
                    break;
                }
            }
        }

        // Final debug dump if still not found
        if (brand == null) {
            System.out.println("=== BRAND NOT FOUND — all labels in DOM ===");
            List<WebElement> allLabels = driver.findElements(By.xpath("//label"));
            for (WebElement l : allLabels) {
                System.out.println("  TEXT='" + l.getText()
                        + "'  FOR='" + l.getAttribute("for") + "'");
            }
            System.out.println("===========================================");
            throw new RuntimeException(
                    "Brand '" + brandName + "' not found even after scrolling. " +
                            "Check brand name spelling.");
        }

        scrollIntoView(brand);
        pause(500);
        jsClick(brand);
        pause(2000);
        System.out.println("Selected brand: " + brandName);
    }

    /**
     * Tries both exact locators instantly using findElements (no timeout penalty).
     * Returns the first matching element, or null if neither matches.
     */
    private WebElement findBrandElement(By byFor, By byText) {
        List<WebElement> found = driver.findElements(byFor);
        if (!found.isEmpty()) {
            System.out.println("Matched brand by FOR attribute.");
            return found.get(0);
        }
        found = driver.findElements(byText);
        if (!found.isEmpty()) {
            System.out.println("Matched brand by exact text.");
            return found.get(0);
        }
        return null;
    }

    public void clickApply(String buttonText) {
        By locator = By.xpath(String.format("//button[normalize-space()='%s']", buttonText));
        WebElement btn = waitForClickable(locator);
        scrollIntoView(btn);
        pause(500);
        jsClick(btn);
        pause(3000);
        System.out.println("Clicked: " + buttonText);

        closeFilterDrawerIfOpen();
        projectSshot.capture(driver, "2_AfterFilter");
    }

    public void displayTopProducts(int count, int maxPrice) {
        pause(5000);
        scrollBy(0, 300);
        pause(2000);

        System.out.println("\n===============================================");
        System.out.println("Total products below ₹" + maxPrice + ": " + productNames.size());
        System.out.println("Displaying Top " + count + " products:");
        System.out.println("===============================================\n");

        int displayed = Math.min(count, productNames.size());
        for (int i = 0; i < displayed; i++) {
            String name  = productNames.get(i).getText();
            String price = (i < productPrices.size()) ? productPrices.get(i).getText() : "Price N/A";
            System.out.println((i + 1) + ". " + name);
            System.out.println("   Price: " + price);
            System.out.println("-----------------------------------------------");
        }
    }

    public List<String[]> getAllProductNamesAndPrices(int count) {
        pause(2000);
        scrollBy(0, 300);
        pause(1000);

        List<String[]> products = new ArrayList<>();
        System.out.println("Total products found: " + productNames.size());

        int limit = Math.min(count, productNames.size());
        for (int i = 0; i < limit; i++) {
            String name  = productNames.get(i).getText();
            String price = (i < productPrices.size()) ? productPrices.get(i).getText() : "0";
            if (!name.isEmpty() && !price.isEmpty()) {
                products.add(new String[]{name, price});
                System.out.println("Product: " + name + " | Price: " + price);
            }
        }

        if (!topProductsCaptured) {
            projectSshot.capture(driver, "3_TopProductsBelow");
            topProductsCaptured = true;
        }

        return products;
    }
}