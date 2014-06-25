package jp.mzw.jsanalyzer.formulator;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.pp.Existence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.property.ACRetry;
import jp.mzw.jsanalyzer.formulator.property.AsyncComm;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.SRWait;
import jp.mzw.jsanalyzer.formulator.property.UEHRegist;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class IADPFormulator extends Formulator {

	
	/**
	 * Constructor
	 * @param filename Contains information about implemented Ajax design patterns (IADP information)
	 */
	public IADPFormulator(String filename) {
		super(filename);
	}
	
	/**
	 * Reads given a XML file containing IADP information
	 */
	@Override
	protected void read(String filename) {
		
	}
	
	@Override
	public List<Specification> generate() {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		
		
		return ret;
	}
	

	public static List<Specification> getSpecList4FileDLer(Analyzer analyzer) {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		
		AsyncComm pAsyncComm = new AsyncComm();
		pAsyncComm.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("Ajax.Request")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)));
		ret.add(new Specification(pAsyncComm));

		ACRetry pACRetry = new ACRetry();
		pACRetry.setTemplateVariables(NuSMV.genOr(fsm.getEventIdList("onFailure")), NuSMV.genOr(fsm.getFuncIdList("Ajax.Request")));
		ret.add(new Specification(pACRetry));
		
		SRWait pSRWait = new SRWait();
		pSRWait.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("inputFormText")), NuSMV.genOr(fsm.getEventIdList("onSuccess")));
		ret.add(new Specification(pSRWait));
		
		UEHRegist pUEHRegist = new UEHRegist();
		pUEHRegist.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("onload")));
		ret.add(new Specification(pUEHRegist));
		
//		UEHSingle pUEHSingle = new UEHSingle();
//		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("addCart")), NuSMV.genOr(fsm.getEventIdList("onclick")), true);
//		ret.add(new Specification(pUEHSingle));
		
		Property pUESubmit = new Property(
				"User event and submit",
				"UESubmit",
				"Ajax apps should require explicit user operations before form data is submitted",
				new AjaxDesignPattern(AjaxDesignPattern.Category.Programming, "Explicit Submittion"),
				new Existence(PropertyPattern.Scope.Before)
				);
		pUESubmit.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null, NuSMV.genOr(fsm.getFuncIdList("doSubmit")));
		ret.add(new Specification(pUESubmit));
		
		return ret;
	}
}
