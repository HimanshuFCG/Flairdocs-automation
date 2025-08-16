package testcases;

import base.BaseTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import org.apache.log4j.Logger;
import pages.AdminPage;

public class Admin extends BaseTest {
    private static final Logger log = Logger.getLogger(Admin.class);

    // Centralized selectors for admin items and their types
    private static final String[][] adminItems = {
        {"//a[@title='Application Logs']//span[@class='rmText' and text()='Application Logs']", "popup"},
        {"//a[@title='Application Roles and Work Group']//span[@class='rmText' and text()='Application Roles and Work Groups']", "navigation"},
        {"//a[@title='User Management']//span[@class='rmText' and text()='User Management']", "navigation"},
        {"//a[@title='Work Group Office Information']//span[@class='rmText' and text()='Work Group Office Information']", "navigation"},
        {"//a[@title='Contracting Company Details']//span[@class='rmText' and text()='Contracting Company Details']", "navigation"},
        {"//a[@title='Contracting Price Agreements']//span[@class='rmText' and text()='Contracting Price Agreements']", "navigation"},
        {"//a[@title='Dropdowns']//span[@class='rmText' and text()='Dropdowns']", "navigation"},
        {"//a[@title='Configure Distribution Lists']//span[@class='rmText' and text()='Configure Distribution Lists']", "navigation"},
        {"//a[@title='Estimation Cost Factors']//span[@class='rmText' and text()='Estimation Cost Factors']", "navigation"},
        {"//a[@title='Expected Work Duration']//span[@class='rmText' and text()='Expected Work Duration']", "navigation"},
        {"//a[@title='Delete Projects and Files']//span[@class='rmText' and text()='Delete Project/File']", "navigation"},
        {"//a[@title='Move File Panel']//span[@class='rmText' and text()='Move File Panel']", "navigation"},
        {"//a[@title='Canned Report']//span[@class='rmText' and text()='Canned Report']", "navigation"},
        {"//a[@title='Bulk Upload Documents']//span[@class='rmText' and text()='Bulk Upload Documents']", "navigation"},
        {"//a[@title='Bulk Generate Documents']//span[@class='rmText' and text()='Bulk Generate Documents']", "navigation"},
        {"//a[@title='Document Metadata Attributes']//span[@class='rmText' and text()='Document Metadata Attributes']", "navigation"},
        {"//a[@title='Document Packages']//span[@class='rmText' and text()='Document Packages']", "navigation"},
        {"//a[@title='Document Type Configuration']//span[@class='rmText' and text()='Document Type Configuration']", "navigation"},
        {"//a[@title='Template and Clause Maintenance']//span[@class='rmText' and text()='Template and Clause Maintenance']", "navigation"},
        {"//a[@title='Civil Certification / Board Approval']//span[@class='rmText' and text()='Civil Certification / Board Approval']", "navigation"},
        {"//a[@title='Production Plans']//span[@class='rmText' and text()='Production Plans']", "navigation"},
        {"//a[@title='QA Data']//span[@class='rmText' and text()='QA Data']", "navigation"},
        {"//a[@title='Help Content']//span[@class='rmText' and text()='Help Content']", "navigation"},
        {"//a[@title='FlairBOT Library']//span[@class='rmText' and text()='FlairBOT Library']", "navigation"},
        {"//a[@title='Workflow Activity']//span[@class='rmText' and text()='Workflow Activity']", "navigation"},
        {"//a[@title='Notifications Configuration']//span[@class='rmText' and text()='Notifications Configuration']", "navigation"},
        {"//a[@title='Workflow Designer']//span[@class='rmText' and text()='Workflow Designer']", "navigation"},
        {"//a[@title='Environment Copy']//span[@class='rmText' and text()='Environment Copy']", "navigation"},
        {"//a[@title='Application Configuration']//span[@class='rmText' and text()='Application Configuration']", "navigation"},
        {"//a[@title='Checklist Configuration']//span[@class='rmText' and text()='Checklist Configuration']", "navigation"}
    };

    @Test
    public void adminMenuNavigation() {
        try {
            login();
            AdminPage adminPage = new AdminPage(page);
            ExtentTest extentTest = getExtentTest();  // Get the ExtentTest instance
            for (int i = 0; i < adminItems.length; i++) {
                String itemSelector = adminItems[i][0];
                String itemType = adminItems[i][1];
                try {
                    adminPage.handleAdminItem(itemSelector, itemType, extentTest);
                    safeExtentPass("Handled admin item: " + itemSelector);
                    log.info("Handled admin item: " + itemSelector);
                } catch (Exception e) {
                    safeExtentFail("Failed to handle admin item: " + itemSelector + ". Exception: " + e.getMessage());
                    log.error("Failed to handle admin item: " + itemSelector, e);
                }
            }
            safeExtentPass("Successfully navigated all admin menu items.");
        } catch (Exception e) {
            log.error("Test failed: " + e.getMessage());
            safeExtentFail("Test failed: " + e.getMessage());
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
