package org.resistance.sampling;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.resistance.graph.Edge;
import org.resistance.graph.GraphReader;
import org.resistance.sampling.sss.RandomCollection;
import org.resistance.util.Utils;

/**
 * 
 * @author qn
 */
public class SSS_SamplingMain {

	public static void main(String[] args) {

		double[] RATIOS = new double[] { 0.01, 0.02, 0.03, 0.04, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5,
				0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95 };

		for (String file : new String[] { "/home/qn/RES_samplingSpectral/dss/can_144.gv" }) {
			try {
				GraphReader reader = new GraphReader();

				String input = file + ".resisSorted";
				String fileName = Paths.get(file).getFileName().toString();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0) {
					fileName = fileName.substring(0, pos);
				}

				System.out.println(input);

				RandomCollection<Edge> edges = reader.readWeightedEdgeList(input);

				Path dirPath = Paths
						.get(Paths.get(file).getParent().toString() + File.separator + fileName + "_samples");
				if (!Files.exists(dirPath)) {
					Files.createDirectory(dirPath);
				}

				long startTime = System.nanoTime();
				long totalTime = 0;

				for (double ratio : RATIOS) {
					System.out.println(" sampling at ratio " + (int) (ratio * 100) + "%");

					startTime = System.nanoTime();
					List<Edge> sample = EdgeSampling.weightedRandomSample(edges, ratio);

					totalTime += (System.nanoTime() - startTime);

					// output the sample to a file
					String sampleFile = dirPath.toString() + File.separator + fileName + "_re_" + (int) (ratio * 100);

					Utils.outputToFile(sampleFile, Utils.toString(sample));
					Utils.outputToFile(input + ".sample.time", "" + totalTime / 1000);
				}

				System.out.println("Finished");
			} catch (Exception ex) {
				System.err.println(file);
				ex.printStackTrace();
			}
		}
	}
}
