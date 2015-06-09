package jp.mzw.jsanalyzer.modeler.visitor;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.EnDisableManager;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.parser.CSSCode;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.rule.Function;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;

public abstract class CSSDetectionVisitor{
	public abstract boolean detect(CSSCode cssCode);
	public abstract void process(Analyzer analyzer, HTMLParser htmlParser, EnDisableManager edManager);
}