package org.resistance.graph;


import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Created by carolchen on 11/2/18.
 * A graph convertor to transform bct_componets file to multiple edgelist files for experiment purpose
 */
public class GraphConvertor {

    public Integer num_of_components;
    public String file_path;
    public ArrayList<Graph> components;
    public Graph this_graph;

    public GraphConvertor(String input_file_path) {
        //file input path
        file_path = input_file_path;
        //number of componets
        num_of_components = 0;
        //an array of all the bct components
        components = new ArrayList<Graph>();
        //current component
        this_graph = new Graph();
    }

    //a method to read all the components into a graph
    public ArrayList<Graph> readEdges() {

        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {

            String line;
            Boolean isedges = false;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                //when read the "---" sign, stop end reading the edge, record the graph
                if (line.startsWith("---")) {
                    isedges=false;
                    components.add(this_graph);
                    System.out.println("Read the "+num_of_components+" component");
                    this_graph= new Graph();

                }

                //read edges
                if (isedges==true) {
                    // edge
                    String[] toks = line.split(" ");

                    this_graph.addUndirectedEdge(this_graph.findNode(toks[0]), this_graph.findNode(toks[1]));

                }

                //when read the "edge" sign, start recording the edge
                if (line.startsWith("edges")) {
                    isedges=true;
                    num_of_components=num_of_components+1;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return components;
    }

    //a method to create a folder with edgelist file of each components
    public void createFiles(){
        //create directory
        File theDir = new File(file_path+"_dir");

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }

        Integer g_num=0;
        //write files
        for (Graph g: components) {
            g_num=g_num+1;
            ArrayList<String> edgelist = g.edgelist;
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(file_path+"_dir/bct"+g_num.toString()));
                for (String edge:edgelist){
                    writer.write(edge);
                    writer.newLine();
                }
                writer.close();

            }catch (Exception ex){
                System.out.println(ex.fillInStackTrace());
            }

        }






    }


    //for testing
    public static void main(String[] args) {
        GraphConvertor gc = new GraphConvertor("/Users/carolchen/Desktop/Summer Research/BC-Tree-based-Sampling-with-Spectral-Sparfification/realworld_data/facebook/facebook_bct_comps");
        gc.readEdges();
        gc.createFiles();
        System.out.println("The number of components: "+gc.num_of_components.toString());


    }






}
