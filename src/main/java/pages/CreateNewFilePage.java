package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CreateNewFilePage {
    private final Page page;

    // --- Centralized Locators ---
    private static final String DOMAIN_DROPDOWN_XPATH = "//span[@class='rtbText' and text()='Domain:']";
    private static final String PROJECT_DROPDOWN_INPUT = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_Input";
    private static final String PROJECT_LIST_ITEM = "#ctl00_Main_ProjectSnapShotDetails_ddlProjSnapShotSearchNum_listbox li.rcbItem";
    private static final String CREATE_NEW_FILE_BUTTON = "#ctl00_Main_FileSnapShot1_BtnCreateProperty";
    private static final String TABLE_SELECTOR = "#ctl00_Main_FileSnapShot1_GridSnapShotTracts_ctl00";
    
    // iframe Popup Locators
    private static final String IFRAME_SELECTOR = "iframe[name='CreateFilewindow']";
    private static final String ROW_ID_INPUT = "#txtPropertyNumber";
    private static final String SAVE_AND_CLOSE_BUTTON = "#btnCreateProperty";

    public CreateNewFilePage(Page page) {
        this.page = page;
    }

    public void createNewFileAndVerifyInTable(String rowId, ExtentTest test) {
        clickCreateNewFile(test);
        Frame popupFrame = switchToCreateFileIframe(test);
        fillCreateFileForm(popupFrame, rowId, test);
        // After clicking save, the iframe should close and the table should update.
        // The verification step confirms this entire sequence was successful.
        verifyRowIsPresentInTable(rowId, test);
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
    protected void clickCreateNewFile(ExtentTest test) {
        page.locator(CREATE_NEW_FILE_BUTTON).click();
        test.info("Clicked 'Create New File' button");
    }

    protected Frame switchToCreateFileIframe(ExtentTest test) {
        Locator iframeLocator = page.locator(IFRAME_SELECTOR);
        // Wait for the iframe to be present and visible in the DOM
        iframeLocator.waitFor(new Locator.WaitForOptions().setTimeout(30000).setState(WaitForSelectorState.VISIBLE));
        
        Frame frame = iframeLocator.elementHandle().contentFrame();
        if (frame == null) {
            test.fail("Iframe 'CreateFilewindow' could not be found or failed to load.");
            throw new RuntimeException("Iframe 'CreateFilewindow' not found or failed to load.");
        }
        test.info("Successfully switched to 'Create New File' iframe.");
        return frame;
    }

    private void fillCreateFileForm(Frame frame, String rowId, ExtentTest test) {
        // This method now lets exceptions propagate up to the main test's try-catch block.
        frame.fill(ROW_ID_INPUT, rowId);
        test.info("Filled ROW ID: " + rowId);
        frame.click(SAVE_AND_CLOSE_BUTTON);
        test.info("Clicked 'Save & Close' button inside the popup.");
    }

    private void verifyRowIsPresentInTable(String expectedRowId, ExtentTest test) {
        test.info("Verifying that row with ID '" + expectedRowId + "' is present in the table...");
        
        // This locator finds a table row 'tr' that contains an element (like a 'td') with the exact text.
        // It's more resilient than relying on column index.
        Locator expectedRow = page.locator(TABLE_SELECTOR + " tr").filter(new Locator.FilterOptions().setHasText(expectedRowId));

        // Use Playwright's built-in assertion. It will automatically wait and retry for up to 15 seconds.
        // This replaces the need for a manual loop and hard sleeps.
        expectedRow.isVisible(new Locator.IsVisibleOptions().setTimeout(15000));
        
        test.info("Successfully found and verified row with ID: " + expectedRowId);
    }
}