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
 * Arc ADT that can hold/modify:
 * 		source vertex
 * 		target vertex
 * 		client data (ie. weight)
 * 		annotations
 * 
 * @author Jasmine Ishigami
 *
 * @param <E>
 */
public class Arc<E> {
	private Vertex<E> source;
	private Vertex<E> target;
	private Object data;
	private HashMap<E,Object> annotations;
	
	/**
	 * Constructs an Arc Object given the source vertex, target vertex, and Object data.
	 * 
	 * @param source
	 * @param target
	 * @param data
	 */
	public Arc(Vertex<E> source, Vertex<E> target, Object data){
		this.source = source;
		this.target = target;
		this.data = data;
		this.annotations = new HashMap<E,Object>();
	}
	
	/**
	 * Constructs an Arc Object given the source vertex and target vertex.
	 * Sets data to be null.
	 * 
	 * @param source
	 * @param target
	 */
	public Arc(Vertex<E> source, Vertex<E> target){
		this(source, target, null);
	}
	
	/**
	 * Returns the source vertex of the arc.
	 * 
	 * @return
	 */
	public Vertex<E> getSource(){
		return source;
	}
	
	/**
	 * Returns the target vertex of the arc.
	 * 
	 * @return
	 */
	public Vertex<E> getTarget(){
		return target;
	}
	
	/**
	 * Changes the data Object associated with the arc.
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
	 * Reverses the direction of the arc.
	 */
	public void reverse(){
		Vertex<E> temp = source;
		source = target;
		target = temp;
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
	 * Returns the annotations indexed by  given key.
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
	 * Returns the source key and target key of the arc in the format: u -> v.
	 * 
	 */
	public String toString(){
		String str = this.source.getKey() + " -> " + this.target.getKey();
		return(str);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj){
		if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Arc<E> arc = (Arc<E>) obj;
		return( (arc.getSource() == this.getSource()) && (arc.getTarget() == this.getTarget()) );	
	}
}
