package pages;
import com.aventstack.extentreports.ExtentTest;
import org.apache.log4j.Logger;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class PinnedPopupPage{

    private static final Logger logger = Logger.getLogger(PinnedPopupPage.class);
    private final Page page;
    
    private static final String PINNED_BUTTON = "span:has-text('Pinned')";
    private static final String CLOSE_BUTTON = "button.popup-div-close-button[onclick*='PinUnpinApplicationToggle']";
    
     public PinnedPopupPage(Page page) {
        this.page = page;
    }
    
   
    public void clickPinned(ExtentTest test) {
        try {
            // First wait for the element to be visible
            page.waitForSelector(PINNED_BUTTON, 
                new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(15000));
                    
            // Then locate and click the element
            page.locator(PINNED_BUTTON).first().click();
            logger.info("Clicked on 'Recently Visited' button");
            if (test != null) {
                test.info("Clicked on 'Recently Visited' button");
            }
        } catch (Exception e) {
            String errorMsg = "Failed to click on 'Recently Visited' button: " + e.getMessage();
            logger.error(errorMsg, e);
            if (test != null) {
                test.fail("Failed to click on 'Recently Visited' button: " + e.getMessage());
            }
            throw e;
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
            logger.info("Clicked on 'Recently Visited' button");
            if (test != null) {
                test.info("Clicked on 'Recently Visited' button");
            }
        } catch (Exception e) {
            String errorMsg = "Failed to click on 'Recently Visited' button: " + e.getMessage();
            logger.error(errorMsg, e);
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
            logger.warn("Error checking popup visibility: " + e.getMessage());
            return false;
        }
    }
    
}
