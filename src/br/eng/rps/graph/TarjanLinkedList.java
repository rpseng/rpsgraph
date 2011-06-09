package br.eng.rps.graph;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A implementation of the Tarjan's algorithm for the detection of Strongly Connected Components.
 * 
 * The algorithm is executed by the {@link #tarjan(BipartiteGraph)} function.
 * 
 * <p>A {@link TarjanLinkedList} object can be reused by multiple calls to {@link #tarjan(BipartiteGraph)}.
 * 
 * <p>This implementation is inspired by the one available at http://algowiki.net/wiki/index.php/Tarjan%27s_algorithm.
 * 
 * @author rafael
 *
 */
public class TarjanLinkedList {

	private int index = 0;
	
	/** Stack linked list (for fast addFirst and removeFirst operations) */
	private LinkedList<BipartiteNode> stack = new LinkedList<BipartiteNode>();
	private LinkedList<BipartiteGraph> SCC = new LinkedList<BipartiteGraph>();

	/**
	 * @param G the graph to be partitioned into SCC (<b>must</b> contain a perfect matching).
	 * @return the strongly connected subgraphs
	 */
	public Collection<BipartiteGraph> tarjan(BipartiteGraph G){
		// clear possibly previous contents
		stack.clear();
		SCC.clear();
		index = 0;
		for(BipartiteNode v : G.getVars())
			v.depth = v.lowDepth = -1;
		
		for(BipartiteNode v : G.getVars())
			tarjan(v);

		return SCC;
	}

	private void tarjan(BipartiteNode v){
		if(v.depth>=0)
			return;
		
		v.depth = index;
		v.lowDepth = index;
		index++;
		stack.addFirst(v);
		if(v.connected !=null){
			for(BipartiteEdge ed=v.connected.getEdges(); ed!=null; ed=ed.getNext()){
				BipartiteNode n = ed.getNode2();
				if(n.depth == -1){
					tarjan(n);
					v.lowDepth = Math.min(v.lowDepth, n.lowDepth);
				}else if(stack.contains(n)){
					v.lowDepth = Math.min(v.lowDepth, n.depth);
				}
			}
		}

		// Is v the root of an SCC
		if(v.lowDepth == v.depth){
			BipartiteNode n;
			BipartiteGraph component = new BipartiteGraph();
			do{
				n = stack.removeFirst();
				component.addV(n);
				component.addE(n.connected);
			}while(n != v);
			SCC.add(component);
		}
	}
}

