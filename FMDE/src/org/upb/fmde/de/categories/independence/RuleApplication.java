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
	public Arr getCoMatch() {
		return M;
	}
	
	private Span<Arr> rule;
	private Arr F;
	private Arr G;
	private Arr M;
	private Arr N;
	

	public RuleApplication(Span<Arr> rule, Arr f, Arr g, Arr m, Arr n) 
	{
		this.rule = rule;
		this.G = g;
		this.F = f;
		this.M = m;
		this.N = n;
	}

	public Span<Arr> getRule() {
		return rule;
	}
	
}
