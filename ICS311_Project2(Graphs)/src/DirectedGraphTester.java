import java.util.Iterator;
import java.util.Map;


public class DirectedGraphTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//all tests based off of "celegansneural.vna" file
		DirectedGraphDriver.digraph = new DirectedGraph<String>();
		boolean celegansneural = false;
		
		//TEST: parseData()
		System.out.println("-----------------------------TESTING: parseData()-----------------------------");
		DirectedGraphDriver.parseData(".\\testFiles\\SCC-Test.vna");
		
		if(celegansneural){
			System.out.println("Does vertex key and data match?");
			System.out.println("\tVertex:");
			System.out.printf("\t%20s %20s %20s %20s\n", "Expected Key","Actual Key","Expected Data", "Actual Data");
			System.out.printf("\t%20s %20s %20s %20s\n", "71", DirectedGraphDriver.digraph.getVertex("71").getKey(), "40", DirectedGraphDriver.digraph.getVertex("71").getData().toString());
			System.out.printf("\t%20s %20s %20s %20s\n", "268", DirectedGraphDriver.digraph.getVertex("268").getKey(), "282", DirectedGraphDriver.digraph.getVertex("268").getData().toString());
			
			
			System.out.println("Does arc key and data match?");
			System.out.println("\tArc:");
			System.out.printf("\t%20s %20s %20s %20s %20s %20s\n", "Expected Source", "Actual Source", "Expected Target", "Actual Target", "Expected Data", "Actual Data");
			System.out.printf("\t%20s %20s %20s %20s %20s %20s\n", "8", DirectedGraphDriver.digraph.getArc("8","36").getSource().getKey(), "36", DirectedGraphDriver.digraph.getArc("8","36").getTarget().getKey(),"3",DirectedGraphDriver.digraph.getArc("8", "36").getData().toString());
			System.out.printf("\t%20s %20s %20s %20s %20s %20s\n", "155", DirectedGraphDriver.digraph.getArc("155","161").getSource().getKey(), "161", DirectedGraphDriver.digraph.getArc("155","161").getTarget().getKey(),"1",DirectedGraphDriver.digraph.getArc("155", "161").getData().toString());
		
			System.out.println("Does the outVertexIterator match?");
		}else{
			System.out.println("Does vertex key and data match?");
			System.out.println("\tVertex:");
			System.out.printf("\t%20s %20s %20s %20s\n", "Expected Key","Actual Key","Expected Data", "Actual Data");
			System.out.printf("\t%20s %20s %20s %20s\n", "24", DirectedGraphDriver.digraph.getVertex("24").getKey(), "null", DirectedGraphDriver.digraph.getVertex("24").getData());
			System.out.printf("\t%20s %20s %20s %20s\n", "58", DirectedGraphDriver.digraph.getVertex("58").getKey(), "null", DirectedGraphDriver.digraph.getVertex("58").getData());
			
			
			System.out.println("Does arc key and data match?");
			System.out.println("\tArc:");
			System.out.printf("\t%20s %20s %20s %20s %20s %20s\n", "Expected Source", "Actual Source", "Expected Target", "Actual Target", "Expected Data", "Actual Data");
			System.out.printf("\t%20s %20s %20s %20s %20s %20s\n", "16", DirectedGraphDriver.digraph.getArc("16","11").getSource().getKey(), "11", DirectedGraphDriver.digraph.getArc("16","11").getTarget().getKey(),"1",DirectedGraphDriver.digraph.getArc("16", "11").getData().toString());
			System.out.printf("\t%20s %20s %20s %20s %20s %20s\n", "36", DirectedGraphDriver.digraph.getArc("36","30").getSource().getKey(), "30", DirectedGraphDriver.digraph.getArc("36","30").getTarget().getKey(),"1",DirectedGraphDriver.digraph.getArc("36", "30").getData().toString());
		
			
			System.out.println("Does the outVertexIterator match?");
			System.out.println("\tOutVertex:");
			System.out.printf("%20s %20s %20s\n", "Vertex", "Expected Out Vertices", "Actual Out Vertices");
			Iterator iter = DirectedGraphDriver.digraph.outAdjacentVertices(DirectedGraphDriver.digraph.getVertex("37"));
			String str = "";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.printf("\t%20s %20s %20s\n", "37", "32, 36", str);
			iter = DirectedGraphDriver.digraph.outAdjacentVertices(DirectedGraphDriver.digraph.getVertex("17"));
			str = "";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.printf("\t%20s %20s %20s\n", "17", "10, 11, 14, 18", str);
			
			
			System.out.println("Does the inVertexIterator match?");
			System.out.println("\tInVertex:");
			System.out.printf("%20s %20s %20s\n", "Vertex", "Expected In Vertices", "Actual In Vertices");
			iter = DirectedGraphDriver.digraph.inAdjacentVertices(DirectedGraphDriver.digraph.getVertex("7"));
			str = "";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.printf("%20s %20s %20s\n", "7", "1, 41", str);
			iter = DirectedGraphDriver.digraph.inAdjacentVertices(DirectedGraphDriver.digraph.getVertex("19"));
			str = "";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.printf("\t%20s %20s %20s\n", "19", "14, 18", str);
			
			/*
			System.out.println("Does removeArc work?");
			System.out.println("\tArcRemove:");
			System.out.printf("%20s\n", "Arc");
			System.out.println("\t\t" + DirectedGraphDriver.digraph.getArc("11", "18").toString());
			System.out.printf("%20s\n", "Before");
			iter = DirectedGraphDriver.digraph.outAdjacentVertices(DirectedGraphDriver.digraph.getVertex("11"));
			str = "outAdjVertices to 11: ";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.println("\t\t" + str);
			iter = DirectedGraphDriver.digraph.inAdjacentVertices(DirectedGraphDriver.digraph.getVertex("18"));
			str = "inAdjVertices to 18: ";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.println("\t\t" + str);
			DirectedGraphDriver.digraph.removeArc(DirectedGraphDriver.digraph.getArc("11", "18"));
			System.out.printf("%20s\n", "After");
			iter = DirectedGraphDriver.digraph.outAdjacentVertices(DirectedGraphDriver.digraph.getVertex("11"));
			str = "outAdjVertices to 11: ";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.println("\t\t" + str);
			iter = DirectedGraphDriver.digraph.inAdjacentVertices(DirectedGraphDriver.digraph.getVertex("18"));
			str = "inAdjVertices to 18: ";
			while(iter.hasNext()){
				str = str + ((Vertex<String>)iter.next()).getKey() + ", ";
			}
			System.out.println("\t\t" + str);
			*/
			
			System.out.println("Does transpose work?");
			System.out.println("\tBefore:");
			System.out.println("\t\tInAdjTree:");
			for(Map.Entry<String, BinarySearchTree<String>> entry: DirectedGraphDriver.digraph.inAdjTree.entrySet()){
				System.out.println("\t\t\tKey - " + entry.getKey());
				System.out.println("\t\t\t\tBST - " + entry.getValue().toFullString());
			}
			System.out.println("\t\tOutAdjTree:");
			for(Map.Entry<String, BinarySearchTree<String>> entry: DirectedGraphDriver.digraph.outAdjTree.entrySet()){
				System.out.println("\t\t\tKey - " + entry.getKey());
				System.out.println("\t\t\t\tBST - " + entry.getValue().toFullString());
			}
			DirectedGraphDriver.digraph.outAdjTree.toString();
			DirectedGraphDriver.Transpose();
			System.out.println("\tAfter:");
			System.out.println("\t\tInAdjTree:");
			for(Map.Entry<String, BinarySearchTree<String>> entry: DirectedGraphDriver.digraph.inAdjTree.entrySet()){
				System.out.println("\t\t\tKey - " + entry.getKey());
				System.out.println("\t\t\t\tBST - " + entry.getValue().toFullString());
			}
			DirectedGraphDriver.digraph.inAdjTree.toString();
			System.out.println("\t\tOutAdjTree:");
			for(Map.Entry<String, BinarySearchTree<String>> entry: DirectedGraphDriver.digraph.outAdjTree.entrySet()){
				System.out.println("\t\t\tKey - " + entry.getKey());
				System.out.println("\t\t\t\tBST - " + entry.getValue().toFullString());
			}
			DirectedGraphDriver.digraph.outAdjTree.toString();
		}
	}

}
