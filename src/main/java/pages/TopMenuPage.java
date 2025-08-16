package pages;

import com.microsoft.playwright.Page;
import org.apache.log4j.Logger;

public class TopMenuPage {
    private final Page page;
   
    // Locators for top menu items (using accesskey for robustness)
    private static final String SEARCH_MENU = "a[accesskey='S']";
    private static final String WORK_QUEUE_MENU = "a[accesskey='W']";
    private static final String MY_PROFILE_MENU = "a[accesskey='M']";
    private static final String NOTIFICATIONS_MENU = "a[accesskey='N']";
    private static final String DOC_MGMT_MENU = "a[accesskey='o']";
    private static final String PM_INQUIRY_MENU = "a[accesskey='I']";
    private static final String GIS_MENU = "a[accesskey='G']";
    private static final String DASHBOARD_MENU = "a[accesskey='D']";
    // Dashboard is not in the provided HTML, so not included for now

    public TopMenuPage(Page page) {
        this.page = page;
    }

    public void clickSearch() {
        page.click(SEARCH_MENU);
        page.waitForLoadState();
        page.waitForTimeout(5000);
    }

    public void clickMyWorkQueue() {
        page.click(WORK_QUEUE_MENU);
        page.waitForLoadState();
        page.waitForTimeout(5000);
    }

    public void clickMyProfile() {
        page.click(MY_PROFILE_MENU);
        page.waitForLoadState();
        page.waitForTimeout(5000);
    }
    public void clickPMInquiry() {
        page.click(PM_INQUIRY_MENU);
        page.waitForLoadState();
        page.waitForTimeout(1000);
    }

    public void clickNotifications() {
        page.click(NOTIFICATIONS_MENU);
        page.waitForLoadState();
        page.waitForTimeout(5000);
    }

    public void clickDocMgmt() {
        page.click(DOC_MGMT_MENU);
        page.waitForLoadState();
        page.waitForTimeout(1000);
    }

     public void clickGIS() {
        page.click(GIS_MENU);
        page.waitForLoadState();
        page.waitForTimeout(1000);
    }

    public void clickDashboard() {
        page.click(DASHBOARD_MENU);
        page.waitForLoadState();
        page.waitForTimeout(5000);
    }
} 