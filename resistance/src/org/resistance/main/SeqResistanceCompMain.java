package org.resistance.main;

import java.util.Arrays;

import org.resistance.graph.Graph;
import org.resistance.graph.GraphReader;
import org.resistance.matrix.GraphMatrixBuilder;
import org.resistance.matrix.Resistance;
import org.resistance.util.Utils;

import Jama.Matrix;

/**
 * Sequential version
 *
 */
public class SeqResistanceCompMain {
	public static void main(String[] args) {
		for (String input : new String[] {
				 "/home/jche6589/PycharmProjects/BC-Tree-based-Sampling-with-Spectral-Sparfification/realworld_data/can_144.gv"
		}) {
			try {
				GraphReader reader = new GraphReader();
				Graph g = reader.readEdges(input);

				GraphMatrixBuilder builder = new GraphMatrixBuilder();
				builder.build(g);

				Matrix L = builder.getL();
				Matrix[] B = builder.getB();

				long startTime = System.currentTimeMillis();

				Resistance resis = new Resistance();

				resis.computeResistance(L, B);


				long endTime = System.currentTimeMillis();


				System.out.println("resis vector = " + Arrays.toString(resis.getResistanceArray().toArray()));

				Utils.outputToFile(input + ".resis.time", "" + (endTime - startTime));

				String resisString = Resistance.toString(builder.getEdges(), resis.getResistanceArray());
				String output = input + ".resis";
				Utils.outputToFile(output, resisString);
				System.out.println("Finished");

				resisString = Resistance.sortByResistance(builder.getEdges(), resis.getResistanceArray());
				output = input + ".resisSorted";
				Utils.outputToFile(output, resisString);
				System.out.println("Finished");
			} catch (Exception ex) {
				System.err.println(input);
				ex.printStackTrace();
			}
		}
	}
}
