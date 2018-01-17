package org.resistance.graph;

import java.util.HashMap;

/**
 * 
 * @author qn
 */
public class WeightedGraph {
	HashMap<Integer, Edge> edgeMap = new HashMap<Integer, Edge>();
	HashMap<Integer, Double> weightMap = new HashMap<Integer, Double>();
	HashMap<String, Integer> nodeMap = new HashMap<String, Integer>();
	HashMap<Integer, String> nodeIndexMap = new HashMap<Integer, String>();
	int totalNodes = 0;
	int totalEdges = 0;

	/**
	 * Adds an edge
	 * 
	 * @param source
	 * @param target
	 * @param weight
	 */
	public void addEdge(String source, String target, double weight) {
		edgeMap.put(totalEdges, new Edge(source, target));
		weightMap.put(totalEdges, weight);
		totalEdges++;

		if (!nodeMap.containsKey(source)) {
			nodeMap.put(source, totalNodes);
			nodeIndexMap.put(totalNodes, source);
			totalNodes++;
		}

		if (!nodeMap.containsKey(target)) {
			nodeMap.put(target, totalNodes);
			nodeIndexMap.put(totalNodes, target);
			totalNodes++;
		}
	}

	public HashMap<Integer, Edge> getEdgeMap() {
		return edgeMap;
	}

	public int getTotalNodes() {
		return totalNodes;
	}

	public HashMap<Integer, Double> getWeightMap() {
		return weightMap;
	}

	public double[][] toInverseWeightedMatrix() {
		double[][] result = new double[totalNodes][totalNodes];
		for (int i = 0; i < totalEdges; i++) {
			Edge e = edgeMap.get(i);
			int s = nodeMap.get(e.s);
			int t = nodeMap.get(e.t);
			result[s][t] = 1 / weightMap.get(i);
		}

		return result;
	}

	/**
	 * Write to gml
	 * 
	 * @param positions
	 * @return
	 */
	public String toGML(double[][] positions) {
		StringBuilder builder = new StringBuilder();
		int nodeCount = 0;

		builder.append("Creator \"ogdf::GraphIO::writeGML\"\n");
		builder.append("graph [\n");
		builder.append("  directed 1\n");

		for (nodeCount = 0; nodeCount < totalNodes; nodeCount++) {
			builder.append("  node [\n");
			builder.append("    id " + nodeCount + "\n");
			builder.append("    label \"" + nodeIndexMap.get(nodeCount) + "\"\n");
			builder.append("    graphics [\n");
			builder.append("      x " + positions[0][nodeCount] + "\n");
			builder.append("      y " + positions[1][nodeCount] + "\n");
			builder.append("    ]\n");
			builder.append("  ]\n");
			nodeCount++;
		}

		builder.append("#\n");

		for (int i = 0; i < totalEdges; i++) {
			Edge e = edgeMap.get(i);
			int s = nodeMap.get(e.s);
			int t = nodeMap.get(e.t);
			builder.append("  edge [\n");
			builder.append("    source " + s + "\n");
			builder.append("    target " + t + "\n");
			builder.append("  ]\n");

		}

		builder.append("]\n");

		return builder.toString();
	}
}
