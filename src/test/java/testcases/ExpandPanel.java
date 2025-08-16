package testcases;

import base.BaseTest;
import config.ConfigReader;
import org.testng.annotations.Test;
import pages.ExpandPanelPage;
import com.aventstack.extentreports.ExtentTest;

public class ExpandPanel extends BaseTest {

    @Test
    public void PanelExpansion() {
        try {
            login(); // Login handled in BaseTest
            ExtentTest extentTest = getExtentTest();
            safeExtentPass("Login completed");

            String domain = ConfigReader.get("domain");
            String project = ConfigReader.get("project");

            ExpandPanelPage expandPanelPage = new ExpandPanelPage(page);
            safeExtentPass("ExpandPanelPage initialized");

            expandPanelPage.selectDomain(domain, extentTest);
            safeExtentPass("Domain selected: " + domain);

            expandPanelPage.selectProject(project, extentTest);
            safeExtentPass("Project selected: " + project);

            expandPanelPage.goToProjectDetails(extentTest);
            safeExtentPass("Navigated to project details");

            expandPanelPage.expandPanel(extentTest);
            safeExtentPass("Panel expanded");

            expandPanelPage.collapsePanel(extentTest);
            safeExtentPass("Panel collapsed");

        } catch (Exception e) {
            
            safeExtentFail("Test failed: " + e.getMessage());

            // Take screenshot on failure
            
            throw new RuntimeException("Test failed due to exception: ", e);
        }
    }
}



