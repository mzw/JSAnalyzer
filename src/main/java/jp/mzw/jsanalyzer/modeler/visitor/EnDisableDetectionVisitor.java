package jp.mzw.jsanalyzer.modeler.visitor;

import jp.mzw.jsanalyzer.modeler.EnDisableManager;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.rule.Function;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;

public abstract class EnDisableDetectionVisitor{
	public abstract boolean detect(Node node, RuleManager rule);
	public abstract void process(Node node, CallGraph callGraph, EnDisableManager edManager);
}