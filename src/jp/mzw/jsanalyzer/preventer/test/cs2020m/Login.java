package jp.mzw.jsanalyzer.preventer.test.cs2020m;

import static junit.framework.Assert.fail;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import Test.TestConstants;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.CS2020m;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class Login extends WebAppTestBase {

	@SuppressWarnings("deprecation")
	@Test
	public void testMultipleLogin() {
		Project project = CS2020m.getProject(CS2020m.Original);
		Analyzer analyzer = new Analyzer(project);
		
		gotoUrl(analyzer.getProject().getUrl());
		
		WebElement user = driver.findElement(By.id("user"));
		user.sendKeys("y");
		user.sendKeys("u");
		user.sendKeys("t");
		user.sendKeys("a");
		
		WebElement pass = driver.findElement(By.id("pass"));
		pass.sendKeys("maezawa");

		WebElement loginbtn = driver.findElement(By.id("loginbtn"));
		loginbtn.click();

		WebElement msg = driver.findElement(By.id("msg"));
		String text = msg.getText();
		
		if(!"Hello, yuta".equals(text)) {
			fail();
		}
	}
	
}
