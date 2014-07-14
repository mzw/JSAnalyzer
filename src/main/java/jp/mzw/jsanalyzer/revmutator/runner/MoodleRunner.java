package jp.mzw.jsanalyzer.revmutator.runner;

import java.io.File;
import java.util.concurrent.TimeUnit;

import jp.mzw.jsanalyzer.util.VersionUtils;
import jp.mzw.jsanalyzer.util.BrowserUtils;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.Moodle;
import jp.mzw.jsanalyzer.revmutator.MutatorPlugin;
import jp.mzw.jsanalyzer.revmutator.MutatorProxyPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.proxy.WebScarabProxyPlugin;
import com.crawljax.core.CandidateElementExtractor;

public class MoodleRunner extends Runner {
	protected static final Logger LOG = LoggerFactory.getLogger(MoodleRunner.class);
	
	public static void main(String[] args) {
		Runner runner = new MoodleRunner();
		runner.run();
	}
	
	public static void test() {
		Project project = Moodle.getProject(Moodle.Original_2_3_1);
		String url = project.getUrl();
		
		CrawljaxRunner crawljax =
		        new CrawljaxRunner(CrawljaxConfiguration.builderFor(url)
		                .build());
		crawljax.call();
	}
	
	@Override
	public void run() {
		LOG.debug("Moodle runner start");
		
		Project project = Moodle.getProject(Moodle.Original_2_3_1);
		String url = project.getUrl();
//		String url = "http://demo.crawljax.com/";
		String outputdir = getOutputDir(project.getDir());

		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		///
		builder.crawlRules().insertRandomDataInInputForms(INSERT_RANDOM_DATA_IN_INPUT_FORMS);
		builder.crawlRules().clickElementsInRandomOrder(CLICK_ELEMENTS_IN_RANDOM_ORDER);
		builder.crawlRules().clickOnce(CLICK_ONCE);
		builder.crawlRules().waitAfterReloadUrl(WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);
		///
		builder.crawlRules().clickDefaultElements();
//		builder.crawlRules().click("div");
//		setClickCrawlRules(builder.crawlRules());
		builder.crawlRules().setInputSpec(getInputSpec());
//		InputSpecification input = new InputSpecification();
//		input.field("username").setValue("admin-Adm1n");
//		builder.crawlRules().setInputSpec(input);
		///
		builder.setMaximumStates(MAX_STATES);
		builder.setMaximumDepth(MAX_DEPTH);
		builder.setOutputDirectory(new File(outputdir));

		/// Proxy
//		ProxyConfiguration proxy = ProxyConfiguration.manualProxyOn(PROXY_HOST, PROXY_PORT);
//		builder.setProxyConfig(proxy);
//
//		/// WebScarab
//		WebScarabProxyPlugin web = new WebScarabProxyPlugin();
//		MutatorProxyPlugin intercepter = new MutatorProxyPlugin();
//		intercepter.setDefaultExcludeContentList();
//		web.addPlugin(intercepter);
//		builder.addPlugin(web);
		
		/// My crawling plugin
//		MutatorPlugin mutator = new MutatorPlugin();
//		mutator.setOutputFolder(outputdir);
//		builder.addPlugin(mutator);
		
		/// Running Browser
		BrowserUtils.setBrowser(BrowserUtils.Type.FIREFOX, VersionUtils.get(17, 0, 11));
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.FIREFOX, 1));
		
		CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
		crawljax.call();
		
	}
	
	@Override
	protected void setClickCrawlRules(CrawlRulesBuilder builder) {
//		builder.click("a").withText("Login");
		builder.click("input").withAttribute("id", "loginbtn");
	}
	
	@Override
	protected InputSpecification getInputSpec() {
		InputSpecification input = new InputSpecification();

		/// Take your greatest care: Multiple entries with same key
		
//		String username = "admin";
//		String password = "admin-Adm1n";
//
//		input.field("username").setValue(username);
//		input.field("password").setValue(password);
		
		Form loginForm = new Form();
		loginForm.field("username").setValue("admin");
		loginForm.field("password").setValue("admin-Adm1n");
		input.setValuesInForm(loginForm).beforeClickElement("input").withAttribute("id", "loginbtn");
		
		return input;
	}
}
