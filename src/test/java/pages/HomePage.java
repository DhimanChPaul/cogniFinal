
package pages;

import Screenshot.projectSshot;
import factory.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BaseClass {

    // Locators
    @FindBy(xpath = "//input[@id='search']")
    private WebElement searchInput;

    @FindBy(xpath = "//a[normalize-space()='GIFT CARDS']")
    private WebElement giftCardsLink;

    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Actions
    public void open(String url) {
        driver.get(url);
        pause(5000);
        System.out.println("Opened: " + url);
    }

    //popup
    public void closePopupIfPresent() {
        try {
            WebElement popupClose = driver.findElement(By.xpath("//div[@class='modal-body']/a"));
            jsClick(popupClose);
            System.out.println("Popup closed.");
        } catch (Exception e) {
            System.out.println("No popup found.");
        }
    }

    //search
    public void searchFor(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.sendKeys(keyword);
        actions.sendKeys(Keys.ENTER).perform();
        pause(3000);
        System.out.println("Searched for: " + keyword);
        projectSshot.capture(driver, "1_BeforeFilter");
    }


    public void goToGiftCards() {
        pause(2000);
        scrollBy(0, -300);
        pause(1000);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'drawer-overlay')]")));
            System.out.println("Overlay cleared — safe to click GIFT CARDS.");
        } catch (Exception e) {
            System.out.println("Overlay wait timed out — attempting JS click anyway.");
        }
//        waitForVisibility
        wait.until(ExpectedConditions.visibilityOf(giftCardsLink));
        scrollIntoView(giftCardsLink);
        pause(500);
        jsClick(giftCardsLink);
        pause(1000);
        System.out.println("Navigated to Gift Cards.");
    }
}