package jp.mzw.jsanalyzer.revmutator.runner;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.Moodle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

public class MoodleRunner extends Runner {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MoodleRunner.class.getName());
	
	public static void main(String[] args) {
		Project project = Moodle.getProject(Moodle.Original_2_3_0);

		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(project.getUrl());
	}
}
