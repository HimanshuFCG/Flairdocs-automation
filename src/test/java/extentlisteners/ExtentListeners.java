// File: extentlisteners/ExtentListeners.java
package extentlisteners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentListeners implements ITestListener {

    private static final Logger log = Logger.getLogger(ExtentListeners.class);
    public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    /**
     * This method is called by BaseTest to get the current ExtentTest object for the thread.
     */
    public static ExtentTest getExtentTest() {
        return testReport.get();
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Test Suite starting: " + context.getSuite().getName());
        // Initialize the report at the very beginning of the suite via the singleton
        ExtentManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
        log.info("=== Starting test: " + testName + " ===");
        ExtentReports extent = ExtentManager.getInstance();
        ExtentTest test = extent.createTest(testName);
        testReport.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        log.info("=== TEST PASSED: " + methodName + " ===");
        String logText = "<b>TEST CASE: " + methodName.toUpperCase() + " PASSED</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        if (getExtentTest() != null) {
            getExtentTest().pass(m);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        log.error("=== TEST FAILED: " + methodName + " ===", result.getThrowable());

        if (getExtentTest() != null) {
            // Log the exception
            getExtentTest().fail(result.getThrowable());

            // You can add screenshot attachment here. BaseTest's afterMethod already does this.
            // Example: getExtentTest().addScreenCaptureFromPath(ScreenshotUtils.takeScreenshot(...));

            String logText = "<b>TEST CASE: " + methodName.toUpperCase() + " FAILED</b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
            getExtentTest().log(Status.FAIL, m);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        log.warn("=== TEST SKIPPED: " + methodName + " ===");
        String logText = "<b>TEST CASE: " + methodName.toUpperCase() + " SKIPPED</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        if (getExtentTest() != null) {
            getExtentTest().skip(m);
        }
    }

    /**
     * This method is required by the ITestListener interface.
     * It is left empty as it is not needed for this implementation.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not implemented.
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test Suite finished: " + context.getSuite().getName());
        // Flush the report at the end of the suite
        if (ExtentManager.getInstance() != null) {
            ExtentManager.getInstance().flush();
        }
    }
}
