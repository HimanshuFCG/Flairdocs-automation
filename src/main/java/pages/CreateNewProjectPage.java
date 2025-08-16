package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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
        page.waitForTimeout(5000); 
    }
    
    private void clickCreateNewProject(ExtentTest test) {
        page.waitForSelector(CREATE_NEW_PROJECT_BUTTON, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.click(CREATE_NEW_PROJECT_BUTTON);
        test.info("Clicked 'Create New Project' button");
    }
}
