package testcases;

import base.BaseTest;
import config.ConfigReader;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentTest;
import utilities.PanelActions;
import pages.CompleteLoginAndPanelPage;
import org.apache.log4j.Logger;


public class CompleteLoginAndPanelTest extends BaseTest {
    private static final Logger log = Logger.getLogger(CompleteLoginAndPanelTest.class);

    @Test
    public void loginAndOpenClosePanels() {
        try {
            login();
            ExtentTest extentTest = getExtentTest();  // Get the ExtentTest instance
            extentTest.info("Logged in using BaseTest.login()");
            log.info("Logged in using BaseTest.login()");

            String domain = ConfigReader.get("domain");
            String project = ConfigReader.get("project");

            CompleteLoginAndPanelPage completePage = new CompleteLoginAndPanelPage(page);
            extentTest.info("Page object created");
            log.info("Page object created");

            completePage.selectDomain(domain, extentTest);
            extentTest.info("Domain selected");
            log.info("Domain selected");
            completePage.selectProject(project, extentTest);
            extentTest.info("Project selected");
            log.info("Project selected");
            completePage.goToProjectDetails(extentTest);
            extentTest.info("Navigated to project details");
            log.info("Navigated to project details");

            String[] panelTitles = {
                "Project Information", "Project Status Log", "Right of Way Maps", "External Agreement","Authorization Summary","Sales Book","Certification","Import","All Project Information Documents"
            };
            PanelActions panelActions = new PanelActions(page);
            for (String panelTitle : panelTitles) {
                try {
                    extentTest.info("Opening panel: " + panelTitle);
                    log.info("Opening panel: " + panelTitle);
                    panelActions.openPanel(panelTitle, getExtentTest());
                    extentTest.info("Opened panel: " + panelTitle);
                    log.info("Opened panel: " + panelTitle);
                    panelActions.closePanel(panelTitle, getExtentTest());
                    extentTest.info("Closed panel: " + panelTitle);
                    log.info("Closed panel: " + panelTitle);
                } catch (Exception e) {
                    extentTest.fail("Error handling panel: " + panelTitle + " - " + e.getMessage());
                    log.error("Error handling panel: " + panelTitle + " - " + e.getMessage());
                }
            }
            extentTest.pass("Panels opened and closed successfully.");
            log.info("Panels opened and closed successfully.");
        } catch (Exception e) {
            safeExtentFail("Test failed: " + e.getMessage());
            log.error("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
