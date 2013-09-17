package Parser;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public class Code {
	protected String url = null;
	protected String src = null;
	public Code(String url, String src) {
		this.url = url;
		this.src = src;
	}
	public String getUrl() {
		return this.url;
	}
	public String toSource() {
		return this.src;
	}
	
	// in-line
	private Element target = null;
	private Attribute attr = null;
	public void setTarget(Element target, Attribute attr) {
		this.target = target;
		this.attr = attr;
	}
	public Element getTarget() {
		return this.target;
	}
	public Attribute getAttr() {
		return this.attr;
	}
}
