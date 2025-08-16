package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
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

    public void selectProject2(String project2, ExtentTest test) {
        try {
            page.click(PROJECT_DROPDOWN_ARROW);
            page.waitForSelector(PROJECT_LIST_ITEM, new Page.WaitForSelectorOptions().setTimeout(60000).setState(WaitForSelectorState.VISIBLE));
            Locator item = page.locator(PROJECT_LIST_ITEM, new Page.LocatorOptions().setHasText(project2));
            int count = item.count();
            if (count == 0) {
                test.fail("No project found with name: " + project2);
                throw new RuntimeException("No project found with name: " + project2);
            } else if (count > 1) {
                test.warning("Multiple projects found with name: " + project2 + ". Clicking the first one.");
            }
            item.waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.VISIBLE));
            page.waitForTimeout(500);
            item.first().click();
            test.info("Selected project: " + project2);
            log.info("Selected project: " + project2);
            page.waitForTimeout(5000);
        } catch (Exception e) {
            test.fail("Failed to select project2: " + e.getMessage());
            log.error("Failed to select project2: " + e.getMessage());
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