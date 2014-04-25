package jp.mzw.jsanalyzer.preventer.test.cs2020m;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.CS2020m;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class UEHRegist extends WebAppTestBase {

	@Test
	public void testUEHRegist() {
		
		Project project = CS2020m.getProject(CS2020m.Original);
//		Project project = CS2020m.getProject(CS2020m.UEHRegist);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }
		this.prepareJsErrorCollector();
		
		this.waitAndClickElementLocated(By.xpath("//*[@id=\"loginboxs\"]/div[3]/a"));
		
		this.assertNoJsErrorObserved();
		
	}
	
}
