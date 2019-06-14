package org.upb.fmde.de.categories.independence;

import java.util.List;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSetPatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSets;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.GraphPatternMatcher;
import org.upb.fmde.de.categories.concrete.graphs.Graphs;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphPrinter;

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
}
