package br.eng.rps.graph;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class representing a node in a bipartite graph.
 * 
 * <p>Besides representing the node, this class also holds all edges of the node
 * and the current connection (the matching node).
 * This implementation (by bidirectional adjacency lists) is very well suited for
 * sparse graphs.
 * 
 * <p>The edges of a node can be added with {@link #addEdge(BipartiteNode)} and
 * the matching is set by {@link #connect(BipartiteNode)}.
 * The current connection can be retrieved by {@link #getConnected()}.
 * 
 * @author rafael
 */
public class BipartiteNode {
	/// Flag of the node.
	int flag;
	/// Node connected with this one.
	BipartiteNode connected;
	/// The differentiated version of this Node if some.
	BipartiteNode diff;
	/// The original version of this Node (when it is a diff).
	BipartiteNode orig;
	/// All edges of this Node.
	BipartiteEdge edges;
	/// The node object.
	Object object;
	
	/** Tarjan depth flag. */
	int depth = -1;
	/** Tarjan low depth flag. */
	int lowDepth = -1;

	/**
	 * Constructor for a given object and original node.
	 * 
	 * <p>The <code>orig</code> is used when the node is the differential version of
	 * another node.
	 * 
	 * @param object the node object
	 * @param orig the original node when this is a differential node (the integral of the node)
	 * 
	 * @see #addEdge(BipartiteNode) {@link #connect(BipartiteNode)} {@link #getConnected()}
	 */
	public BipartiteNode(Object object, BipartiteNode orig){
		flag = 0;
		connected = diff = null;
		this.orig = orig;
		this.object = object;
		if(orig!=null)
			orig.diff = this;
	}
	
	/**
	 * Creates a node for the given object.
	 * @param object the node object.
	 */
	public BipartiteNode(Object object){
		this(object, null);
	}
	
	public String toString(){
		return object==null ? "null" : object.toString();
	}

	public Object getObject(){
		return object;
	}

	/** Add a new edge between two nodes (this and the @a node).
	 * This function should be called only in one of the sides, because the vertex is added
	 * in both.
	 */
	public void addEdge(BipartiteNode node){
		if(!findEdge(node))
			edges = new BipartiteEdge(this, node, edges);

		if(!node.findEdge(this))
			node.edges = new BipartiteEdge(node, this, node.edges);
	}
	
	public boolean findEdge(BipartiteNode n){
		BipartiteEdge e = edges;
		while(e!=null){
			if(e.node2 == n)
				return true;
			e = e.getNext();
		}
		return false;
	}

	/**
	 * Connects two nodes.
	 * 
	 * <p>Before the new connection is made the current connection removed because
	 * in a matching each node has a unique connection.
	 * 
	 * <p>If the given node is <code>null</code> the current connection is removed
	 * and the node remains disconnected.
	 * 
	 * <p><b>NOTE</b> each node can have any number of edges but only one connection.
	 * 
	 * @see #getConnected()
	 */
	public void connect(BipartiteNode node){
		if(connected != null)
			connected.connected = null;
		connected = node;
		if(node != null)
			node.connected = this;		
	}

	/**
	 * Adds the given bit flag f to the node.
	 * @param the flag to add
	 * 
	 * @see removeFlag hasFlag
	 */
	void setFlag(int f){
		flag |= f;
	}
	/**
	 * Removes the given bit flag of the node.
	 * @param f the flag to remove
	 * 
	 * @see #setFlag(int) #hasFlag(int)
	 */
	public void removeFlag(int f){
		flag &= ~f;
	}
	
	/**
	 * Checks if the this object has the given bit flag set.
	 * @param f the flag to check for
	 * 
	 * @see #setFlag(int)
	 */
	public boolean hasFlag(int f) {
		return (flag&f)!=0;
	}

	/**
	 * @return the edges of this node
	 */
	public BipartiteEdge getEdges(){
		return edges;
	}
	
	/**
	 * Returns the differential version of the node.
	 * 
	 * If a node was {@link #differentiate()}d it has a differential version otherwise,
	 * <code>null</code> is returned.
	 * 
	 * @see #getOrig()
	 */
	public BipartiteNode getDiff(){
		return diff;
	}

	/**
	 * @return the connected node
	 */
	public BipartiteNode getConnected(){
		return connected;
	}
	/**
	 * Get the original (integral) version of a differential node.
	 * 
	 * <p>If the node is a differential version of some other node,
	 * the original version is returned, otherwise returns <code>null</code>.
	 * 
	 * @see #differentiate() #getDiff()
	 */
	public BipartiteNode getOrig(){
		return orig;
	}

	/**
	 * Differentiates the node.
	 * @return the differential of the current node
	 * @see getDiff getOrig
	 */
	protected BipartiteNode differentiate(){
		if(diff!=null)
			return diff;
		return new BipartiteNode(object==null ? null : object.toString()+'\'', this);
	}
}
