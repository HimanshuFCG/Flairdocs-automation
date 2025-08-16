package testcases;

import base.BaseTest;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import pages.ExpandAllPanelsInAllTabsPage;
import com.aventstack.extentreports.ExtentTest;

public class ExpandAllPanelsInAllTabsTest extends BaseTest {
    private static final Logger log = Logger.getLogger(ExpandAllPanelsInAllTabsTest.class);
    private ExpandAllPanelsInAllTabsPage expandPage;

    @Test
    public void expandAndCollapseAllPanelsInAllTabs() {
        try {
            login(); // Use login from BaseTest
            ExtentTest extentTest = getExtentTest();
            extentTest.info("Logged in"); 
            log.info("Logged in");

            String domain = config.ConfigReader.get("domain");
            String project = config.ConfigReader.get("project");
            expandPage = new ExpandAllPanelsInAllTabsPage(page); // page from BaseTest
            extentTest.info("Page object created");
            log.info("Page object created");

            expandPage.selectDomain(domain, extentTest);
            extentTest.info("Domain selected");
            log.info("Domain selected");
            expandPage.selectProject(project, extentTest);
            extentTest.info("Project selected");
            log.info("Project selected");
            expandPage.goToProjectDetails(extentTest);
            extentTest.info("Navigated to project details");
            log.info("Navigated to project details");

            expandPage.expandAndCollapseAllPanelsInAllTabs(extentTest);
            extentTest.pass("Successfully expanded and collapsed all panels in all tabs.");
            log.info("Successfully expanded and collapsed all panels in all tabs.");
        } catch (Exception e) {
            safeExtentFail("Test failed: " + e.getMessage());
            log.error("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 