package jp.mzw.jsanalyzer.preventer.test;

import com.google.common.base.Stopwatch;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.mzw.jsanalyzer.config.Bin;
import static org.junit.Assert.assertEquals;

/**
 * Base class to provide common functionalities shared among test cases.
 */
public class WebAppTestBase {
	
    private static final String SCRIPT_TO_OBSERVE_JS_ERROR
            = "window.collectedErrors = [];"
            + "window.onerror = function(errorMessage) { "
            + "window.collectedErrors[window.collectedErrors.length] = errorMessage;"
            + "}";
    private static final String SCRIPT_TO_OBTAIN_OBSERVED_JS_ERRORS
            = "return window.collectedErrors";
    private static final String SCRIPT_TO_DISABLE_CSS
    		= "javascript:(function(){with(document)var e=getElementsByTagName('*'),s=styleSheets,i;"
    		+ "for(i=0;i<e.length;i++)e[i].removeAttribute('style');"
    		+ "for(i=0;i<s.length;i++)s[i].disabled=true})()";
    protected static final int TIMEOUT_SEC = 3;
    private static Stopwatch stopwatch;
    protected WebDriver driver;
    protected WebDriverWait wait;
    private boolean errorCollectorInstrumented = false;

    @BeforeClass
    public static void startTimer() {
        stopwatch = new Stopwatch();
        stopwatch.start();
    }

    @AfterClass
    public static void outputTime() {
        System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) / 1000.0 + " sec.");
    }

    @Before
    public void setupBrowser() throws Exception {
    	File file = new File(Bin.Firefox);
    	FirefoxBinary binary = new FirefoxBinary(file);
    	FirefoxProfile profile = new FirefoxProfile();
    	/// Firefox (ver. 10.0.2 or later)
    	/// https://code.google.com/p/selenium/wiki/FirefoxDriver#-Beta-_load_fast_preference
        profile.setPreference("webdriver.load.strategy", "unstable");
    	
        driver = new FirefoxDriver(binary, profile);
        
        wait = new WebDriverWait(driver, TIMEOUT_SEC);
    }

    @After
    public void closeBrowser() {
//        driver.close();
        driver.quit();
    }

    protected void gotoUrl(String url) {
        driver.get(url);
    }

    protected Object executeJavaScript(String script, Object... params) {
        return ((JavascriptExecutor) driver).executeScript(script, params);
    }

    protected void prepareJsErrorCollector() {
        executeJavaScript(SCRIPT_TO_OBSERVE_JS_ERROR);
        errorCollectorInstrumented = true;
    }
    
    protected void disableCssStylesheet() {
        executeJavaScript(SCRIPT_TO_DISABLE_CSS);
    }

    protected void assertNoJsErrorObserved() {
        if (!errorCollectorInstrumented) {
            throw new IllegalStateException(
                    "To capture JS error, you MUST execute prepareJsErrorCollector before calling this method.");
        }
        List<?> capturedErrors = (List<?>) executeJavaScript(SCRIPT_TO_OBTAIN_OBSERVED_JS_ERRORS);
        for(Object capturedError: capturedErrors) {
        	System.out.println(capturedError.toString());
        }
        assertEquals("Expect no JS Error", capturedErrors, Collections.emptyList());
        errorCollectorInstrumented = false;
    }
    
    /**
     * Waits for a while to the element to be appear, and then clicks it. When operating web
     * applications using WebDriver, UI on web applications do not always change immediately
     * after certain user event; there might be some time lug, thus using this method instead of
     * simple WebDriver#click is recommended.
     *
     * @param by locator to determine which element to click
     * @throws TimeoutException when the specified element don't become visible for given time.
     */
    protected void waitAndClickElementLocated(By by) throws TimeoutException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).click();
    }

    protected void waitAndSendKeysToElementLocated(By by, String keys) throws TimeoutException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).sendKeys(keys);
    }

    protected void waitAndClearKeysToElementLocated(By by) throws TimeoutException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).clear();
    }
    

	protected static void sleep(int microtime) {
        try {
            Thread.sleep(microtime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}