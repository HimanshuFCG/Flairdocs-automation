package pages;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class AdminPage {
    private final Page page;

    // Centralized locators
    public static final String ADMIN_BUTTON = "//span[@class='rmText rmExpandDown' and text()='Admin']";
    public static final String CLOSE_ICON = "//a[@class='rwCloseButton' and @title='Close']";

    public AdminPage(Page page) {
         this.page = page; }

    public boolean isAdminMenuVisible() {
        return page.isVisible(ADMIN_BUTTON);
    }

    /**
     * Returns the text of the Admin menu button (should be 'Admin').
     */
    public String getAdminMenuTitle() {
        if (page.isVisible(ADMIN_BUTTON)) {
            return page.textContent(ADMIN_BUTTON).trim();
        }
        return null;
    }

    public void clickAdminMenu(ExtentTest test) {
        page.waitForSelector(ADMIN_BUTTON);
        page.click(ADMIN_BUTTON);
        if (test != null) {
            test.info("Clicked Admin button");
        }
    }

    public void clickAdminItem(String itemSelector, ExtentTest test) {
        page.waitForSelector(itemSelector, new Page.WaitForSelectorOptions().setTimeout(20000).setState(WaitForSelectorState.VISIBLE));
        page.click(itemSelector);
        if (test != null) {
            test.info("Clicked admin menu item: " + itemSelector);
        }
    }

    public void closePopup(ExtentTest test) {
        page.waitForTimeout(2000);
        page.waitForSelector(CLOSE_ICON, new Page.WaitForSelectorOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
        page.click(CLOSE_ICON);
        page.waitForTimeout(1000);
        page.waitForSelector(CLOSE_ICON, new Page.WaitForSelectorOptions().setTimeout(10000).setState(WaitForSelectorState.HIDDEN));
        if (test != null) {
            test.info("Closed popup");
        }
    }

    public void clickAdminAgain(ExtentTest test) {
        page.waitForTimeout(2000);
        page.waitForSelector(ADMIN_BUTTON, new Page.WaitForSelectorOptions().setTimeout(10000).setState(WaitForSelectorState.VISIBLE));
        page.click(ADMIN_BUTTON);
        if (test != null) {
            test.info("Clicked Admin button again to return to menu");
        }
    }

    public void handleAdminItem(String itemSelector, String itemType, ExtentTest test) {
        clickAdminMenu(test);
        clickAdminItem(itemSelector, test);
        if ("popup".equals(itemType)) {
            closePopup(test);
        } else if ("navigation".equals(itemType)) {
            clickAdminAgain(test);
        }
    }
} 