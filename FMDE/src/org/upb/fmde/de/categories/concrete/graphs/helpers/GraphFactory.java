package org.upb.fmde.de.categories.concrete.graphs.helpers;

import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;

public class GraphFactory {
	public Graph createGraph(int[][] graphMatrix, String name) {
		FinSet vertices = new FinSet(name+"_V");
		FinSet edges = new FinSet(name+"_E");
		
		TotalFunction src = new TotalFunction(edges, "src", vertices);
		TotalFunction trg = new TotalFunction(edges, "trg", vertices);
		
		for(int i = 0; i<graphMatrix.length; i++) {
			vertices.elts().add(name+":"+i);
			for(int j = 0; j<graphMatrix[i].length; j++) {
				if(graphMatrix[i][j] == 1 ) {
					String edge = name+":"+i+"->"+j;
					edges.elts().add(edge);
					src.addMapping(edge, name+":"+i);
					trg.addMapping(edge, name+":"+j);
				}
			}
		}
		return new Graph(name, edges, vertices, src, trg);
	}
	
	public GraphMorphism createGraphMorphism(Graph source, String label, Graph target, int[][] morphismMatrixVertices, int[][] morphismMatrixEdges) {
		TotalFunction f_E = new TotalFunction(source.edges(), label, target.edges());
		TotalFunction f_V = new TotalFunction(source.vertices(), label, target.vertices());
		
		for(int i = 0; i<morphismMatrixEdges.length; i++) {
			for(int j = 0; j<morphismMatrixEdges[i].length; j++) {
				if(morphismMatrixEdges[i][j]==1) {
					f_E.addMapping(source.edges().elts().get(i), target.edges().elts().get(j));
				}
			}
		}
		for(int i = 0; i<morphismMatrixVertices.length; i++) {
			for(int j = 0; j<morphismMatrixVertices[i].length; j++) {
				if(morphismMatrixVertices[i][j]==1) {
					f_V.addMapping(source.vertices().elts().get(i), target.vertices().elts().get(j));
				}
			}
		}
		return new GraphMorphism(label, source, target,f_E,f_V);
	}
	
	public GraphWrapper createGraphWrapper(String name) {
		return new GraphWrapper(name);
	}
	
	public GraphWrapper createGraphWrapper(int[][] graphMatrix, String name) {
		return new GraphWrapper(this.createGraph(graphMatrix, name));
	}
	
	private class GraphWrapper{
		private Graph g;
		private GraphWrapper(Graph pg) {
			this.g = pg;
		}
		private GraphWrapper(String name) {
			FinSet vertices = new FinSet(name+"_V");
			FinSet edges = new FinSet(name+"_E");
			
			TotalFunction src = new TotalFunction(edges, "src", vertices);
			TotalFunction trg = new TotalFunction(edges, "trg", vertices);
			
			g = new Graph(name, edges, vertices, src, trg);
		}
		
		public void addEdge(String srcLabel, String label, String trgLabel) {
			g.edges().elts().add(label);
			g.src().addMapping(g.vertices().get(srcLabel), g.edges().get(label));
			g.trg().addMapping(g.edges().get(label), g.vertices().get(srcLabel));
		}
		
		public void addNode(String label) {
			g.vertices().elts().add(label);
		}
		
		public Graph getGraph() {
			return this.g;
		}
	}
}
