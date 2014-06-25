package jp.mzw.jsanalyzer.rule;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jp.mzw.jsanalyzer.util.StringUtils;

public class JSControl extends Control {

	protected String mTarget;
	protected String mProp;
	protected String mValue;
	protected String mCond;
	protected String mSemantic;
	protected String mDisabled;

	protected static final String Semantic_Disable = "disable";
	protected static final String Semantic_Enable = "enable";
	
	
	public JSControl(String func, String target, String prop, String value, String cond, String semantic, String disabled) {
		super(func, value);
		
		this.mTarget = target;
		this.mProp = prop;
		this.mValue = value;
		this.mCond = cond;
		this.mSemantic = semantic;
		this.mDisabled = disabled;
	}
	
	public String getTarget() {
		return this.mTarget;
	}
	
	public String getProp() {
		return this.mProp;
	}
	public String getValue() {
		return this.mValue;
	}
	
	public String getSemantic() {
		return this.mSemantic;
	}
	
	public boolean disabled(String prop, String value) {
		String script = this.mDisabled;
		script = script.replaceAll("<prop>", prop);
		script = script.replaceAll("<value>", value);
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		
		try {
			Boolean result = (Boolean)engine.eval(script);
			return result;
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		StringUtils.printError(this, "might be improper JSControl.cond", this.mCond);
		return false;
	}
	
	public boolean rematch(String prop, String value) {
		String script = this.mCond;
		script = script.replaceAll("<prop>", prop);
		script = script.replaceAll("<value>", value);
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		
		try {
			Boolean result = (Boolean)engine.eval(script);
			return result;
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		StringUtils.printError(this, "might be improper JSControl.cond", this.mCond);
		return false;
	}
}
