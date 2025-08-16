package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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
        page.click(PROJECT_DROPDOWN_INPUT);
        test.info("Clicked project dropdown input");

        page.waitForSelector(PROJECT_LIST_ITEM, new Page.WaitForSelectorOptions().setTimeout(60000).setState(WaitForSelectorState.VISIBLE));
        Locator item = page.locator(PROJECT_LIST_ITEM, new Page.LocatorOptions().setHasText(project));
        
        if (item.count() == 0) {
            String errorMsg = "No project found with name: " + project;
            test.fail(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        item.first().click();
        test.info("Selected project: " + project);
        
        page.waitForSelector(".loading-spinner", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
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