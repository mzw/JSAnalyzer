package Graph;

import Analyzer.Config;
import Analyzer.Util;

public class Entry extends State {
	
	
	public Entry(int id) {
		super(id, "entry");
	}
	

	public String toString_dot() {
		return this.id + " [id=\"" + this.id + "\",label=\"\",shape=circle,style=filled,color=black,fixedsize=true,width=0.3]";
	}
	
	public String toString_xml() {
		String ret = "";
		
		ret += "<" + Config.entryTag  + " id=\"" + this.id + "\" name=\"" + Util.esc_xml(this.name) + "\" ";
		if(this.layouted) {
			ret += "xpos=\"" + (int)this.xpos + "\" ypos=\"" + (int)this.ypos + "\" ";
		}
		ret += "/>";
		
		return ret;
	}
}
