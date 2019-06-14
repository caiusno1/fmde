package org.upb.fmde.de.tests;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.upb.fmde.de.categories.concrete.finsets.OpFinSets.OpFinSets;

import java.io.IOException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.upb.fmde.de.categories.colimits.pushouts.DirectDerivation;
import org.upb.fmde.de.categories.colimits.pushouts.Span;
import org.upb.fmde.de.categories.concrete.finsets.CounterExampleChecker;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.concrete.finsets.FinSets;
import org.upb.fmde.de.categories.concrete.finsets.OpCounterExampleChecker;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.independence.RuleApplication;
import org.upb.fmde.de.categories.independence.RuleApplications;


public class ConfluenceTests {
	@Test
	public void parallelIndependence() {
		//TODO: Implement parallelIndependence
		FinSet L1 = new FinSet("L1", "existingCard");
		FinSet R1 = new FinSet("R1", "existingCard", "buy beer");
		FinSet K1 = new FinSet("K1", "existingCard");
		FinSet G = new FinSet("G", "install new sink", "buy some paint");
		
		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("existingCard"), R1.get("existingCard"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("existingCard"), L1.get("existingCard"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("existingCard"), G.get("install new sink"));
		
		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets,l1,r1);
		
		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);
		
		
		FinSet L2 = new FinSet("L2", "existingCard");
		FinSet R2 = new FinSet("R2", "existingCard", "buy beer");
		FinSet K2 = new FinSet("K2", "existingCard");
		
		TotalFunction r2 = new TotalFunction(K2, "r2", R2).addMapping(K2.get("existingCard"), R2.get("existingCard"));
		TotalFunction l2 = new TotalFunction(K2, "l2", L2).addMapping(K2.get("existingCard"), L2.get("existingCard"));
		TotalFunction m2 = new TotalFunction(L2, "m2", G).addMapping(L2.get("existingCard"), G.get("install new sink"));
		
		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets,l2,r2);
		
		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second, dpo1.get().pushout.left, m1); 
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second, dpo2.get().pushout.left, m2);
		boolean independent = new RuleApplications().areIndependentFinSets(rule1, rule2);
		Assert.assertTrue("should be independent", independent);
	}
	
	@Test
	public void parallelIndependence2() {
		//TODO: Implement parallelIndependence
		FinSet L1 = new FinSet("L1", "a");
		FinSet R1 = new FinSet("R1", "a", "b");
		FinSet K1 = new FinSet("K1", "a");
		FinSet G = new FinSet("G", "c", "d");
		
		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("a"), R1.get("a"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("a"), L1.get("a"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("a"), G.get("c"));
		
		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets,l1,r1);
		
		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);
				
		FinSet L2 = new FinSet("L2", "a");
		FinSet R2 = new FinSet("R2");
		FinSet K2 = new FinSet("K2");
		
		TotalFunction r2 = new TotalFunction(K2, "r2", R2);
		TotalFunction l2 = new TotalFunction(K2, "l2", L2);
		TotalFunction m2 = new TotalFunction(L2, "m2", G).addMapping(L2.get("a"), G.get("c"));
		
		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets,l2,r2);
		
		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second, dpo1.get().pushout.left, m1); 
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second, dpo2.get().pushout.left, m2);
		boolean independent = new RuleApplications().areIndependentFinSets(rule1, rule2);
		Assert.assertFalse("should not be independent", independent);
	}
}
