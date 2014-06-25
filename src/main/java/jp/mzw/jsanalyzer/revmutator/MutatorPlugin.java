package jp.mzw.jsanalyzer.revmutator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.CandidateElement;
import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.ExitNotifier.ExitStatus;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.GeneratesOutput;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.PostCrawlingPlugin;
import com.crawljax.core.plugin.PreCrawlingPlugin;
import com.crawljax.core.plugin.PreStateCrawlingPlugin;
import com.crawljax.core.state.StateVertex;
import com.google.common.collect.ImmutableList;

public class MutatorPlugin
	implements PreStateCrawlingPlugin, OnNewStatePlugin, PreCrawlingPlugin, PostCrawlingPlugin, GeneratesOutput {
	private static final Logger LOGGER = LoggerFactory.getLogger(MutatorPlugin.class);

	protected String outputFolder;	
	@Override
	public void setOutputFolder(String absolutePath) {
		outputFolder = absolutePath;
	}

	@Override
	public String getOutputFolder() {
		return null;
	}

	@Override
	public void preCrawling(CrawljaxConfiguration config) throws RuntimeException {
		
	}

	@Override
	public void postCrawling(CrawlSession session, ExitStatus exitReason) {
		
	}

	@Override
	public void onNewState(CrawlerContext context, StateVertex newState) {
		
	}

	@Override
	public void preStateCrawling(CrawlerContext context, ImmutableList<CandidateElement> candidateElements, StateVertex state) {
		
	}

}
