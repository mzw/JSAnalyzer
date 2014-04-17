package jp.mzw.jsanalyzer.preventer.test.esa;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.base.Predicate;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.ESA;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class UEHRegist extends WebAppTestBase {

	@SuppressWarnings("deprecation")
	@Test
	public void testUEHRegist() {
		
		Project project = ESA.getProject(ESA.Original);
//		Project project = ESA.getProject(ESA.UEHRegist);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
			this.prepareJsErrorCollector();
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }
		

		WebElement target = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div[4]/div[2]/a"));
		WebElement logo = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div[1]/a/img"));
		
		Actions actions = new Actions(driver);
		actions.moveToElement(target);
		actions.moveToElement(logo);
		
//		this.assertNoJsErrorObserved();
		
	}
	
}
