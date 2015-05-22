package jp.mzw.jsanalyzer.revmutator.test.pwdunmask;


import static org.junit.Assert.*;
import jp.mzw.jsanalyzer.revmutator.test.TestBase;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.*;

public class TestSetImproved extends TestBase {

	protected String url = "http://localhost/yuta/research/example/pwdunmask/0.1.mut/";
	protected static final String filepath_to_icse15pdf = "/Users/yuta/Documents/paper/icse15.pdf";
	protected static final String password_to_upload = "top_conf";
	
	@Test
	public void testSubmit() {
		driver.get(url);
		driver.findElement(By.id("paper")).sendKeys(filepath_to_icse15pdf);
		driver.findElement(By.id("pwd")).sendKeys("top_conf");
		
		/// start: improvement
		WebElement pf = driver.findElement(By.id("pwd"));
		driver.findElement(By.id("pwdunmask")).click();
		assertEquals("text", pf.getAttribute("type"));
		driver.findElement(By.id("pwdunmask")).click();
		assertEquals("password", pf.getAttribute("type"));
		/// end: improvement
		
		driver.findElement(By.id("submit")).click();
		
		assertEquals("Your paper was successfully submitted.",
					driver.findElement(By.id("result")).getText());
	}
	
}