package jp.mzw.jsanalyzer.verifier.modelchecker;

import java.util.Iterator;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.fsm.State;
import jp.mzw.jsanalyzer.modeler.model.fsm.Transition;

public class Uppaal extends ModelChecker {

	public Uppaal(FiniteStateMachine fsm, Analyzer analyzer) {
		super(fsm, analyzer);
	}
	
	@Override
	public String translate() {
		String ret = "";
		
		///// header
		ret += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		ret += "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_1.dtd'>\n";
		ret += "<nta>\n";

		ret += "\n";
		
		///// global declarations
		ret += "<declaration>\n";
		// built-in
		ret += "bool isExit = false;\n";
		// events
		for(Transition trans : this.mFSM.getTransList()) {
			String ev = trans.getEvent().getId();
			// skip 'after...' due to special deals
			if(ev.startsWith("after")) {
				continue;
			}
			ret += "chan " + ev + ";\n";
		}
		// clock
		ret += "clock x;\n";
		ret += "</declaration>\n";

		ret += "\n";
		
		///// templates
		// Application model
		ret += "<template>\n";
		ret += "\t" + "<name x=\"5\" y=\"5\">TemplateApp</name>\n";
		ret += "\t" + "<declaration>\n";
		// variables for given conditions here, e.g. bool cond;
		ret += "bool cond;\n";
		// end of variable declaration
		ret += "\t" + "</declaration>\n";
		// states
		for(State state : this.mFSM.getStateList()) {
			ret += state.getId();
		}
		// initial state
		ret += "\t<init ref=\"" + this.mFSM.getInitState().getId() + "\"/>\n";
		// transitions
		for(Transition trans : this.mFSM.getTransList()) {
			ret += "\t<transition>\n";
			ret += "\t\t<source ref=\"" + trans.getFromStateId() + "\" />\n";
			ret += "\t\t<target ref=\"" + trans.getToStateId() + "\" />\n";
			if(trans.hasEvent()) {
				ret += "\t\t<label kind=\"synchronisation\""
						+ " x=\"" + trans.getFromStateId() + "\" />\n";
			}
			ret += "\t</transition>\n";
		}
		
		ret += "</template>\n";

		ret += "\n";

		// Interaction model
		ret += "<template>\n";
		ret += "\t" + "<name x=\"5\" y=\"5\">TemplateInteraction</name>\n";
		// initial state
		ret += "\t<location id=\"ISiIM\"/>\n";
		
		ret += "\t<init ref=\"ISiIM\"/>\n";
		ret += "</template>\n";

		ret += "\n";
		
		
		///// footer
		ret += "<lsc>\n";
		ret += "\t<name>LscTemplate</name>\n";
		ret += "\t<type>Universal</type>\n";
		ret += "\t<mode>Invariant</mode>\n";
		ret += "\t<declaration></declaration>\n";
		ret += "\t<yloccoord number=\"0\" y=\"0\"/>\n";
		ret += "\t<yloccoord number=\"1\" y=\"220\"/>\n";
		ret += "\t<instance id=\"instanceId\" x=\"0\" y=\"0\">\n";
		ret += "\t\t<name x=\"0\" y=\"0\">Instance</name>\n";
		ret += "\t</instance>\n";
		ret += "</lsc>\n";
		// system
		ret += "<system>\n";
		ret += "App = TemplateApp();\n";
		ret += "Interaction = TemplateInteraction();\n";
		ret += "Scenario = LscTemplate();\n";
		ret += "system App, Interaction;\n";
		ret += "</system>\n";
		ret += "</nta>\n";
		
		return ret;
	}

}
