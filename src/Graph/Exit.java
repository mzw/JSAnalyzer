package Graph;

import Analyzer.Config;
import Analyzer.Util;

public class Exit extends State {
	public Exit(int id) {
		super(id, "exit");
	}

	public String toString_dot() {
		return this.id + " [id=\"" + this.id + "\",label=\"\",shape=doublecircle,style=filled,color=black,fixedsize=true,width=0.3];";
	}
	
	public String toString_xml() {
		String ret = "";
		
		ret += "<" + Config.exitTag  + " id=\"" + this.id + "\" name=\"" + Util.esc_xml(this.name) + "\" ";
		if(this.layouted) {
			ret += "xpos=\"" + (int)this.xpos + "\" ypos=\"" + (int)this.ypos + "\" ";
		}
		ret += "/>";
		
		return ret;
	}
}
