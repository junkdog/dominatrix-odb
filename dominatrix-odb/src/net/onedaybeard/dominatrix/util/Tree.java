/*
 * Copyright 2011 Adrian Papari
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.onedaybeard.dominatrix.util;

import java.util.LinkedList;
import java.util.List;

/**
 * A hierarchical data structure operating on a single type. A node's
 * value can be <code>null</code>.
 */
public class Tree<T>
{
	private final List<Tree<T>> childNodes;
	
	private Tree<T> parent;
	
	private T value;
	
	public Tree()
	{
		this(null, null);
	}
	
	public Tree(T root)
	{
		this(null, root);
	}
	
	private Tree(Tree<T> parent, T value)
	{
		if (parent != null)
			parent.childNodes.add(this);
		
		this.parent = parent;
		this.value = value;
		childNodes = new LinkedList<Tree<T>>();
	}
	
	/**
	 * Adds a new child node to the tree.
	 * 
	 * @param value
	 *            Object associated with the new node.
	 * @return View from newly allocated child node.
	 */
	public Tree<T> addNode(T value)
	{
		return new Tree<T>(this, value);
	}
	
	public boolean isLastNode()
	{
		return (parent != null && (parent.childNodes.indexOf(this) + 1) == parent.childNodes.size());
	}
	
	/**
	 * Checks if this node represents the root node. A root node has no parent node.
	 * 
	 * @return True if parent is null.
	 */
	public boolean isRootNode()
	{
		return parent == null;
	}
	
	/**
	 * Gets the distance between the current node and the root node. 
	 * 
	 * @return Distance to root node.
	 */
	public int getDepth()
	{
		int depth = 0;
		Tree<T> t = this;
		while ((t = t.parent) != null) {
			depth++;
		}
		
		return depth;
	}
	
	@Override
	public String toString()
	{
		StringBuilder indent = new StringBuilder();
		Tree<T> t = this;
		while ((t = t.parent) != null) {
			indent.append("   ");
		}
		
		return String.format("value=%s (%s)", value, childNodes);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Tree<T> getParent() {
		return parent;
	}

	public List<Tree<T>> getChildNodes() {
		return childNodes;
	}
}
