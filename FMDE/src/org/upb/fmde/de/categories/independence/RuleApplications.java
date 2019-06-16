package org.upb.fmde.de.categories.independence;

import java.util.List;
import java.util.function.BiFunction;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSetPatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSets;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.GraphPatternMatcher;
import org.upb.fmde.de.categories.concrete.graphs.Graphs;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;

public class RuleApplications {

	public boolean areIndependentFinSets(RuleApplication<TotalFunction> rule1, RuleApplication<TotalFunction> rule2) {

		FinSetPatternMatcher matcher_i = new FinSetPatternMatcher(FinSets.FinSets.target(rule1.getRule().left),
				FinSets.FinSets.source(rule2.getArrowF()));
		List<TotalFunction> possibleis = matcher_i.determineMatches(true);

		FinSetPatternMatcher matcher_j = new FinSetPatternMatcher(FinSets.FinSets.target(rule2.getRule().left),
				FinSets.FinSets.source(rule1.getArrowF()));
		List<TotalFunction> possiblejs = matcher_j.determineMatches(true);

		// Check whether squares commute
		for (TotalFunction i : possibleis) {
			for (TotalFunction j : possiblejs) {
				if (FinSets.FinSets.compose(i, rule2.getArrowF()).isTheSameAs(rule1.getMatch())
						&& FinSets.FinSets.compose(j, rule1.getArrowF()).isTheSameAs(rule2.getMatch())) {
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean areIndependentGraphs(RuleApplication<GraphMorphism> rule1, RuleApplication<GraphMorphism> rule2) {

		GraphPatternMatcher matcher_i = new GraphPatternMatcher(Graphs.Graphs.target(rule1.getRule().left),
				Graphs.Graphs.source(rule2.getArrowF()));
		List<GraphMorphism> possibleis = matcher_i.determineMatches(true);

		GraphPatternMatcher matcher_j = new GraphPatternMatcher(Graphs.Graphs.target(rule2.getRule().left),
				Graphs.Graphs.source(rule1.getArrowF()));
		List<GraphMorphism> possiblejs = matcher_j.determineMatches(true);

		// Check whether squares commute
		for (GraphMorphism i : possibleis) {
			for (GraphMorphism j : possiblejs) {
				if (Graphs.Graphs.compose(i, rule2.getArrowF()).isTheSameAs(rule1.getMatch())
						&& Graphs.Graphs.compose(j, rule1.getArrowF()).isTheSameAs(rule2.getMatch())) {
					return true;
				}
			}
		}

		return false;
	}
	
	public boolean areIndependent(RuleApplication<TotalFunction> r, RuleApplication<TotalFunction> r2, FinSets cat) {
		return this.areIndependent(r, r2,(P,G)-> new FinSetPatternMatcher(P, G),cat);
	}
	
	public boolean areIndependent(RuleApplication<GraphMorphism> r, RuleApplication<GraphMorphism> r2, Graphs cat) {
		return this.areIndependent(r, r2,(P,G)-> new GraphPatternMatcher(P, G), cat);
	}
	
	public boolean areIndependent(RuleApplication<TGraphMorphism> r, RuleApplication<TGraphMorphism> r2, TGraphs cat) {
		//TODO find the TGraphPatternMatcher & implement this method
		return false;
	}
	
	private <Ob,Arr> boolean areIndependent(RuleApplication<Arr> r, RuleApplication<Arr> r2, BiFunction<Ob,Ob,PatternMatcher<Ob, Arr>> matcher, Category<Ob, Arr> cat) {
		CandidateIsAndJs<Arr> candiates = findIsAndJs(r, r2, matcher, cat);
		return asquareCommutes(candiates.iList, candiates.jList, r, r2, (Category<Ob, Arr>)FinSets.FinSets);	
	}
	
	private <Ob,Arr> CandidateIsAndJs<Arr> findIsAndJs(RuleApplication<Arr> r, RuleApplication<Arr> r2, BiFunction<Ob,Ob,PatternMatcher<Ob, Arr>> matcher, Category<Ob, Arr> cat) {
		PatternMatcher<Ob, Arr> matcher_i = matcher.apply(cat.target((Arr)r.getRule().left),
				cat.source((Arr)r2.getArrowF()));
		List<Arr> possibleis = matcher_i.determineMatches(true);

		PatternMatcher<Ob, Arr> matcher_j = matcher.apply(cat.target((Arr)r2.getRule().left),
				cat.source((Arr)r.getArrowF()));
		List<Arr> possiblejs = matcher_j.determineMatches(true);
		return new CandidateIsAndJs<Arr>(possibleis,possiblejs);

	}
	
	private <Ob,Arr> boolean asquareCommutes(List<Arr> possibleis,List<Arr> possiblejs, RuleApplication<Arr> r, RuleApplication<Arr> r2, Category<Ob, Arr> cat) {
		for (Arr i : possibleis) {
			for (Arr j : possiblejs) {
				if (((ComparableArrow<Arr>)cat.compose(i, (Arr)r2.getArrowF())).isTheSameAs((Arr)r.getMatch())
						&& ((ComparableArrow<Arr>) cat.compose(j, (Arr) r.getArrowF())).isTheSameAs((Arr)r2.getMatch())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public class CandidateIsAndJs<Arr>{
		public List<Arr> iList; 
		public List<Arr> jList; 
		public CandidateIsAndJs(List<Arr>piList,List<Arr>pjList ){
			this.iList=piList;
			this.jList=pjList;
		}
	}
}
