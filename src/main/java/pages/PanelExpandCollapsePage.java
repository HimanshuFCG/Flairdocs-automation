package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.aventstack.extentreports.ExtentTest;
import org.apache.log4j.Logger;

/**
 * Represents the Panel Expand Collapse page.
 */
public class PanelExpandCollapsePage {
    private static final Logger log = Logger.getLogger(PanelExpandCollapsePage.class);
    private Page page;

    // --- Locators ---
    private static final String DOMAIN_DROPDOWN_XPATH = "//span[@class='rtbText' and text()='Domain:']";
    private static final String PROJECT_DROPDOWN_INPUT = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Input";
    private static final String PROJECT_LIST_ITEM = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_listbox li.rcbItem";
    private static final String FILE_ROW_SELECTOR = "//tr[contains(@id, 'ctl00_Main_FileSnapShot1_GridSnapShotTracts_ctl00__0')]/td[text()='001']";
    private static final String TAB_XPATH_TEMPLATE = "//a[.//span[@class='rtsTxt' and normalize-space(text())='%s']]";
    private static final String EXPAND_XPATH = "//*[@id='ctl00_Main_DynamicContent1_ibPlus']";
    private static final String COLLAPSE_XPATH = "//*[@id='ctl00_Main_DynamicContent1_ibMinus']";
    private static final String LOADING_SPINNER_SELECTOR = ".loading";

    // --- Page Data ---
    private static final String[] TAB_NAMES = {
        "File Information", "Assignments", "Lease/Rental/Permit", "Legal Description",
        "Appraisal", "Surplus", "Marketing", "Sale/Closing", "Property Diary",
        "Financials", "Checklist"
    };

    public PanelExpandCollapsePage(Page page) {
        this.page = page;
    }

    public void iterateAndTestPanelsInAllTabs(ExtentTest test) {
        for (String tabName : TAB_NAMES) {
            clickTabByName(tabName, test);
            
            page.waitForSelector(LOADING_SPINNER_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(60000));
            
            expandAndCollapseAllPanelsInCurrentTab(tabName, test);
            
            page.waitForTimeout(500);
        }
    }

    public void selectDomain(String domainName, ExtentTest test) {
        Locator domainDropdown = page.locator(DOMAIN_DROPDOWN_XPATH);
        domainDropdown.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
        domainDropdown.click();
        test.info("Clicked 'Domain:' dropdown");

        String domainOptionXpath = String.format("//span[@class='rtbText' and text()='%s']", domainName);
        Locator domainOption = page.locator(domainOptionXpath);
        domainOption.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
        domainOption.click();
        test.info("Selected '" + domainName + "' from domain dropdown");
    }

    public void selectProject(String project2, ExtentTest test) {
        try {
            // Wait for page stability
            page.waitForLoadState(LoadState.NETWORKIDLE, 
                new Page.WaitForLoadStateOptions().setTimeout(60000));
            page.waitForTimeout(5000);
            
            // Try to wait for Telerik (but don't fail if it doesn't exist)
            try {
                page.waitForFunction(
                    "() => window.Telerik && window.Telerik.Web && window.Telerik.Web.UI",
                    new Page.WaitForFunctionOptions().setTimeout(10000)
                );
            } catch (Exception e) {
                test.pass("Telerik check failed, proceeding anyway");
            }
            
            boolean dropdownOpened = false;
            
            // Strategy 1: Click the combobox input (based on our earlier analysis)
            try {
                Locator comboboxInput = page.getByRole(AriaRole.COMBOBOX, 
                    new Page.GetByRoleOptions().setName("Snapshot Project Selector"));
                comboboxInput.click();
                dropdownOpened = true;
                test.info("Opened dropdown using combobox role");
            } catch (Exception e) {
                test.warning("Combobox role failed: " + e.getMessage());
            }
            
            // Strategy 2: Click the input field directly
            if (!dropdownOpened) {
                try {
                    page.locator(PROJECT_DROPDOWN_INPUT).click();
                    dropdownOpened = true;
                    test.info("Opened dropdown using input field");
                } catch (Exception e) {
                    test.warning("Input field click failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Click the dropdown arrow
            if (!dropdownOpened) {
                try {
                    page.locator("#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Arrow").click();
                    dropdownOpened = true;
                    test.info("Opened dropdown using arrow");
                } catch (Exception e) {
                    test.warning("Arrow click failed: " + e.getMessage());
                }
            }
            
            if (!dropdownOpened) {
                throw new RuntimeException("Failed to open project dropdown with all strategies");
            }
            
            // Wait for dropdown to open
            page.waitForTimeout(2000);
            
            // Select the project from the dropdown
            try {
                page.locator(PROJECT_LIST_ITEM)
                    .filter(new Locator.FilterOptions().setHasText(project2))
                    .click();
                test.info("Selected project: " + project2);
            } catch (Exception e) {
                // Fallback: simple text search
                page.getByText(project2).click();
                test.info("Selected project using text search: " + project2);
            }
            
            // Wait for selection to complete
            page.waitForTimeout(2000);
            
        } catch (Exception e) {
            test.fail("Project selection failed: " + e.getMessage());
            throw e;
        }
    }

    public void clickFirstFileInTable(ExtentTest test) {
        page.waitForSelector(FILE_ROW_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        page.locator(FILE_ROW_SELECTOR).first().click();
        test.info("Clicked the first file in the file table.");
        log.info("Clicked the first file in the file table.");
    }

    private void clickTabByName(String tabName, ExtentTest test) {
        String tabXpath = String.format(TAB_XPATH_TEMPLATE, tabName);
        Locator tab = page.locator(tabXpath);
        tab.waitFor(new Locator.WaitForOptions().setTimeout(30000).setState(WaitForSelectorState.VISIBLE));
        tab.click();
        test.info("Clicked tab: " + tabName);
        log.info("Clicked tab: " + tabName);
    }

    private void expandAndCollapseAllPanelsInCurrentTab(String tabName, ExtentTest test) {
        // --- Expand all panels ---
        Locator expandButtons = page.locator(EXPAND_XPATH);
        int expandCount = expandButtons.count();
        log.info("Found " + expandCount + " expandable panels in tab: " + tabName);

        // Use a standard for-loop as the DOM may change after each click
        for (int i = 0; i < expandCount; i++) {
            // Re-locate the button in each iteration to avoid stale elements
            Locator expandBtn = page.locator(EXPAND_XPATH).first(); 
            if (expandBtn.isVisible() && expandBtn.isEnabled()) {
                expandBtn.click();
                test.info("Expanded panel " + (i + 1) + " in tab: " + tabName);
                log.info("Expanded panel " + (i + 1) + " in tab: " + tabName);
                // Wait for content to load/animations to finish
                page.waitForSelector(LOADING_SPINNER_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(60000));
            }
        }

        // --- Collapse all panels ---
        Locator collapseButtons = page.locator(COLLAPSE_XPATH);
        int collapseCount = collapseButtons.count();
        log.info("Found " + collapseCount + " collapsible panels in tab: " + tabName);
        
        for (int i = 0; i < collapseCount; i++) {
            // Re-locate the button, always clicking the first one available
            Locator collapseBtn = page.locator(COLLAPSE_XPATH).first();
            if (collapseBtn.isVisible() && collapseBtn.isEnabled()) {
                collapseBtn.click();
                test.info("Collapsed panel " + (i + 1) + " in tab: " + tabName);
                log.info("Collapsed panel " + (i + 1) + " in tab: " + tabName);
                // Wait for content to load/animations to finish
                 page.waitForSelector(LOADING_SPINNER_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(60000));
            }
        }
    }
}
