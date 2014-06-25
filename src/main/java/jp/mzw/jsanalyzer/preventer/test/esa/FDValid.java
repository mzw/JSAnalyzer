package jp.mzw.jsanalyzer.preventer.test.esa;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.ESA;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class FDValid extends WebAppTestBase {

	@Test
	public void testUEHRegist() {

//		Project project = ESA.getProject(ESA.Original);
		Project project = ESA.getProject(ESA.FDValid);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
			this.prepareJsErrorCollector();
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }
		
		WebElement dest = driver.findElement(By.id("Search_apartments"));
		dest.clear();
		
		WebElement search = driver.findElement(By.id("text"));
		search.click();
		
		try {
            wait.until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver webDriver) {
                    return !webDriver.getCurrentUrl().equals(startUrl);
                }
            });
            fail("We shouldn't allow page transition, but current url is " + driver.getCurrentUrl());
        } catch (TimeoutException e) {
            // Expected exception.
        }
		
//		this.assertNoJsErrorObserved();
		
	}
	
}
