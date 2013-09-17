package Verifier;

import Analyzer.Util;
import Graph.State;
import Graph.StateMachine;

import java.util.Iterator;

public class Uppaal extends Verifier {

	public Uppaal(StateMachine sm) {
		super(sm);
	}
	
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
		for(Iterator<String> it = this.sm.getEvents().iterator(); it.hasNext(); ) {
			String ev = it.next();
			// skip 'after...' due to special deals
			if(ev.startsWith("after")) {
				continue;
			}
			ret += "chan " + Util.esc_uppaal(ev) + ";\n";
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
		// variables for given conditions here, e.g. int sleepTime = 5
		ret += "\t" + "</declaration>\n";
		// states
		for(Iterator<State> it = this.sm.getStates().iterator(); it.hasNext(); ) {
			State s = it.next();
			ret += s.toString_uppaal();
		}
		// initial state
		ret += "\t<init ref=\"" + this.sm.getInitState().getId() + "\"/>\n";
		// transitions
		ret += "</template>\n";

		ret += "\n";

		// Interaction model
		ret += "<template>\n";
		ret += "\t" + "<name x=\"5\" y=\"5\">TemplateInteraction</name>\n";
		// initial state
		ret += "\t<location id=\"ISiIM\">\n";
		
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
