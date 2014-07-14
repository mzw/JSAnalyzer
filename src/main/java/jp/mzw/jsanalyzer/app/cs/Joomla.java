package jp.mzw.jsanalyzer.app.cs;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.util.VersionUtils;

public class Joomla extends Project {

	public static Joomla getProject(VersionUtils ver) {
		if(ver.equals(3, 3, 1)) {
			return new Joomla("Joomla.3.3.1.Original", "http://localhost/~yuta/research/test/joomla/3.3.1/");
		}
		return null;
	}

	private Joomla(String projName, String projUrl) {
		super(projName, projUrl,
				Project.getDefaultRuleFilenames(),
				"projects/joomla");
	}
}
