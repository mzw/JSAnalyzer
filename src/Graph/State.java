package Graph;

import Analyzer.Config;
import Analyzer.Util;

public class State extends Node {
	
	protected boolean layouted = false;
	protected String name = "";
	public State(int id, String name) {
		super(id, null); // debug later
		this.name = name;
	}

	protected double xpos = 0.0;
	protected double ypos = 0.0;
	public void setPos(double xpos, double ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.setLayouted(true);
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public void setLayouted(boolean layouted) {
		this.layouted = layouted;
	}
	
	public String toString_dot() {
		return this.id + "[id=\"" + this.id + "\",label=\"" + Util.esc_dot(this.name) + "\"];";
	}
	
	public String toString_xml() {
		String ret = "";
		
		ret += "<" + Config.stateTag + " id=\"" + this.id + "\" name=\"" + Util.esc_xml(this.name) + "\" ";
		if(this.layouted) {
			ret += "xpos=\"" + (int)this.xpos + "\" ypos=\"" + (int)this.ypos + "\" ";
		}
		ret += "/>";
		return ret;
	}
	
	public String toString_uppaal() {
		String ret = "";
		
		ret += "\t" + "<location id=\"" + this.getId() + "\" x=\"" + (int)this.xpos + "\" ypos=\"" + (int)this.ypos + "\">\n";
		ret += "\t\t<name x=\"" + ((int)this.xpos - 10) + "\" y=\"" + ((int)this.ypos - 30) + "\">" + Util.esc_uppaal(this.getName()) + "</name>\n";
		ret += "\t</location>\n";
		
		return ret;
	}
}
