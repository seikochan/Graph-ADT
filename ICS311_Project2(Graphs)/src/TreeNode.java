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

public class TreeNode<E> {

  private E key;
  private Object data;
  private TreeNode<E> left;
  private TreeNode<E> right;
  private TreeNode<E> parent;

  /**
   * Constructs a new node with the given data and references to the
   * given left and right nodes.
   */
  public TreeNode(E key, Object data, TreeNode<E> left, TreeNode<E> right, TreeNode<E> parent) {
    this.key = key;
	this.data = data;
    this.left = left;
    this.right = right;
    this.parent = parent;
  }

  /**
   * Constructs a new node containing the given data.
   * Its left and right references will be set to null.
   */
  public TreeNode(E key, Object data) {
    this(key, data, null, null, null);
  }

  
  /**
   * Constructs a new node containing the given data.
   * Its left and right references will be set to null.
   */
  public TreeNode(E key) {
    this(key, null, null, null, null);
  }

  /** Returns the key of this node. */
  public E getKey(){
	  return key;
  }
  
  /** Overwrites the key of this Node with the given key. */
  public void setKey(E key){
	  this.key = key;
  }
  
  /** Returns the item currently stored in this node. */
  public Object getData() {
    return data;
  }

  /** Overwrites the item stored in this Node with the given data item. */
  public void setData(Object data) {
    this.data = data;
  }

  /**
   * Returns this Node's left child.
   * If there is no left left, returns null.
   */
  public TreeNode<E> getLeft() {
    return left;
  }

  /** Causes this Node to point to the given left child Node. */
  public void setLeft(TreeNode<E> left) {
    this.left = left;
  }

  /**
   * Returns this nodes right child.
   * If there is no right child, returns null.
   */
  public TreeNode<E> getRight() {
    return right;
  }

  /** Causes this Node to point to the given right child Node. */
  public void setRight(TreeNode<E> right) {
    this.right = right;
  }
  
  /**
   * Returns this nodes parent.
   * If there is no parent, returns null.
   */
  public TreeNode<E> getParent() {
    return parent;
  }

  /** Causes this Node to point to the given parent Node. */
  public void setParent(TreeNode<E> parent) {
    this.parent = parent;
  }
}