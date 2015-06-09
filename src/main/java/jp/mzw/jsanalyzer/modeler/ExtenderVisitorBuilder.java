package jp.mzw.jsanalyzer.modeler;

import java.util.Collections;
import java.util.Set;

import jp.mzw.jsanalyzer.modeler.visitor.interaction.*;
import jp.mzw.jsanalyzer.modeler.visitor.endisable.*;
import jp.mzw.jsanalyzer.modeler.visitor.css.*;

// used in each specific project
// define visitor list (Interaction, EnDisable, CSS)
public class ExtenderVisitorBuilder {
	//Interaction
	protected BuiltinInteractionVisitor builtinInteractionVisitor;
	protected PrototypeInteractionVisitor prototypeInteractionVisitor;
	protected JQueryInteractionVisitor jqueryInteractionVisitor;
	protected FirstInteractionVisitor firstInteractionVisitor;
	protected SecondInteractionVisitor secondInteractionVisitor;

	//EnDisable
	protected BuiltinEnDisableVisitor builtinEnDisableVisitor;
	protected SecondEnDisableVisitor secondEnDisableVisitor;
	protected ThirdEnDisableVisitor thirdEnDisableVisitor;
	
	//CSS
	protected InlineCSSVisitor inlineCSSVisitor;
	protected NotInlineCSSVisitor notInlineCSSVisitor;
	
	public ExtenderVisitorBuilder(){
		this.builtinInteractionVisitor = new BuiltinInteractionVisitor();
		this.prototypeInteractionVisitor = new PrototypeInteractionVisitor();
		this.jqueryInteractionVisitor = new JQueryInteractionVisitor();
		this.firstInteractionVisitor = new FirstInteractionVisitor();
		this.secondInteractionVisitor = new SecondInteractionVisitor();
		this.builtinEnDisableVisitor = new BuiltinEnDisableVisitor();
		this.secondEnDisableVisitor = new SecondEnDisableVisitor();
		this.thirdEnDisableVisitor = new ThirdEnDisableVisitor();
		this.inlineCSSVisitor = new InlineCSSVisitor();
		this.notInlineCSSVisitor = new NotInlineCSSVisitor();
	}
	

	public BuiltinInteractionVisitor getBuiltinInteractionVisitor(){
		return this.builtinInteractionVisitor;
	}

	public PrototypeInteractionVisitor getPrototypeInteractionVisitor(){
		return this.prototypeInteractionVisitor;
	}
	
	public JQueryInteractionVisitor getJQueryInteractionVisitor(){
		return this.jqueryInteractionVisitor;
	}

	public FirstInteractionVisitor getFirstInteractionVisitor(){
		return this.firstInteractionVisitor;
	}

	public SecondInteractionVisitor getSecondInteractionVisitor(){
		return this.secondInteractionVisitor;
	}
	
	public BuiltinEnDisableVisitor getBuiltinEnDisableVisitor(){
		return this.builtinEnDisableVisitor;
	}

	public SecondEnDisableVisitor getSecondEnDisableVisitor(){
		return this.secondEnDisableVisitor;
	}

	public ThirdEnDisableVisitor getThirdEnDisableVisitor(){
		return this.thirdEnDisableVisitor;
	}

	public InlineCSSVisitor getInlineCSSVisitor(){
		return this.inlineCSSVisitor;
	}

	public NotInlineCSSVisitor getNotInlineCSSVisitor(){
		return this.notInlineCSSVisitor;
	}
}