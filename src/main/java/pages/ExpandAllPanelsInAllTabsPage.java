package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.log4j.Logger;


public class ExpandAllPanelsInAllTabsPage {
    private static final Logger log = Logger.getLogger(ExpandAllPanelsInAllTabsPage.class);
    private static final String[] TAB_NAMES = {
        "Project Information",
        "Assignments",
        "Liaison Files",
        "Estimates",
        "Contracting",
        "Authorization Management",
        "Issue Management",
        "Financials",
        "Checklist",
        "Utility Coordination",
        "Railroad Coordination"
    };
    private static final String TAB_XPATH_TEMPLATE = "//span[@class='rtsTxt' and text()='%s']";
    private static final String EXPAND_XPATH = "//*[@id='ctl00_Main_DynamicContent1_ibPlus']";
    private static final String COLLAPSE_XPATH = "//*[@id='ctl00_Main_DynamicContent1_ibMinus']";
    private static final String DOMAIN_DROPDOWN_XPATH = "//span[@class='rtbText' and text()='Domain:']";
    private static final String PROJECT_DROPDOWN_INPUT = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Input";
    private static final String PROJECT_LIST_ITEM = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_listbox li.rcbItem";
    private static final String GO_TO_PROJECT_DETAILS_XPATH = "//input[@id='ctl00_Main_ProjectSnapShotDetails_btnProjeSnapShotOpen']";
    private final Page page;

    public ExpandAllPanelsInAllTabsPage(Page page) {
        this.page = page;
    }

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
                    .filter(new Locator.FilterOptions().setHasText(project))
                    .click();
                test.info("Selected project: " + project);
            } catch (Exception e) {
                // Fallback: simple text search
                page.getByText(project).click();
                test.info("Selected project using text search: " + project);
            }
            
            // Wait for selection to complete
            page.waitForTimeout(2000);
            
        } catch (Exception e) {
            test.fail("Project selection failed: " + e.getMessage());
            throw e;
        }
    }
    
    public void goToProjectDetails(ExtentTest test) {
        Locator goToDetails = page.locator(GO_TO_PROJECT_DETAILS_XPATH);
        goToDetails.waitFor(new Locator.WaitForOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
        goToDetails.click();
        test.info("Clicked 'Go to Project Details' button");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }


    public void expandAndCollapseAllPanelsInAllTabs(ExtentTest test) {
        for (String tabName : TAB_NAMES) {
            String tabXPath = String.format(TAB_XPATH_TEMPLATE, tabName);
            page.waitForSelector(tabXPath, new Page.WaitForSelectorOptions().setTimeout(60000).setState(WaitForSelectorState.VISIBLE));
            page.click(tabXPath);
            test.info("Clicked tab: " + tabName);
            log.info("Clicked tab: " + tabName);
            page.waitForTimeout(3000);
            page.waitForSelector(".loading", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(60000));
            int expandCount = page.locator(EXPAND_XPATH).count();
            test.info("Expand button count in tab " + tabName + ": " + expandCount);
            log.info("Expand button count in tab " + tabName + ": " + expandCount);
            for (int i = 0; i < expandCount; i++) {
                Locator expandBtn = page.locator(EXPAND_XPATH).nth(i);
                if (expandBtn.isVisible() && expandBtn.isEnabled()) {
                    expandBtn.click();
                    test.info("Expanded panel " + (i + 1) + " in tab: " + tabName);
                    log.info("Expanded panel " + (i + 1) + " in tab: " + tabName);
                    page.waitForTimeout(5000);
                } else {
                    test.info("Expand button " + (i + 1) + " not visible or not enabled in tab: " + tabName);
                    log.info("Expand button " + (i + 1) + " not visible or not enabled in tab: " + tabName);
                }
            }
            page.waitForTimeout(2000);
            page.waitForSelector(".loading", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(60000));
            int collapseCount = page.locator(COLLAPSE_XPATH).count();
            for (int i = 0; i < collapseCount; i++) {
                Locator collapseBtn = page.locator(COLLAPSE_XPATH).nth(i);
                if (collapseBtn.isVisible() && collapseBtn.isEnabled()) {
                    collapseBtn.click();
                    test.info("Collapsed panel " + (i + 1) + " in tab: " + tabName);
                    log.info("Collapsed panel " + (i + 1) + " in tab: " + tabName);
                    page.waitForTimeout(1000);
                } else {
                    test.info("Collapse button " + (i + 1) + " not visible or not enabled in tab: " + tabName);
                    log.info("Collapse button " + (i + 1) + " not visible or not enabled in tab: " + tabName);
                }
            }
            page.waitForTimeout(2000);
        }
        test.info("Successfully expanded and collapsed all panels in all tabs.");
        log.info("Successfully expanded and collapsed all panels in all tabs.");
    }
} 