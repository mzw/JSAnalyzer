package jp.mzw.jsanalyzer.core.examples;

import jp.mzw.jsanalyzer.core.Project;

public class Honiden extends Project {
	public Honiden() {
		super("Honiden",
				"http://www.honiden.nii.ac.jp/index.php",
				Project.getDefaultRuleFilenames(),
				"projects/test2");
	}
}
