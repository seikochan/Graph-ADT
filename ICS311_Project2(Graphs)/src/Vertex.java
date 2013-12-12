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

import java.util.HashMap;

/**
 * Vertex ADT that can hold/modify:
 * 		vertex key
 * 		client data
 * 		inDegree 
 * 		outDegree
 * 		annotations
 * 
 * @author Jasmine Ishigami
 *
 * @param <E>
 */
public class Vertex<E> {
	private E key;
	private Object data;
	private int inDegree;
	private int outDegree;
	private HashMap<E,Object> annotations;
	
	/**
	 * Constructs a vertex with specified key and null data.
	 * 
	 * @param key
	 */
	public Vertex(E key){
		this(key, null);
	}
	
	/**
	 * Constructs a vertex with specified key and client data.
	 * 
	 * @param key
	 * @param data
	 */
	public Vertex (E key,Object data){
		this.key = key;
		this.data = data;
		this.inDegree = 0;
		this.outDegree = 0;
		this.annotations = new HashMap<E,Object>();
	}
	
	/**
	 * Returns the key of the vertex.
	 * 
	 * @return
	 */
	public E getKey(){
		return key;
	}
	
	/**
	 * Changes the data Object associated with the vertex.
	 * 
	 * @param newData
	 */
	public void setData(Object newData){
		data = newData;
	}
	
	/**
	 * Returns the data of the vertex.
	 * 
	 * @return
	 */
	public Object getData(){
		return data;
	}
	
	/**
	 * Increases the number of arcs incoming on vertex by 1.
	 * 
	 */
	public void addInDegree(){
		inDegree++;
	}
	
	/**
	 * Increase the number of arcs outgoing from vertex by 1.
	 *
	 */
	public void addOutDegree(){
		outDegree++;
	}
	
	/**
	 * Decrease the number of arcs incoming on vertex by 1.
	 * 
	 */
	public void minusInDegree(){
		inDegree--;
	}
	
	/**
	 * Increase the number of arcs outgoing from vertex by 1.
	 * 
	 */
	public void minusOutDegree(){
		outDegree--;
	}
	
	/**
	 * Returns the number of arcs incoming on vertex.
	 * 
	 * @return
	 */
	public int getInDegree(){
		return(inDegree);
	}
	
	/**
	 * Returns the number of arcs outgoing from vertex.
	 * 
	 * @return
	 */
	public int getOutDegree(){
		return(outDegree);
	}
	
	/**
	 * Adds the annotation E = note.
	 * ie. COLOR = grey
	 * 
	 * @param key
	 * @param note
	 */
	public void setAnnotation(E key, Object note){
		annotations.put(key, note);
	}
	
	/**
	 * Returns the annotations indexed by given key.
	 * Returns null if no annotation with specified key exists.
	 * 
	 * @param key
	 * @return
	 */
	public Object getAnnotation(E key){
		return(annotations.get(key));
	}
	
	/**
	 * Removes the annotation indexed by given key and returns it.
	 * 
	 * @param key
	 */
	public Object removeAnnotation(E key){
		return(annotations.remove(key));
	}
	
	/**
	 * Returns the key of the vertex as a string.
	 * 
	 */
	@Override
	public String toString(){
		return (String) (this.key);
	}
}
