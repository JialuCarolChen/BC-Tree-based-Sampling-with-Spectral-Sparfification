package org.resistance.main;

import java.util.ArrayList;
import java.util.Arrays;

import org.resistance.graph.EdgeIndex;
import org.resistance.graph.Graph;
import org.resistance.graph.GraphReader;
import org.resistance.matrix.GraphMatrixBuilder;
import org.resistance.matrix.Resistance;
import org.resistance.util.Utils;

import Jama.Matrix;

/**
 * 
 * Parallel resistance computation
 * 
 *
 */
public class ParResistanceCompMain {
	public static void main(String[] args) {
		for (String input : new String[] {
				"/Users/carolchen/Desktop/test1edge.txt"
				//"/Users/carolchen/Desktop/Summer Research/BC-Tree-based-Sampling-with-Spectral-Sparfification/realworld_data/facebook/facebook_combined.txt"

		}) {
			try {
				GraphReader reader = new GraphReader();
				Graph g = reader.readEdges(input);

				GraphMatrixBuilder builder = new GraphMatrixBuilder();
				builder.build(g);

				Matrix L = builder.getL();
				ArrayList<EdgeIndex> edgeIndices = builder.getEdgeIndices();

				long startTime = System.currentTimeMillis();

				Resistance resis = new Resistance();
				resis.parallelResistance(L, edgeIndices);

				long endTime = System.currentTimeMillis();

				System.out.println("resis vector = " + Arrays.toString(resis.getResistanceArray().toArray()));

				Utils.outputToFile(input + ".resis.time",
						"N=" + resis.getN() + ", M=" + resis.getM() + "\n" + (endTime - startTime));

				String resisString = Resistance.toString(builder.getEdges(), resis.getResistanceArray());
				String output = input + ".resis";
				Utils.outputToFile(output, resisString);
				System.out.println("Finished");

				resisString = Resistance.sortByResistance(builder.getEdges(), resis.getResistanceArray());
				output = input + ".resisSorted";
				Utils.outputToFile(output, resisString);
				System.out.println("Finished");

				reader = null;
				g = null;
				builder = null;
				L = null;
				resis = null;
				resisString = null;
			} catch (Exception ex) {
				System.err.println(input);
				ex.printStackTrace();
			} finally {
				System.gc();
			}
		}
	}
}
