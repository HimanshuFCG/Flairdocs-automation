package testcases;

import base.BaseTest;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import pages.TopMenuPage;
import config.ConfigReader;
import com.microsoft.playwright.Page;
import com.aventstack.extentreports.ExtentTest;

public class TopMenuNavigationTest extends BaseTest {
    private static final Logger log = Logger.getLogger(TopMenuNavigationTest.class);


    @Test
    public void clickTopMenuItems() {
       
        try {
            login();
            ExtentTest extentTest = getExtentTest();
            extentTest.info("Logged in using BaseTest.login()");
            log.info("Logged in using BaseTest.login()");

            TopMenuPage topMenu = new TopMenuPage(page);

            // Click 'Search'
            topMenu.clickSearch();
            extentTest.info("Clicked 'Search' menu item");
            log.info("Clicked 'Search' menu item");
            page.waitForTimeout(5000);

            // Click 'My Work Queue'
            topMenu.clickMyWorkQueue();
            extentTest.info("Clicked 'My Work Queue' menu item");
            log.info("Clicked 'My Work Queue' menu item");
            page.waitForTimeout(5000);

            // Click 'My Profile'
            topMenu.clickMyProfile();
            extentTest.info("Clicked 'My Profile' menu item");
            log.info("Clicked 'My Profile' menu item");
            page.waitForTimeout(5000);

            // Click 'PM Inquiry' (new window)
            Page pmInquiryPage = page.context().waitForPage(() -> {
                topMenu.clickPMInquiry();
            });
            extentTest.info("Clicked 'PM Inquiry' menu item and new window opened");
            log.info("Clicked 'PM Inquiry' menu item and new window opened");
            int width = Integer.parseInt(ConfigReader.get("viewportWidth", "1581"));
            int height = Integer.parseInt(ConfigReader.get("viewportHeight", "864"));
            pmInquiryPage.setViewportSize(width, height);
            pmInquiryPage.waitForTimeout(2000);
            pmInquiryPage.close(); // Close new window

            // Now just continue with the original page (main tab)
            topMenu.clickNotifications();
            extentTest.info("Clicked 'Notifications' menu item");
            log.info("Clicked 'Notifications' menu item");
            page.waitForTimeout(5000);

            // Click 'Doc Mgmt' (new window)
            Page docMgmtPage = page.context().waitForPage(() -> {
                topMenu.clickDocMgmt();
            });
            extentTest.info("Clicked 'Doc Mgmt' menu item and new window opened");
            log.info("Clicked 'Doc Mgmt' menu item and new window opened");
            docMgmtPage.setViewportSize(width, height);
            docMgmtPage.waitForTimeout(2000);
            docMgmtPage.close();
            page.waitForTimeout(5000);

            // Click 'GIS' (new window)
            Page gisPage = page.context().waitForPage(() -> {
                topMenu.clickGIS();
            });
            extentTest.info("Clicked 'GIS' menu item and new window opened");
            log.info("Clicked 'GIS' menu item and new window opened");
            gisPage.setViewportSize(width, height);
            gisPage.waitForTimeout(2000);
            gisPage.close();
            page.waitForTimeout(5000);

            // Click 'Dashboard' (same window)
            topMenu.clickDashboard();
            extentTest.info("Clicked 'Dashboard' menu item");
            log.info("Clicked 'Dashboard' menu item");
            page.setViewportSize(width, height);
            page.waitForTimeout(2000);

            extentTest.pass("Successfully clicked all top menu items.");
            log.info("Successfully clicked all top menu items.");
        } catch (Exception e) {
            safeExtentLog("Test failed: " + e.getMessage());
            log.error("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 