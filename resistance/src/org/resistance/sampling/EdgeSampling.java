package org.resistance.sampling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.resistance.graph.Edge;
import org.resistance.sampling.sss.RandomCollection;

/**
 * Creates samples of a graph
 * 
 * @author qn
 */
public class EdgeSampling {

	/**
	 * Assumes the edges are sorted with highest weights first
	 * 
	 * @param edges
	 *            the list of edges to sample
	 * @param ratio
	 *            the ratio to keep
	 * @return
	 */
	public static List<Edge> sortedSample(List<Edge> edges, double ratio) {
		int m = edges.size();
		int n = (int) (ratio * m);

		List<Edge> result = new ArrayList<Edge>();
		if (n > 0) {
			result = edges.subList(0, n);
		}

		return result;
	}

	/**
	 * Sample edges with random probability
	 * 
	 * @param edges
	 * @param ratio
	 * @return
	 */
	public static List<Edge> randomSample(List<Edge> edges, double ratio) {
		int m = edges.size();
		int n = (int) (ratio * m);
		List<Edge> result = Arrays.asList();
		if (n > 0) {
			// shuffle the edge indices
			List<Integer> arr = IntStream.range(0, m).boxed().collect(Collectors.toList());
			Collections.shuffle(arr);

			// then select the edges to the sample from top down
			arr = arr.subList(0, n);

			result = arr.stream().map(i -> edges.get(i)).collect(Collectors.toList());
		}

		return result;
	}

	/**
	 * Sample weighted edges with random probability
	 * 
	 * @param edges
	 * @param ratio
	 * @return
	 */
	public static List<Edge> weightedRandomSample(RandomCollection<Edge> randCollection, double ratio) {
		int m = randCollection.size();
		int n = (int) (ratio * m);

		List<Edge> result = new ArrayList<Edge>();

		for (int i = 0; i < n; i++) {
			Edge e = randCollection.next();
			result.add(e);
		}

		return result;
	}
}
