package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CreateNewProjectPage {
    private final Page page;

    // --- Centralized Locators ---
    private static final String DOMAIN_DROPDOWN_XPATH = "//span[@class='rtbText' and text()='Domain:']";
    private static final String PROJECT_DROPDOWN_INPUT = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Input";
    private static final String PROJECT_LIST_ITEM = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_listbox li.rcbItem";
    private static final String CREATE_NEW_PROJECT_BUTTON = "#ctl00_Main_ProjectSnapShotDetails_btnCreateNewProject";
    private static final String IFRAME_SELECTOR = "iframe[name='CreateProjectwindow']";
    private static final String POPUP_CLOSE_BUTTON_SELECTOR = "button[title='Close']";

    public CreateNewProjectPage(Page page) {
        this.page = page;
    }

    public void verifyPopupOpensAndCloses(ExtentTest test) {
        clickCreateNewProject(test);

        // Step 2: Verify the popup is visible
        test.info("Verifying create new project popup is visible...");
        Locator iframeLocator = page.locator(IFRAME_SELECTOR);
        iframeLocator.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
        test.info("Popup is visible as expected.");

        // Step 3: Close the popup
        test.info("Closing the create new project popup...");
        page.locator(POPUP_CLOSE_BUTTON_SELECTOR).click();
        test.info("Clicked the popup's close button.");

        // Step 4: Verify the popup is hidden
        test.info("Verifying popup is now closed...");
        iframeLocator.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.HIDDEN));
        test.info("Popup is closed as expected.");
    }
    
    public void selectDomain(String domain, ExtentTest test) {
        Locator domainDropdown = page.locator(DOMAIN_DROPDOWN_XPATH);
        domainDropdown.waitFor(new Locator.WaitForOptions().setTimeout(15000).setState(WaitForSelectorState.VISIBLE));
        domainDropdown.click();
        test.info("Clicked 'Domain:' dropdown");

        String domainOptionXpath = String.format("//span[@class='rtbText' and text()='%s']", domain);
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
    
    private void clickCreateNewProject(ExtentTest test) {
        page.waitForSelector(CREATE_NEW_PROJECT_BUTTON, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.click(CREATE_NEW_PROJECT_BUTTON);
        test.info("Clicked 'Create New Project' button");
    }
}
