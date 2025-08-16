package utilities;

import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.*;
import java.nio.file.Paths;

public class VideoUtils {

    
    public static void attachGoogleDriveVideo(ExtentTest test, String googleDriveFileId) {
        String videoUrl = "https://drive.google.com/uc?export=download&id=" + googleDriveFileId;
        test.info("Test Video: <a href='" + videoUrl + "' target='_blank'>Watch Video</a>");
        test.info("<video width='600' controls><source src='" + videoUrl + "' type='video/webm'></video>");
    }
}
