package org.resistance.bctCompare;

import Jama.Matrix;
import org.resistance.graph.EdgeIndex;
import org.resistance.graph.Graph;
import org.resistance.graph.GraphReader;
import org.resistance.matrix.GraphMatrixBuilder;
import org.resistance.matrix.Resistance;
import org.resistance.util.Utils;
import org.resistance.graph.GraphConvertor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by carolchen on 12/2/18.
 */
public class ResisBctCompare {
    HashMap<Integer, Long> bct_resis_time;
    long whole_resis_time;
    long bct_resis_time_sum;
    //path of the whole network file
    String input_file_path;
    //path of the bct_components file
    String bct_input_file_path;

    public ResisBctCompare(String input_file, String bct_input_file) {
        input_file_path = input_file;
        bct_input_file_path = bct_input_file;
        bct_resis_time = new HashMap<>();
        whole_resis_time = 0;
        bct_resis_time_sum = 0;
    }

    public void wholeResisCompute() {
        try {
            GraphReader reader = new GraphReader();
            Graph g = reader.readEdges(input_file_path);

            GraphMatrixBuilder builder = new GraphMatrixBuilder();
            builder.build(g);

            Matrix L = builder.getL();
            ArrayList<EdgeIndex> edgeIndices = builder.getEdgeIndices();

            long startTime = System.currentTimeMillis();

            Resistance resis = new Resistance();
            resis.parallelResistance(L, edgeIndices);

            long endTime = System.currentTimeMillis();

            System.out.println("resis vector = " + Arrays.toString(resis.getResistanceArray().toArray()));

            whole_resis_time=endTime-startTime;
            System.out.println("Time: "+whole_resis_time);

            String resisString = Resistance.toString(builder.getEdges(), resis.getResistanceArray());
            String output = input_file_path + ".resis";
            Utils.outputToFile(output, resisString);
            System.out.println("Finished");

            resisString = Resistance.sortByResistance(builder.getEdges(), resis.getResistanceArray());
            output = input_file_path + ".resisSorted";
            Utils.outputToFile(output, resisString);
            System.out.println("Finished");

            reader = null;
            g = null;
            builder = null;
            L = null;
            resis = null;
            resisString = null;
        } catch (Exception ex) {
            System.err.println(input_file_path);
            ex.printStackTrace();
        } finally {
            System.gc();
        }



    }

    public void bctResisCompare() {

        String bct_path;
        long startTime;
        long endTime;

        //Decompose the bct component file
        GraphConvertor gc = new GraphConvertor(bct_input_file_path);
        gc.readEdges();
        gc.createFiles();
        System.out.println("The number of components: "+gc.num_of_components.toString());
        Integer bct_num = gc.num_of_components;

        //Create folder
        File bct_resis_dir = new File(bct_input_file_path + "_resis");
        // if the directory does not exist, create it
        if (!bct_resis_dir.exists()) {
            System.out.println("creating directory: " + bct_resis_dir.getName());
            boolean result = false;

            try{
                bct_resis_dir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                se.printStackTrace();
            }
        }


        //Compute the resistance value of each bct components
        for (Integer i=1; i<=bct_num; i++) {

            bct_path = bct_input_file_path + "_dir/bct" + i.toString();

            try {
                GraphReader reader = new GraphReader();
                Graph g = reader.readEdges(bct_path);

                GraphMatrixBuilder builder = new GraphMatrixBuilder();
                builder.build(g);

                Matrix L = builder.getL();
                ArrayList<EdgeIndex> edgeIndices = builder.getEdgeIndices();

                startTime = System.currentTimeMillis();

                Resistance resis = new Resistance();
                resis.parallelResistance(L, edgeIndices);

                endTime = System.currentTimeMillis();

                System.out.println("resis vector = " + Arrays.toString(resis.getResistanceArray().toArray()));
                //accumulating the time used to compute the resistance value of each component
                if(resis.getM()>3 && resis.getN()>3) {
                    bct_resis_time.put(i, endTime-startTime);
                    bct_resis_time_sum = bct_resis_time_sum + (endTime-startTime);
                }

                //recording the resistance value
                String resisString = Resistance.toString(builder.getEdges(), resis.getResistanceArray());
                String output = bct_input_file_path + "_resis/bct"+i.toString();
                Utils.outputToFile(output, resisString);
                System.out.println("Finished Recording the resistance value of component "+i);
                //sorting the resistance value
                resisString = Resistance.sortByResistance(builder.getEdges(), resis.getResistanceArray());
                output = bct_input_file_path + "_resis/bct"+i.toString()+"_sorted";
                Utils.outputToFile(output, resisString);
                System.out.println("Finished Sorting the resistance value of component "+i);


                reader = null;
                g = null;
                builder = null;
                L = null;
                resis = null;
                resisString = null;
            } catch (Exception ex) {
                System.err.println(bct_input_file_path);
                ex.printStackTrace();
            } finally {
                System.gc();
            }

        }

    }

    public static void main(String[] args) {
        String input_path="/Users/chenjialu/Desktop/BC-Tree-based-Sampling-with-Spectral-Sparfification/Synthetic_Graphs/synthesis_tree_4_9_10_10_30_True_2/synthesis_tree_4_9_10_10_30_True_2";
        String bct_input_path="/Users/chenjialu/Desktop/BC-Tree-based-Sampling-with-Spectral-Sparfification/Synthetic_Graphs/synthesis_tree_4_9_10_10_30_True_2/synthesis_tree_4_9_10_10_30_True_2_bct_comps";
        
        ResisBctCompare rbc = new ResisBctCompare(input_path, bct_input_path);
        rbc.wholeResisCompute();
        rbc.bctResisCompare();
        //print and compare the resis time
        System.out.println(rbc.bct_resis_time);
        Utils.outputToFile(input_path + ".resis.timeCompare",
                "whole: " + rbc.whole_resis_time+"\n" +
                        "bcts: " + "\n" +
                        rbc.bct_resis_time + "\n" +
                        "total_bct: " + rbc.bct_resis_time_sum
        );



    }













}
