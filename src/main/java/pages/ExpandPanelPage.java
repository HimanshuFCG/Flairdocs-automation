package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

public class ExpandPanelPage {
    private final Page page;

    // Panel section locators (adjust as per your UI)
    private static final String EXPAND_BUTTON  ="#ctl00_Main_DynamicContent1_ibPlus";
    private static final String COLLAPSE_BUTTON  = "#ctl00_Main_DynamicContent1_ibMinus";

    /**
     * Returns true if the main panel section is visible.
     */
    public boolean isPanelCollapsed() {
        return page.isVisible(COLLAPSE_BUTTON );
    }

    /**
     * Returns the title text of the main panel section.
     */
    public boolean isPanelExpanded() {
        return page.isVisible(COLLAPSE_BUTTON);
    }
    public ExpandPanelPage(Page page) {
        this.page = page;
    }

    // Centralized locators
    private static final String DOMAIN_DROPDOWN_XPATH = "//span[@class='rtbText' and text()='Domain:']";
    private static final String PROJECT_DROPDOWN_INPUT = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Input";
    private static final String PROJECT_DROPDOWN_ARROW = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Arrow";
    private static final String PROJECT_LIST_ITEM = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_listbox li.rcbItem";
    private static final String GO_TO_PROJECT_DETAILS_XPATH = "//input[@id='ctl00_Main_ProjectSnapShotDetails_btnProjeSnapShotOpen']";

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
        // Try clicking the input first, fallback to arrow if needed
        try {
            page.click(PROJECT_DROPDOWN_INPUT);
            test.info("Clicked project dropdown input");
        } catch (Exception e) {
            test.warning("Failed to click input, trying arrow: " + e.getMessage());
            page.click(PROJECT_DROPDOWN_INPUT);
            test.info("Clicked project dropdown arrow");
        }
        
        // Wait for the dropdown items to be visible
        page.waitForSelector(PROJECT_LIST_ITEM, new Page.WaitForSelectorOptions().setTimeout(60000).setState(WaitForSelectorState.VISIBLE));
        Locator item = page.locator(PROJECT_LIST_ITEM, new Page.LocatorOptions().setHasText(project));
        int count = item.count();
        if (count == 0) {
            test.fail("No project found with name: " + project);
            throw new RuntimeException("No project found with name: " + project);
        } else if (count > 1) {
            test.warning("Multiple projects found with name: " + project + ". Clicking the first one.");
        }
        item.waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.VISIBLE));
        page.waitForTimeout(500);
        item.first().click();
        test.info("Selected project: " + project);
        page.waitForTimeout(5000);
    }
    public void goToProjectDetails(ExtentTest test) {
        Locator goToDetails = page.locator(GO_TO_PROJECT_DETAILS_XPATH);
        goToDetails.waitFor(new Locator.WaitForOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
        goToDetails.click();
        test.info("Clicked 'Go to Project Details' button");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void expandPanel(ExtentTest test) {
        String expandButtonXpath = "//*[@id='ctl00_Main_DynamicContent1_ibPlus']";
        Locator expandButton = page.locator(expandButtonXpath);
        expandButton.waitFor(new Locator.WaitForOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
        expandButton.click();
        test.info("Clicked 'Expand' button");

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.evaluate("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'})");
        test.info("Scrolled to bottom after expanding");
    }

    public void collapsePanel(ExtentTest test) {
        String collapseButtonXpath = "//*[@id='ctl00_Main_DynamicContent1_ibMinus']";
        Locator collapseButton = page.locator(collapseButtonXpath);
        collapseButton.waitFor(new Locator.WaitForOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
        page.waitForTimeout(1000); 
        collapseButton.click();
        test.info("Clicked 'Collapse' button");

        
        page.waitForTimeout(1200);
        page.evaluate("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'})");
        test.info("Scrolled to bottom after collapsing");

    }

   
}
