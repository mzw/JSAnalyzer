package jp.mzw.jsanalyzer.rule;

public class Manipulate extends Rule {
	
	protected String mSemantic;
	protected String mBy;
	protected String mValue;
	protected String mTarget;
	
	public Manipulate(String func, String semantic, String by, String value, String target) {
		super(func);
		this.mSemantic = semantic;
		this.mBy = by;
		this.mValue = value;
		this.mTarget = target;
	}
}
