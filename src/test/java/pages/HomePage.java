
package pages;

import Screenshot.projectSshot;
import factory.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BaseClass {

    // ── Locators ─────────────────────────────────────────────────────────────────
    @FindBy(xpath = "//input[@id='search']")
    private WebElement searchInput;

    @FindBy(xpath = "//a[normalize-space()='GIFT CARDS']")
    private WebElement giftCardsLink;

    // ── Constructor ──────────────────────────────────────────────────────────────
    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ── Actions ──────────────────────────────────────────────────────────────────

    /** Opens the Pepperfry website and waits for page load. */
    public void open(String url) {
        driver.get(url);
        pause(5000);
        System.out.println("Opened: " + url);
    }

    /** Closes the popup if it appears on page load. */
    public void closePopupIfPresent() {
        try {
            WebElement popupClose = driver.findElement(By.xpath("//div[@class='modal-body']/a"));
            jsClick(popupClose);
            System.out.println("Popup closed.");
        } catch (Exception e) {
            System.out.println("No popup found.");
        }
    }

    /** Types keyword in the search box and hits ENTER. */
    public void searchFor(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.sendKeys(keyword);
        actions.sendKeys(Keys.ENTER).perform();
        pause(3000);
        System.out.println("Searched for: " + keyword);

        // Screenshot #1 — BEFORE filter
        projectSshot.capture(driver, "1_BeforeFilter");
    }

    /**
     * Navigates to the Gift Cards page.
     *
     * FIX: replaced element.click() with jsClick() to bypass the
     * drawer-overlay div that intercepts clicks after the filter panel
     * is used. Also waits for the overlay to disappear first, and falls
     * back to JS click if the overlay is still blocking.
     */
    public void goToGiftCards() {
        pause(2000);
        scrollBy(0, -300);
        pause(1000);

        // Wait for any lingering drawer-overlay to disappear before clicking
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'drawer-overlay')]")));
            System.out.println("Overlay cleared — safe to click GIFT CARDS.");
        } catch (Exception e) {
            System.out.println("Overlay wait timed out — attempting JS click anyway.");
        }

        // Use JS click so the drawer-overlay cannot intercept it
        wait.until(ExpectedConditions.visibilityOf(giftCardsLink));
        scrollIntoView(giftCardsLink);
        pause(500);
        jsClick(giftCardsLink);
        pause(1000);
        System.out.println("Navigated to Gift Cards.");
    }
}