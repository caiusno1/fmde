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
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.graphs.GraphPrinter;
import org.upb.fmde.de.categories.concrete.graphs.Graphs;
import org.upb.fmde.de.categories.concrete.graphs.helpers.GraphFactory;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs;
import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.independence.RuleApplication;
import org.upb.fmde.de.categories.independence.RuleApplications;

public class ConfluenceTests {
	@Test
	public void parallelIndependence() {
		// TODO: Implement parallelIndependence
		FinSet L1 = new FinSet("L1", "existingCard");
		FinSet R1 = new FinSet("R1", "existingCard", "buy beer");
		FinSet K1 = new FinSet("K1", "existingCard");
		FinSet G = new FinSet("G", "install new sink", "buy some paint");

		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("existingCard"), R1.get("existingCard"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("existingCard"), L1.get("existingCard"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("existingCard"), G.get("install new sink"));

		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets, l1, r1);

		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);

		FinSet L2 = new FinSet("L2", "existingCard");
		FinSet R2 = new FinSet("R2", "existingCard", "buy beer");
		FinSet K2 = new FinSet("K2", "existingCard");

		TotalFunction r2 = new TotalFunction(K2, "r2", R2).addMapping(K2.get("existingCard"), R2.get("existingCard"));
		TotalFunction l2 = new TotalFunction(K2, "l2", L2).addMapping(K2.get("existingCard"), L2.get("existingCard"));
		TotalFunction m2 = new TotalFunction(L2, "m2", G).addMapping(L2.get("existingCard"), G.get("install new sink"));
		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets, l2, r2);

		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1,null);
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2, null);
		boolean independent = new RuleApplications().areParallelIndependent(rule1, rule2, FinSets.FinSets);
		Assert.assertTrue("should be independent", independent);
	}

	/**
	 * This test should check that the constructed rule applications in finsets are
	 * not parallel independent
	 */
	@Test
	public void notParallelIndependent() {
		FinSet L1 = new FinSet("L1", "a");
		FinSet R1 = new FinSet("R1", "a", "b");
		FinSet K1 = new FinSet("K1", "a");
		FinSet G = new FinSet("G", "c", "d");

		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("a"), R1.get("a"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("a"), L1.get("a"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("a"), G.get("c"));

		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets, l1, r1);

		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);
		FinSet L2 = new FinSet("L2", "a");
		FinSet R2 = new FinSet("R2");
		FinSet K2 = new FinSet("K2");

		TotalFunction r2 = new TotalFunction(K2, "r2", R2);
		TotalFunction l2 = new TotalFunction(K2, "l2", L2);
		TotalFunction m2 = new TotalFunction(L2, "m2", G).addMapping(L2.get("a"), G.get("c"));

		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets, l2, r2);

		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1, null);
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2, null);

		boolean independent = new RuleApplications().areParallelIndependent(rule1, rule2, FinSets.FinSets);
		Assert.assertFalse("should not be independent", independent);
	}

	@Test
	public void NotParallelIndependenceMoreComplex() {
		// TODO: Implement parallelIndependence
		FinSet L1 = new FinSet("L1", "a", "b", "c");
		FinSet R1 = new FinSet("R1", "a", "d");
		FinSet K1 = new FinSet("K1", "a");
		FinSet G = new FinSet("G", "a", "b", "c", "d");

		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("a"), R1.get("a"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("a"), L1.get("a"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("a"), G.get("a"))
				.addMapping(L1.get("b"), G.get("b")).addMapping(L1.get("c"), G.get("c"));

		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets, l1, r1);

		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);

		FinSet L2 = new FinSet("L2", "a", "b", "c");
		FinSet R2 = new FinSet("R2", "a", "d");
		FinSet K2 = new FinSet("K2", "a");

		TotalFunction r2 = new TotalFunction(K2, "r2", R2).addMapping(K2.get("a"), R2.get("a"));
		TotalFunction l2 = new TotalFunction(K2, "l2", L2).addMapping(K2.get("a"), L2.get("a"));
		TotalFunction m2 = new TotalFunction(L2, "m2", G)
				.addMapping(L2.get("a"), G.get("d"))
				.addMapping(L2.get("b"), G.get("b"))
				.addMapping(L2.get("c"), G.get("c"));

		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets, l2, r2);

		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1, null);
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2, null);
		boolean independent = new RuleApplications().areParallelIndependent(rule1, rule2, FinSets.FinSets);
		Assert.assertFalse("should not be independent", independent);
	}
	
	@Test
	public void ParallelIndependenceMoreComplex() {
		// TODO: Implement parallelIndependence
		FinSet L1 = new FinSet("L1", "a", "b", "c");
		FinSet R1 = new FinSet("R1", "a", "d");
		FinSet K1 = new FinSet("K1", "a");
		FinSet G = new FinSet("G", "a", "b", "c", "d","e");

		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("a"), R1.get("a"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("a"), L1.get("a"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("a"), G.get("a"))
				.addMapping(L1.get("b"), G.get("b")).addMapping(L1.get("c"), G.get("c"));

		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets, l1, r1);

		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);

		FinSet L2 = new FinSet("L2", "a", "b", "c");
		FinSet R2 = new FinSet("R2", "a", "d");
		FinSet K2 = new FinSet("K2", "a");

		TotalFunction r2 = new TotalFunction(K2, "r2", R2).addMapping(K2.get("a"), R2.get("a"));
		TotalFunction l2 = new TotalFunction(K2, "l2", L2).addMapping(K2.get("a"), L2.get("a"));
		TotalFunction m2 = new TotalFunction(L2, "m2", G)
				.addMapping(L2.get("a"), G.get("a"))
				.addMapping(L2.get("b"), G.get("d"))
				.addMapping(L2.get("c"), G.get("e"));

		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets, l2, r2);

		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1,null);
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2,null);
		boolean independent = new RuleApplications().areParallelIndependent(rule1, rule2, FinSets.FinSets);
		Assert.assertTrue("should be independent", independent);
	}
	
	@Test
	public void notSequentialIndependentSets() {
		FinSet L1 = new FinSet("L1", "a");
		FinSet R1 = new FinSet("R1", "a_2", "b");
		FinSet K1 = new FinSet("K1", "a_1");
		FinSet G = new FinSet("G", "a");

		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("a_1"), R1.get("a_2"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("a_1"), L1.get("a"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("a"), G.get("a"));

		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets, l1, r1);

		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);

		FinSet L2 = new FinSet("L2", "a", "b");
		FinSet R2 = new FinSet("R2");
		FinSet K2 = new FinSet("K2");

		TotalFunction r2 = new TotalFunction(K2, "r2", R2);
		TotalFunction l2 = new TotalFunction(K2, "l2", L2);
		TotalFunction m2 = new TotalFunction(L2, "m2", dpo1.get().pushout.left.trg()).addMapping(L2.get("a"), dpo1.get().pushout.left.trg().get("a")).addMapping(L2.get("b"), dpo1.get().pushout.left.trg().get("b"));

		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets, l2, r2);

		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1,dpo1.get().pushout.right);
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2, dpo2.get().pushout.right);
		boolean independent = new RuleApplications().areSequentialIndependent(rule1, rule2, FinSets.FinSets);
		Assert.assertFalse("should not be independent", independent);
	}
	
	@Test
	public void sequentialIndependentSets() {
		FinSet L1 = new FinSet("L1", "a");
		FinSet R1 = new FinSet("R1", "a_2", "b");
		FinSet K1 = new FinSet("K1", "a_1");
		FinSet G = new FinSet("G", "a");

		TotalFunction r1 = new TotalFunction(K1, "r1", R1).addMapping(K1.get("a_1"), R1.get("a_2"));
		TotalFunction l1 = new TotalFunction(K1, "l1", L1).addMapping(K1.get("a_1"), L1.get("a"));
		TotalFunction m1 = new TotalFunction(L1, "m1", G).addMapping(L1.get("a"), G.get("a"));

		Span<TotalFunction> L1_K1_R1 = new Span<TotalFunction>(FinSets.FinSets, l1, r1);

		Optional<DirectDerivation<TotalFunction>> dpo1 = FinSets.FinSets.doublePushout(L1_K1_R1, m1);

		FinSet L2 = new FinSet("L2", "a_5");
		FinSet R2 = new FinSet("R2", "a_4", "c");
		FinSet K2 = new FinSet("K2", "a_3");

		TotalFunction r2 = new TotalFunction(K2, "r2", R2).addMapping(K2.get("a_3"), R2.get("a_4"));
		TotalFunction l2 = new TotalFunction(K2, "l2", L2).addMapping(K2.get("a_3"), L2.get("a_5"));
		TotalFunction m2 = new TotalFunction(L2, "m2", dpo1.get().pushout.left.trg()).addMapping(L2.get("a_5"), dpo1.get().pushout.left.trg().get("a"));


		Span<TotalFunction> L2_K2_R2 = new Span<TotalFunction>(FinSets.FinSets, l2, r2);

		Optional<DirectDerivation<TotalFunction>> dpo2 = FinSets.FinSets.doublePushout(L2_K2_R2, m2);
		RuleApplication<TotalFunction> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1,dpo1.get().pushout.right);
		RuleApplication<TotalFunction> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2, dpo2.get().pushout.right);
		boolean independent = new RuleApplications().areSequentialIndependent(rule1, rule2, FinSets.FinSets);
		Assert.assertTrue("should be independent", independent);
	}
	
	//########### Graphs #####################
	
	@Test
	public void ParallelIndependenceGraphs() {
		// TODO: Implement parallelIndependence
		FinSet L1_Nodes = new FinSet("L1", "a");
		FinSet R1_Nodes = new FinSet("R1", "a","b");
		FinSet K1_Nodes = new FinSet("K1", "a");
		FinSet G_Nodes = new FinSet("G", "a");
		
		FinSet L1_Edges = new FinSet("L1");
		FinSet R1_Edges = new FinSet("R1", "m");
		FinSet K1_Edges = new FinSet("K1");
		FinSet G_Edges = new FinSet("G");
		
		TotalFunction src_L1=new TotalFunction(L1_Edges, "src", L1_Nodes);
		TotalFunction trg_L1=new TotalFunction(L1_Edges, "src", L1_Nodes);
		TotalFunction src_R1=new TotalFunction(R1_Edges, "src", R1_Nodes)
				.addMapping(R1_Edges.get("m"),R1_Nodes.get("a"));
		TotalFunction trg_R1=new TotalFunction(R1_Edges, "src", R1_Nodes)
				.addMapping(R1_Edges.get("m"),R1_Nodes.get("b"));
		TotalFunction src_K1=new TotalFunction(K1_Edges, "src", K1_Nodes);
		TotalFunction trg_K1=new TotalFunction(K1_Edges, "src", K1_Nodes);
		TotalFunction src_G=new TotalFunction(G_Edges, "src", G_Nodes);
		TotalFunction trg_G=new TotalFunction(G_Edges, "src", G_Nodes);

		
		TotalFunction r1_nodes = new TotalFunction(K1_Nodes, "r1", R1_Nodes).addMapping(K1_Nodes.get("a"), R1_Nodes.get("a"));
		TotalFunction l1_nodes = new TotalFunction(K1_Nodes, "l1", L1_Nodes).addMapping(K1_Nodes.get("a"), L1_Nodes.get("a"));
		TotalFunction m1_nodes = new TotalFunction(L1_Nodes, "m1", G_Nodes).addMapping(L1_Nodes.get("a"), G_Nodes.get("a"));
		
		TotalFunction r1_edges = new TotalFunction(K1_Edges, "r1", R1_Edges);
		TotalFunction l1_edges = new TotalFunction(K1_Edges, "l1", L1_Edges);
		TotalFunction m1_edges = new TotalFunction(L1_Edges, "m1", G_Edges);
				
		Graph L1_Graph = new Graph("L1",L1_Edges,L1_Nodes,src_L1,trg_L1);
		Graph K1_Graph = new Graph("K1",K1_Edges,K1_Nodes,src_K1,trg_K1);
		Graph R1_Graph = new Graph("R1",R1_Edges,R1_Nodes,src_R1,trg_R1);
		Graph G_Graph = new Graph("G",G_Edges,G_Nodes,src_G,trg_G);
		
		GraphMorphism r1 = new GraphMorphism("r1", K1_Graph, R1_Graph, r1_edges, r1_nodes);
		GraphMorphism l1 = new GraphMorphism("k1", K1_Graph, L1_Graph, l1_edges, l1_nodes);
		GraphMorphism m1 = new GraphMorphism("l1", L1_Graph, G_Graph, m1_edges, m1_nodes);
		Span<GraphMorphism> L1_K1_R1 = new Span<GraphMorphism>(Graphs.Graphs, l1, r1);

		Optional<DirectDerivation<GraphMorphism>> dpo1 = Graphs.Graphs.doublePushout(L1_K1_R1, m1);

		FinSet L2_Nodes = new FinSet("L2", "a");
		FinSet R2_Nodes = new FinSet("R2", "a","b");
		FinSet K2_Nodes = new FinSet("K2", "a");
		
		FinSet L2_Edges = new FinSet("L2");
		FinSet R2_Edges = new FinSet("R2", "m");
		FinSet K2_Edges = new FinSet("K2");
		
		TotalFunction src_L2=new TotalFunction(L2_Edges, "src", L2_Nodes);
		TotalFunction trg_L2=new TotalFunction(L2_Edges, "src", L2_Nodes);
		TotalFunction src_R2=new TotalFunction(R2_Edges, "src", R2_Nodes)
				.addMapping(R2_Edges.get("m"),R2_Nodes.get("a"));
		TotalFunction trg_R2=new TotalFunction(R2_Edges, "src", R2_Nodes)
				.addMapping(R2_Edges.get("m"),R2_Nodes.get("b"));
		TotalFunction src_K2=new TotalFunction(K2_Edges, "src", K2_Nodes);
		TotalFunction trg_K2=new TotalFunction(K2_Edges, "src", K2_Nodes);
		
		TotalFunction r2_nodes = new TotalFunction(K2_Nodes, "r2", R2_Nodes).addMapping(K2_Nodes.get("a"), R2_Nodes.get("a"));
		TotalFunction l2_nodes = new TotalFunction(K2_Nodes, "l2", L2_Nodes).addMapping(K2_Nodes.get("a"), L2_Nodes.get("a"));
		TotalFunction m2_nodes = new TotalFunction(L2_Nodes, "m2", G_Nodes).addMapping(L2_Nodes.get("a"), G_Nodes.get("a"));
		
		TotalFunction r2_edges = new TotalFunction(K2_Edges, "r2", R2_Edges);
		TotalFunction l2_edges = new TotalFunction(K2_Edges, "l2", L2_Edges);
		TotalFunction m2_edges = new TotalFunction(L2_Edges, "m2", G_Edges);
				
		Graph L2_Graph = new Graph("L2",L2_Edges,L2_Nodes,src_L2,trg_L2);
		Graph K2_Graph = new Graph("K2",K2_Edges,K2_Nodes,src_K2,trg_K2);
		Graph R2_Graph = new Graph("R2",R2_Edges,R2_Nodes,src_R2,trg_R2);
		
		GraphMorphism r2 = new GraphMorphism("r2", K2_Graph, R2_Graph, r2_edges, r2_nodes);
		GraphMorphism l2 = new GraphMorphism("k2", K2_Graph, L2_Graph, l2_edges, l2_nodes);
		GraphMorphism m2 = new GraphMorphism("l2", L2_Graph, G_Graph, m2_edges, m2_nodes);
		Span<GraphMorphism> L2_K2_R2 = new Span<GraphMorphism>(Graphs.Graphs, l2, r2);

		Optional<DirectDerivation<GraphMorphism>> dpo2 = Graphs.Graphs.doublePushout(L2_K2_R2, m2);
		
		
		RuleApplication<GraphMorphism> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1, null);
		RuleApplication<GraphMorphism> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2, null);
		boolean independent = new RuleApplications().areParallelIndependent(rule1, rule2, Graphs.Graphs);
		Assert.assertTrue("should be independent", independent);
	}
	@Test
	public void sequentialIndependentGraphs() {
		GraphFactory fac = new GraphFactory();
		Graph L1=fac.createGraph(new int[][] {{0}}, "L1");
		Graph K1=fac.createGraph(new int[][] {{0}}, "K1");
		Graph R1=fac.createGraph(new int[][] {{0,1},{0,0}}, "R1");
		Graph G=fac.createGraph(new int[][] {{1}}, "K1");
		
		GraphMorphism l1= fac.createGraphMorphism(K1, "l1", L1, new int[][] {{1}}, new int[][] {});
		GraphMorphism r1= fac.createGraphMorphism(K1, "r1", R1, new int[][] {{1}}, new int[][] {});
		GraphMorphism m1= fac.createGraphMorphism(L1, "m1", G, new int[][] {{1}}, new int[][] {});
		Span<GraphMorphism> L1_K1_R1 = new Span<GraphMorphism>(Graphs.Graphs, l1, r1);
		
		Optional<DirectDerivation<GraphMorphism>> dpo1 = Graphs.Graphs.doublePushout(L1_K1_R1, m1);
		
		Graph L2=fac.createGraph(new int[][] {{0}}, "L2");
		Graph K2=fac.createGraph(new int[][] {{0}}, "K2");
		Graph R2=fac.createGraph(new int[][] {{0,1},{0,0}}, "R2");
		
		GraphMorphism l2= fac.createGraphMorphism(K2, "l2", L2, new int[][] {{1}}, new int[][] {});
		GraphMorphism r2= fac.createGraphMorphism(K2, "r2", R2, new int[][] {{1}}, new int[][] {});
		GraphMorphism m2= fac.createGraphMorphism(L2, "m2", dpo1.get().pushout.left.trg(), new int[][] {{1}}, new int[][] {});
		Span<GraphMorphism> L2_K2_R2 = new Span<GraphMorphism>(Graphs.Graphs, l2, r2);
		
		Optional<DirectDerivation<GraphMorphism>> dpo2 = Graphs.Graphs.doublePushout(L2_K2_R2, m2);
		
		RuleApplication<GraphMorphism> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1,dpo1.get().pushout.right);
		RuleApplication<GraphMorphism> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2,dpo2.get().pushout.right);
		boolean independent = new RuleApplications().areSequentialIndependent(rule1, rule2, Graphs.Graphs);
		assertTrue("should be independent",independent);
	}
	@Test
	public void notSequentialIndependentGraphs() {
		GraphFactory fac = new GraphFactory();
		Graph TG = fac.createGraph(new int[][] {}, "TG");
		Graph L1=fac.createGraph(new int[][] {{0,1},{0,0}}, "L1");
		Graph K1=fac.createGraph(new int[][] {{0}}, "K1");
		Graph R1=fac.createGraph(new int[][] {{0,0},{1,0}}, "R1");
		Graph G=fac.createGraph(new int[][] {{0,1}, {0,0}}, "K1");
		
		GraphMorphism l1= fac.createGraphMorphism(K1, "l1", L1, new int[][] {{1,0}, {0,0}}, new int[][] {});
		GraphMorphism r1= fac.createGraphMorphism(K1, "r1", R1, new int[][] {{1,0}, {0,0}}, new int[][] {});
		GraphMorphism m1= fac.createGraphMorphism(L1, "m1", G, new int[][] {{1,0}, {0,1}}, new int[][] {{1}});
		Span<GraphMorphism> L1_K1_R1 = new Span<GraphMorphism>(Graphs.Graphs, l1, r1);
		
		Optional<DirectDerivation<GraphMorphism>> dpo1 = Graphs.Graphs.doublePushout(L1_K1_R1, m1);
		
		Graph L2=fac.createGraph(new int[][] {{0,0}, {1, 0}}, "L2");
		Graph K2=fac.createGraph(new int[][] {{0}}, "K2");
		Graph R2=fac.createGraph(new int[][] {{0,0},{1,0}}, "R2");
		
		GraphMorphism l2= fac.createGraphMorphism(K2, "l2", L2, new int[][] {{1}}, new int[][] {});
		GraphMorphism r2= fac.createGraphMorphism(K2, "r2", R2, new int[][] {{1}}, new int[][] {});
		GraphMorphism m2= fac.createGraphMorphism(L2, "m2", dpo1.get().pushout.left.trg(), new int[][] {{1,0}, {0,1}}, new int[][] {{1}});
		Span<GraphMorphism> L2_K2_R2 = new Span<GraphMorphism>(Graphs.Graphs, l2, r2);
		
		Optional<DirectDerivation<GraphMorphism>> dpo2 = Graphs.Graphs.doublePushout(L2_K2_R2, m2);
		
		RuleApplication<GraphMorphism> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1,dpo1.get().pushout.right);
		RuleApplication<GraphMorphism> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2,dpo2.get().pushout.right);
		boolean independent = new RuleApplications().areSequentialIndependent(rule1, rule2, Graphs.Graphs);
		assertFalse("should not be independent",independent);
	}
	//########### TGraphs #####################
	@Test
	public void sequentialIndependentTGraphs() {
		GraphFactory fac = new GraphFactory();
		Graph TG = fac.createGraph(new int[][] {{1}}, "TG");
		Graph uL1=fac.createGraph(new int[][] {{0}}, "uL1");
		TGraph L1 = new TGraph("L1", fac.createGraphMorphism(uL1, "t", TG, new int[][] {{1}}, new int[][] {{0}}));
		Graph uK1=fac.createGraph(new int[][] {{0}}, "K1");
		TGraph K1 = new TGraph("K1", fac.createGraphMorphism(uK1, "t", TG, new int[][] {{1}}, new int[][] {{0}}));
		Graph uR1=fac.createGraph(new int[][] {{0,1},{0,0}}, "R1");
		TGraph R1 = new TGraph("R1", fac.createGraphMorphism(uR1, "t", TG, new int[][] {{1,0},{1,0}}, new int[][] {{1}}));
		Graph uG=fac.createGraph(new int[][] {{0}}, "G");
		TGraph G = new TGraph("G", fac.createGraphMorphism(uG, "t", TG, new int[][] {{1}}, new int[][] {{0}}));
		
		TGraphMorphism l1= new TGraphMorphism("l1",fac.createGraphMorphism(uK1, "l1", uL1, new int[][] {{1}}, new int[][] {}),K1,L1);
		TGraphMorphism r1= new TGraphMorphism("r1",fac.createGraphMorphism(uK1, "r1", uR1, new int[][] {{1}}, new int[][] {}),K1,R1);
		TGraphMorphism m1= new TGraphMorphism("m1",fac.createGraphMorphism(uL1, "m1", uG, new int[][] {{1}}, new int[][] {}),L1,G);
		Span<TGraphMorphism> L1_K1_R1 = new Span<TGraphMorphism>(TGraphs.TGraphsFor(TG), l1, r1);
		
		Optional<DirectDerivation<TGraphMorphism>> dpo1 = TGraphs.TGraphsFor(TG).doublePushout(L1_K1_R1, m1);
		
		Graph uL2=fac.createGraph(new int[][] {{0}}, "L2");
		TGraph L2=new TGraph("L2", fac.createGraphMorphism(uL2, "t", TG, new int[][] {{1}}, new int[][] {{}}));
		Graph uK2=fac.createGraph(new int[][] {{0}}, "K2");
		TGraph K2=new TGraph("K2", fac.createGraphMorphism(uK2, "t", TG, new int[][] {{1}}, new int[][] {{}}));
		Graph uR2=fac.createGraph(new int[][] {{0,1},{0,0}}, "R2");
		TGraph R2=new TGraph("R2", fac.createGraphMorphism(uR2, "t", TG, new int[][] {{1,0},{1,0}}, new int[][] {{1}}));
		
		TGraphMorphism l2= new TGraphMorphism("l2",fac.createGraphMorphism(uK2, "l2", uL2, new int[][] {{1}}, new int[][] {}),K2,L2);
		TGraphMorphism r2= new TGraphMorphism("r2",fac.createGraphMorphism(uK2, "r2", uR2, new int[][] {{1}}, new int[][] {}),K2,R2);
		TGraphMorphism m2= new TGraphMorphism("m2",fac.createGraphMorphism(uL2, "m2", dpo1.get().pushout.left.trg().type().src(), new int[][] {{1}}, new int[][] {}),L2,dpo1.get().pushout.left.trg());
		Span<TGraphMorphism> L2_K2_R2 = new Span<TGraphMorphism>(TGraphs.TGraphsFor(TG), l2, r2);
		
		Optional<DirectDerivation<TGraphMorphism>> dpo2 = TGraphs.TGraphsFor(TG).doublePushout(L2_K2_R2, m2);
		
		RuleApplication<TGraphMorphism> rule1 = new RuleApplication<>(L1_K1_R1, dpo1.get().pushoutComplement.second,
				dpo1.get().pushout.left, m1,dpo1.get().pushout.right);
		RuleApplication<TGraphMorphism> rule2 = new RuleApplication<>(L2_K2_R2, dpo2.get().pushoutComplement.second,
				dpo2.get().pushout.left, m2,dpo2.get().pushout.right);
		boolean independent = new RuleApplications().areSequentialIndependent(rule1, rule2, TGraphs.TGraphsFor(TG));
		assertTrue("should be independent",independent);
	}
}
