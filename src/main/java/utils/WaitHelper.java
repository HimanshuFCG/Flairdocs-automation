package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.log4j.Logger;

public class WaitHelper {
    private static final Logger log = Logger.getLogger(WaitHelper.class);

    /**
     * Waits for a selector to be visible and enabled, returns the Locator.
     * Throws RuntimeException if not found or not enabled within timeout.
     */
    public static Locator waitForVisibleAndEnabled(Page page, String selector, int timeoutMs) {
        log.info("Waiting for selector to be visible: " + selector);
        try {
            page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setTimeout(timeoutMs)
                .setState(WaitForSelectorState.VISIBLE));
            Locator locator = page.locator(selector);
            if (!locator.isEnabled()) {
                log.error("Selector is visible but not enabled: " + selector);
                throw new RuntimeException("Selector is visible but not enabled: " + selector);
            }
            log.info("Selector is visible and enabled: " + selector);
            return locator;
        } catch (Exception e) {
            log.error("Failed to find visible and enabled selector: " + selector + ", error: " + e.getMessage());
            throw new RuntimeException("Failed to find visible and enabled selector: " + selector + ", error: " + e.getMessage());
        }
    }

    /**
     * Waits for a selector to be hidden (e.g., overlays/loaders).
     */
    public static void waitForHidden(Page page, String selector, int timeoutMs) {
        log.info("Waiting for selector to be hidden: " + selector);
        try {
            page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setTimeout(timeoutMs)
                .setState(WaitForSelectorState.HIDDEN));
            log.info("Selector is now hidden: " + selector);
        } catch (Exception e) {
            log.warn("Selector did not become hidden within timeout: " + selector + ", error: " + e.getMessage());
        }
    }

    /**
     * Waits for the page to reach network idle state after navigation or action.
     */
    public static void waitForNetworkIdle(Page page, int timeoutMs) {
        log.info("Waiting for network idle state");
        try {
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(timeoutMs));
            log.info("Page reached network idle state");
        } catch (Exception e) {
            log.warn("Page did not reach network idle state within timeout: " + e.getMessage());
        }
    }
}
