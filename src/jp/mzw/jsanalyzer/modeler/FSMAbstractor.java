package jp.mzw.jsanalyzer.modeler;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.CallGraph;
import jp.mzw.jsanalyzer.rule.RuleManager;

public class FSMAbstractor extends Modeler {
	public FSMAbstractor(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * Abstracts extended call graph focusing on interactions with Ajax apps
	 * @param xcg Extended call graph
	 * @param ruleManager To distinguish interactions
	 * @return Abstracted call graph
	 */
	public CallGraph abst(CallGraph xcg) {
		RuleManager ruleManager = this.mAnalyzer.getRuleManager();
		return null;
	}
}
