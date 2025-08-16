package testcases;
import org.testng.annotations.Test;
import base.BaseTest;
import org.apache.log4j.Logger;
import pages.CreateNewFilePage;
import com.microsoft.playwright.Frame;
import config.ConfigReader;
import com.aventstack.extentreports.ExtentTest;

public class CreateNewFile extends BaseTest {
    private static final Logger log = Logger.getLogger(CreateNewFile.class);

    @Test
    public void loginAndOpenClosePanels() {
        try {
            // Use BaseTest's login and page
            login();
            ExtentTest extentTest = getExtentTest();
            
                log.info("Starting create new file test");
                safeExtentLog("Starting create new file test");
                
                // Get configuration and test data
                String domain = ConfigReader.get("domain2");
                String project = ConfigReader.get("project2");
                String newRowId = "TEST_ID_" + System.currentTimeMillis(); // Generate a unique ID for the test run
                
                // Initialize page object
                CreateNewFilePage createNewFilePage = new CreateNewFilePage(page);
                
                // --- Test Steps ---
                // 1. Navigate to the correct context
                createNewFilePage.selectDomain(domain, getExtentTest());
                createNewFilePage.selectProject(project, getExtentTest());
                
                // 2. Perform the entire create-and-verify workflow with a single call
                createNewFilePage.createNewFileAndVerifyInTable(newRowId, getExtentTest());
        
                // 3. Mark test as passed
                safeExtentPass("Successfully created a new file and verified its presence in the table.");
                
            } finally {
                log.info("Create new file test completed");
            }
        }
}
