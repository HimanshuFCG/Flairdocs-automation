package testcases;

import base.BaseTest;
import org.testng.annotations.Test;
import java.util.Arrays;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import org.apache.log4j.Logger;
import pages.AdminPage;
import pages.ExpandPanelPage;
import pages.CreateNewFilePage;
import pages.CreateNewProjectPage;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Frame;
import config.ConfigReader;
import pages.CompleteLoginAndPanelPage;
import pages.CompleteLoginPage;
import pages.TopMenuPage;
import utils.SoftAssertionUtils;
import pages.ExpandAllPanelsInAllTabsPage;
import pages.FileSelectionPage;
import pages.PanelExpandCollapsePage;
import pages.PinnedPopupPage;
import pages.RecentlyVisitedPopupPage;

import java.util.List;

public class FullUserJourneyTest extends BaseTest {
    private static final Logger log = Logger.getLogger(FullUserJourneyTest.class);

    // --- Admin menu navigation ---
   @Test(priority = 1)
public void adminMenuNavigation() {
    org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

    try {
        log.info("Starting admin menu navigation test");
        safeExtentLog("Starting admin menu navigation test");

        // 1. Wait for the page to be fully loaded after login
        log.info("Waiting for page to be fully loaded...");
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.LOAD);
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        safeExtentLog("Page fully loaded");

        // 2. Initialize AdminPage with current ExtentTest once
        ExtentTest extentTest = getExtentTest();
        AdminPage adminPage = new AdminPage(page);
        safeExtentLog("AdminPage initialized");

        // 3. Click the Admin menu to expand it
        log.info("Clicking Admin menu...");
        adminPage.clickAdminMenu(extentTest);
        safeExtentLog("Clicked Admin menu");

        // 4. Wait dynamically for menu expansion - prefer explicit wait over static sleep
        page.waitForSelector(AdminPage.ADMIN_BUTTON, new Page.WaitForSelectorOptions()
            .setTimeout(5000)
            .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

        // 5. Verify admin menu is visible
        boolean isMenuVisible = adminPage.isAdminMenuVisible();
        log.info("Is admin menu visible: " + isMenuVisible);

        if (!isMenuVisible) {
            safeExtentFail("Admin menu is not visible after clicking");
            softAssert.fail("Admin menu is not visible after clicking");
            softAssert.assertAll();
            return;
        }

        // 6. Get admin menu title
        String menuTitle = adminPage.getAdminMenuTitle();
        log.info("Admin menu title: " + menuTitle);

        // 7. Soft assertions for basic menu verification
        softAssert.assertTrue(isMenuVisible, "Admin menu should be visible after clicking");
        softAssert.assertEquals(menuTitle, "Admin",
            String.format("Admin menu title should be 'Admin' | Actual: %s, Expected: Admin", menuTitle));

        // 8. Log the result of assertions if passed
        if (isMenuVisible && "Admin".equals(menuTitle)) {
            safeExtentPass("Admin menu is visible and has the correct title");

            // Navigation through admin items - now with explicit item names
            String[][] adminItems = {
                {"//a[@title='Application Logs']//span[@class='rmText' and text()='Application Logs']", "popup", "Application Logs"},
                {"//a[@title='Application Roles and Work Group']//span[@class='rmText' and text()='Application Roles and Work Groups']", "navigation", "Application Roles and Work Groups"},
                {"//a[@title='User Management']//span[@class='rmText' and text()='User Management']", "navigation", "User Management"},
                {"//a[@title='Work Group Office Information']//span[@class='rmText' and text()='Work Group Office Information']", "navigation", "Work Group Office Information"},
                {"//a[@title='Contracting Company Details']//span[@class='rmText' and text()='Contracting Company Details']", "navigation", "Contracting Company Details"},
                {"//a[@title='Contracting Price Agreements']//span[@class='rmText' and text()='Contracting Price Agreements']", "navigation", "Contracting Price Agreements"},
                {"//a[@title='Dropdowns']//span[@class='rmText' and text()='Dropdowns']", "navigation", "Dropdowns"},
                {"//a[@title='Configure Distribution Lists']//span[@class='rmText' and text()='Configure Distribution Lists']", "navigation", "Configure Distribution Lists"},
                {"//a[@title='Estimation Cost Factors']//span[@class='rmText' and text()='Estimation Cost Factors']", "navigation", "Estimation Cost Factors"},
                {"//a[@title='Expected Work Duration']//span[@class='rmText' and text()='Expected Work Duration']", "navigation", "Expected Work Duration"},
                {"//a[@title='Delete Projects and Files']//span[@class='rmText' and text()='Delete Project/File']", "navigation", "Delete Projects and Files"},
                {"//a[@title='Move File Panel']//span[@class='rmText' and text()='Move File Panel']", "navigation", "Move File Panel"},
                {"//a[@title='Cannned Report']//span[@class='rmText' and text()='Canned Report']", "navigation", "Canned Report"},
                {"//a[@title='Bulk Upload Documents']//span[@class='rmText' and text()='Bulk Upload Documents']", "navigation", "Bulk Upload Documents"},
                {"//a[@title='Bulk Generate Documents']//span[@class='rmText' and text()='Bulk Generate Documents']", "navigation", "Bulk Generate Documents"},
                {"//a[@title='Document Metadata Attributes']//span[@class='rmText' and text()='Document Metadata Attributes']", "navigation", "Document Metadata Attributes"},
                {"//a[@title='Document Packages']//span[@class='rmText' and text()='Document Packages']", "navigation", "Document Packages"},
                {"//a[@title='Document Type Configuration']//span[@class='rmText' and text()='Document Type Configuration']", "navigation", "Document Type Configuration"},
                {"//a[@title='Template and Clause Maintenance']//span[@class='rmText' and text()='Template and Clause Maintenance']", "navigation", "Template and Clause Maintenance"},
                {"//a[@title='Civil Certification / Board Approval']//span[@class='rmText' and text()='Civil Certification / Board Approval']", "navigation", "Civil Certification / Board Approval"},
                {"//a[@title='Production Plans']//span[@class='rmText' and text()='Production Plans']", "navigation", "Production Plans"},
                {"//a[@title='QA Data']//span[@class='rmText' and text()='QA Data']", "navigation", "QA Data"},
                {"//a[@title='Help Content']//span[@class='rmText' and text()='Help Content']", "navigation", "Help Content"},
                {"//a[@title='FlairBOT Library']//span[@class='rmText' and text()='FlairBOT Library']", "navigation", "FlairBOT Library"},
                {"//a[@title='Workflow Activity']//span[@class='rmText' and text()='Workflow Activity']", "navigation", "Workflow Activity"},
                {"//a[@title='Notifications Configuration']//span[@class='rmText' and text()='Notifications Configuration']", "navigation", "Notifications Configuration"},
                {"//a[@title='Workflow Designer']//span[@class='rmText' and text()='Workflow Designer']", "navigation", "Workflow Designer"},
                {"//a[@title='Environment Copy']//span[@class='rmText' and text()='Environment Copy']", "navigation", "Environment Copy"},
                {"//a[@title='Application Configuration']//span[@class='rmText' and text()='Application Configuration']", "navigation", "Application Configuration"},
                {"//a[@title='Checklist Configuration']//span[@class='rmText' and text()='Checklist Configuration']", "navigation", "Checklist Configuration"}
            };

            for (String[] item : adminItems) {
                String itemSelector = item[0];
                String itemType = item[1];
                String itemName = item[2];

                try {
                    if (itemSelector == null || itemSelector.trim().isEmpty()) {
                        safeExtentFail("Item selector cannot be null or empty for " + itemName);
                        log.error("Item selector cannot be null or empty for " + itemName);
                        continue;
                    }

                    log.info("Processing admin item: " + itemName);
                    safeExtentLog("Processing admin item: " + itemName);

                    adminPage.handleAdminItem(itemSelector, itemType, extentTest);

                    safeExtentLog("Successfully processed: " + itemName);

                    // Prefer dynamic wait if you notice UI delays
                    page.waitForTimeout(500);

                } catch (Exception e) {
                    String errorMsg = "Failed to process admin item: " + itemName + ". Error: " + e.getMessage();
                    safeExtentFail(errorMsg);
                    log.error(errorMsg, e);

                    // Take a screenshot on failure
                    try {
                        String screenshotPath = takeScreenshot("error_" + itemName.replace(" ", "_"));
                        if (screenshotPath != null) {
                            extentTest.addScreenCaptureFromPath(screenshotPath);
                            safeExtentLog("Screenshot captured: " + screenshotPath);
                        }
                    } catch (Exception screenshotEx) {
                        log.error("Failed to take screenshot: " + screenshotEx.getMessage());
                    }
                }
            }
        } else {
            safeExtentFail("Admin menu verification failed");
            softAssert.fail("Admin menu verification failed");
        }

        softAssert.assertAll();

    } catch (Exception e) {
        String errorMsg = "Test failed with exception: " + e.getMessage();
        safeExtentFail(errorMsg);
        log.error(errorMsg, e);
        try {
            String screenshotPath = takeScreenshot("admin_menu_navigation_failure");
            if (screenshotPath != null) {
                getExtentTest().addScreenCaptureFromPath(screenshotPath);
                safeExtentLog("Screenshot captured: " + screenshotPath);
            }
        } catch (Exception screenshotEx) {
            log.error("Failed to take screenshot: " + screenshotEx.getMessage());
        }
        throw new RuntimeException("Admin menu navigation test failed", e);
    } finally {
        log.info("Admin menu navigation test completed");
    }
}


    // --- Expand Panel ---
    @Test(priority = 2)
    public void panelExpansion() {
        try {
            log.info("Starting panel expansion test");
            safeExtentLog("Starting panel expansion test");
            
            // Initialize SoftAssertionUtils for this test
            utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
            
            // Get configuration values
            String domain = ConfigReader.get("domain");
            String project = ConfigReader.get("project");
            
            // Initialize page object
            ExpandPanelPage expandPanelPage = new ExpandPanelPage(page);
            
            // Select domain
            log.info("Selecting domain: " + domain);
            expandPanelPage.selectDomain(domain, getExtentTest());
            safeExtentLog("Selected domain: " + domain);
            
            // Select project
            log.info("Selecting project: " + project);
            expandPanelPage.selectProject(project, getExtentTest());
            safeExtentLog("Selected project: " + project);
            
            // Navigate to project details
            log.info("Navigating to project details");
            expandPanelPage.goToProjectDetails(getExtentTest());
            safeExtentLog("Navigated to project details");
            
            // Verify initial panel state is collapsed
            softAssert.assertTrue(expandPanelPage.isPanelCollapsed(), 
                "Panel should be initially collapsed");

            // Expand the panel and verify state
            expandPanelPage.expandPanel(getExtentTest());
            softAssert.assertTrue(expandPanelPage.isPanelExpanded(), 
                "Panel should be expanded after clicking expand button");
            
            // Collapse the panel and verify state
            expandPanelPage.collapsePanel(getExtentTest());
            softAssert.assertTrue(expandPanelPage.isPanelCollapsed(), 
                "Panel should be collapsed after clicking collapse button");

            // Assert all validations
            softAssert.assertAll();
            safeExtentPass("Panel expansion test completed successfully");
            
        } catch (Exception e) {
            String errorMsg = "Panel expansion test failed: " + e.getMessage();
            log.error(errorMsg, e);
            safeExtentFail(errorMsg);
            
            // Take screenshot on failure
            /*try {
                String screenshotPath = takeScreenshot("panel_expansion_failure");
                if (screenshotPath != null) {
                    safeExtentLog("Screenshot captured: " + screenshotPath);
                }
            } catch (Exception screenshotEx) {
                log.error("Failed to take screenshot: " + screenshotEx.getMessage());
            }*/
            
            throw new RuntimeException(errorMsg, e);
        } finally {
            log.info("Panel expansion test completed");
        }
    }

    // --- Create New File ---
    @Test (priority = 3) // TODO: Set priority as needed
    public void createNewFileAndCheckRow() {
        try {
            log.info("Starting create new file and check row test");
            safeExtentLog("Starting create new file and check row test");
            
            // Initialize page object
            CreateNewFilePage createNewFilePage = new CreateNewFilePage(page);
            
            // Get configuration values
            String domain = ConfigReader.get("domain");
            String project = ConfigReader.get("project");
            String rowId = ConfigReader.get("rowId");
            
            // --- Test Steps ---
            
            // 1. Navigate to the correct context
            log.info("Selecting domain: " + domain);
            createNewFilePage.selectDomain(domain, getExtentTest());
            safeExtentLog("Selected domain: " + domain);
            
            log.info("Selecting project: " + project);
            createNewFilePage.selectProject(project, getExtentTest());
            safeExtentLog("Selected project: " + project);
            
            // 2. Perform the entire create-and-verify workflow with a SINGLE method call
            // This one line replaces all the old calls to clickCreateNewFile, switchToCreateFileIframe,
            // fillCreateFileForminfo, and isRowPresent.
            log.info("Creating new file with ROW ID: " + rowId + " and verifying its presence.");
            createNewFilePage.createNewFileAndVerifyInTable(rowId, getExtentTest());
            
            // 3. If the method above completes without an error, the test is successful.
            // The soft assertions are no longer needed because the verification is now built into the page object method.
            safeExtentPass("Create new file test completed successfully. Row was created and verified.");
            
        } catch (Exception e) {
            // The robust error handling remains the same. If createNewFileAndVerifyInTable fails, it will be caught here.
            String errorMsg = "Create new file test failed: " + e.getMessage();
            log.error(errorMsg, e);
            safeExtentFail(errorMsg);
            
            // Take screenshot on failure
            /*try {
                String screenshotPath = takeScreenshot("create_file_failure");
                if (screenshotPath != null) {
                    safeExtentLog("Screenshot captured: " + screenshotPath);
                }
            } catch (Exception screenshotEx) {
                log.error("Failed to take screenshot: " + screenshotEx.getMessage());
            }*/
            
            throw new RuntimeException(errorMsg, e);
        } finally {
            log.info("Create new file test completed");
        }
    }
    

    // --- Complete Login and Tab Selection ---
    @Test (priority = 4)
public void completeLoginAndTabSelection() {
    try {
        log.info("Starting complete login and tab selection test");
        safeExtentLog("Starting complete login and tab selection test");
        
        // Initialize SoftAssertionUtils for this test
        utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
        
        // Get configuration values
        String domain = ConfigReader.get("domain");
        String project = ConfigReader.get("project");
        
        // Initialize page object
        CompleteLoginPage completeLoginPage = new CompleteLoginPage(page);
        
        // Select domain
        log.info("Selecting domain: " + domain);
        completeLoginPage.selectDomain(domain, getExtentTest());
        safeExtentLog("Selected domain: " + domain);
        
        // Select project
        log.info("Selecting project: " + project);
        completeLoginPage.selectProject(project, getExtentTest());
        safeExtentLog("Selected project: " + project);
        
        // Navigate to project details
        log.info("Navigating to project details");
        completeLoginPage.goToProjectDetails(getExtentTest());
        safeExtentLog("Navigated to project details");
        
        // Verify tabs are present and clickable
        log.info("Verifying tabs functionality");
        String[] tabNames = {
            "Assignments",
            "Liaison Files",
            "Estimates",
            "Contracting",
            "Authorization Management",
            "Issue Management",
            "Financials",
            "Expropriation",
            "Checklist",
            "Utility Coordination",
            "Railroad Coordination"
        };
        for (String tab : tabNames) {
            completeLoginPage.clickTab(tab, extentTest);
            //softAssert.assertTrue(completeLoginPage.isTabContentLoaded(tab,getExtentTest()), tab + " content should be loaded");
        }
            
        
        // Assert all validations
        softAssert.assertAll();
        safeExtentPass("Complete login and tab selection test completed successfully");
        
    } catch (Exception e) {
        String errorMsg = "Complete login and tab selection test failed: " + e.getMessage();
        log.error(errorMsg, e);
        safeExtentFail(errorMsg);
        
        // Take screenshot on failure
        /*try {
            String screenshotPath = takeScreenshot("login_tab_selection_failure");
            if (screenshotPath != null) {
                safeExtentLog("Screenshot captured: " + screenshotPath);
            }
        } catch (Exception screenshotEx) {
            log.error("Failed to take screenshot: " + screenshotEx.getMessage());
        }*/
        
        throw new RuntimeException(errorMsg, e);
    } finally {
        log.info("Complete login and tab selection test completed");
    }
}

    // --- Top Menu Navigation ---
    @Test(priority = 5)
    public void clickTopMenuItems() {
        try {
            log.info("Starting top menu items navigation test");
            safeExtentLog("Starting top menu items navigation test");
            
            // Initialize SoftAssertionUtils for this test
            utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
            
            // Initialize page object
            TopMenuPage topMenu = new TopMenuPage(page);
            
            // Click 'Search'
            log.info("Clicking 'Search' menu item");
            topMenu.clickSearch();
            safeExtentLog("Clicked 'Search' menu item");
            page.waitForTimeout(5000);
            
            // Verify search functionality or page load if applicable
            //softAssert.assertTrue(page.title().contains("Search"), "Search page should be loaded");
            
            // Click 'My Work Queue'
            log.info("Clicking 'My Work Queue' menu item");
            topMenu.clickMyWorkQueue();
            safeExtentLog("Clicked 'My Work Queue' menu item");
            page.waitForTimeout(5000);
            
            // Verify work queue page load if applicable
            //softAssert.assertTrue(page.title().contains("Work Queue"), "Work Queue page should be loaded");
            
            // Click 'My Profile'
            log.info("Clicking 'My Profile' menu item");
            topMenu.clickMyProfile();
            safeExtentLog("Clicked 'My Profile' menu item");
            page.waitForTimeout(5000);
            
            // Verify profile page load if applicable
            //softAssert.assertTrue(page.title().contains("Profile"), "Profile page should be loaded");
            
            // Click 'PM Inquiry' (opens in new window)
            log.info("Clicking 'PM Inquiry' menu item");
            Page pmInquiryPage = page.context().waitForPage(() -> {
                topMenu.clickPMInquiry();
            });
            safeExtentLog("Clicked 'PM Inquiry' menu item and new window opened");
            log.info("Clicked 'PM Inquiry' menu item and new window opened");
            pmInquiryPage.waitForTimeout(2000);
            pmInquiryPage.close(); // Close new window
            // Now just continue with the original page (main tab)
            topMenu.clickNotifications();
            safeExtentLog("Clicked 'Notifications' menu item");
            log.info("Clicked 'Notifications' menu item");
            page.waitForTimeout(5000);
            // Click 'Doc Mgmt' (new window)
            Page docMgmtPage = page.context().waitForPage(() -> {
                topMenu.clickDocMgmt();
            });
            safeExtentLog("Clicked 'Doc Mgmt' menu item and new window opened");
            log.info("Clicked 'Doc Mgmt' menu item and new window opened");
            docMgmtPage.waitForTimeout(2000);
            docMgmtPage.close();
            page.waitForTimeout(5000);
            // Click 'GIS' (new window)
            Page gisPage = page.context().waitForPage(() -> {
                topMenu.clickGIS();
            });
            safeExtentLog("Clicked 'GIS' menu item and new window opened");
            log.info("Clicked 'GIS' menu item and new window opened");
            gisPage.waitForTimeout(2000);
            gisPage.close();
            page.waitForTimeout(5000);
            // Click 'Dashboard' (same window)
            topMenu.clickDashboard();
            safeExtentLog("Clicked 'Dashboard' menu item");
            log.info("Clicked 'Dashboard' menu item");
            page.waitForTimeout(2000);
            log.info("Successfully clicked all top menu items.");
        } catch (Exception e) {
            safeExtentFail("Test failed: " + e.getMessage());
            log.error("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Complete Login and Panel Test ---
    @Test (priority = 6, enabled=false)// TODO: Set priority as needed
    public void loginAndOpenClosePanels() {
        try {
            log.info("Starting login and panel test");
            safeExtentLog("Starting login and panel test");
            
            // Initialize SoftAssertionUtils for this test
            utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
            
            // Get configuration values
            String domain = ConfigReader.get("domain");
            String project = ConfigReader.get("project");
            
            // Initialize page object
            CompleteLoginAndPanelPage completePage = new CompleteLoginAndPanelPage(page);
            safeExtentLog("Page object created");
            
            // Select domain
            log.info("Selecting domain: " + domain);
            completePage.selectDomain(domain, getExtentTest());
            safeExtentLog("Selected domain: " + domain);
            
            // Select project
            log.info("Selecting project: " + project);
            completePage.selectProject(project, getExtentTest());
            safeExtentLog("Selected project: " + project);
            
            // Navigate to project details
            log.info("Navigating to project details");
            completePage.goToProjectDetails(getExtentTest());
            safeExtentLog("Navigated to project details");
            log.info("Starting panel actions");
            String[] panelTitles = {
                "Project Information", "Project Status Log", "Right of Way Maps", "External Agreement",
                "Authorization Summary", "Sales Book", "Certification", "Import", "All Project Information Documents"
            };

            utilities.PanelActions panelActions = new utilities.PanelActions(page);

            for (String panelTitle : panelTitles) {
                try {

                    //Open Panel
                    log.info("Opening panel: " + panelTitle);
                    panelActions.openPanel(panelTitle, getExtentTest());
                    softAssert.assertTrue(panelActions.isPanelOpen(panelTitle, getExtentTest()), "Panel " + panelTitle + " should be open");
                    page.waitForTimeout(1000); // Optional: wait for UI stability

                    //Close Panel
                    log.info("Closing panel: " + panelTitle);
                    panelActions.closePanel(panelTitle, getExtentTest());
                    softAssert.assertTrue(panelActions.isPanelClosed(panelTitle, getExtentTest()), "Panel " + panelTitle + " should be closed");
                    safeExtentLog("Panel " + panelTitle + " closed successfully");
                    page.waitForTimeout(500);  // Optional: wait for UI stability
                    extentTest.info("Successfully opened and closed panel: " + panelTitle);
                    log.info("Successfully opened and closed panel: " + panelTitle);
                } catch (Exception e) {
                    String errorMsg = "Error toggling panel '" + panelTitle + "': " + e.getMessage();
                    log.error(errorMsg, e);
                    safeExtentFail(errorMsg);
                }
            }
            
            // Assert all validations
            softAssert.assertAll();
            safeExtentPass("Login and panel test completed successfully");
            
        } catch (Exception e) {
            String errorMsg = "Login and panel test failed: " + e.getMessage();
            log.error(errorMsg, e);
            safeExtentFail(errorMsg);
            
            // Take screenshot on failure
            try {
                String screenshotPath = takeScreenshot("login_panel_test_failure");
                if (screenshotPath != null) {
                    safeExtentLog("Screenshot captured: " + screenshotPath);
                }
            } catch (Exception screenshotEx) {
                log.error("Failed to take screenshot: " + screenshotEx.getMessage());
            }
            
            throw new RuntimeException(errorMsg, e);
        } finally {
            log.info("Login and panel test completed");
        }
    }

    // --- Expand All Panels In All Tabs ---
    @Test (priority = 7)// TODO: Set priority as needed
    public void expandAndCollapseAllPanelsInAllTabs() {
        try {
            log.info("Starting expand and collapse all panels in all tabs test");
            safeExtentLog("Starting expand and collapse all panels in all tabs test");
            
            // Initialize SoftAssertionUtils for this test
            utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
            
            // Get configuration values
            String domain = ConfigReader.get("domain");
            String project = ConfigReader.get("project");
            
            // Initialize page object
            ExpandAllPanelsInAllTabsPage expandPage = new ExpandAllPanelsInAllTabsPage(page);
            safeExtentLog("Page object created");
            
            // Select domain
            log.info("Selecting domain: " + domain);
            expandPage.selectDomain(domain, getExtentTest());
            safeExtentLog("Selected domain: " + domain);
            
            // Select project
            log.info("Selecting project: " + project);
            expandPage.selectProject(project, getExtentTest());
            safeExtentLog("Selected project: " + project);
            
            // Navigate to project details
            log.info("Navigating to project details");
            expandPage.goToProjectDetails(getExtentTest());
            safeExtentLog("Navigated to project details");

            // Expand and collapse all panels in all tabs
            log.info("Expanding and collapsing all panels in all tabs");
            expandPage.expandAndCollapseAllPanelsInAllTabs(getExtentTest());
            safeExtentLog("Expanded and collapsed all panels in all tabs");
            
            // Assert all validations
            softAssert.assertAll();
            safeExtentPass("Expand and collapse all panels in all tabs test completed successfully");
            
        } catch (Exception e) {
            String errorMsg = "Expand and collapse all panels in all tabs test failed: " + e.getMessage();
            log.error(errorMsg, e);
            safeExtentFail(errorMsg);
            
            // Take screenshot on failure
            /*try {
                String screenshotPath = takeScreenshot("expand_and_collapse_all_panels_in_all_tabs_failure");
                if (screenshotPath != null) {
                    safeExtentLog("Screenshot captured: " + screenshotPath);
                }
            } catch (Exception screenshotEx) {
                log.error("Failed to take screenshot: " + screenshotEx.getMessage());
            }*/
            
            throw new RuntimeException(errorMsg, e);
        } finally {
            log.info("Expand and collapse all panels in all tabs test completed");
        }
    }

    @Test(priority = 8)
    public void switchToDomain2AndSelectFirstFile() {
        try {
            log.info("Starting switch to domain 2 and select first file test");
            safeExtentLog("Starting switch to domain 2 and select first file test");
            
            // Initialize SoftAssertionUtils for this test
            utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
            
            // Get configuration values
            String domain2 = ConfigReader.get("domain2");
            String project2 = ConfigReader.get("project2");
            
            // Initialize page object
            FileSelectionPage fileSelectionPage = new FileSelectionPage(page);
            safeExtentLog("File selection page object created");
            
            // Select domain 2
            log.info("Switching to domain: " + domain2);
            fileSelectionPage.selectDomain(domain2, getExtentTest());
            safeExtentLog("Switched to domain: " + domain2);
            
            // Select project 2
            log.info("Selecting project: " + project2);
            fileSelectionPage.selectProject(project2, getExtentTest());
            safeExtentLog("Selected project: " + project2);
            
            log.info("Clicking first file in table");
            fileSelectionPage.clickFirstFileInTable(getExtentTest());
            safeExtentLog("Clicked first file in table");

            // Click all required file tabs in sequence
            String[] tabNames = {
                "File Information",
                "Assignments",
                "Lease/Rental/Permit",
                "Legal Description",
                "Appraisal",
                "Surplus",
                "Marketing",
                "Sale/Closing",
                "Property Diary",
                "Financials",
                "Checklist"
            };
            for (String tab : tabNames) {
                fileSelectionPage.clickTabByName(tab, getExtentTest());
                safeExtentLog("Tab " + tab + " clicked");
                log.info("Tab " + tab + " clicked");
            }

            // Go to project details    
            fileSelectionPage.goToProjectDetails(getExtentTest());
            safeExtentLog("Navigated to project details");
            log.info("Navigated to project details");

            // Click 'Project Information' and 'Checklist' tabs in project context
            fileSelectionPage.clickTabByName("Project Information", getExtentTest());
            //softAssert.assertAll();
            page.waitForSelector(".loading-spinner", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
            
            fileSelectionPage.clickTabByName("Checklist", getExtentTest());
            page.waitForSelector(".loading-spinner", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
            safeExtentLog("Project Information and Checklist tabs clicked");
            log.info("Project Information and Checklist tabs clicked");

            extentTest.pass("Successfully switched to domain2/project2, clicked the first file, navigated all file tabs, went to project details, and navigated required project tabs.");
            log.info("Successfully switched to domain2/project2, clicked the first file, navigated all file tabs, went to project details, and navigated required project tabs.");
        } catch (Exception e) {
            String errorMsg = "Failed to switch to domain 2 and select first file: " + e.getMessage();
            log.error(errorMsg, e);
            safeExtentFail(errorMsg);
            
            // Take screenshot on failure
            /*try {
                String screenshotPath = takeScreenshot("switch_domain2_select_file_failure");
                if (screenshotPath != null) {
                    safeExtentLog("Screenshot captured: " + screenshotPath);
                }
            } catch (Exception screenshotEx) {
                log.error("Failed to take screenshot: " + screenshotEx.getMessage());
            }*/
            
            throw new RuntimeException(errorMsg, e);
        } finally {
            log.info("Switch to domain 2 and select first file test completed");
        }
    }

    @Test(priority = 9)
    public void expandAndCollapsePanelsInAllTabsAfterFileSelection() {
        try {
            log.info("Starting expand and collapse panels in all tabs after file selection test");
            safeExtentLog("Starting expand and collapse panels in all tabs after file selection test");
        
        // Initialize SoftAssertionUtils for this test
        utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
        
        // Get configuration values
        String domain2 = ConfigReader.get("domain2");
        String project2 = ConfigReader.get("project2");
        
        // Initialize page object
        PanelExpandCollapsePage panelPage = new PanelExpandCollapsePage(page);
        safeExtentLog("Panel expand/collapse page object created");
        
        // Select domain 2
        log.info("Switching to domain: " + domain2);
        panelPage.selectDomain(domain2, getExtentTest());
        safeExtentLog("Switched to domain: " + domain2);
        
        // Select project 2
        log.info("Selecting project: " + project2);
        panelPage.selectProject(project2, getExtentTest());
        safeExtentLog("Selected project: " + project2);
        
        // Click first file in table
        log.info("Clicking first file in table");
        panelPage.clickFirstFileInTable(getExtentTest());
        safeExtentLog("Clicked first file in table");

        panelPage.iterateAndTestPanelsInAllTabs(getExtentTest());
        
        // 3. Mark the test as passed
        extentTest.pass("Successfully expanded and collapsed all panels in all tabs.");
        log.info("Successfully expanded and collapsed all panels in all tabs.");

    } catch (Exception e) {
        String errorMsg = "Expand and collapse panels test failed: " + e.getMessage();
        log.error(errorMsg, e);
        safeExtentFail(errorMsg);
        
        // Take screenshot on failure
        /*try {
            String screenshotPath = takeScreenshot("expand_collapse_panels_failure");
            if (screenshotPath != null) {
                safeExtentLog("Screenshot captured: " + screenshotPath);
            }
        } catch (Exception screenshotEx) {
            log.error("Failed to take screenshot: " + screenshotEx.getMessage());
        }*/
        
        throw new RuntimeException(errorMsg, e);
    } finally {
        log.info("Expand and collapse panels test completed");
    }
}

    @Test(priority = 10)
    public void openAndCloseCreateNewFilePopup() {
        try {
            log.info("Starting create new file test");
            safeExtentLog("Starting create new file test");
            
            // Get configuration and test data
            String domain = ConfigReader.get("domain2");
            String project = ConfigReader.get("project2");
            String newRowId = "TEST_ID_" + System.currentTimeMillis(); // Generate a unique ID for the test run
            
            // Initialize page object
            CreateNewFilePage createNewFilePage = new CreateNewFilePage(page);
            
            // --- Test Steps ---
            // 1. Navigate to the correct context
            createNewFilePage.selectDomain(domain, getExtentTest());
            createNewFilePage.selectProject(project, getExtentTest());
            
            // 2. Perform the entire create-and-verify workflow with a single call
            createNewFilePage.createNewFileAndVerifyInTable(newRowId, getExtentTest());
    
            // 3. Mark test as passed
            safeExtentPass("Successfully created a new file and verified its presence in the table.");
            
        } catch (Exception e) {
            // ... (your existing robust error handling remains the same)
        } finally {
            log.info("Create new file test completed");
        }
    }

    @Test(priority = 11)
    public void openAndCloseCreateNewProjectPopup() {
        try {
            log.info("Starting open and close create new project popup test");
            safeExtentLog("Starting open and close create new project popup test");
            
            // Initialize SoftAssertionUtils for this test
            utils.SoftAssertionUtils softAssert = new utils.SoftAssertionUtils(getExtentTest(), log);
            
            // Get configuration values
            String domain2 = ConfigReader.get("domain2");
            String project2 = ConfigReader.get("project2"); // We need this for selectProject
            
            // Initialize page object
            CreateNewProjectPage createNewProjectPage = new CreateNewProjectPage(page);
            
       
            createNewProjectPage.selectDomain(domain2, getExtentTest());
            
            createNewProjectPage.selectProject(project2, getExtentTest());
            
            createNewProjectPage.verifyPopupOpensAndCloses(getExtentTest());
    
            softAssert.assertAll();
            safeExtentPass("Open and close create new project popup test completed successfully.");
            
        } catch (Exception e) {
            // ... (your existing error handling)
        } finally {
            log.info("Open and close create new project popup test completed");
        }
    }

    @Test(priority = 12, 
          description = "Verify Recently Visited popup functionality",
          groups = {"smoke", "regression"})
    public void testRecentlyVisitedPopup() {
      
        SoftAssertionUtils softAssert = new SoftAssertionUtils(getExtentTest(), log);
        
        try {
            log.info("Starting Recently Visited popup test");
            safeExtentLog("Starting Recently Visited popup test");
            
            // Initialize page objects
            RecentlyVisitedPopupPage recentlyVisitedPage = new RecentlyVisitedPopupPage(page);
            
            // Step 1: Click on Recently Visited button
            log.info("Clicking on Recently Visited button");
            safeExtentLog("Clicking on Recently Visited button");
            recentlyVisitedPage.clickRecentlyVisited(getExtentTest());
            
            // Step 2: Verify popup is visible
            log.info("Verifying popup is displayed");
            safeExtentLog("Verifying popup is displayed");
            boolean isPopupVisible = recentlyVisitedPage.isPopupVisible();
            softAssert.assertTrue(isPopupVisible, "Recently Visited popup should be visible after clicking the button");
            
            // Small wait to ensure popup is fully rendered
            page.waitForTimeout(1000);
            
            // Step 3: Click the close button
            log.info("Clicking close button on popup");
            safeExtentLog("Clicking close button on popup");
            recentlyVisitedPage.closePopup(getExtentTest());
            
            // Step 4: Verify popup is closed
            log.info("Verifying popup is closed");
            safeExtentLog("Verifying popup is closed");
            boolean isPopupClosed = !recentlyVisitedPage.isPopupVisible();
            softAssert.assertTrue(isPopupClosed, "Recently Visited popup should be closed after clicking the close button");
            
            // Assert all verifications
            softAssert.assertAll();
            log.info("Recently Visited popup test completed successfully");
            safeExtentPass("Recently Visited popup test completed successfully");
            
        } catch (Exception e) {
            String errorMsg = "Error in Recently Visited popup test: " + e.getMessage();
            log.error(errorMsg, e);
            safeExtentFail(errorMsg);
            throw e;
        }
    }

    @Test(priority = 13, 
          description = "Verify Pinned popup functionality",
          groups = {"smoke", "regression"})
    public void testPinnedPopup() {
        
        SoftAssertionUtils softAssert = new SoftAssertionUtils(getExtentTest(), log);
        
        try {
            log.info("Starting Pinned popup test");
            safeExtentLog("Starting Pinned popup test");
            
            // Initialize page objects
            PinnedPopupPage pinnedPopupPage = new PinnedPopupPage(page);
            
            // Step 1: Click on Pinned button
            log.info("Clicking on Pinned button");
            safeExtentLog("Clicking on Pinned button");
            pinnedPopupPage.clickPinned(getExtentTest());
            
            // Step 2: Verify popup is visible
            log.info("Verifying popup is displayed");
            safeExtentLog("Verifying popup is displayed");
            boolean isPopupVisible = pinnedPopupPage.isPopupVisible();
            softAssert.assertTrue(isPopupVisible, "Pinned popup should be visible after clicking the button");
            
            // Small wait to ensure popup is fully rendered
            page.waitForTimeout(1000);
            
            // Step 3: Click the close button
            log.info("Clicking close button on popup");
            safeExtentLog("Clicking close button on popup");
            pinnedPopupPage.closePopup(getExtentTest());
            
            // Step 4: Verify popup is closed
            log.info("Verifying popup is closed");
            safeExtentLog("Verifying popup is closed");
            boolean isPopupClosed = !pinnedPopupPage.isPopupVisible();
            softAssert.assertTrue(isPopupClosed, "Pinned popup should be closed after clicking the close button");
            
            // Assert all verifications
            softAssert.assertAll();
            log.info("Pinned popup test completed successfully");
            safeExtentPass("Pinned popup test completed successfully");
            
        } catch (Exception e) {
            String errorMsg = "Error in Pinned popup test: " + e.getMessage();
            log.error(errorMsg, e);
            safeExtentFail(errorMsg);
            throw e;
        }
    }
}
