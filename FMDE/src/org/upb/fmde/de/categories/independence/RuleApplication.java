package org.upb.fmde.de.categories.independence;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.colimits.pushouts.Span;

public class RuleApplication<Arr> {
	
	public Arr getArrowF() {
		return F;
	}

	public Arr getArrowG() {
		return G;
	}

	public Arr getMatch() {
		return M;
	}
	
	private Span<Arr> rule;
	private Arr F;
	private Arr G;
	private Arr M;
	

	public RuleApplication(Span<Arr> rule, Arr f, Arr g, Arr m) 
	{
		this.rule = rule;
		this.G = g;
		this.F = f;
		this.M = m;
	}

	public Span<Arr> getRule() {
		return rule;
	}
	
}
