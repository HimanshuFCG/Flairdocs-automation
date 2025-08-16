    package utilities;

    import com.microsoft.playwright.Page;
    import com.aventstack.extentreports.ExtentTest;
    import com.microsoft.playwright.Locator;
    import org.apache.log4j.Logger;
    import com.microsoft.playwright.options.WaitForSelectorState;

    public class PanelActions {
        
            private final Page page;
            private static final Logger log = Logger.getLogger(PanelActions.class);
        
            public PanelActions(Page page) {
                this.page = page;
            }
        
            public void openPanel(String panelTitle, ExtentTest extentTest) {
                try {
                    if ("Project Information".equals(panelTitle)) {
                        // Click the unique expand image icon for Project Information
                        Locator expandBtn = page.locator("#ctl00_Main_DynamicContent1_projinfo_AcqProjectProfile_ColProjectprofile____img");
                        expandBtn.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                        expandBtn.scrollIntoViewIfNeeded();
                        expandBtn.click(new Locator.ClickOptions().setForce(true));

                        // Wait until the outer div loses 'panelclosed' class
                        Locator panel = page.locator("#ctl00_Main_DynamicContent1_projinfo_AcqProjectProfile_ColProjectprofile____title");
                        page.waitForFunction(
                            "el => !el.className.includes('panelclosed')",
                            panel.first(),
                            new Page.WaitForFunctionOptions().setTimeout(20000)
                        );

                        // Wait for loader to disappear
                        page.waitForSelector(".loading", 
                            new com.microsoft.playwright.Page.WaitForSelectorOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN).setTimeout(20000));
                        // Slow down between panels
                        page.waitForTimeout(2000);
                        log.info("Successfully opened Project Information");
                    } else {
                        Locator panel = page.locator("div.collapsible-panel-main-open-close.panelclosed", 
                            new com.microsoft.playwright.Page.LocatorOptions().setHasText(panelTitle));
                        panel.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                        panel.scrollIntoViewIfNeeded();
                        panel.click(new Locator.ClickOptions().setForce(true));

                        // Wait until panel opens
                        page.waitForFunction(
                            "el => !el.className.includes('panelclosed')",
                            panel.first(),
                            new Page.WaitForFunctionOptions().setTimeout(20000)
                        );

                        page.waitForSelector(".loading", 
                            new com.microsoft.playwright.Page.WaitForSelectorOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN).setTimeout(20000));
                        page.waitForTimeout(2000);
                        log.info("Successfully opened panel: " + panelTitle);
                    }
                } catch (Exception e) {
                    log.warn("Could not open panel '" + panelTitle + "' - " + e.getMessage());
                }
            }
            
            public void closePanel(String panelTitle, ExtentTest extentTest) {
                try {
                    if ("Project Information".equals(panelTitle)) {
                        Locator collapseBtn = page.locator("#ctl00_Main_DynamicContent1_projinfo_AcqProjectProfile_ColProjectprofile____img");
                        collapseBtn.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                        collapseBtn.scrollIntoViewIfNeeded();
                        collapseBtn.click(new Locator.ClickOptions().setForce(true));

                        Locator panel = page.locator("#ctl00_Main_DynamicContent1_projinfo_AcqProjectProfile_ColProjectprofile____title");
                        page.waitForFunction(
                            "el => el.className.includes('panelclosed')",
                            panel.first(),
                            new Page.WaitForFunctionOptions().setTimeout(20000)
                        );
                        page.waitForTimeout(1000);
                        log.info("Successfully closed Project Information");
                    } else {
                        Locator panel = page.locator("div.collapsible-panel-main-open-close:not(.panelclosed)",
                            new com.microsoft.playwright.Page.LocatorOptions().setHasText(panelTitle));
                        panel.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                        panel.scrollIntoViewIfNeeded();
                        panel.click(new Locator.ClickOptions().setForce(true));

                        page.waitForFunction(
                            "el => el.className.includes('panelclosed')",
                            panel.first(),
                            new Page.WaitForFunctionOptions().setTimeout(20000)
                        );
                        page.waitForTimeout(1000);
                        log.info("Successfully closed panel: " + panelTitle);
                    }
                } catch (Exception e) {
                    log.warn("Could not close panel '" + panelTitle + "' - " + e.getMessage());
                }
            }

            public boolean isPanelOpen(String panelTitle, ExtentTest extentTest) {
                try {
                    boolean isOpen = false;
                    
                    if ("Project Information".equals(panelTitle)) {
                        isOpen = page.locator("#ctl00_Main_DynamicContent1_projinfo_AcqProjectProfile_ColProjectprofile____img")
                                     .isVisible();
                    } else if ("Project Status Log".equals(panelTitle)) {
                        isOpen = page.locator("#ctl00_Main_DynamicContent1_ProjectStatusLog_ColProjectStatusLog____img")
                                     .isVisible();
                    }
                    // Add more panel title conditions as needed
                    
                    String logMessage = "Panel '" + panelTitle + "' is " + (isOpen ? "open" : "closed");
                    if (extentTest != null) {
                        extentTest.info(logMessage);
                    }
                    log.info(logMessage);
                    
                    return isOpen;
                } catch (Exception e) {
                    String errorMsg = "Error checking if panel '" + panelTitle + "' is open: " + e.getMessage();
                    log.error(errorMsg, e);
                    if (extentTest != null) {
                        extentTest.fail(errorMsg);
                    }
                    return false;
                }
            }
            
            public boolean isPanelClosed(String panelTitle, ExtentTest extentTest) {
                try {
                    return !isPanelOpen(panelTitle, extentTest);
                } catch (Exception e) {
                    String errorMsg = "Error checking if panel '" + panelTitle + "' is closed: " + e.getMessage();
                    log.error(errorMsg, e);
                    if (extentTest != null) {
                        extentTest.fail(errorMsg);
                    }
                    return false;
                }
            }
        }
