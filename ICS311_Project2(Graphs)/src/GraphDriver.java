/*
 * Copyright (c) <2013>, <Jasmine Ishigami>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <Jasmine Ishigami> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Creates a Directed Graph ADT based off of the node data and tie data provided in the .vna file.
 * It then output the following metrics:
 * 			|V| = Number of Vertices
 * 			|A| = Number of Arcs
 * 			Density = |A|/(|V|(|V|-1))
 * 			Degree distribution: minimum  average  maximum
 * 					inDegree     0        1.634    4       
 * 					outDegree    0        1.634    4       
 * 			Total Strongly Connected Components:12
 * 			Number of Strongly Connected Components: SCC								Size				Members
 * 													arbitrary value given by DFS        size of scc			list of members in scc
 * 				*note the sccs are ordered by decreasing size (largest to smallest)
 *			Percent Vertices in Largest Strongly Connected Component:  (|V| in largest scc / |V|) * 100
 * 
 * @author Jasmine Ishigami
 */
public class GraphDriver {
	static Graph<String> digraph;
	private static String nodeHeader = "";
	private static String arcHeader = "";
	private static String inDegreeStats = "";
	private static String outDegreeStats = "";
	private static int totalSCC = -1;
	private static int largestSCCVertices = -1;
	private static double maxGeodesicDistance = -1;
	private static boolean fullSCCPrint = false;
	private static boolean SCCPrint = true;
	
	/**
	 * Formats the output.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		digraph = new Graph<String>();
		parseData(args[0]);
		System.out.println("------------------------------------------------------------");
		System.out.println("Graph: " + args[0]);
		System.out.println("------------------------------------------------------------");
		System.out.println("|V| = " + digraph.numVertices());
		System.out.println("|A| = " + digraph.numArcs());
		System.out.println("|E| = " + digraph.numEdges());
		System.out.printf("Density = %.3f%n",getDensity());
		DegreeStats();
		System.out.println("Degree distribution: minimum  average  maximum");
		System.out.println("\tinDegree     " + inDegreeStats);
		System.out.println("\toutDegree    " + outDegreeStats);
		if(SCCPrint){
			ArrayList<LinkedList<Vertex<String>>> sccList = StronglyConnectedComponents();
			System.out.println("Total Strongly Connected Components:" + totalSCC);
			
			if(fullSCCPrint){
				System.out.println("Number of Strongly Connected Components: SCC\tSize\tMembers");
				for(int i = 0; i < sccList.size(); i++){
					LinkedList<Vertex<String>> vertexList = sccList.get(i);
					System.out.print("\t\t\t\t\t " +  vertexList.getFirst().getAnnotation("SCC"));
					System.out.print("\t" + vertexList.size() +"\t");
					Iterator<Vertex<String>> vertexIter = vertexList.iterator();
					while(vertexIter.hasNext()){
						Vertex<String> vertex = vertexIter.next();
						System.out.print(vertex.getKey() + ", ");
					}
					System.out.println();
				}
			}
			System.out.println("Percent Vertices in Largest Strongly Connected Component: " + PercentVertices() + "%");
			//undo the transpose
			Transpose();
		}
		System.out.println("Reciprocity: " + Reciprocity());
		System.out.println("Degree Correlation: " + DegreeCorrelation());
		System.out.println("Clustering Coefficient: " + ClusteringCoefficient());
		System.out.println("Mean Geodesic Distance " + MeanGeodesicDistance());
		System.out.println("Diameter: " + maxGeodesicDistance);
		
	}
	
	/**
	 * Parses in the data form the .vna file and adds the specified node or tie data as vertices or arc to graph.
	 * 
	 * @param fileName
	 */
	public static void parseData(String fileName){
		//System.out.println("Entered parseData()");
		final int NODE_DATA = 0;
		final int TIE_DATA = 1;
		final int ERROR_DATA = -1;
		int inputDataType = ERROR_DATA;
		
		if(fileName == null){
			System.out.println("ERROR: Please provide a .VNA file.");
			System.exit(0);
		}
		
		File file = new File(fileName);
		BufferedReader fileIn = null;
		
		try {
			String currLine;
			fileIn = new BufferedReader(new FileReader(file));
			
			while( (currLine = fileIn.readLine()) != null){
				String[] currArr = currLine.split(" ");
				if(currArr.length == 2){
					currLine = "";
					currLine =currArr[0] + " " + currArr[1];
				}
				if(currLine.charAt(0) == '*'){
					if(currLine.equalsIgnoreCase("*node data") || currLine.equalsIgnoreCase("*node properties") ){
						inputDataType = NODE_DATA;
						currLine = fileIn.readLine();
						nodeHeader = currLine;
					}if(currLine.equalsIgnoreCase("*tie data")){
						inputDataType = TIE_DATA;
						currLine = fileIn.readLine();
						arcHeader = currLine;
					}
				}else{
					if(inputDataType == NODE_DATA){
						//split the line on multiple spaces
						String[] strArr = currLine.split("\\s+");
						//only ID available
						if(strArr.length == 1){
							//System.out.println("Inserting Vertex: " + strArr[0]);
							digraph.insertVertex(strArr[0]);
						}else{
							//System.out.println("Inserting Vertex: " +strArr[0]);
							String dataStr = "";
							for(int i = 1; i < strArr.length; i++){
								dataStr = dataStr + strArr[i] + " ";
							}
							digraph.insertVertex(strArr[0], dataStr);
						}
					}else if(inputDataType == TIE_DATA){
						//split the line on multiple spaces
						String[] strArr = currLine.split("\\s+");
						if(strArr.length==2){
							//System.out.println("Inserting Arc: " + strArr[0] + " , " + strArr[1]);
							//add only if not a self-loop
							if(!(strArr[0].equals(strArr[1]))){
								digraph.insertArc(digraph.getVertex(strArr[0]) , digraph.getVertex(strArr[1]));
							}
						}else{
							//System.out.println("Inserting Arc: " + strArr[0] + " , " + strArr[1]);
							String dataStr = "";
							for(int i = 2; i < strArr.length; i++){
								dataStr = dataStr + strArr[i] + " ";
							}
							//add only if not a self-loop
							if(!(strArr[0].equals(strArr[1]))){
								digraph.insertArc(digraph.getVertex(strArr[0]) ,digraph.getVertex(strArr[1]), dataStr);
							}
						}
					}else{
						System.out.println("ERROR: .VNA file not set up correctly.  Need to have '*Node Data' or '*Tie Data' at beginning of file.");
						System.exit(0);
					}
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Please provide a file with data to run the Directed Graph tests on.");
		}
		//System.out.println("Exiting parseData()");
	}
	
	/**
	 * Helper method that finds the density of the current graph.
	 * 
	 * @return
	 */
	private static double getDensity(){
		double density =  (double)digraph.numArcs()/(double)((double)digraph.numVertices()*((double)digraph.numVertices()-1));
		return(density);
	}
	
	/**
	 * Helper method that updates the class Strings of the inDegree stats and outDegree stats based off of the current graph.
	 * 
	 */
	private static void DegreeStats(){
		//initialize all values to smallest or largest possible value
		long inMin = Long.MAX_VALUE;
		long outMin = Long.MAX_VALUE;
		long inMax = Long.MIN_VALUE;
		long outMax = Long.MIN_VALUE;
		long inSum = 0;
		long outSum = 0;
		
		Iterator<Vertex<String>> iter = digraph.vertices();
		while(iter.hasNext()){
			Vertex<String> currVertex = (Vertex<String>)iter.next();
			long inNum = currVertex.getInDegree();
			long outNum = currVertex.getOutDegree();
			//System.out.println(inNum + ", " + outNum);
			//increment sums
			inSum = inSum + inNum;
			outSum = outSum + outNum;
			//check if either indegree or outdegree are new max or min
			if(inNum < inMin){
				inMin = inNum;
			}
			if(outNum < outMin){
				outMin = outNum;
			}
			if(inNum > inMax){
				inMax = inNum;
			}
			if(outNum > outMax){
				outMax = outNum;
			}
		}
		
		inDegreeStats = String.format("%-8d %-8.3f %-8d", inMin, (double)inSum/(double)digraph.numVertices(), inMax);
		outDegreeStats = String.format("%-8d %-8.3f %-8d", outMin, (double)outSum/(double)digraph.numVertices(), outMax);
	}
	
	/**
	 * Runs depth first search on the current graph and depending on runNum:
	 * 		1 - bases DFS off any order of the vertices in the Graph
	 * 			returns vertices in topological sort based of their finished runtimes
	 * 		2 - bases DFS off of the given ordererd vertices
	 * 
	 * @param orderedVertices
	 * @param runNum
	 * @return
	 */
	private static LinkedList<Vertex<String>> DepthFirstSearch(LinkedList<Vertex<String>> orderedVertices, int runNum){
		//System.out.println("--------Entered: DepthFirstSearch--------");
		LinkedList<Vertex<String>> topologicalSort = new LinkedList<Vertex<String>>();
		Iterator<Vertex<String>> vertexIter;
		
		//checks if a specific vertex order is given
		if(orderedVertices == null){
			//if not, loop through the vertices in any given order
			vertexIter = digraph.vertices();
		}else{
			//if so, use the order given
			vertexIter = orderedVertices.iterator();
		}
		
		int time = 0;
		int scc = 0;
		
		//initialize all vertices to unvisited (white) with no parent pointer
		while(vertexIter.hasNext()){
			Vertex<String> vertex = vertexIter.next();
			digraph.setAnnotations(vertex, "COLOR", "white");
			digraph.setAnnotations(vertex, "PARENT", "nil");
		}
		
		if(orderedVertices == null){
			vertexIter = digraph.vertices();
		}else{
			vertexIter = orderedVertices.iterator();
		}

		while(vertexIter.hasNext()){
			Vertex<String> vertex = vertexIter.next();
			if(digraph.getAnnotations(vertex, "COLOR").equals("white")){
				//not yet visited so visit it
				scc++;
				time = DepthFirstSearchVisit(vertex, scc, time, topologicalSort, runNum);		
			}
		}
		totalSCC = scc;
		return(topologicalSort);
	}
	
	/**
	 * Helper method for DFS.
	 * 
	 * @param vertex
	 * @param scc
	 * @param time
	 * @param finishedVertices
	 * @param runNum
	 * @return
	 */
	private static int DepthFirstSearchVisit(Vertex<String> vertex, int scc, int time, LinkedList<Vertex<String>> finishedVertices, int runNum){
		//System.out.println("--------Entered: DepthFirstSearchVisit--------");
		time++;
		digraph.setAnnotations(vertex, "DISCOVERED", time);
		digraph.setAnnotations(vertex, "COLOR", "grey");
		digraph.setAnnotations(vertex, "SCC", scc);

		//check all the vertices adjacent to it (u -> v)
		Iterator<Vertex<String>> adjVertexIter = digraph.outAdjacentVertices(vertex);
		while(adjVertexIter.hasNext()){
			Vertex <String> adjVertex = adjVertexIter.next();
			if(digraph.getAnnotations(adjVertex, "COLOR").equals("white")){
				digraph.setAnnotations(adjVertex, "PARENT", vertex);
				DepthFirstSearchVisit(adjVertex, scc, time, finishedVertices, runNum);
			}
		}
		digraph.setAnnotations(vertex, "COLOR", "black");
		time++;
		digraph.setAnnotations(vertex, "FINISHED", time);
		if(runNum == 1){
			finishedVertices.add(0, vertex);
		}
		return(time);
	}
	
	/**
	 * Transposes (reverses all the arcs) in the current graph.
	 * 
	 */
	public static void Transpose(){
		//System.out.println("--------Entered: Transpose--------");
		Iterator<Arc<String>> arcIter = digraph.arcs();
		while(arcIter.hasNext()){
			Arc<String> arc = (Arc<String>) arcIter.next();
			//System.out.println("Transposing: " + arc.toString());
			digraph.reverseDirection(arc);
		}
	}
	
	/**
	 * Finds all the strongly connected components in the current graph and returns them as an ArrayList of sccs, ordered by size.
	 * 
	 * @return
	 */
	private static ArrayList<LinkedList<Vertex<String>>> StronglyConnectedComponents(){
		//System.out.println("--------Entered: SCC--------");
		LinkedList<Vertex<String>> topologicallySortedVertices;
		topologicallySortedVertices = DepthFirstSearch(null, 1);
		Transpose();
		DepthFirstSearch(topologicallySortedVertices, 2);
		
		//separate vertices by scc
		ArrayList<LinkedList<Vertex<String>>> sortedSCC = new ArrayList<LinkedList<Vertex<String>>>(totalSCC);
		//initialize all arraylist items with LinkedList
		for(int i = 0; i < totalSCC; i++){
			sortedSCC.add(i,new LinkedList<Vertex<String>>());
		}
		//go through all vertices in the graph and put them in the correct scc
		Iterator<Vertex<String>> vertexIter = topologicallySortedVertices.iterator();
		while(vertexIter.hasNext()){
			Vertex<String> vertex = (Vertex<String>)vertexIter.next();
			//System.out.println("vertex = " + vertex.getKey() + ", scc = " + scc);
			sortedSCC.get((int)vertex.getAnnotation("SCC")-1).add(vertex);
		}
		
		//order scc by decreasing size (largest -> smallest)
		ArrayList<LinkedList<Vertex<String>>> orderedSCC = new ArrayList<LinkedList<Vertex<String>>>(totalSCC);
		while(!sortedSCC.isEmpty()){
			if(sortedSCC.size()==1){
				orderedSCC.add(sortedSCC.remove(0));
			}else{
				int largest = 0;
				for(int i = 1; i < sortedSCC.size(); i++){
					if(sortedSCC.get(i).size() > sortedSCC.get(largest).size()){
						largest = i;
					}
				}
				orderedSCC.add(sortedSCC.remove(largest));
			}
		}
		//set the largestSCCVertices
		largestSCCVertices = orderedSCC.get(0).size();
		
		return(orderedSCC);
	}
	
	/**
	 * TODO
	 * 
	 * @return
	 */
	private static double PercentVertices(){
		double percentVertices  = ((double)largestSCCVertices/(double)digraph.numVertices()) * 100;
		return(percentVertices);
	}
	
	/**
	 * "If I friend someone, do they friend me back?"
	 * 
	 * Computes the percentage of directed edges (u,v) for which there is a reciprocated edge (v,u).
	 * Returns the fraction of edges that are reciprocated.
	 * 
	 * Formula:  # of reciprocated edges / total edges
	 * 
	 * @return
	 */
	private static double Reciprocity(){
		double reciprocated = 0;
		Iterator<Arc<String>> arcIter = digraph.arcs();
		
		while(arcIter.hasNext()){
			Arc<String> arc = arcIter.next();
			String sourceKey = arc.getSource().getKey();
			String targetKey = arc.getTarget().getKey();
			
			//if the reciprocal arc exists, increment the counter
			if(digraph.getArc(targetKey, sourceKey) != null){
				reciprocated++;
			}
		}		
		//System.out.println("ADSG");
		//System.out.println(reciprocated);
		//System.out.println(digraph.numArcs());
		return(reciprocated/digraph.numArcs());
	}
	
	/**
	 * "Do people friend those of similar popularity?"
	 * 
	 * AKA:  Assortative Mixing (describes the level to which things of different types mix with each other.
	 * Homophily:  high degree of correlation; "love of same"
	 * Computes the correlation between degree(u) and degree(v) over all pairs for which there exists edge(u,v).
	 * Usually done one undirected graphs but can be done on directed graphs if take into account both indegree and outdegree.
	 * 
	 * Formula: (S1*Se - S2^2) / (S1*S3 - S2^2)
	 * 			where -
	 * 					S1 = SUM(degree)	, over all vertices
	 * 					S2 = SUM(degree^2)	, over all vertices
	 * 					S3 = SUM(degree^3)	, over all vertices
	 * 					Se = 2 [ SUM(degree(u) * degree(v)) ] 	, over all edges
	 * @return
	 */
	private static double DegreeCorrelation(){
		double S1 = 0;
		double S2 = 0;
		double S3 = 0;
		double Se = 0;
		double count = 0;
		
		//go through all the vertices
		Iterator<Vertex<String>> vertexIter = digraph.vertices();
		while(vertexIter.hasNext()){
			Vertex<String> vertex = (Vertex<String>) vertexIter.next();
			double degree = digraph.degree(vertex);
			//System.out.println("vertex - " + vertex.getKey() + " : " + degree);
			S1 = S1 + degree;
			S2 = S2 + Math.pow(degree, 2.0);
			S3 = S3 + Math.pow(degree, 3.0);
		}
		
		//go over all the edges (undirected)
		Iterator<Arc<String>> edgeIterator = digraph.edges();
		while(edgeIterator.hasNext()){
			count ++;
			Arc<String> edge = edgeIterator.next();
			Se = Se + (digraph.degree(edge.getSource()) * digraph.degree(edge.getTarget()) );
		}
		Se = Se * 2;
		//System.out.println("S1: " + S1);
		//System.out.println("S2: " + S2);
		//System.out.println("S3: " + S3);
		//System.out.println("Se: " + Se);
		//System.out.println("Edges: " + count);
		double formula = ( ( (S1*Se) - Math.pow(S2, 2) ) / ( (S1 * S3) - Math.pow(S2, 2) ) );
		return formula;
	}
	
	/**
	 * "Are my friends friends with each other?"
	 * 
	 * AKA: Triadic Closure; Transitivity
	 * Computes the percentage of triads (3 vertices) for which there exists edges (u,v) and (u,w)(the denominator)
	 * for which there also exists edge (v,w)(the numerator).
	 * Fraction of paths of length 2 in the network that are closed.
	 * 
	 * Formula:
	 * 			Ci = (# of pairs of neighbors of i that are connected) / (# of pairs of neighbors of i)
	 * @return
	 */
	private static double ClusteringCoefficient(){
		double connectedTriangles = 0;
		double totalTriangles = 0;
		int count = 0;
		
		//iterate over all the vertices
		Iterator<Vertex<String>> vertexIter = digraph.vertices(); 
		while(vertexIter.hasNext()){ 
			count++;
			Vertex<String> a = vertexIter.next();
			Iterator<Vertex<String>> adjVertexIter = digraph.adjacentVertices(a);
			
			//then over all the adjacent vertices
			while(adjVertexIter.hasNext()){
				Vertex<String> b = adjVertexIter.next();
				Iterator<Vertex<String>> adjadjVertexIter = digraph.adjacentVertices(b);
				
				//then over all the adjacent vertices of the adjacent vertices
				while(adjadjVertexIter.hasNext()){
					Vertex<String> c = adjadjVertexIter.next();
					if(c != a){
						if(digraph.getArc(a.getKey(), c.getKey()) != null){
							connectedTriangles++;
						}
						else if(digraph.getArc(c.getKey(), a.getKey()) != null){
							connectedTriangles++;
						}
						totalTriangles++;
					}
				}
			}
		}		
		//System.out.println(connectedTriangles);
		//System.out.println(totalTriangles);
		//System.out.println(count);
		return connectedTriangles/totalTriangles; 
	} 
	
	/**
	 * "Is this a 'small world'?"
	 * 
	 * AKA: average path length
	 * maximum path length = diameter
	 * Compute the average length of the shortest path between each pair of vertices in the graph.
	 * 
	 * @return
	 */
	private static double MeanGeodesicDistance(){
		double geoDist = 0;
		double numPath = 0;
		
		//iterate over all the vertices
		Iterator<Vertex<String>> vertexIter = digraph.vertices();
		while(vertexIter.hasNext()){
			Vertex<String> start = vertexIter.next();
			//do BFS on each vertex
			BreathFirstSearch(start);
			Iterator<Vertex<String>> vertexIter2 = digraph.vertices();
			//and sum up all the shortest paths found by BFS as well as the number of paths
			while(vertexIter2.hasNext()){
				Vertex<String> vertex = vertexIter2.next();
				if( (digraph.getAnnotations(vertex, "DISTANCE") != "inf") && (digraph.getAnnotations(vertex, "DISTANCE") != "0.0")){
					numPath++;
					double distance = Double.valueOf((String)digraph.getAnnotations(vertex, "DISTANCE"));
					geoDist = geoDist + distance;
					if(distance > maxGeodesicDistance){
						maxGeodesicDistance = distance;
					}
				}
			}	
		}
		return(geoDist/numPath);		
	}
	
	/**
	 * Helper method for Mean Geodesic Distance of BFS.
	 * An ADT that uses the idea of a FIFO queue to explore nodes at each distance before going to the next distance (ie. a tsunami effect).
	 * It helps to find the shortest distance from a source vertex to all other vertices.
	 * 
	 * COLORS:	
	 * 		white - not yet visited
	 * 		gray  -	visited but not yet processed
	 * 		black -	visited and processed
	 * 
	 * @param start
	 */
	private static void BreathFirstSearch(Vertex<String> start){
		//iterate over all the vertices
		Iterator<Vertex<String>> vertexIter = digraph.vertices();
		while(vertexIter.hasNext()){
			Vertex<String> vertex = vertexIter.next();
			//intialize all vertices other than the start vertex
			if(!vertex.equals(start)){
				digraph.setAnnotations(vertex, "COLOR", "white");
				digraph.setAnnotations(vertex, "DISTANCE", "inf");
				digraph.setAnnotations(vertex, "PARENT", "nil");
			}
		}
		//set the start vertex info
		digraph.setAnnotations(start, "COLOR", "gray");
		digraph.setAnnotations(start, "DISTANCE", "0.0");
		digraph.setAnnotations(start, "PARENT", "nil");
		//initialze queue and add in start vertex
		Queue<Vertex<String>> queue = new LinkedList<Vertex<String>>();
		queue.offer(start);
		while(!queue.isEmpty()){
			Vertex<String> currVertex = queue.poll();
			//for each adjacent vertex
			Iterator<Vertex<String>> adjVertexIter =digraph.outAdjacentVertices(currVertex);
			while(adjVertexIter.hasNext()){
				Vertex<String> adjVertex = adjVertexIter.next();
				//that has not yet been visited
				if(digraph.getAnnotations(adjVertex, "COLOR").equals("white")){
					//update its color and parent, increment the distance
					digraph.setAnnotations(adjVertex, "COLOR", "gray");
					double dist =Double.valueOf((String)digraph.getAnnotations(currVertex, "DISTANCE"));
					dist = dist + 1.0;
					digraph.setAnnotations(adjVertex, "DISTANCE", String.valueOf(dist));
					digraph.setAnnotations(adjVertex, "PARENT", currVertex);
					queue.offer(adjVertex);
				}
			}
			digraph.setAnnotations(currVertex, "COLOR", "black");
		}
	}
}
