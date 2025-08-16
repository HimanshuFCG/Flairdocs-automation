package utilities.screenshotUtils;

import com.microsoft.playwright.Page;
import java.nio.file.Paths;

public class ScreenshotUtils {
    public static String takeScreenshot(Page page, String testName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String screenshotPath = Paths.get("src", "test", "resources", "screenshots", testName + "_" + timestamp + ".png").toString();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
        return screenshotPath;
    }
} 