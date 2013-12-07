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
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BinarySearchTree <E extends Comparable<E>> implements Iterable<Object> {
	/**
	 * A binary search tree (BST) is a sorted ADT that uses a binary
	 * tree to keep all elements in sorted order.  If the tree is
	 * balanced, performance is very good: O(n lg n) for most operations.
	 * If unbalanced, it performs more like a linked list: O(n).
	 *
	 * @author Jasmine Ishigami
	 */

	  public TreeNode<E> root = null;
	  private int size = 0;

	  /** Creates an empty tree. */
	  public BinarySearchTree() {
	  }

	  public BinarySearchTree(TreeNode<E> node, int size){
		  this.root = node;
		  this.size = size;
	  }
	  
	  /**
	   * Creates a BST from the given collection.
	   * 
	   * @param col
	   */
	  public BinarySearchTree(Collection<E> col){
		  List<E> list = new ArrayList<E>(col);
		  //making copy of originally passed unshuffled collection
		  List<E> listCopy = new ArrayList<E>(list);
		  Collections.shuffle(list);
		  for(E item : list)
		  	{insert(item,null);}
	  }
	  
	  /** Adds the given item to this BST. */
	  //@Override
	  public void insert(E key, Object data) {
	      //since we are adding a node we need to increase the size of the BST.
		  this.size++;
	    if (this.root == null) {
	      //tree is empty, so just add item
	      this.root = new TreeNode<E>(key, data);
	    }else {
	      //find where to insert, with pointer to parent node
	      TreeNode<E> parent = null;
	      TreeNode<E> curr = this.root;
	      boolean wentLeft = true;
	      while (curr != null) {  //will execute at least once
	        parent = curr;
	        if (key.compareTo(curr.getKey()) <= 0) {
	            //current key is less than key we are looking for so go left.
	        	curr = curr.getLeft();
	          wentLeft = true;
	        }else {
	          //current key is greater than key we are looking for so go right.
	          curr = curr.getRight();
	          wentLeft = false;
	        }
	      }

	      //now add new node on appropriate side of parent
	      curr = new TreeNode<E>(key, data);
	      if (wentLeft) {
	        parent.setLeft(curr);
	      }else {
	        parent.setRight(curr);
	      }
	      curr.setParent(parent);
	    }
	  }

	  /**
	   * Returns the node with maximum key in the current BST.
	   * Uses iteration.
	   * 
	   */
	 //@Override
	 public TreeNode<E> maximum(){
		 return(maximum(this.root));
	 }
	  
	 /**
	  * Returns the node with the maximum key within the BST given.
	  * Uses iteration.
	  * 
	  * @param currNode
	  * @return
	  */
	 private TreeNode<E> maximum(TreeNode<E> currNode){
		 if(currNode == null){
			 return(null);
		 }
		 
		 while(currNode.getRight() != null){
			  //keep going right
			  currNode = currNode.getRight();
		  }
		  return(currNode);
	 }
	 
	  /** Returns the greatest (earliest right-most node) of the given tree.
	   *  Uses recursion.
	   *  
	   *  @param currNode
	   *  @return
	   */
	  private TreeNode<E> maximumR(TreeNode<E> currNode) {
	    if (currNode == null) {
	      return null;
	    }else if (currNode.getRight() == null) {
	      //can't go right any more, so this is max value
	      return currNode;
	    }else {
	      return maximum(currNode.getRight());
	    }
	  }

	  /**
	   * Returns the node with the minimum key in the current BST.
	   * Uses iteration.
	   * 
	   */
	  //@Override
	  public TreeNode<E> minimum(){
		  return(minimum(this.root));
	  }
	  
	  /**
	   * Returns the node with the minimum key in the given BST.
	   * Uses iteration.
	   * 
	   * @param currNode
	   * @return
	   */
	  private TreeNode<E> minimum(TreeNode<E> currNode){
		  if(currNode == null){
			  return(null);
		  }
		  
		  while(currNode.getLeft() != null){
			  //keep going left 
			  currNode = currNode.getLeft();
		  }
		  return(currNode);
	  }
	  
	  /**
	   * Returns minimum by using recursion.
	   * @param n
	   * @return
	   */
	  public TreeNode<E> minimumR(TreeNode<E> n){
	    if(n==null){
	      return null;
	    }else if (n.getLeft() == null) {
	      //can't go left any more, so this is min value
	      return n;
	    }else {
	      return minimum(n.getLeft());
	    }
	  }
	  
	  public TreeNode<E> search(E key){
		  return(search(key, this.root));
	  }
	  
	  //@Override
	  /**
	   * Returns item from current BST that is equivalent (according to compareTo)
	   * to the given item.  If item is not in tree, returns null.
	   * Uses iteration.
	   * 
	   * @param key
	   * @return
	   */
	  private TreeNode<E> search(E key, TreeNode<E> currNode) {		  
		  while(currNode != null){
			  //go left
			  if (key.compareTo(currNode.getKey()) < 0){
				  currNode = currNode.getLeft();
			  }else if (key.compareTo(currNode.getKey()) > 0) {  
				  //go right
				  currNode = currNode.getRight();
			  }else {
				  //found it!
				  return(currNode);
			  }
	      }
		//key not within tree!
		return(null);
	  }

	  /** 
	   * Returns item from given BST that is equivalent (according to compareTo)
	   * to the given item.  If item is not in tree, returns null.
	   * Uses recursion.
	   */
	  private TreeNode<E> searchR(E key, TreeNode<E> node) {
	    if (node == null) {
	      return null;
	    }else if (key.compareTo(node.getKey()) < 0) {
	      return searchR(key, node.getLeft());
	    }else if (key.compareTo(node.getKey()) > 0) {
	      return searchR(key, node.getRight());
	    }else {
	      //found it!
	      return node;
	    }
	  }

	  /**
	   * Removes the first equivalent item found in the current BST.
	   * If item does not exist to be removed, does nothing.
	   * 
	   * @param key
	   */
	  //@Override
	  public void delete(E key) {
	    this.root = delete(key, this.root);
	    if(this.root!=null){
	    	this.size--; 
	    }
	  }

	  /**
	   * Removes the first equivalent item found in given BST.
	   * If item does not exist to be removed, returns null.
	   * 
	   * @param key
	   * @param node
	   * @return
	   */
	  private TreeNode<E> delete(E key, TreeNode<E> node) {
		    if (node == null) {
		      //didn't find item
		      //do nothing
		    	this.size++;
		    	return(null);
		    }else if (key.compareTo(node.getKey()) < 0) {
		      //go to left, saving resulting changes made to left tree
		      node.setLeft(delete(key, node.getLeft()));
		      return node;
		    }else if (key.compareTo(node.getKey()) > 0) {
		      //go to right, saving any resulting changes
		      node.setRight(delete(key, node.getRight()));
		      return node;
		    }else {
		      //found node to be removed!
		      if (node.getLeft() == null && node.getRight() == null) {
		        //leaf node 
		        return null;
		      }else if (node.getRight() == null) {
		        //has only a left child
		        return node.getLeft();
		      }else if (node.getLeft() == null) {
		        //has only a right child
		        return node.getRight();
		      }else {
		        //two children, so replace the contents of this node with max of left tree
		        E maxKey = maximum(node.getLeft()).getKey();  //get max value
		        Object maxData =maximum(node.getLeft()).getData();
		        node.setLeft(delete(maxKey, node.getLeft())); //and remove its node from tree
		        node.setKey(maxKey);
		        node.setData(maxData);
		        return node;
		      }
		    }
		  }
	  /** Returns the number of elements currently in this BST. 
	   * 
	   * @return
	   */
	  //@Override
	  public int size() {
	    return this.size;
	  }
	  
	  /**
	   * Returns the node with the next key after the given key. 
	   *
	   * @param key
	   * @return
	   */
	  //@Override
	  public TreeNode<E> successor(E key){
		  TreeNode<E> currNode = search(key);
		  
		  //given key does not exist or is already the largest value
		  if(currNode == null || currNode == maximum()){
			  return(null);
		  }
		  
		  //if node has non-empty right subtree
		 if(currNode.getRight()!=null){
			  //successor is min in that subtree
			  minimum(currNode.getRight());
		  }
		  //else the successor is the lowest ancestor of the node whose right child is also the ancestor of the node
		  TreeNode<E> parentNode = currNode.getParent();
		  while(parentNode!=null && currNode==parentNode.getRight()){
			  currNode = parentNode;
			  parentNode = currNode.getParent();
		  }
		  return(parentNode);
	  }

	  /**
	   * Returns the node with the key that comes before the given key.
	   *  
	   * @param key
	   * @return
	   */
	  //@Override
	  public TreeNode<E> predecessor(E key){
		  TreeNode<E> currNode = search(key);
		  
		  //given key does not exist or is already the smallest value
		  if(currNode == null || currNode == minimum()){
			  return(null);
		  }
		  
		  //if node has non-empty left subtree
		 if(currNode.getLeft()!=null){
			  //predecessor is max in that subtree
			  maximum(currNode.getLeft());
		  }
		  //else the predecessor is the lowest ancestor of the node whose right child is also the ancestor of the node
		  TreeNode<E> parentNode = currNode.getParent();
		  while(parentNode!=null && currNode==parentNode.getLeft()){
			  currNode = parentNode;
			  parentNode = currNode.getParent();
		  }
		  return(parentNode);
	  }
	  
	  /**
	   * Returns a single-line representation of this BST's contents.
	   * Specifically, this is a comma-separated list of all elements in their
	   * natural Comparable ordering.  The list is surrounded by [] characters.
	   * 
	   * @return
	   */
	  //@Override
	  public String toString() {
	    return "[" + toString(this.root) + "]";
	  }

	  private String toString(TreeNode<E> n) {
	    //would have been simpler to use Iterator... but not implemented yet.
	    if (n == null) {
	      return "";
	    }else {
	      String str = "";
	      str += toString(n.getLeft());
	      if (str.compareTo("")!=0) {
	        str += ", ";
	      }
	      str += n.getKey();
	      if (n.getRight() != null) {
	        str += ", ";
	        str += toString(n.getRight());
	      }
	      return str;
	    }
	  }
	  
	  public String toFullString(){
		  return(toFullString(this.root,""));
	  }
	  
	  private String toFullString(TreeNode<E> n, String path){
		  String str = "";
		  if(n==null){
		  return("");
		  }
		  else{
			  if(n==this.root){
				str = str + "|" + n.getKey() + "\n";  
			  }
			  if(n.getLeft()!=null){
				 str = str + "|" + path + "<" + n.getLeft().getKey() + "\n" + toFullString(n.getLeft(), path+"<");
			 }
			 if(n.getRight()!=null){
				 str = str + "|" + path + ">" + n.getRight().getKey() + "\n" + toFullString(n.getRight(), path+">");
			}
		 }
		 return(str);
	 }
	  
	  public BstIterator iterator(){
		  return(new BstIterator(this.root));
	  }
	  
	  public BstIterator iterator(TreeNode<E> node){
		  return(new BstIterator(node));
	  }

	  public class BstIterator implements Iterator<Object>{
		private TreeNode<E> last;
		private Stack<TreeNode<E>> stack;
		  
		private BstIterator(TreeNode<E> n){
			last = null;
			stack = new Stack<TreeNode<E>>();
			if(n==null){
				return;
			}
			else{
				TreeNode<E> curr = n;
				stack.push(curr);
				while(curr.getLeft()!=null){
					curr = curr.getLeft();
					stack.push(curr);
				}
			}
		}
		  
		//@Override
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

		//@Override
		public Object next() {
			try{
				last = stack.pop();
				if(last.getRight()!=null){
					stack.push(last.getRight());
					TreeNode<E> curr = last.getRight();
					while(curr.getLeft()!=null){
						curr = curr.getLeft();
						stack.push(curr);
					}
				}
				return(last.getData());
			}
			catch(EmptyStackException e){
				last = null;
				throw new NoSuchElementException();
			}
		}

		//@Override
		public void remove() {
			if(last==null){
			  throw new IllegalStateException("There is no item to remove or an item has already been removed.");}
			else{
				BinarySearchTree.this.delete(last.getKey());
				last = null;
			}
		}
		  
	  }

}
