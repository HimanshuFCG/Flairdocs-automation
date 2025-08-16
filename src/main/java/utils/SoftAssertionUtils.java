package utils;

import com.aventstack.extentreports.ExtentTest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for soft assertions with ExtentReports and Log4j integration.
 * Usage: Create one instance per test method, call assertTrue/assertEquals/etc, then assertAll() at the end.
 */
public class SoftAssertionUtils {
    private final List<String> failureMessages = new ArrayList<>();
    private final ExtentTest extentTest;
    private final Logger logger;
    private int passCount = 0;

    public SoftAssertionUtils(ExtentTest extentTest, Logger logger) {
        this.extentTest = extentTest;
        this.logger = logger;
    }

    public void assertTrue(boolean condition, String message) {
        if (condition) {
            extentTest.pass("PASS: " + message);
            logger.info("PASS: " + message);
            passCount++;
        } else {
            extentTest.fail("FAIL: " + message);
            logger.error("FAIL: " + message);
            failureMessages.add("FAIL: " + message);
        }
    }

    public void assertEquals(Object actual, Object expected, String message) {
        if (actual == null ? expected == null : actual.equals(expected)) {
            extentTest.pass("PASS: " + message + " | Actual: " + actual + ", Expected: " + expected);
            logger.info("PASS: " + message + " | Actual: " + actual + ", Expected: " + expected);
            passCount++;
        } else {
            extentTest.fail("FAIL: " + message + " | Actual: " + actual + ", Expected: " + expected);
            logger.error("FAIL: " + message + " | Actual: " + actual + ", Expected: " + expected);
            failureMessages.add("FAIL: " + message + " | Actual: " + actual + ", Expected: " + expected);
        }
    }

    public void assertAll() {
        if (!failureMessages.isEmpty()) {
            String allFailures = String.join("\n", failureMessages);
            extentTest.fail("Soft assertion failures:\n" + allFailures);
            logger.error("Soft assertion failures:\n" + allFailures);
            Assert.fail("Soft assertion failures:\n" + allFailures);
        } else {
            extentTest.pass("All soft assertions passed. Total passes: " + passCount);
            logger.info("All soft assertions passed. Total passes: " + passCount);
        }
    }
}
