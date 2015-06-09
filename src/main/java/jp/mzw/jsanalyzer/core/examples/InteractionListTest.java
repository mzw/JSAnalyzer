package jp.mzw.jsanalyzer.core.examples;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.SRWait;
import jp.mzw.jsanalyzer.formulator.property.UEHRegist;
import jp.mzw.jsanalyzer.formulator.property.UEHSingle;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;
import jp.mzw.jsanalyzer.modeler.ExtenderVisitorBuilder;
import jp.mzw.jsanalyzer.modeler.visitor.CSSDetectionVisitor;
import jp.mzw.jsanalyzer.modeler.visitor.EnDisableDetectionVisitor;
import jp.mzw.jsanalyzer.modeler.visitor.InteractionDetectionVisitor;

public class InteractionListTest extends Project {

	public static void main(String[] args) {
		Project project = new InteractionListTest();
		Analyzer analyzer = new Analyzer(project);
		
		Modeler modeler = new Modeler(analyzer);
		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
		Serializer.serialze(analyzer, fsm);
		
		Verifier verifier = new Verifier(analyzer);
		verifier.setup();
		verifier.verifyIADP();
		List<Specification> specList = QAsiteCorrect.getSpecList(analyzer, verifier.getExtractedFSM());
 		verifier.verifyIADP(specList);
	}

	public InteractionListTest() {
		super("InteractionListTest",
//				"http://localhost/~yuta/research/ex/QAsite/error/index.html",
				"http://localhost/research/honda/tse_sws/fault_quick.html",//
				setRuleFilenames(),
				"projects/project");
	}

	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		//ret.add("res/libRules/prototype.xml");
		ret.add("res/libRules/jquery.xml");
		//res/libRules/jquery.xml
		
		return ret;
	}
	
	// interaction list
	public List<InteractionDetectionVisitor> getInteractionList(){
		List<InteractionDetectionVisitor> list = new ArrayList<InteractionDetectionVisitor>();
		ExtenderVisitorBuilder builder = new ExtenderVisitorBuilder();
		list.add(builder.getBuiltinInteractionVisitor());
		list.add(builder.getPrototypeInteractionVisitor());
		list.add(builder.getJQueryInteractionVisitor());
		list.add(builder.getFirstInteractionVisitor());
		list.add(builder.getSecondInteractionVisitor());
		return list;
	}
	
	//EnDisable List
	public List<EnDisableDetectionVisitor> getEnDisableList(){
		List<EnDisableDetectionVisitor> list = new ArrayList<EnDisableDetectionVisitor>();
		ExtenderVisitorBuilder builder = new ExtenderVisitorBuilder();
		list.add(builder.getBuiltinEnDisableVisitor());
		list.add(builder.getSecondEnDisableVisitor());
		list.add(builder.getThirdEnDisableVisitor());
		return list;
	}
	
	//CSS List
	public List<CSSDetectionVisitor> getCSSList(){
		List<CSSDetectionVisitor> list = new ArrayList<CSSDetectionVisitor>();
		ExtenderVisitorBuilder builder = new ExtenderVisitorBuilder();
		list.add(builder.getInlineCSSVisitor());
		list.add(builder.getNotInlineCSSVisitor());
		return list;
	}

	//phD example faulty
	
	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();

		/////
		// Fundamental properties
		/////
		
		/// UEHRegist
		UEHRegist pUEHRegist = new UEHRegist();
		pUEHRegist.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("onload")));
		ret.add(new Specification(pUEHRegist));
		
		/// UEHSingle
		UEHSingle pUEHSingle = new UEHSingle();
		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("addCart")), NuSMV.genOr(fsm.getEventIdList("onclick")), true);
		ret.add(new Specification(pUEHSingle));
		
		return ret;
	}

	//QAsite correct

}
