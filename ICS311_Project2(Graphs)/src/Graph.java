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

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Represents an ADT for a Directed Graph G=(V,A):
 * 		-V is a set of vertices
 * 		-A is a set of directed edges (arcs)
 * 
 * Represented using adjacency tree:
 * 		-modified adjacency list where instead of each row being a LinkedList, its a BinarySearchTree.
 * 
 * @author Jasmine Ishigami
 * @param <E>
 *
 */
public class Graph<E extends Comparable<E>> {
	//number of vertices in the graph
	private int numVertices;
	//number of arcs in the graph
	private int numArcs;
	//number of edges in the graph
	public int numEdges;
	//represents an AdjacencyTree of what arcs are outgoing from each vertex
	//        vertex.key
	//			|  | ------>  BST (where root node is vertex, and all other children are arcs)
	//			|  |
	//			|  |
	//			 .
	//			 .
	//			 .
	//			|  |
	//			|  |
	// * key of items in BST is referenced by key of vertex or target key of arc.
	public Hashtable<E, BinarySearchTree<E>> outAdjTree;
	//represents an AdjacencyTree of what arcs are incoming on each vertex
	//        vertex.key
	//			|  | ------>  BST (where all nodes are arcs)
	//			|  |
	//			|  |
	//			 .
	//			 .
	//			 .
	//			|  |
	//			|  |
	// * key of items in BST is referenced by target key of arc.
	public Hashtable<E, BinarySearchTree<E>> inAdjTree;
	//represents an AdjacencyTree of incident edges on each vertex
    //need to keep a separate hashtable since it needs to disregard u -> v and v-> u and just make 1 edge (u,v)
    private Hashtable<E, BinarySearchTree<E>> edgeAdjTree;
	//true only when reverseDirection is called
	private boolean reversing;
	
	
	//-------------------------------CONSTRUCTOR(S)------------------------------------
	public Graph(){
		numVertices = 0;
		numArcs = 0;
		outAdjTree = new Hashtable<E, BinarySearchTree<E>>();
		inAdjTree = new Hashtable<E, BinarySearchTree<E>>();
		edgeAdjTree = new Hashtable<E, BinarySearchTree<E>>();
		reversing = false;
	}
	
	//=================================================================================================
	//=											DIRECTED METHODS									  =
	//=================================================================================================	

	
	//---------------------------------ACCESSORS----------------------------------------
	
	/**
	 * Returns the number of vertices |V|.
	 * 
	 * @return
	 */
	public int numVertices(){
		return(numVertices);
	}
	
	/**
	 * Returns the number of arcs |A|.
	 * 
	 * @return
	 */
	public int numArcs(){
		return(numArcs);
	}
	
	/**
	 * Returns an iterator over the vertices V.
	 * 
	 * @return
	 */
	public Iterator<Vertex<E>> vertices(){
		return(new VertexIterator());
	}
	
	/**
	 * Returns an iterator over the arcs A.
	 * 
	 * @return
	 */
	public Iterator<Arc<E>> arcs(){
		return(new ArcIterator());
	}
	
	/**
	 * Returns the Vertex associated with the client key.
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vertex<E> getVertex(E key){
		//MUST use outAdjTree because it stores the vertices as the root nodes of the bst,
		//the inAdjTree does not store vertices at all
		return((Vertex<E>)outAdjTree.get(key).search(key).getData());
	}
	
	/**
	 * Returns the Arc that connects client keys source and target, or null if none.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Arc<E> getArc(E source, E target){
		TreeNode<E> node = outAdjTree.get(source).search(target);
		
		//arc does not exist, return null
		if(node == null){
			return null;
		}
		
		//arc exists so return it
		Arc<E> arc = (Arc<E>) node.getData();
		return(arc);
	}
	
	/**
	 * Returns the client data Object associated with Vertex v.
	 * 
	 * @param v
	 * @return
	 */
	public Object getVertexData(Vertex<E> v){
		return(v.getData());
	}
	
	/**
	 * Returns the client data Object associated with Arc a.
	 * 
	 * @param a
	 * @return
	 */
	public Object getArcData(Arc<E> a){
		return(a.getData());
	}
	
	/**
	 * Returns the number of arc incoming to v.
	 * 
	 * @param v
	 * @return
	 */
	public int inDegree(Vertex<E> v){
		return(v.getInDegree());
	}
	
	/**
	 * Returns the number of arcs outgoing from v.
	 * 
	 * @param v
	 * @return
	 */
	public int outDegree(Vertex<E> v){
		return(v.getOutDegree());
	}
	
	/**
	 * Returns an iterator over the vertices adjacent to v by incoming arcs.
	 * 
	 * @param v
	 * @return
	 */
	public Iterator<Vertex<E>> inAdjacentVertices(Vertex<E> v){
		return(new InVertexIterator(v));
	}
	
	/**
	 * Returns an iterator over the vertices adjacent to v by outgoing arcs.
	 * @param v
	 * @return
	 */
	public Iterator<Vertex<E>> outAdjacentVertices(Vertex<E> v){
		return(new OutVertexIterator(v));
	}
	
	/**
	 * Returns an iterator over the incoming arcs of v.
	 * 
	 * @param v
	 * @return
	 */
	public Iterator<Arc<E>> inIncidentArcs(Vertex<E> v){
		return(new InArcIterator(v));
	}
	
	/**
	 * Returns an iterator over the outgoing arcs of v.
	 * 
	 * @param v
	 * @return
	 */
	public Iterator<Arc<E>> outIncidentArcs(Vertex<E> v){
		return(new OutArcIterator(v));
	}
	
	/**
	 * Returns the origin(start) vertex of Arc a.
	 * 
	 * @param a
	 * @return
	 */
	public Vertex<E> origin(Arc<E> a){
		return(a.getSource());
	}
	
	/**
	 * Returns the destination(end) vertex of Arc a.
	 * @param a
	 * @return
	 */
	public Vertex<E> destination(Arc<E> a){
		return(a.getTarget());
	}
	
	//---------------------------------MUTATORS----------------------------------------
	
	/**
	 * Inserts a new isolated vertex indexed under (retrievable via) key.
	 * Data defaultly set to null.
	 * 
	 * @param key
	 * @return
	 */
	public Vertex<E> insertVertex(E key){
		return(insertVertex(key,null));
	}
	
	/**
	 * Inserts a new isolated vertex indexed under (retrievable via) key and containing
	 * specified client data.
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public Vertex<E> insertVertex(E key, Object data){
		Vertex<E> vertex = new Vertex<E>(key,data);
		return(insertVertex(vertex));
	}
	
	/**
	 * Inserts the given vertex.
	 *  
	 * @param key
	 * @param data
	 * @return
	 */
	public Vertex<E> insertVertex(Vertex<E> vertex){
		if(outAdjTree.get(vertex.getKey()) == null){
			BinarySearchTree<E> bst = new BinarySearchTree<E>();
			bst.insert(vertex.getKey(), vertex);
			outAdjTree.put(vertex.getKey(), bst);
		}else{
			System.out.println("ERROR: From method insertVertex() - This should not happen! Exited.");
			System.exit(0);
		}
		numVertices++;
		return(vertex);
	}
	
	/**
	 * Inserts a new arc from an existing vertex to another.
	 * Data defaultly set to null.
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	public Arc<E> insertArc(Vertex<E> u, Vertex<E> v){
		return(insertArc(u,v,null));
	}
	
	/**
	 * Inserts a new arc from an existing vertex to another and containing
	 * specified client data.
	 * 
	 * @param u
	 * @param v
	 * @param data
	 * @return
	 */
	public Arc<E> insertArc(Vertex<E> u, Vertex<E> v, Object data){
		Arc<E> arc = new Arc<E>(u,v,data);
		return(insertArc(arc));
	}
	
	public Arc<E> insertArc(Arc<E> arc){
		Vertex<E> u = arc.getSource();
		Vertex<E> v = arc.getTarget();
		
		//outAdjTree Hashmap will have u.getKey() since the vertex has been added in the bst
		outAdjTree.get(u.getKey()).insert(v.getKey(), arc);
		
		//inAdjTree Hashmap may not have the key so must check
		if(inAdjTree.get(v.getKey()) == null){
			BinarySearchTree<E> bst = new BinarySearchTree<E>();
			bst.insert(u.getKey(),arc);
			inAdjTree.put(v.getKey(), bst);
		}else{
			inAdjTree.get(v.getKey()).insert(u.getKey(), arc);
		}
		numArcs++;
		
		//update inDegree and outDegree of source and target vertices
		v.addInDegree();
		u.addOutDegree();
		
		if(!reversing){
			//check if the reverse arc exists
			Arc<E> undirectedArc = null;
			if( (undirectedArc = getArc(v.getKey(),u.getKey())) != null){
				System.out.println("Edge not Added: " + arc.toString());
				//if so do not "add" this arc to the undirected graph
				arc.setAnnotation((E) "undirected", false);
				undirectedArc.setAnnotation((E)"undirected", true);
			}else{
				System.out.println("Edge: " + arc.toString());
				//if not "add" arc to undirected graph
				arc.setAnnotation((E)"undirected", true);
				numEdges++;
				
				//since adding new edge, update the edgeAdjTree to match
                //add edge to source vertex
                if(edgeAdjTree.get(v.getKey()) == null){
                        System.out.println("\tAdded under: " + v.getKey());
                        BinarySearchTree<E> bst = new BinarySearchTree<E>();
                        bst.insert(u.getKey(),arc);
                        edgeAdjTree.put(v.getKey(), bst);
                }else{
                        System.out.println("\tAdded under: " + v.getKey());
                        edgeAdjTree.get(v.getKey()).insert(u.getKey(), arc);
                }
                //add edge to target vertex
                if(edgeAdjTree.get(u.getKey()) == null){
                        System.out.println("\tAdded under: " + u.getKey());
                        BinarySearchTree<E> bst = new BinarySearchTree<E>();
                        bst.insert(v.getKey(),arc);
                        edgeAdjTree.put(u.getKey(), bst);
                }else{
                        System.out.println("\tAdded under: " + u.getKey());
                        edgeAdjTree.get(u.getKey()).insert(v.getKey(), arc);
                }
			}
		}
		return(arc);
	}
	
	/**
	 * Changes the data associated with Vertex v to data.
	 * 
	 * @param v
	 * @param data
	 */
	public void setVertexData(Vertex<E> v, Object data){
		v.setData(data);
	}
	
	/**
	 * Changes the data Object associated with Arc a to data.
	 * 
	 * @param a
	 * @param data
	 */
	public void setArcData(Arc<E> a, Object data){
		a.setData(data);
	}
	
	/**
	 * Deletes a vertex and all its incident arcs (and edges under the undirected extension).
	 * Returns the client data formerly stored at v.
	 * 
	 * @param v
	 * @return
	 */
	public Object removeVertex(Vertex<E> v){
		//before remove adjTree, iter over it and delete x <- v arcs
		Iterator<Vertex<E>> outIter = outAdjacentVertices(v);
		while(outIter.hasNext()){
			Vertex<E> vertex = (Vertex<E>)outIter.next(); 
			inAdjTree.get(vertex.getKey()).delete(v.getKey());
		}
		//remove vertex and all outgoing arcs (v -> x)
		outAdjTree.remove(v.getKey());
		
		//before remove adjTree, iter over it and delete x -> v arcs
		Iterator<Vertex<E>> inIter = inAdjacentVertices(v);
		while(inIter.hasNext()){
			Vertex<E> vertex = (Vertex<E>)inIter.next();
			outAdjTree.get(vertex.getKey()).delete(v.getKey());
		}
		//removes all incoming arcs  (v <- x)
		inAdjTree.remove(v.getKey());
		
		//decrement overall vertices in graph by 1
		numVertices--;
		return(v.getData());
	}
	
	/**
	 * Removes an arc.
	 * Returns the client data object formerly stored at a.
	 * 
	 * @param a
	 * @return
	 */
	public Object removeArc(Arc<E> a){
		//System.out.println("ENTERED: remove arc()");
		
		E sourceKey = a.getSource().getKey();
		E targetKey = a.getTarget().getKey();
		//System.out.println("arc: " + sourceKey + " -> " + targetKey);
		
		//System.out.println("Removing for out: " + ((Arc<E>)outAdjTree.get(sourceKey).search(targetKey).getData()).getSource().getKey() + " -> " + ((Arc<E>)outAdjTree.get(sourceKey).search(targetKey).getData()).getTarget().getKey());
		outAdjTree.get(sourceKey).delete(targetKey);
		//System.out.println("Removing for in: " + ((Arc<E>)inAdjTree.get(targetKey).search(sourceKey).getData()).getSource().getKey() + " -> " + ((Arc<E>)inAdjTree.get(targetKey).search(sourceKey).getData()).getTarget().getKey());
		inAdjTree.get(targetKey).delete(sourceKey);
		
		numArcs--;
		//update inDegree and OutDegree of source and target vertices
		a.getSource().minusOutDegree();
		a.getTarget().minusInDegree();
		
		return(a.getData());
	}
	
	/**
	 * Reverses the direction of an arc.
	 * 
	 * @param a
	 */
	public void reverseDirection(Arc<E> a){
		reversing = true;
		
		//modify inAdjTree and outAdjTree get rid of old arc
		removeArc(a);
		//System.out.println("Removed from outAdjTree: " + outAdjTree.get(a.getSource().getKey()).search(a.getTarget().getKey()));
		//System.out.println("Removed from inAdjTree: " + inAdjTree.get(a.getTarget().getKey()).search(a.getSource().getKey()));
		
		//reverse: u -> v   becomes    u <- v
		a.reverse();
		
		//modify inAdjTree and outAdjTree add new arc
		insertArc(a);
		//System.out.println("Inserted to outAdjTree: " + outAdjTree.get(a.getSource().getKey()).search(a.getTarget().getKey()).getData().toString());
		//System.out.println("Inserted to inAdjTree: " + inAdjTree.get(a.getTarget().getKey()).search(a.getSource().getKey()).getData().toString() + "\n");
	
		reversing = false;
	}
	
	//-------------------------------ANNOTATORS------------------------------------------
	/**
	 * Annotates the vertex specified with the note indexed under key.
	 * 
	 * @param vertex
	 * @param key
	 * @param note
	 */
	public void setAnnotations(Vertex<E> vertex, E key, Object note){
		vertex.setAnnotation(key, note);
	}
	
	/**
	 * Annotates the arc specified with the note indexed under key.
	 * 
	 * @param arc
	 * @param key
	 * @param note
	 */
	public void setAnnotations(Arc<E> arc, E key, Object note){
		arc.setAnnotation(key, note);
	}
	
	/**
	 * Returns the note indexed by key annotating the vertex specified.
	 * 
	 * @param vertex
	 * @param key
	 * @return
	 */
	public Object getAnnotations(Vertex<E> vertex, E key){
		return(vertex.getAnnotation(key));
	}
	
	/**
	 * Returns the note indexed by key annotating the arc specified.
	 * 
	 * @param arc
	 * @param key
	 * @return
	 */
	public Object getAnnotations(Arc<E> arc, E key){
		return(arc.getAnnotation(key));
	}
	
	/**
	 * Removes the annotation on the specified vertex indexed by key and returns it.
	 * 
	 * @param vertex
	 * @param key
	 * @return
	 */
	public Object removeAnnotations(Vertex<E> vertex, E key){
		return(vertex.removeAnnotation(key));
	}
	
	/**
	 * Removes the annotation on the specified arc indexed by key and returns it.
	 * 
	 * @param arc
	 * @param key
	 * @return
	 */
	public Object removeAnnotations(Arc<E> arc, E key){
		return(arc.removeAnnotation(key));
	}
	
	/**
	 * Removes all annotations on vertices or arcs indexed by key.
	 * Use this method to clean up between runs.
	 * 
	 * @param key
	 */
	public void clearAnnotations(E key){
		Iterator<Vertex<E>> vertexIter = vertices();
		Iterator<Arc<E>> arcIter = arcs();
		
		//remove all annotations indexed by key from all vertices (if present)
		while(vertexIter.hasNext()){
			Vertex<E> vertex = vertexIter.next();
			vertex.removeAnnotation(key);
		}
		
		//remove all annotations indexed by key from all arcs (if present)
		while(arcIter.hasNext()){
			Arc<E> arc = arcIter.next();
			arc.removeAnnotation(key);
		}
	}
	
	//-----------------------------NESTED CLASSES----------------------------------------
	/**
	 * Nested Iterator over all arcs in the graph.
	 * 
	 * @author Jasmine Ishigami
	 *
	 */
	public class ArcIterator implements Iterator<Arc<E>>{
		private Arc<E> last;
		private Stack<Arc<E>> stack;
		
		@SuppressWarnings("unchecked")
		private ArcIterator(){
			last = null;
			stack = new Stack<Arc<E>>();
			
			
			//only need to iterate over 1 adjTree since both have the same arcs, just in different directions
			for(Map.Entry<E, BinarySearchTree<E>> entry: inAdjTree.entrySet()){
				//create iterator over each bst 
				Iterator<Object> iter = entry.getValue().iterator();
				while(iter.hasNext()){
					stack.push((Arc<E>)iter.next());
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Arc<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of ArcIterator not yet implemented");
			
		}
		
	}
	
	/**
	 * Nested Iterator over all incoming arcs into given vertex.
	 * 
	 * @author Jasmine Ishigami
	 *
	 */
	public class InArcIterator implements Iterator<Arc<E>>{
		private Arc<E> last;
		private Stack<Arc<E>> stack;
		
		@SuppressWarnings("unchecked")
		private InArcIterator(Vertex<E> vertex){
			last = null;
			stack = new Stack<Arc<E>>();
			
			//create iterator over bst of vertex given
			Iterator<Object> iter = inAdjTree.get(vertex.getKey()).iterator();
			while(iter.hasNext()){
				Arc<E> arc = (Arc<E>)iter.next();
				stack.push(arc);
			}
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Arc<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of ArcIterator not yet implemented");
			
		}
		
	}
	
	/**
	 * Nested Iterator over all outgoing arcs from the given vertex.
	 * 
	 * @author Jasmine Ishigami
	 *
	 */
	public class OutArcIterator implements Iterator<Arc<E>>{
		private Arc<E> last;
		private Stack<Arc<E>> stack;
		
		@SuppressWarnings("unchecked")
		private OutArcIterator(Vertex<E> vertex){
			last = null;
			stack = new Stack<Arc<E>>();
			
			//create iterator over bst of vertex given
			//skip root node since it is a vertex and not an arc
			if(outAdjTree.get(vertex.getKey()).root.getLeft() != null){
				BinarySearchTree<E> bstLeft = new BinarySearchTree<E>(outAdjTree.get(vertex.getKey()).root.getLeft(),outAdjTree.get(vertex.getKey()).size()-1);
				Iterator<Object> bstIter = bstLeft.iterator();
				while(bstIter.hasNext()){
					Arc<E> arc = (Arc<E>)bstIter.next();
					stack.push(arc);
				}
			}
			if(outAdjTree.get(vertex.getKey()).root.getRight() != null){
				//System.out.println("outAdjTree: vertex - " + vertex.getKey());
				BinarySearchTree<E> bstRight = new BinarySearchTree<E>(outAdjTree.get(vertex.getKey()).root.getRight(),outAdjTree.get(vertex.getKey()).size()-1);
				Iterator<Object> bstIter = bstRight.iterator();
				while(bstIter.hasNext()){
					Arc<E> arc = (Arc<E>)bstIter.next();
					//System.out.println("arc = " +  arc.getSource().getKey() + ", " + arc.getTarget().getKey());
					stack.push(arc);
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Arc<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of OutArcIterator not yet implemented");
			
		}
		
	}
	
	/**
	 * Nested class Iterator over all vertices in the graph.
	 * 
	 * @author Jasmine Ishigami
	 *
	 */
	public class VertexIterator implements Iterator<Vertex<E>>{
		private Vertex<E> last;
		private Stack<Vertex<E>> stack;
		
		@SuppressWarnings("unchecked")
		private VertexIterator(){
			last = null;
			stack = new Stack<Vertex<E>>();
			
			for(Map.Entry<E, BinarySearchTree<E>> entry: outAdjTree.entrySet()){
				//create iterator over each bst 
				//need only the root node of each bst, which is the Vertex
				Vertex<E> curr = (Vertex<E>)entry.getValue().root.getData();
				stack.push(curr);
			}
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Vertex<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of VertexIterator not yet implemented");
		}
	}
	
	/**
	 * Nested class Iterator over all incoming arcs to given vertex.
	 * 
	 * @author Jasmine Ishigami
	 *
	 */
	public class InVertexIterator implements Iterator<Vertex<E>>{
		private Vertex<E> last;
		private Stack<Vertex<E>> stack;
		
		@SuppressWarnings("unchecked")
		private InVertexIterator(Vertex<E> vertex){
			last = null;
			stack = new Stack<Vertex<E>>();
			
			//if vertex has no inIncidentArcs exit
			if(inAdjTree.get(vertex.getKey()) == null){
				return;
			}
			
			//create iterator over bst of vertex given
			Iterator<Object> iter = inAdjTree.get(vertex.getKey()).iterator();
			while(iter.hasNext()){
				Arc<E> arc = (Arc<E>)iter.next();
				stack.push(arc.getSource());
			}
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Vertex<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of InVertexIterator not yet implemented");	
		}	
	}
	
	/**
	 * Nested class Iterator over all outgoing vertices from the given vertex.
	 * 
	 * @author Jasmine Ishigami
	 *
	 */
	public class OutVertexIterator implements Iterator<Vertex<E>>{
		private Vertex<E> last;
		private Stack<Vertex<E>> stack;
		
		@SuppressWarnings("unchecked")
		private OutVertexIterator(Vertex<E> vertex){
			last = null;
			stack = new Stack<Vertex<E>>();
			
			//create iterator over bst of vertex given
			//skip root node since it is a vertex and not an arc
			if(outAdjTree.get(vertex.getKey()).root.getLeft() != null){
				BinarySearchTree<E> bstLeft = new BinarySearchTree<E>(outAdjTree.get(vertex.getKey()).root.getLeft(),outAdjTree.get(vertex.getKey()).size()-1);
				Iterator<Object> bstIter = bstLeft.iterator();
				while(bstIter.hasNext()){
					Arc<E> arc = (Arc<E>)bstIter.next();
					stack.push(arc.getTarget());
				}
			}
			if(outAdjTree.get(vertex.getKey()).root.getRight() != null){
				//System.out.println("outAdjTree: vertex - " + vertex.getKey());
				BinarySearchTree<E> bstRight = new BinarySearchTree<E>(outAdjTree.get(vertex.getKey()).root.getRight(),outAdjTree.get(vertex.getKey()).size()-1);
				Iterator<Object> bstIter = bstRight.iterator();
				while(bstIter.hasNext()){
					Arc<E> arc = (Arc<E>)bstIter.next();
					//System.out.println("arc = " +  arc.getSource().getKey() + ", " + arc.getTarget().getKey());
					stack.push(arc.getTarget());
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Vertex<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of OutVertexIterator not yet implemented");	
		}	
	}
	
	
	
	//=================================================================================================
	//=									      UNDIRECTED METHODS									  =
	//=================================================================================================

	public Iterator<Arc<E>> edges(){
		return new EdgeIterator();
	}
	
	public int numEdges(){
		return(numEdges);
	}
	
	public int degree(Vertex<E> vertex){
		//cannot just return inDegree() + outDegree() because if you have u -> v and v -> u  the degree(u) = 2 when it should equal 1 in a undirected graph
		//if there is no entry (bst) under the vertex key in the edgeAdjTree it means that vertex is isolated
		if(edgeAdjTree.get(vertex.getKey())==null){
			return 0;
		}
		
		//else it has some number of degrees
		return(edgeAdjTree.get(vertex.getKey()).size());
	}
	
	public Iterator<Vertex<E>> adjacentVertices(Vertex<E> vertex){
		return vertices();
	}
	
	public ArrayList<Vertex<E>> adjacentVertexArr(Vertex<E>  vertex){
		ArrayList<Vertex<E>> arr = new ArrayList<Vertex<E>>();
		
		Iterator<Vertex<E>> vertexIter = adjacentVertices(vertex);
		while(vertexIter.hasNext()){
			arr.add(vertexIter.next());
		}
		
		return(arr);
	}
	
	public Iterator<Arc<E>> incidentEdges(Vertex<E> vertex){
		return new adjacentEdgeIterator(vertex);
	}
	
	public ArrayList<Vertex<E>> endVertices(Arc<E> a){
		ArrayList<Vertex<E>> arr = new ArrayList<Vertex<E>>();
		arr.add(a.getSource());
		arr.add(a.getTarget());
		
		return(arr);
	}
	
	public Vertex<E> opposite(Vertex<E> vertex, Arc<E> arc) throws Exception{
		if(arc.getSource() == vertex){
			return(arc.getTarget());
		}else if(arc.getTarget() == vertex){
			return(arc.getSource());
		}else{
			throw new Exception("InvalidEdgeException");
		}
	}
	
	public Iterator<Arc<E>> undirectedEdges(){
		return arcs();
		//TODO
	}
	
	public boolean isDirected(Arc<E> a){
		return false;
		//TODO
	}
	
	private class EdgeIterator implements Iterator<Arc<E>>{
		private Arc<E> last;
		private Stack<Arc<E>> stack;
		
		private EdgeIterator(){
			last = null;
			stack = new Stack<Arc<E>>();
			
			Iterator<Arc<E>> arcIter = arcs();
			while(arcIter.hasNext()){
				Arc<E> arc = arcIter.next();
				if((boolean)arc.getAnnotation((E)"undirected")){
					stack.add(arc);
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Arc<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of EdgeIterator not yet implemented");	
		}	
	}
	
	private class adjacentEdgeIterator implements Iterator<Arc<E>>{
		private Arc<E> last;
		private Stack<Arc<E>> stack;
		
		@SuppressWarnings("unchecked")
		private adjacentEdgeIterator(Vertex<E> vertex){
			last = null;
			stack = new Stack<Arc<E>>();
			
			//if vertex has no adjacent edges exit
            if(edgeAdjTree.get(vertex.getKey()) == null){
                    return;
            }
            Iterator<Object> bstIter = edgeAdjTree.get(vertex.getKey()).iterator();
            while(bstIter.hasNext()){
                    Arc<E> arc = (Arc<E>) bstIter.next();
                    stack.add(arc);
            }
		}
		
		@Override
		public boolean hasNext() {
			try{
				if(stack.peek()!=null){
					return(true);
				}
				return(false);
			  }
			  catch(EmptyStackException e){
				  return(false);
			  }
		}

		@Override
		public Arc<E> next() {
			try{
				last = stack.pop();
				return(last);
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			System.out.println("Remove of EdgeIterator not yet implemented");	
		}	
	}
}
