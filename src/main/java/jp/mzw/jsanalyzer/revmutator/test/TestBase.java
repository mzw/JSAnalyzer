package jp.mzw.jsanalyzer.revmutator.test;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {
	protected WebDriver driver;
//	protected WebDriverWait wait;
	protected String url = "http://localhost";
//	protected static final int TIMEOUT = 3; // second

    @Before
    public void setupBrowser() throws Exception {
        driver = new FirefoxDriver();
//        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @After
    public void quitBrowser() {
        driver.quit();
    }
}
