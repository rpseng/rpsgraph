package br.eng.rps.graph;

/**
 * The edge class, connecting two nodes.
 * 
 * @author rafael
 *
 */
public class BipartiteEdge {
	/** First connecting node */
	BipartiteNode node1;
	/** Second connecting node */
	BipartiteNode node2;
	
	BipartiteEdge next;
	
	public String toString(){
		return node1.toString() + "--" + node2.toString();
	}
	
	public BipartiteEdge(BipartiteNode n1, BipartiteNode n2, BipartiteEdge next) {
		this.node1 = n1;
		this.node2 = n2;
		this.next = next;
	}
	
	public BipartiteNode getNode1(){
		return node1;
	}
	
	public BipartiteNode getNode2(){
		return node2;
	}
	
	public BipartiteEdge getNext(){
		return next;
	}
}
