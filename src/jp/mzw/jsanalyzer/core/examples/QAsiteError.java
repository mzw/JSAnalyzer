package jp.mzw.jsanalyzer.core.examples;

import java.util.List;

import jp.mzw.jsanalyzer.core.Project;

public class QAsiteError extends Project {
	
	public QAsiteError() {
		super("QAsiteError",
				"http://localhost/~yuta/research/ex/QAsite/error/index.html",
				setRuleFilenames(),
				"projects/test2");
	}

	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}
	
}
