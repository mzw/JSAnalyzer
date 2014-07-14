package jp.mzw.jsanalyzer.app.cs;

import jp.mzw.jsanalyzer.util.VersionUtils;
import jp.mzw.jsanalyzer.core.Project;

public class WordPress extends Project {
	
	
	public static WordPress getProject(VersionUtils ver) {
		if(ver.equals(2, 3, 4)) {
			return new WordPress("WordPress.2.3.4.Original", "http://localhost/~yuta/...");
		}
		return null;
	}

	private WordPress(String projName, String projUrl) {
		super(projName, projUrl,
				Project.getDefaultRuleFilenames(),
				"projects/wordpress");
	}
}
