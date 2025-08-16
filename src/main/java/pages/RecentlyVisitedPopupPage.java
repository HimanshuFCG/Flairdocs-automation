package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.log4j.Logger;

public class RecentlyVisitedPopupPage {
    private static final Logger log = Logger.getLogger(RecentlyVisitedPopupPage.class);
    private final Page page;
    
    // Locators
    private static final String RECENTLY_VISITED_BUTTON = "span:has-text('Recently Visited')";
    private static final String CLOSE_BUTTON = "button.popup-div-close-button[onclick*='RecentlyVisitedHistoryToggle']";
    
    public RecentlyVisitedPopupPage(Page page) {
        this.page = page;
    }
    
   
    public void clickRecentlyVisited(ExtentTest test) {
        final int MAX_RETRIES = 3;
        final int RETRY_DELAY_MS = 1000;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                // Wait for any modal overlay to be hidden
                page.waitForSelector("div.TelerikModalOverlay:not([style*='display: none'])", 
                    new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(5000));
                
                // Wait for the element to be visible and stable
                page.waitForSelector(RECENTLY_VISITED_BUTTON, 
                    new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000));
                
                // Scroll into view and ensure it's stable
                page.locator(RECENTLY_VISITED_BUTTON).first().scrollIntoViewIfNeeded();
                page.waitForTimeout(500); // Small delay for stability
                
                // Click with force if needed on last attempt
                if (attempt == MAX_RETRIES) {
                    page.locator(RECENTLY_VISITED_BUTTON).first().click(new Locator.ClickOptions().setForce(true));
                } else {
                    page.locator(RECENTLY_VISITED_BUTTON).first().click();
                }
                
                log.info("Clicked on 'Recently Visited' button (attempt " + attempt + ")");
                if (test != null) {
                    test.info("Clicked on 'Recently Visited' button (attempt " + attempt + ")");
                }
                return; // Success, exit the retry loop
            } catch (Exception e) {
                // If we've exhausted all retries, log and throw the last error
                if (attempt == MAX_RETRIES) {
                    String errorMsg = String.format("Failed to click on 'Recently Visited' button after %d attempts: %s", 
                        MAX_RETRIES, e.getMessage());
                    log.error(errorMsg, e);
                    if (test != null) {
                        test.fail(errorMsg);
                    }
                    throw e;
                }
                
                // Log the failed attempt and wait before retrying
                String retryMsg = String.format("Attempt %d/%d failed, retrying in %dms: %s", 
                    attempt, MAX_RETRIES, RETRY_DELAY_MS, e.getMessage());
                log.warn(retryMsg);
                if (test != null) {
                    test.warning(retryMsg);
                }
                page.waitForTimeout(RETRY_DELAY_MS);
            }
        }
    }
    
    
    public void closePopup(ExtentTest test) {
        try {
            // First wait for the element to be visible
            page.waitForSelector(CLOSE_BUTTON, 
                new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(15000));
                    
            // Then locate and click the element
            page.locator(CLOSE_BUTTON).first().click();
            log.info("Clicked on 'Recently Visited' button");
            if (test != null) {
                test.info("Clicked on 'Recently Visited' button");
            }
        } catch (Exception e) {
            String errorMsg = "Failed to click on 'Recently Visited' button: " + e.getMessage();
            log.error(errorMsg, e);
            if (test != null) {
                test.fail("Failed to click on 'Recently Visited' button: " + e.getMessage());
            }
            throw e;
        }
    }
    
    public boolean isPopupVisible() {
        try {
            return page.locator(CLOSE_BUTTON).first().isVisible();
        } catch (Exception e) {
            log.warn("Error checking popup visibility: " + e.getMessage());
            return false;
        }
    }
}
