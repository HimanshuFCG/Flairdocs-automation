package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.aventstack.extentreports.ExtentTest;
import org.apache.log4j.Logger;

public class FileTabsExpand_Collapse {
    private static final Logger log = Logger.getLogger(FileTabsExpand_Collapse.class);
    private Page page;

    // Locators (update as needed)
    private static final String DOMAIN_DROPDOWN_XPATH = "//span[@class='rtbText' and text()='Domain:']";
    private static final String PROJECT_DROPDOWN_ARROW = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Arrow";
    private static final String PROJECT_LIST_ITEM = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_listbox li.rcbItem";
    private static final String FILE_ROW_SELECTOR = "//tr[contains(@id, 'ctl00_Main_FileSnapShot1_GridSnapShotTracts_ctl00__0')]/td[text()='001']";
    private static final String TAB_XPATH_TEMPLATE = "//a[.//span[@class='rtsTxt' and normalize-space(text())='%s']]";
    private static final String EXPAND_XPATH = "//*[@id='ctl00_Main_DynamicContent1_ibPlus']";
    private static final String COLLAPSE_XPATH = "//*[@id='ctl00_Main_DynamicContent1_ibMinus']";
    private static final String PROJECT_DROPDOWN_INPUT = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Input";

    public FileTabsExpand_Collapse(Page page) {
        this.page = page;
    }

    public void selectDomain2(String domain2, ExtentTest test) {
        try {
            Locator domainDropdown = page.locator(DOMAIN_DROPDOWN_XPATH);
            domainDropdown.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
            domainDropdown.click();
            test.info("Clicked 'Domain' dropdown ");
            log.info("Clicked 'Domain' dropdown");

            String domainOptionXpath = "//span[@class='rtbText' and text()='" + domain2 + "']";
            Locator domainOption = page.locator(domainOptionXpath);
            domainOption.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
            domainOption.click();
            test.info("Selected '" + domain2 + "' from domain dropdown ");
            log.info("Selected '" + domain2 + "' from domain dropdown ");
        } catch (Exception e) {
            test.fail("Failed to select domain2: " + e.getMessage());
            log.error("Failed to select domain2: " + e.getMessage());
            throw e;
        }
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
        try {
            page.waitForSelector(FILE_ROW_SELECTOR);
            page.locator(FILE_ROW_SELECTOR).first().click();
            test.info("Clicked the first file in the file table.");
            log.info("Clicked the first file in the file table.");
        } catch (Exception e) {
            test.fail("Failed to click the first file: " + e.getMessage());
            log.error("Failed to click the first file: " + e.getMessage());
            throw e;
        }
    }

    public void clickTabByName(String tabName, ExtentTest test) {
        String tabXpath = String.format(TAB_XPATH_TEMPLATE, tabName);
        try {
            Locator tab = page.locator(tabXpath);
            tab.waitFor(new Locator.WaitForOptions().setTimeout(30000).setState(WaitForSelectorState.VISIBLE));
            tab.click();
            test.info("Clicked tab: " + tabName);
            log.info("Clicked tab: " + tabName);
        } catch (Exception e) {
            test.fail("Failed to click tab '" + tabName + "': " + e.getMessage());
            log.error("Failed to click tab '" + tabName + "': " + e.getMessage());
            throw e;
        }
    }

    public void expandAndCollapseAllPanelsInTab(ExtentTest test) {
        // Wait after clicking the tab (should be called right after clickTabByName)
        page.waitForTimeout(2000); // Wait 2 seconds after clicking tab
        int expandCount = page.locator(EXPAND_XPATH).count();
        for (int i = 0; i < expandCount; i++) {
            Locator expandBtn = page.locator(EXPAND_XPATH).nth(i);
            if (expandBtn.isVisible() && expandBtn.isEnabled()) {
                expandBtn.click();
                test.info("Expanded panel " + (i + 1));
                log.info("Expanded panel " + (i + 1));
                page.waitForTimeout(2000); // Wait 2 seconds after expanding
            }
        }
        int collapseCount = page.locator(COLLAPSE_XPATH).count();
        for (int i = 0; i < collapseCount; i++) {
            Locator collapseBtn = page.locator(COLLAPSE_XPATH).nth(i);
            if (collapseBtn.isVisible() && collapseBtn.isEnabled()) {
                collapseBtn.click();
                test.info("Collapsed panel " + (i + 1));
                log.info("Collapsed panel " + (i + 1));
                page.waitForTimeout(1000); // Wait 1 second after collapsing
            }
        }
    }
} 