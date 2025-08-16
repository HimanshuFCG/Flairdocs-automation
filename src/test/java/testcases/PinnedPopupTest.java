package testcases;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.Test;
import pages.PinnedPopupPage;
import org.apache.log4j.Logger;
import utils.SoftAssertionUtils;

public class PinnedPopupTest extends BaseTest {
    private static final Logger log = Logger.getLogger(PinnedPopupTest.class);

    /**
     * Test to verify the Recently Visited popup functionality
     * 1. Logs in successfully (inherited from BaseTest)
     * 2. Clicks on the Recently Visited button
     * 3. Verifies the popup is displayed
     * 4. Clicks the close button
     * 5. Verifies the popup is closed
     */
    @Test(priority = 10, 
          description = "Verify Pinned popup functionality",
          groups = {"smoke", "regression"})
    public void testPinnedPopup() {
        ExtentTest test = getExtentTest();
        SoftAssertionUtils softAssert = new SoftAssertionUtils(getExtentTest(), log);
        
        try {
            log.info("Starting Pinned popup test");
            test.info("Starting Pinned popup test");
            
            // Initialize page objects
            PinnedPopupPage pinnedPopupPage = new PinnedPopupPage(page);
            
            // Step 1: Click on Pinned button
            log.info("Clicking on Pinned button");
            test.info("Clicking on Pinned button");
            pinnedPopupPage.clickPinned(test);
            
            // Step 2: Verify popup is visible
            log.info("Verifying popup is displayed");
            test.info("Verifying popup is displayed");
            boolean isPopupVisible = pinnedPopupPage.isPopupVisible();
            softAssert.assertTrue(isPopupVisible, "Pinned popup should be visible after clicking the button");
            
            // Small wait to ensure popup is fully rendered
            page.waitForTimeout(1000);
            
            // Step 3: Click the close button
            log.info("Clicking close button on popup");
            test.info("Clicking close button on popup");
            pinnedPopupPage.closePopup(test);
            
            // Step 4: Verify popup is closed
            log.info("Verifying popup is closed");
            test.info("Verifying popup is closed");
            boolean isPopupClosed = !pinnedPopupPage.isPopupVisible();
            softAssert.assertTrue(isPopupClosed, "Pinned popup should be closed after clicking the close button");
            
            // Assert all verifications
            softAssert.assertAll();
            log.info("Pinned popup test completed successfully");
            test.pass("Pinned popup test completed successfully");
            
        } catch (Exception e) {
            String errorMsg = "Error in Pinned popup test: " + e.getMessage();
            log.error(errorMsg, e);
            test.fail(errorMsg);
            throw e;
        }
    }
}
