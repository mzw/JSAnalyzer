package jp.mzw.jsanalyzer.core.examples;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.UEHSingle;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class FileDLerRetry extends Project {
	
	
	public static void main(String[] args) {
		Project project = new FileDLerRetry();
		Analyzer analyzer = new Analyzer(project);
		
//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
//		Property.setOriginalPropertyList();
//		TextFileUtils.write("/Users/yuta/Sites/research/ex/iadp/", "properties.xml", Property.getPropertyListXML());
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
		List<Specification> specList = FileDLerRetry.getSpecList(analyzer, verifier.getExtractedFSM());
 		verifier.verifyIADP(specList);
	}

	public FileDLerRetry() {
		super("FileDLerRetry",
				"http://mzw.jp/yuta/research/ex/fd/motivatingExample.retry.correct.html",
				FileDLerCorrect.setRuleFilenames(),
				"projects/project");
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}
	

	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		

		/////
		// Fundamental properties
		/////

		Property pAsyncComm = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("Ajax.Request")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm));
		
		/// ACRetry
		Property pACRetry = Property.getPropertyByNameAbbr("ACRetry").clone();
		pACRetry.setTemplateVariables(NuSMV.genOr(fsm.getEventIdList("onFailure")), NuSMV.genOr(fsm.getFuncIdList("Ajax.Request")), null, null);
		ret.add(new Specification(pACRetry));
		
		/// SRWait
		Property pSRWait = Property.getPropertyByNameAbbr("SRWait").clone();
		pSRWait.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("inputFormText")), NuSMV.genOr(fsm.getEventIdList("onSuccess")), null, null);
		ret.add(new Specification(pSRWait));
		
		/// UEHRegist
		Property pUEHRegist = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("onload")), null, null);
		ret.add(new Specification(pUEHRegist));
		
		/// UEHSingle
		UEHSingle pUEHSingle = new UEHSingle();
//		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("doDownload")), NuSMV.genOr(fsm.getEventIdList("onclick")), true);
		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("doDownload")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), false);
		ret.add(new Specification(pUEHSingle));
		
		return ret;
	}
}
