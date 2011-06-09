package br.eng.rps.graph;

import java.util.ArrayList;
import java.util.List;

import br.eng.rps.graph.utils.Utils;


/**
 * Bipartite graph G(V_e V_v, E).
 * 
 * <p>The nodes (or vertices) are considered to be bipartite in two sets V_e and V_v.
 * When analyzing system of equations (which is the initial purpose of this library),
 * the partitions of the graph are as follows:
 * <ul>
 * <li>V_e: the nodes representing the equations of the system</li>
 * <li>V_v: the nodes representing the variables of the system</li>
 * </ul>
 * 
 * <p>This class holds not only the graph itself (vertices and edges) but also
 * a matching.
 * So, there is one matching per graph.
 * Actually the matching is stored by the vertices of the graph,
 * see {@link BipartiteNode#getConnected()}. 
 * 
 * <p>In order to build a graph there are some options.
 * The first one, is to manually construct the graph by giving the edges:
 * <blockquote><pre>
 * BipartiteGraph G;
 * G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
 * G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
 * G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
 * G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6")
 * </pre></blockquote>
 * With the code above, the vertices are automatically created as they appear.
 * 
 * <p>A second option is to create the nodes and manually set the
 * edges:
 * <blockquote><pre>
 * BipartiteGraph G;
 * BipartiteNode eq1 = new BipartiteNode("eq1"); 
 * BipartiteNode eq2 = new BipartiteNode("eq2"); 
 * BipartiteNode v1 = new BipartiteNode("v2"); 
 * BipartiteNode v2 = new BipartiteNode("v2");
 * 
 * eq1.addEdge(v1);
 * eq1.addEdge(v2);
 * eq2.addEdge(v2);
 * 
 * G.addEq(eq1);
 * G.addEq(eq2);
 * G.addVar(v1);
 * G.addVar(v2);
 * </pre></blockquote>
 * 
 * <p>Another option is to import a GraphViz Dot file using {@link Utils#ImportGraphViz(java.io.InputStream, BipartiteGraph)}:
 * <blockquote><pre>
 * String file = "filename.dot";
 * BipartiteGraph G = new BipartiteGraph();
 *
 * InputStream in = new FileInputStream(file);
 * Utils.ImportGraphViz(in, G);
 * </pre></blockquote>
 * 
 * <p>A sample DOT file is:
 * <blockquote><pre>
 * graph InitialSet{
 * node[shape=circle,fixedsize=1]
 * edge[color="gray"]
 *  f_1--"x_1'"  f_1--"x_2'"
 *  f_2--x_2
 * }
 * </pre></blockquote>
 * This is a subset of the GraphViz DOT language.
 * 
 * <p>Once you have a graph G, execute one of the algorithms,
 * for example a maximum matching:
 * <blockquote><pre>
 * if(Algorithms.MaximumMatching(G)!=true){
 * 	System.err.println("Maximum matching is not perfect for V_e.");
 * }
 * </pre></blockquote>
 * 
 * @see Algorithms
 */
public class BipartiteGraph {
	/** Equation node list */
	List<BipartiteNode> Ve = new ArrayList<BipartiteNode>();
	/** Differentiated equations, pending to add */
	List<BipartiteNode> VeDiff = new ArrayList<BipartiteNode>();
	/** Variable node list. */
	List<BipartiteNode> Vv = new ArrayList<BipartiteNode>();
	/** Differentiated variables, pending to add */
	List<BipartiteNode> VvDiff = new ArrayList<BipartiteNode>();

	/** The graph name */
	String name;

	/**
	 * Crates a graph for the given name
	 * @param name the name of the graph
	 */
	public BipartiteGraph(String name){
		this.name = name;
	};

	/**
	 * Creates an unamed graph
	 */
	public BipartiteGraph(){
		this("unamed");
	};

	/**
	 * @return the list of equations
	 */
	public List<BipartiteNode> getEqs(){
		return Ve;
	}
	/**
	 * @return the list of variables
	 */
	public List<BipartiteNode> getVars(){
		return Vv;
	}

	/**
	 * @return The number of V_e nodes in the graph.
	 */
	public int ne() {
		return Ve.size();
	}

	/**
	 * @return The number of V_v nodes in the graph.
	 */
	public int nv() {
		return Vv.size();
	}

	/**
	 * @return the graph name.
	 */
	public String getName() {
		return name;
	}	

	/**
	 * Set the graph name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}	
	/**
	 * Add a new V_v node.
	 * One of the ways to build the graph is adding nodes.
	 * @see #addEdge(String, String)
	 */
	public void addV(BipartiteNode v){
		Vv.add(v);
	}
	/**
	 * Add a new V_e node.
	 * One of the ways to build the graph is adding nodes.
	 * @see #addV(BipartiteNode)
	 */
	public void addE(BipartiteNode e){
		Ve.add(e);
	}

	/**
	 * @param name the name of the variable
	 * @return the variable if some
	 */
	public BipartiteNode getVar(String name){
		return find(getVars(), name);
	}
	/**
	 * @param name the name of the equation
	 * @return the equation if some
	 */
	public BipartiteNode getEq(String name){
		return find(getEqs(), name);
	}

	/**
	 * Try to find an element with the given name on the given list
	 * @param list the list to search into
	 * @param name the name to look for
	 * @return the Node if some
	 */
	private static BipartiteNode find(List<BipartiteNode> list, String name){
		for(BipartiteNode n : list){
			if(n.toString().equals(name))
				return n;
		}
		return null;
	}

	/**
	 * Adds an given edge.
	 * This is a convenient way to build a graph given the labels of a ve and vv node.
	 * If the given node labels does not exists in the graph they are created on the fly.
	 */
	public void addEdge(String ve, String vvdiff){
		BipartiteNode vep = find(Ve, ve);
		if(vep==null)
			Ve.add(vep = new BipartiteNode(ve));

		BipartiteNode vvp = findVar(vvdiff);
		vep.addEdge(vvp);
	}
	
	BipartiteNode findVar(String vvdiff){
		String vv = vvdiff;
		if(vvdiff.charAt(vvdiff.length()-1) == '\''){
			vv = vvdiff.substring(0, vvdiff.length()-1);
			BipartiteNode vvp = findVar(vv);

			if(vvp.getDiff()!=null)
				vvp = vvp.getDiff();
			else{
				vvp = vvp.differentiate();
				Vv.add(vvp);
				return vvp;
			}
		}
		BipartiteNode vvp = find(Vv, vv);
		if(vvp==null)
			Vv.add(vvp = new BipartiteNode(vv));
		
		return vvp;
	}

	/**
	 * Differentiate a V_e node
	 * Creates a differential version of the node, and the edges accordingly structural differentiation.
	 * 
	 * <b>NOTE</b> If a differential version of the node already exists the function only
	 * returns the previous created node.
	 */
	public BipartiteNode diffEquation(BipartiteNode e){
		return diffEquation(e, true);
	}

	/**
	 * Differentiate a V_e node
	 * Creates a differential version of the node, and the edges accordingly structural differentiation.
	 * If the parameter addNow is true the new equation and variables are added to the corresponding
	 * lists, otherwise the addition is postpone until the user calls {@link #applyDiffs()}.
	 * 
	 * @note If a differential version of the node already exists the function only
	 * returns the previous created node.
	 */
	BipartiteNode diffEquation(BipartiteNode e, boolean addNow){
		BipartiteNode diff = e.getDiff();
		if(diff!=null)
			return diff;
		diff = e.differentiate();
		BipartiteNode vdiff;
		
		for(BipartiteEdge ed=e.getEdges(); ed!=null; ed=ed.getNext()){
			BipartiteNode vv = ed.getNode2();
			vdiff = vv.getDiff();
			if(vdiff==null){
				vdiff = vv.differentiate();
				if(addNow)
					Vv.add(vdiff);
				else
					VvDiff.add(vdiff);
			}
			diff.addEdge(vdiff);
		}
		if(addNow)
			Ve.add(diff);
		else
			VeDiff.add(diff);
		return diff;
	}

	/**
	 * Differentiate the given variable node.
	 * @return the new variable node.
	 */
	public BipartiteNode diffVariable(BipartiteNode v){
		return diffVariable(v, true);
	}

	/**
	 * Differentiate the given variable node.
	 * If addNow is false remember to call {@link #applyDiffs()} sometime later.
	 * 
	 * @param v the node to diff
	 * @param addNow if true add the variable to the list, otherwise the addition is postponed
	 * @return the new variable node.
	 */
	BipartiteNode diffVariable(BipartiteNode v, boolean addNow){
		BipartiteNode diff = v.getDiff();
		if(diff!=null)
			return diff;
		diff = v.differentiate();
		if(addNow)
			Vv.add(diff);
		else
			VvDiff.add(diff);
		return diff;
	}

	/**
	 * Add to the list of variables and equations any pending elements.
	 * This function should be called if {@link #diffEquation(BipartiteNode, boolean)} or
	 * {@link #diffVariable(BipartiteNode, boolean)} was called with the addNow argument false.
	 */
	void applyDiffs(){
		Ve.addAll(VeDiff);
		VeDiff.clear();
		Vv.addAll(VvDiff);
		VvDiff.clear();
	}

	/**
	 * Clear the current matching.
	 * This function clear all connections of all V_e Node's of the Graph.
	 * Then {@link BipartiteNode#getConnected()} will return null for all nodes after this function.
	 */
	public void clearMatching(){
		for(BipartiteNode ve : getEqs())
			ve.connect(null);
	}


	public String toString(){
		String out;
		out = "graph " + this.getName() + '{';

		for(BipartiteNode ve : getEqs()){
			out += "\n\t";
			for(BipartiteEdge ed=ve.getEdges(); ed!=null; ed=ed.getNext()){
				BipartiteNode vv = ed.getNode2();
				if(vv.getConnected() != ve)
					out += ve.toString() + "--" + vv.toString() + ' ';
				else
					out += ve.toString() + "==" + vv.toString() + ' ';
			}
		}
		out += "\n}";
		return out;
	}
}

