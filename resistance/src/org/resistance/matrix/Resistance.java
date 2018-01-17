package org.resistance.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openimaj.math.matrix.PseudoInverse;
import org.resistance.graph.Edge;
import org.resistance.graph.EdgeIndex;

import Jama.Matrix;

/**
 * 
 * @author qn
 */
public class Resistance {
	/** inverse L */
	Matrix inverseL;

	/** resistance array */
	ArrayList<Double> resistanceArray;

	int N;

	int M;

	public void computeResistance(Matrix L, Matrix[] B) {
		N = L.getRowDimension();
		M = B.length;

		System.out.println("computeResistance N= " + N + ", M=" + M);

		// inverseL = PseudoInverse.pseudoInverse(L,1);
		inverseL = PseudoInverse.pseudoInverse(L);

		resistanceArray = new ArrayList<Double>(M);
		for (int row = 0; row < M; row++) {
			System.out.print("computeResistance of row " + row);
			Matrix Brow = B[row];
			Matrix resistanceMatrix = Brow.times(inverseL).times(Brow.transpose());
			double r = resistanceMatrix.get(0, 0);
			// System.out.println( " finished. Value r = " + r);
			resistanceArray.add(r);
		}

		System.out.println("computeResistance done !");
	}

	public void resistance(Matrix L, List<EdgeIndex> edgeIndices) {
		N = L.getRowDimension();
		M = edgeIndices.size();

		System.out.println("computeResistance N= " + N + ", M=" + M);

		// inverseL = PseudoInverse.pseudoInverse(L,1);
		inverseL = PseudoInverse.pseudoInverse(L);

		resistanceArray = new ArrayList<Double>(M);
		for (int row = 0; row < M; row++) {
			System.out.print("row " + row);
			EdgeIndex edgeIndex = edgeIndices.get(row);
			int s = edgeIndex.getS();
			int t = edgeIndex.getT();
			double r = inverseL.get(s, s) + inverseL.get(t, t) - inverseL.get(s, t) - inverseL.get(t, s);

			// System.out.println( " finished. Value r = " + r);
			resistanceArray.add(r);
		}

		System.out.println("computeResistance done !");
	}

	public void parallelResistance(Matrix L, final List<EdgeIndex> edgeIndices) {
		N = L.getRowDimension();
		M = edgeIndices.size();

		System.out.println("computeResistance N= " + N + ", M=" + M);

		// inverseL = PseudoInverse.pseudoInverse(L,1);
		inverseL = PseudoInverse.pseudoInverse(L);

		System.out.println("Finished Pseudo Inverse");

		List<Double> resis = IntStream.range(0, M).parallel().mapToDouble(row -> {
			System.out.print("row " + row);
			EdgeIndex edgeIndex = edgeIndices.get(row);
			int s = edgeIndex.getS();
			int t = edgeIndex.getT();
			double r = inverseL.get(s, s) + inverseL.get(t, t) - inverseL.get(s, t) - inverseL.get(t, s);
			return r;
		}).boxed().collect(Collectors.toList());

		resistanceArray = new ArrayList<Double>(resis);
		System.out.println("computeResistance done !");
	}

	public void computeInverse(Matrix L, final List<EdgeIndex> edgeIndices) {
		N = L.getRowDimension();
		M = edgeIndices.size();

		System.out.println("computeInverse N= " + N + ", M=" + M);

		// inverseL = PseudoInverse.pseudoInverse(L,1);
		inverseL = PseudoInverse.pseudoInverse(L);
		System.out.println("computeInverse done !");
	}

	public void parallelResistance(final List<EdgeIndex> edgeIndices) {
		List<Double> resis = IntStream.range(0, M).parallel().mapToDouble(row -> {
			System.out.print("row " + row);
			EdgeIndex edgeIndex = edgeIndices.get(row);
			int s = edgeIndex.getS();
			int t = edgeIndex.getT();
			double r = inverseL.get(s, s) + inverseL.get(t, t) - inverseL.get(s, t) - inverseL.get(t, s);
			return r;
		}).boxed().collect(Collectors.toList());

		resistanceArray = new ArrayList<Double>(resis);
		System.out.println("computeResistance done !");
	}

	public Matrix getInverseL() {
		return inverseL;
	}

	public ArrayList<Double> getResistanceArray() {
		return resistanceArray;
	}

	public double getResistance(int edgeIndex) {
		if (edgeIndex >= 0 && edgeIndex < M)
			return resistanceArray.get(edgeIndex);
		return -1;
	}

	public int getN() {
		return N;
	}

	public int getM() {
		return M;
	}

	/**
	 * @param edges
	 * @param resistance
	 * @return
	 */
	public static String toString(ArrayList<Edge> edges, List<Double> resistance) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < edges.size(); ++i) {
			Edge edge = edges.get(i);

			double r = resistance.get(i);
			sb.append(edge.getS() + " " + edge.getT() + " " + r + "\n");
		}

		return sb.toString();
	}

	/**
	 * @param edges
	 * @param resistance
	 * @return
	 */
	public static String sortByResistance(List<Edge> edges, List<Double> resistance) {
		int M = edges.size();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		IntStream.range(0, M).forEachOrdered(i -> indices.add(i));
		Collections.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return -resistance.get(o1.intValue()).compareTo(resistance.get(o2.intValue()));
			}
		});

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indices.size(); ++i) {
			int index = indices.get(i);
			Edge edge = edges.get(index);
			double r = resistance.get(index);
			sb.append(edge.getS() + " " + edge.getT() + " " + r + "\n");
		}

		return sb.toString();
	}
}
