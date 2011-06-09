package br.eng.rps.graph;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A implementation of the Tarjan's algorithm for the detection of Strongly Connected Components.
 * 
 * <p>The algorithm is executed by {@link #tarjan(BipartiteGraph)} call.
 * 
 * <p>A {@link Tarjan} instance can be reused by multiple calls to {@link #tarjan(BipartiteGraph)}.
 * 
 * @author rafael
 *
 */
public class Tarjan {

	private int index = 0;
	
	/** Stack linked list (for fast addFirst and removeFirst operations) */
	private ArrayList<BipartiteNode> stack = new ArrayList<BipartiteNode>();
	private ArrayList<BipartiteGraph> SCC = new ArrayList<BipartiteGraph>();

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
		
//		stack.ensureCapacity(G.getVars().size());
		
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
		stack.add(0, v);
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
				n = stack.remove(0);
				component.addV(n);
				component.addE(n.connected);
			}while(n != v);
			SCC.add(component);
		}
	}
}

