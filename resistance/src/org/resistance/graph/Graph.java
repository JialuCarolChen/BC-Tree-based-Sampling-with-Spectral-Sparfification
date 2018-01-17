package org.resistance.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author qn
 */
public class Graph {
	HashMap<String, Set<String>> adjacency = new HashMap<String, Set<String>>();

	public String findNode(String name) {
		if (!adjacency.containsKey(name)) {
			adjacency.put(name, new LinkedHashSet<String>());
		}
		return name;
	}

	public void addEdge(String source, String target) {
		Set<String> adj = adjacency.get(source);
		if (adj == null) {
			adj = new LinkedHashSet<String>();
			adjacency.put(source, adj);
		}
		adj.add(target);
	}

	public void addUndirectedEdge(String source, String target) {
		addEdge(source, target);
		addEdge(target, source);
	}

	public boolean hasEdge(String source, String target) {
		Set<String> adj = adjacency.get(source);
		if (adj != null && adj.contains(target))
			return true;
		return false;
	}

	/**
	 * @return the adjacency
	 */
	public HashMap<String, Set<String>> getAdjacency() {
		return adjacency;
	}

	public ArrayList<String> getSortedNodes() {
		ArrayList<String> nodes = new ArrayList<String>(adjacency.keySet());
		Collections.sort(nodes);
		return nodes;
	}

	@Override
	public String toString() {
		return "Graph [adjacency=" + adjacency + "]";
	}
}
