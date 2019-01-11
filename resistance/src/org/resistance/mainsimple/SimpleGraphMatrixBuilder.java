package org.resistance.mainsimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.resistance.graph.Edge;
import org.resistance.graph.EdgeIndex;
import org.resistance.graph.Graph;

import Jama.Matrix;

/**
 * 
 * @author qn
 */
public class SimpleGraphMatrixBuilder {
	// Builds the Laplician matrix
	Matrix L;

	ArrayList<Edge> edges;

	ArrayList<EdgeIndex> edgeIndices;

	public void build(Graph g) {
		ArrayList<String> nodeLabels = g.getSortedNodes();
		HashMap<String, Integer> nodeIndexMap = new HashMap<String, Integer>();
		edges = new ArrayList<Edge>();
		edgeIndices = new ArrayList<EdgeIndex>();

		int N = nodeLabels.size();
		L = new Matrix(N, N);

		HashMap<String, Set<String>> adjacency = g.getAdjacency();

		System.out.println("build L of g");
		// set diagonal entries
		int M = 0;
		for (int i = 0; i < N; ++i) {
			nodeIndexMap.put(nodeLabels.get(i), i);
			int deg = adjacency.get(nodeLabels.get(i)).size();
			L.set(i, i, deg);
			M += deg;
		}

		M /= 2;

		int e = 0;

		System.out.println("build L, B of g");
		// set edges
		for (int s = 0; s < nodeLabels.size(); ++s) {
			for (String neigh : adjacency.get(nodeLabels.get(s))) {
				int t = nodeIndexMap.get(neigh);

				if (s != t) {
					// set -1 if s != t and v_s, v_t are connected
					L.set(s, t, -1);
				}

				if (s < t) {
					// set 1 in s and -1 in t for B matrix
					edges.add(new Edge(nodeLabels.get(s), nodeLabels.get(t)));
					edgeIndices.add(new EdgeIndex(s, t));
					e++;
				}
			}
		}
	}

	public Matrix getL() {
		return L;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public ArrayList<EdgeIndex> getEdgeIndices() {
		return edgeIndices;
	}
}
