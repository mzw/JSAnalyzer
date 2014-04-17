package jp.mzw.jsanalyzer.preventer.test.ucdchina;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.base.Predicate;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.UCDChina;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class UEHRegist extends WebAppTestBase {

	@SuppressWarnings("deprecation")
	@Test
	public void testUEHRegist() {
		
		Project project = UCDChina.getProject(UCDChina.Original);
//		Project project = UCDChina.getProject(UCDChina.UEHRegist);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
			this.prepareJsErrorCollector();
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }

        WebElement searchForm = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("searchBox")));
        searchForm.submit();
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
		
		this.assertNoJsErrorObserved();
		
	}
	
}
