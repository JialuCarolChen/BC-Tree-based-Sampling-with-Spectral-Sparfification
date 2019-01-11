package org.resistance.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.resistance.graph.Edge;

/**
 * 
 * @author qn
 */
public class Utils {
	/**
	 * @param file
	 * @param s
	 */
	public static void outputToFile(String file, String s) {
		String output = file;
		PrintWriter writer = null;
		;
		try {
			writer = new PrintWriter(output, "UTF-8");
			writer.println(s);
			writer.flush();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	/**
	 * @param edges
	 * @return
	 */
	public static String toString(List<Edge> edges) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < edges.size(); ++i) {
			Edge edge = edges.get(i);
			sb.append(edge.getS() + " " + edge.getT() + "\n");
		}

		return sb.toString();
	}
}
