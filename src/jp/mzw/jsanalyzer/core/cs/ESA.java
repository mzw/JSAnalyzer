package jp.mzw.jsanalyzer.core.cs;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.pp.Existence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.property.ACRetry;
import jp.mzw.jsanalyzer.formulator.property.AsyncComm;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.SRWait;
import jp.mzw.jsanalyzer.formulator.property.UEHRegist;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.Event;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.Transition;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class ESA extends Project {
	
	public static void main(String[] args) {
		Project project = ESA.getProject(ESA.Origin);
		Analyzer analyzer = new Analyzer(project);

//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
		List<Specification> specList = ESA.getSpecList(analyzer, verifier.getExtractedFSM());
		verifier.verifyIADP(specList);
	}
	
	private ESA(String projName, String projUrl) {
		super(projName, projUrl,
				ESA.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		Origin = 0,
		MutatedError = 1;
	
	public static ESA getProject(int ver) {
		switch(ver) {
		case ESA.Origin:
			return new ESA("ESA.Original", "http://localhost/~yuta/research/cs/esa/0.1.origin/esa.html");
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
		
		Property pAsyncComm = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.post")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm));
		
		
		
		Property pUEHRegist_onload = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_onload.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("onload")), null, null);
		ret.add(new Specification(pUEHRegist_onload));
		
		Property pUEHRegist_ready = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_ready.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("ready")), null, null);
		ret.add(new Specification(pUEHRegist_ready));
		
		
		// to be mod
//		Property pUEHSingle = Property.getPropertyByNameAbbr("UEHSingle").clone();
//		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("submit")), null, null);
//		ret.add(new Specification(pUEHSingle));
		
		

		Property pSeedRetrieval = Property.getPropertyByNameAbbr("SeedRetrieval").clone();
		pSeedRetrieval.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("validateLogin")), NuSMV.genOr(fsm.getFuncIdList("getSeed")), null, null);
		ret.add(new Specification(pSeedRetrieval));
		


		Property pLFDisable = Property.getPropertyByNameAbbr("LFDisable").clone();
		pLFDisable.setTemplateVariables(
				NuSMV.genOr(fsm.getFuncIdList("window.location.replace ")),
				NuSMV.genOr(fsm.getEventIdList("submit")),
				null,
				null
				);
		ret.add(new Specification(pLFDisable));
		
		return ret;
	}
}
