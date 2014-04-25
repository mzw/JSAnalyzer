package jp.mzw.jsanalyzer.preventer.test.esa;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.TimeoutException;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.ESA;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class UEHRegist extends WebAppTestBase {
	
	@Test
	public void testUEHRegist() {
		
//		Project project = ESA.getProject(ESA.Original);
		Project project = ESA.getProject(ESA.UEHRegist);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }
		this.prepareJsErrorCollector();
		
		/// Correspond to mouse out callback
		this.executeJavaScript("addthis_close();");
		
		
		this.assertNoJsErrorObserved();
		
	}
	
}
