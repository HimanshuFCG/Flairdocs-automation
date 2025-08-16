package testcases;

import base.BaseTest;
import config.ConfigReader;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import pages.CompleteLoginPage;

public class CompleteLoginTest extends BaseTest {
    @Test
    public void completeLoginAndTabSelection() {
        try {
            // Login and ExtentTest setup are handled by BaseTest
            login();
            ExtentTest extentTest = getExtentTest();
            String domain = ConfigReader.get("domain");
            String project = ConfigReader.get("project");

            CompleteLoginPage completeLoginPage = new CompleteLoginPage(page);
            completeLoginPage.selectDomain(domain, extentTest);
            completeLoginPage.selectProject(project, extentTest);
            completeLoginPage.goToProjectDetails(extentTest);

            // Click through the required tabs
            String[] tabNames = {
                "Assignments",
                "Liaison Files",
                "Estimates",
                "Contracting",
                "Authorization Management",
                "Issue Management",
                "Financials",
                    "Expropriation",
                "Checklist",
                "Utility Coordination",
                    "Railroad Coordination"

            };
            for (String tab : tabNames) {
                completeLoginPage.clickTab(tab, extentTest);
            }
            extentTest.pass("Successfully completed login and tab selection flow.");
        } catch (Exception e) {
            safeExtentLog("Test failed: " + e.getMessage());
            safeExtentFail("Test failed: " + e.getMessage());
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 