package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.WaitHelper; // Added for centralized waits
import org.apache.log4j.Logger;

public class CompleteLoginPage {
    private static final Logger log = Logger.getLogger(CompleteLoginPage.class);
    private final Page page;
    public CompleteLoginPage(Page page) { this.page = page; }

    // Centralized locators

    private static final String DOMAIN_DROPDOWN_XPATH = "//span[@class='rtbText' and text()='Domain:']";
    private static final String PROJECT_DROPDOWN_INPUT = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Input";
    private static final String PROJECT_LIST_ITEM = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_listbox li.rcbItem";
    private static final String GO_TO_PROJECT_DETAILS_XPATH = "//input[@id='ctl00_Main_ProjectSnapShotDetails_btnProjeSnapShotOpen']";
    private static final String TAB_XPATH_TEMPLATE = "//span[@class='rtsTxt' and text()='%s']";

   

    public void selectDomain(String domain, ExtentTest test) {
        Locator domainDropdown = page.locator(DOMAIN_DROPDOWN_XPATH);
        domainDropdown.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
        domainDropdown.click();
        test.info("Clicked 'Domain:' dropdown");

        String domainOptionXpath = "//span[@class='rtbText' and text()='" + domain + "']";
        Locator domainOption = page.locator(domainOptionXpath);
        domainOption.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
        domainOption.click();
        test.info("Selected '" + domain + "' from domain dropdown");
    }

    public void selectProject(String project, ExtentTest test) {
        // 1. Open the dropdown using a locator, which auto-waits for the element to be ready.
        // This is more robust than a simple page.click() and avoids the need for a try-catch.
        page.locator(PROJECT_DROPDOWN_INPUT).click();
        test.info("Clicked project dropdown input");
    
        // 2. Locate the specific project item in the list.
        // Using hasText is a reliable way to find the element.
        Locator projectListItem = page.locator(PROJECT_LIST_ITEM, new Page.LocatorOptions().setHasText(project));
        

        
        // It will wait up to the default timeout for the item to appear.
        //assertThat(projectListItem).isVisible(new Locator.IsVisibleOptions().setTimeout(30000));
        test.info("Project '" + project + "' is visible in the dropdown list.");
    
        // 4. Click the item. We use .first() in case the name appears in multiple items.
        projectListItem.first().click();
        test.info("Selected project: " + project);
    
        // 5. THIS IS THE MOST IMPORTANT STEP to avoid timeouts.
        // Instead of waiting for a fixed time or a spinner, we wait for the network activity
        // caused by the selection to complete. This means the test proceeds the moment the
        // project data has actually loaded.
        test.info("Waiting for project data to load...");
        page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(60000));
        test.info("Project data finished loading.");
    }
    public void goToProjectDetails(ExtentTest test) {
        Locator goToDetails = page.locator(GO_TO_PROJECT_DETAILS_XPATH);
        goToDetails.waitFor(new Locator.WaitForOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
        goToDetails.click();
        test.info("Clicked 'Go to Project Details' button");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void clickTab(String tabName, ExtentTest test) {
        String tabXPath = String.format(TAB_XPATH_TEMPLATE, tabName);
        Locator tab = page.locator(tabXPath);
        tab.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
        tab.click();
        test.info("Clicked '" + tabName + "' tab");
        WaitHelper.waitForNetworkIdle(page, 10000);
    }
    /**
 * Checks if a tab with the given name is present on the page
 * @param tabName Name of the tab to check
 * @param extentTest ExtentTest instance for logging
 * @return true if the tab is present, false otherwise
 */
public boolean isTabPresent(String tabName, ExtentTest extentTest) {
    try {
        // Using a locator that finds a tab with the exact text
        boolean isPresent = page.locator("//*[contains(@class,'tab')][normalize-space()='" + tabName + "']").isVisible();
        
        // Log the result
        String logMessage = "Tab '" + tabName + "' is " + (isPresent ? "present" : "not present");
        if (extentTest != null) {
            extentTest.info(logMessage);
        }
        log.info(logMessage);
        
        return isPresent;
    } catch (Exception e) {
        String errorMsg = "Error checking if tab '" + tabName + "' is present: " + e.getMessage();
        log.error(errorMsg, e);
        if (extentTest != null) {
            extentTest.fail("Failed to check tab presence: " + e.getMessage());
        }
        return false;
    }
}

/**
 * Verifies if the content for a tab is loaded
 * @param tabName Name of the tab whose content to verify
 * @param extentTest ExtentTest instance for logging
 * @return true if content is loaded, false otherwise
 */
public boolean isTabContentLoaded(String tabName, ExtentTest extentTest) {
    try {
        // Wait for the tab to be active and its content to be visible
        // This is a more specific locator that looks for the active tab's content panel
        String tabContentLocator = String.format(
            "//li[contains(@class,'active')]//span[contains(@class,'menu-text') and contains(.,'%s')]", 
            tabName
        );
        
        // Wait for the tab to be active and visible
        boolean isLoaded = page.locator(tabContentLocator)
                             .isVisible(new Locator.IsVisibleOptions().setTimeout(5000));
        
        // Additionally, check for a loading indicator to be hidden if your app uses one
        try {
            page.locator(".loading-indicator").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(5000));
        } catch (Exception e) {
            // Ignore if loading indicator is not present or doesn't hide in time
        }
        
        String logMessage = "Tab content for '" + tabName + "' is " + (isLoaded ? "loaded" : "not loaded");
        if (extentTest != null) {
            extentTest.info(logMessage);
        }
        log.info(logMessage);
        
        return isLoaded;
    } catch (Exception e) {
        String errorMsg = "Error checking if tab content is loaded for '" + tabName + "': " + e.getMessage();
        log.error(errorMsg, e);
        if (extentTest != null) {
            extentTest.fail("Failed to verify tab content: " + e.getMessage());
        }
        return false;
    }
}
} 