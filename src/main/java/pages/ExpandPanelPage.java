package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
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
