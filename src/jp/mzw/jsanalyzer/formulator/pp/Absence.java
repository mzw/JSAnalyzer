package jp.mzw.jsanalyzer.formulator.pp;

public class Absence extends Occurrence {
	
	public Absence() {
		super("[](!(App_state == $arg_1))");
	}
	
}
