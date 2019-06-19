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
	public boolean areParallelIndependent(RuleApplication<TotalFunction> r, RuleApplication<TotalFunction> r2, FinSets cat) {
		return this.areParallelIndependent(r, r2,(P,G)-> new FinSetPatternMatcher(P, G),cat);
	}
	
	public boolean areSequentialIndependent(RuleApplication<TotalFunction> r, RuleApplication<TotalFunction> r2, FinSets cat) {
		return this.areSequentialIndependent(r, r2,(P,G)-> new FinSetPatternMatcher(P, G),cat);
	}
	
	public boolean areParallelIndependent(RuleApplication<GraphMorphism> r, RuleApplication<GraphMorphism> r2, Graphs cat) {
		return this.areParallelIndependent(r, r2,(P,G)-> new GraphPatternMatcher(P, G), cat);
	}
	
	public boolean areSequentialIndependent(RuleApplication<GraphMorphism> r, RuleApplication<GraphMorphism> r2, Graphs cat) {
		return this.areSequentialIndependent(r, r2,(P,G)-> new GraphPatternMatcher(P, G), cat);
	}
	
	public boolean areParallelIndependent(RuleApplication<TGraphMorphism> r, RuleApplication<TGraphMorphism> r2, TGraphs cat) {
		//TODO find the TGraphPatternMatcher & implement this method
		return false;
	}
	
	public boolean areSequentialIndependent(RuleApplication<TGraphMorphism> r, RuleApplication<TGraphMorphism> r2, TGraphs cat) {
		//TODO find the TGraphPatternMatcher & implement this method
		return false;
	}
	
	private <Ob,Arr> boolean areParallelIndependent(RuleApplication<Arr> r, RuleApplication<Arr> r2, BiFunction<Ob,Ob,PatternMatcher<Ob, Arr>> matcher, Category<Ob, Arr> cat) {
		CandidateIsAndJs<Arr> candiates = parallelFindIsAndJs(r, r2, matcher, cat);
		return parallelAsquareCommutes(candiates.iList, candiates.jList, r, r2, cat);	
	}
	
	private <Ob,Arr> boolean areSequentialIndependent(RuleApplication<Arr> r, RuleApplication<Arr> r2, BiFunction<Ob,Ob,PatternMatcher<Ob, Arr>> matcher, Category<Ob, Arr> cat) {
		CandidateIsAndJs<Arr> candiates = sequentialFindIsAndJs(r, r2, matcher, cat);
		return sequentialAsquareCommutes(candiates.iList, candiates.jList, r, r2, cat);	
	}
	
	private <Ob,Arr> CandidateIsAndJs<Arr> parallelFindIsAndJs(RuleApplication<Arr> r, RuleApplication<Arr> r2, BiFunction<Ob,Ob,PatternMatcher<Ob, Arr>> matcher, Category<Ob, Arr> cat) {
		PatternMatcher<Ob, Arr> matcher_i = matcher.apply(cat.target((Arr)r.getRule().left),
				cat.source((Arr)r2.getArrowF()));
		List<Arr> possibleis = matcher_i.determineMatches(true);

		PatternMatcher<Ob, Arr> matcher_j = matcher.apply(cat.target((Arr)r2.getRule().left),
				cat.source((Arr)r.getArrowF()));
		List<Arr> possiblejs = matcher_j.determineMatches(true);
		return new CandidateIsAndJs<Arr>(possibleis,possiblejs);

	}
	
	private <Ob,Arr> CandidateIsAndJs<Arr> sequentialFindIsAndJs(RuleApplication<Arr> r, RuleApplication<Arr> r2, BiFunction<Ob,Ob,PatternMatcher<Ob, Arr>> matcher, Category<Ob, Arr> cat) {
		PatternMatcher<Ob, Arr> matcher_i = matcher.apply(cat.target((Arr)r.getRule().right),
				cat.source((Arr)r2.getArrowF()));
		List<Arr> possibleis = matcher_i.determineMatches(true);

		PatternMatcher<Ob, Arr> matcher_j = matcher.apply(cat.target((Arr)r2.getRule().left),
				cat.source((Arr)r.getArrowG()));
		List<Arr> possiblejs = matcher_j.determineMatches(true);
		return new CandidateIsAndJs<Arr>(possibleis,possiblejs);
	}

	private <Ob,Arr> boolean sequentialAsquareCommutes(List<Arr> possibleis,List<Arr> possiblejs, RuleApplication<Arr> r, RuleApplication<Arr> r2, Category<Ob, Arr> cat) {
		for (Arr i : possibleis) {
			for (Arr j : possiblejs) {
				if (((ComparableArrow<Arr>)cat.compose(i, (Arr)r2.getArrowF())).isTheSameAs((Arr)r.getCoMatch())
						&& ((ComparableArrow<Arr>) cat.compose(j, (Arr) r.getArrowG())).isTheSameAs((Arr)r2.getMatch())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private <Ob,Arr> boolean parallelAsquareCommutes(List<Arr> possibleis,List<Arr> possiblejs, RuleApplication<Arr> r, RuleApplication<Arr> r2, Category<Ob, Arr> cat) {
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
