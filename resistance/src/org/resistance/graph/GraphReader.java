package org.resistance.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.resistance.sampling.sss.RandomCollection;

/**
 * 
 * @author qn
 */
public class GraphReader {

	/**
	 * Reads Net
	 * 
	 * @param input
	 * @return
	 */
	public Graph readNet(String input) {
		Graph g = new Graph();

		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = br.readLine()) != null) {
				boolean isEdge = false;
				if (line.startsWith("#")) {
					isEdge = true;
					continue;
				}

				if (!isEdge) {
					String nodeName = line;
					g.findNode(nodeName);
				} else {
					// edge
					String[] toks = line.split(" ");
					g.addUndirectedEdge(g.findNode(toks[0]), g.findNode(toks[1]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return g;
	}

	/**
	 * Reads edges
	 * 
	 * @param input
	 */
	public Graph readEdges(String input) {
		Graph g = new Graph();

		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || line.startsWith("%")) {
					continue;
				}

				// edge
				String[] toks = line.split(" ");
				g.addUndirectedEdge(g.findNode(toks[0]), g.findNode(toks[1]));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return g;
	}

	/**
	 * Reads edges
	 * 
	 * @param input
	 */
	public List<Edge> readEdgeList(String input) {
		List<Edge> edges = new ArrayList<Edge>();

		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || line.startsWith("%") || line.isEmpty()) {
					continue;
				}

				// edge
				String[] toks = line.split(" ");
				edges.add(new Edge(toks[0], toks[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return edges;
	}

	/**
	 * Read the weighted edges
	 * 
	 * @param input
	 * @return
	 */
	public RandomCollection<Edge> readWeightedEdgeList(String input) {
		RandomCollection<Edge> edges = new RandomCollection<Edge>();

		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || line.startsWith("%") || line.isEmpty()) {
					continue;
				}

				// edge
				String[] toks = line.split(" ");
				double weight = Double.parseDouble(toks[2]);
				edges.add(weight, new Edge(toks[0], toks[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return edges;
	}

	/**
	 * Returns a weighted graph from the input file
	 * 
	 * @param input
	 * @return
	 */
	public WeightedGraph readWeightedGraph(String input) {
		WeightedGraph wGraph = new WeightedGraph();

		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || line.startsWith("%") || line.isEmpty()) {
					continue;
				}

				// edge
				String[] toks = line.split(" ");
				double weight = Double.parseDouble(toks[2]);
				wGraph.addEdge(toks[0], toks[1], weight);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wGraph;
	}
}
