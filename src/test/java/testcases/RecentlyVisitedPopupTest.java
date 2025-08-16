package testcases;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.Test;
import pages.RecentlyVisitedPopupPage;
import org.apache.log4j.Logger;
import utils.SoftAssertionUtils;

public class RecentlyVisitedPopupTest extends BaseTest {
    private static final Logger log = Logger.getLogger(RecentlyVisitedPopupTest.class);

    /**
     * Test to verify the Recently Visited popup functionality
     * 1. Logs in successfully (inherited from BaseTest)
     * 2. Clicks on the Recently Visited button
     * 3. Verifies the popup is displayed
     * 4. Clicks the close button
     * 5. Verifies the popup is closed
     */
    @Test(priority = 10, 
          description = "Verify Recently Visited popup functionality",
          groups = {"smoke", "regression"})
    public void testRecentlyVisitedPopup() {
        ExtentTest test = getExtentTest();
        SoftAssertionUtils softAssert = new SoftAssertionUtils(getExtentTest(), log);
        
        try {
            log.info("Starting Recently Visited popup test");
            test.info("Starting Recently Visited popup test");
            
            // Initialize page objects
            RecentlyVisitedPopupPage recentlyVisitedPage = new RecentlyVisitedPopupPage(page);
            
            // Step 1: Click on Recently Visited button
            log.info("Clicking on Recently Visited button");
            test.info("Clicking on Recently Visited button");
            recentlyVisitedPage.clickRecentlyVisited(test);
            
            // Step 2: Verify popup is visible
            log.info("Verifying popup is displayed");
            test.info("Verifying popup is displayed");
            boolean isPopupVisible = recentlyVisitedPage.isPopupVisible();
            softAssert.assertTrue(isPopupVisible, "Recently Visited popup should be visible after clicking the button");
            
            // Small wait to ensure popup is fully rendered
            page.waitForTimeout(1000);
            
            // Step 3: Click the close button
            log.info("Clicking close button on popup");
            test.info("Clicking close button on popup");
            recentlyVisitedPage.closePopup(test);
            
            // Step 4: Verify popup is closed
            log.info("Verifying popup is closed");
            test.info("Verifying popup is closed");
            boolean isPopupClosed = !recentlyVisitedPage.isPopupVisible();
            softAssert.assertTrue(isPopupClosed, "Recently Visited popup should be closed after clicking the close button");
            
            // Assert all verifications
            softAssert.assertAll();
            log.info("Recently Visited popup test completed successfully");
            test.pass("Recently Visited popup test completed successfully");
            
        } catch (Exception e) {
            String errorMsg = "Error in Recently Visited popup test: " + e.getMessage();
            log.error(errorMsg, e);
            test.fail(errorMsg);
            throw e;
        }
    }
}
