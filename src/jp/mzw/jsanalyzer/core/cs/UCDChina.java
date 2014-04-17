package jp.mzw.jsanalyzer.core.cs;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class UCDChina extends Project {
	
	public static void main(String[] args) {
		Project project = UCDChina.getProject(UCDChina.Original);
		Analyzer analyzer = new Analyzer(project);
		
//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
		List<Specification> specList = UCDChina.getSpecList(analyzer, verifier.getExtractedFSM());
		verifier.verifyIADP(specList);
	}
	
	private UCDChina(String projName, String projUrl) {
		super(projName, projUrl,
				UCDChina.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		Original = 0,
		Mutated_Original = 1;
	
	public static UCDChina getProject(int ver) {
		switch(ver) {
		case UCDChina.Original:
			return new UCDChina("UCDChina.Original", "http://localhost/~yuta/research/cs/ucdchina/0.origin/cdchina.html");
		}
		return null;
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
	
	
	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		
		/////
		// Fundamental properties
		/////
		
		/// AyncComm
		// Implemented but not called from anywhere, i.e., function va(id) {...}
//		Property pAsyncComm_get = Property.getPropertyByNameAbbr("AsyncComm").clone();
//		pAsyncComm_get.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.get")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
//		ret.add(new Specification(pAsyncComm_get));

		Property pAsyncComm_post = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm_post.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.post")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm_post));
		
		/// ACRetry
		// Could not find any failure events
		
		/// SRWait
		// response: used only in callback
		
		/// UEHRegist
		Property pUEHRegist_onload = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_onload.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("ready")), null, null);
		ret.add(new Specification(pUEHRegist_onload));
		
		/// UEHSingle
		// Could not determine
		
		/////
		/// Additional properties
		/////
		
		/// Properties from Live Form Pattern
		/// b/c keyword "onblur=checkInput" at NerdyData
		
		/// FDValid
		String checkInput = fsm.getFuncId("checkInput", 1, 0);
		String goSearch = fsm.getFuncId("goSearch", 1, 0);
		
		Property pFDValid = Property.getPropertyByNameAbbr("FDValid").clone();
		pFDValid.setTemplateVariables(NuSMV.genExpr(goSearch), NuSMV.genExpr(checkInput), null, null);
		ret.add(new Specification(pFDValid));

		
		
		return ret;
	}
}
