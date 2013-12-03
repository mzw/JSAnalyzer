package jp.mzw.jsanalyzer.core.example;

import jp.mzw.jsanalyzer.core.Project;

public class QAsite extends Project {
	
	public QAsite() {
		super("QAsiteError",
				"http://mzw.jp/research/ex/QAsite/error/index.html",
				Project.getDefaultRuleFilenames(),
				"projects/test2");
	}
	
}
