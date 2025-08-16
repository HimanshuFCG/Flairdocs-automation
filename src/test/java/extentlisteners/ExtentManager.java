package extentlisteners;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class ExtentManager {

    private static volatile ExtentReports extent;

    // Private constructor to prevent anyone else from instantiating
    private ExtentManager() {}

    /**
     * Gets the thread-safe, singleton instance of ExtentReports.
     * Initializes it if it's null.
     */
    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (ExtentManager.class) {
                if (extent == null) {
                    // Create the file name and directory for the report
                    Date d = new Date();
                    String fileName = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
                    Path reportsDir = Paths.get(System.getProperty("user.dir"), "reports");

                    try {
                        if (!Files.exists(reportsDir)) {
                            Files.createDirectories(reportsDir);
                        }
                    } catch (IOException e) {
                        e.printStackTrace(); // Consider using a logger here
                    }

                    String reportPath = reportsDir.resolve(fileName).toString();
                    ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

                    // Configure the report
                    sparkReporter.config().setTheme(Theme.STANDARD);
                    sparkReporter.config().setDocumentTitle("Automation Test Report");
                    sparkReporter.config().setEncoding("utf-8");
                    sparkReporter.config().setReportName("FlairDocs Automation Results");
                    sparkReporter.config().setTimelineEnabled(true);

                    // Create the ExtentReports instance
                    extent = new ExtentReports();
                    extent.attachReporter(sparkReporter);

                    // Add system information
                    extent.setSystemInfo("Tester", "Himanshu Batham");
                    extent.setSystemInfo("Project", "CADOTV2");
                    extent.setSystemInfo("Test Type", "Smoke Test");
                    extent.setSystemInfo("OS", System.getProperty("os.name"));
                    extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                }
            }
        }
        return extent;
    }

    /**
     * Flushes the report to write all logs to the HTML file.
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
