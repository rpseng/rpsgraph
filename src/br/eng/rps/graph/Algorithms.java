package br.eng.rps.graph;

import java.util.Collection;


/**
 * Algorithms to be applied on {@link BipartiteGraph} objects.
 * 
 * <p>This class should not be instantiated by the user, it only encapsulates
 * a set of algorithms to be applied to {@link BipartiteGraph} objects.
 * 
 * @author rafael
 */
public class Algorithms {
	/** Algorithm successfully returned */
	public static final int RET_SUCCESS = 1;
	/** Algorithm failed */
	public static final int RET_FAILED = 0;
	/** Algorithm returned in an intermediate step */
	public static final int RET_STEP = -1;

	/** Bit flag colored */
	public static final int FLAG_COLORED = 0x0001;
	/** Bit flag deleted */
	public static final int FLAG_DELETED = 0x0002;
	/** Bit flag marked */
	public static final int FLAG_MARKED  = 0x0004;
	/** Bit flag under-constrained */
	public static final int FLAG_UNDER   = 0x0010;
	/** Bit flag over-constrained */
	public static final int FLAG_OVER    = 0x0020;
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private Algorithms(){}

	/**
	 * Augment a matching in a given bipartite graph.
	 * 
	 * <p>This function tries to augment the current matching stored in G
	 * by including the given node ve into the match.
	 * This function is the base of {@link #MaximumMatching(BipartiteGraph)}
	 * and {@link #Pantelides(BipartiteGraph, boolean)} algorithms.
	 * 
	 * @param G the graph to augment the matching
	 * @param ve the vertex to include in the matching
	 * @return {@link #RET_SUCCESS} if ve could be included in the matching,
	 * otherwise {@link #RET_FAILED}.
	 */
	public static int AugmentMatching(BipartiteGraph G, BipartiteNode ve){
		// try a direct connection
		for(BipartiteEdge e=ve.getEdges(); e!=null; e=e.getNext()){
			BipartiteNode vv = e.getNode2();
			
			if(vv.getConnected()==null && !(vv.hasFlag(FLAG_DELETED)) ){
				ve.connect(vv);
				return RET_SUCCESS;
			}
		}
		// try an alternating path
		for(BipartiteEdge e=ve.getEdges(); e!=null; e=e.getNext()){
			BipartiteNode vv = e.getNode2();
			
			ve.setFlag(FLAG_COLORED);
			BipartiteNode ve2 = vv.getConnected();
			if(!(vv.hasFlag(FLAG_DELETED)) && ve2!=null && !(ve2.hasFlag(FLAG_COLORED))
				&& AugmentMatching(G, ve2)==RET_SUCCESS){
				ve.connect(vv);
				return RET_SUCCESS;
			}
		}
		return RET_FAILED;
	}

	/**
	 * Helper function for {@link #AugmentMatching2(BipartiteGraph, BipartiteNode, boolean)}
	 * @param vv the node to check for elegibility
	 * @param alg the algebraic flag
	 * @return TRUE if the node is elegible, otherwise FALSE
	 */
	static private boolean isElegible(BipartiteNode vv, boolean alg){
		if(alg)
			return true;
		else
			return vv.getDiff() == null;
	}

	/**
	 * Augment a matching in a given bipartite graph.
	 * 
	 * <p>This function is similar to {@link #AugmentMatching(BipartiteGraph, BipartiteNode)}
	 * but with an additional flag <b>alg</b>.
	 * If it is <code>false</code>, only differential variables are included in the
	 * search, otherwise all variables are include.
	 * This function is for {@link #SoaresSecchi(BipartiteGraph, boolean)} as
	 * {@link #AugmentMatching(BipartiteGraph, BipartiteNode)} is for {@link #Pantelides(BipartiteGraph, boolean)}.
	 */
	public static int AugmentMatching2(BipartiteGraph G, BipartiteNode ve, boolean alg){
		// colour ve
		ve.setFlag(FLAG_COLORED);
		// try a direct connection
		for(BipartiteEdge e=ve.getEdges(); e!=null; e=e.getNext()){
			BipartiteNode vv = e.getNode2();
			if(vv.getConnected()==null && !(vv.hasFlag(FLAG_DELETED)) &&
					isElegible(vv, alg)){
				ve.connect(vv);
				return RET_SUCCESS;
			}
		}
		// try an alternating path
		for(BipartiteEdge e=ve.getEdges(); e!=null; e=e.getNext()){
			BipartiteNode vv = e.getNode2();
			BipartiteNode ve2 = vv.getConnected();
			if(!(vv.hasFlag(FLAG_DELETED)) && isElegible(vv, alg) &&
					ve2!=null && !(ve2.hasFlag(FLAG_COLORED)) && AugmentMatching2(G, ve2, alg)==RET_SUCCESS){
				ve.connect(vv);
				return RET_SUCCESS;
			}
		}
		return RET_FAILED;
	}

	/**
	 * Utility function to remove the given flag of a range of elements.
	 * 
	 * <p> This function just execute the following code:
	 * <code>
	 * 		for(GraphNode i : list)
	 * 			i.removeFlag(flag);
	 * </code>
	 * 
	 * @param the list of nodes
	 * @param the flag to be removed
	 */
	static void removeFlag(Collection<BipartiteNode> list, int flag){
		for(BipartiteNode i : list)
			i.removeFlag(flag);
	}

	/**
	 * Utility function to exchange a flag by another in a given list of nodes.
	 * 
	 * <p>This function just execute the following code:
	 * <blockquote><pre>
	 * 		for(BipartiteNode i : list){
	 * 			if(i.hasFlag(f1)){
	 * 				i.removeFlag(f1);
	 * 				i.setFlag(f2);
	 * 			}
	 * 		}
	 * <blockquote><pre>
	 */
	static void replaceFlag(Collection<BipartiteNode> list, int f1, int f2){
		for(BipartiteNode i : list){
			if(i.hasFlag(f1)){
				i.removeFlag(f1);
				i.setFlag(f2);
			}
		}
	}

	/**
	 * Construct a maximum matching for a bipartite graph.
	 * 
	 * <p>If the matching is perfect for V_e returns <code>true</code>, otherwise <code>false</code>.
	 * This algorithm runs {@link #AugmentMatching(BipartiteGraph, BipartiteNode)}
	 * for each V_e node of the Graph.
	 * 
	 * @param G the graph to be analyzed
	 * @return <code>true</code> if a perfect matching with respect to V_e is found (the
	 * matching contains all V_e vertices) 
	 */
	public static boolean MaximumMatching(BipartiteGraph G){
		boolean isPerfect = true;
		// augment the matching one by one
		for(BipartiteNode ve : G.getEqs()){
			if(ve.getConnected()==null && AugmentMatching(G, ve)!=RET_SUCCESS){
				isPerfect = false;
			}
			// uncolour all nodes
			removeFlag(G.getEqs(), FLAG_COLORED);
		}
		return isPerfect;
	}
	
	/**
	 * Mark with the given flag all alternating paths starting on the given node.
	 * 
	 * <p>This function considers that the given node is exposed by the current
	 * matching and will mark all alternating paths starting on that node.
	 * This algorithm is used by the {@link #DulmageMendelshon(BipartiteGraph, int[])}
	 * algorithm.
	 * 
	 * <p><b>NOTE</b> if the matching contains the starting node then this function
	 * will not work properly.
	 * 
	 * @param v the exposed stating node
	 * @param flag the flag to be used when marking the nodes with {@link BipartiteNode#setFlag(int)}
	 * @return the number of nodes marked, including the given node
	 */
	public static int markAlternating(BipartiteNode v, int flag){
		int nMarked = 0;
		if(v.hasFlag(flag))
			return nMarked; // already marked
		
		// colour ve
		v.setFlag(flag);
		++nMarked;
		
		// look for alternating paths and mark it
		for(BipartiteEdge e=v.getEdges(); e!=null; e=e.getNext()){
			BipartiteNode vv = e.getNode2();
			BipartiteNode ve2 = vv.getConnected(); 
			if(ve2!=null && ve2!=v && !ve2.hasFlag(flag)){
				nMarked += markAlternating(ve2, flag);
			}
		}
		return nMarked;
	}

	/**
	 * Executes the Dulmage-Mendelshon decomposition on the given bipartite graph.
	 * 
	 * <p>This algorithm starts from a maximum matching and mark the nodes with the
	 * flags {@link #FLAG_OVER} AND {@link #FLAG_UNDER} for the over- and under-constrained
	 * partitions, respectively.
	 * 
	 * <p>This algorithm will only work if the given graph contains a maximum matching.
	 * This can be achieved by calling {@link #MaximumMatching(BipartiteGraph)} prior to
	 * this function. For Differential-Algebraic Equation (DAE) problems a maximum matching
	 * can be obtained by calling {@link #SoaresSecchi(BipartiteGraph, boolean)}.
	 * 
	 * @param G the bipartite graph with a maximum matching already computed
	 * @param partitionSizes vector where to put the partition sizes, should have
	 * length = 2. The over- and under-constrained sizes will be in
	 * <code>partitionSizes[0]</code> and <code>partitionSizes[1]</code>, respectively.
	 */
	public static void DulmageMendelshon(BipartiteGraph G, int []partitionSizes){
		// reset the counters
		partitionSizes[0] = partitionSizes[1] = 0;

		// mark the over constrained partition
		for(BipartiteNode ve : G.getEqs()){
			if(ve.getConnected() == null){
				partitionSizes[0] += markAlternating(ve, FLAG_OVER);
			}
		}
		// mark the under constrained partition
		for(BipartiteNode vv : G.getVars()){
			if(vv.getConnected()==null){
				partitionSizes[1] += markAlternating(vv, FLAG_UNDER);
			}
		}

	}

	/**
	 * Pantelides's algorithm for the analysis of Differential-Algebraic Equation (DAE) systems.
	 * 
	 * <p>This is the classic Pantelides' algorithm but implemented in the light
	 * of the graph theory.
	 * It runs {@link #AugmentMatching(BipartiteGraph, BipartiteNode)}
	 * sequentially for each V_e node. For each V_e node, if a matching is not
	 * found the colored set of nodes (reached by alternating paths) is differentiated.
	 * 
	 * <p><b>NOTE</b> this algorithm will run indefinitely if the given graph is
	 * structurally singular.
	 * 
	 * @param G the graph to be analyzed
	 * @param oneStep if <code>true</code> only one step is executed, then consecutive steps can
	 * be exported or printed for documentation or debugging purposes.
	 * 
	 * @see #Pantelides2(BipartiteGraph, boolean)
	 */
	public static int Pantelides(BipartiteGraph G, boolean oneStep){
		boolean shouldReturn = false;
		
		// remove the pure algebraic variables (x)
		for(BipartiteNode vv : G.getVars()){
			if(vv.getDiff()!=null && vv.getDiff().getEdges()!=null && !(vv.hasFlag(FLAG_DELETED)) ){
				vv.setFlag(FLAG_DELETED);
				shouldReturn = true;
			}
		}

		if(oneStep){
			// diff all COLORED equations
			for(BipartiteNode ve2 : G.getEqs()){
				if(ve2.hasFlag(FLAG_COLORED) ){
					G.diffEquation(ve2, false);
					ve2.removeFlag(FLAG_COLORED);
				}
			}
			G.applyDiffs();
		}
		if(oneStep && shouldReturn)
			return RET_STEP;
			
		// augment the matching one by one
		for(BipartiteNode ve : G.getEqs()){
			if(ve.getDiff()!=null || ve.getConnected()!=null)
				continue;
			if(AugmentMatching(G, ve)==RET_FAILED){
				if(oneStep)
					return RET_STEP;
				// diff all COLORED equations
				for(BipartiteNode ve2 : G.getEqs()){
					if(ve2.hasFlag(FLAG_COLORED) ){
						G.diffEquation(ve2, false);
						ve2.removeFlag(FLAG_COLORED);
					}
				}
				G.applyDiffs();
			}
			else
				removeFlag(G.getEqs(), FLAG_COLORED);
		}
		return RET_SUCCESS;
	}

	/**
	 * Pantelides's algorithm for the analysis of Differential-Algebraic Equation (DAE) systems (version 2).
	 * 
	 * <p>This is pretty much the same as {@link #Pantelides(BipartiteGraph, boolean)}.
	 * The only difference is that it run in <i>phases</i>, the differentiations are only
	 * added to the graph at the end of a phase.
	 * A phase ends when all equations are analyzed. In the original version the
	 * differentiations are executed as they are discovered.
	 * Potential result or efficiency differences between the versions were not inspected yet. 
	 * 
	 * <p><b>NOTE</b> this algorithm will run indefinitely if the given graph is
	 * structurally singular.
	 * 
	 * @param G the graph to be analyzed
	 * @param oneStep if <code>true</code> only one step is executed, then consecutive steps can
	 * be exported or printed for documentation or debugging purposes.
	 * 
	 * @see #Pantelides(BipartiteGraph, boolean)
	 */
	public static int Pantelides2(BipartiteGraph G, boolean oneStep){
		boolean shouldReturn = false, needDiff = false;
		
		if(oneStep){
			// diff all COLORED equations
			for(BipartiteNode ve2 : G.getEqs()){
				if(ve2.hasFlag(FLAG_COLORED) ){
					G.diffEquation(ve2, false);
					ve2.removeFlag(FLAG_COLORED);
				}
			}
			G.applyDiffs();
		}
		if(oneStep && shouldReturn)
			return RET_STEP;
		
		while(true){
			// remove the pure algebraic variables (x)
			for(BipartiteNode vv : G.getVars()){
				if(vv.getDiff()!=null && vv.getDiff().getEdges()!=null && !(vv.hasFlag(FLAG_DELETED)) ){
					vv.setFlag(FLAG_DELETED);
					shouldReturn = true;
				}
			}
			if(oneStep && shouldReturn)
				return RET_STEP;

			// augment the matching one by one
			for(BipartiteNode ve : G.getEqs()){
				if(ve.getDiff()!=null || ve.getConnected()!=null)
					continue;
				if(AugmentMatching(G, ve)!=RET_SUCCESS)
					needDiff = true;
				else
					removeFlag(G.getEqs(), FLAG_COLORED);
			}
			if(!needDiff)
				return RET_SUCCESS;
			if(oneStep)
				return RET_STEP;

			// diff all COLORED equations
			for(BipartiteNode ve2 : G.getEqs()){
				if(ve2.hasFlag(FLAG_COLORED) ){
					G.diffEquation(ve2, false);
					ve2.removeFlag(FLAG_COLORED);
				}
			}
			G.applyDiffs();
		}
	}


	/**
	 * Algorithm of Soares and Secchi for the analysis of Differential-Algebraic Equation (DAE) systems.
	 * 
	 * <p>This algorithm was recently developed by Soares in his doctoral studies.
	 * It was inspired in the algorithm of Pantelides but with some advantages:
	 * <ul>
	 * <li> It does not suffer from the infinite recursion for singular systems
	 * <li> It can produce an index-zero (and Ordinary Differential Equation -ODE), hence
	 * it can distinguish between index-one and index-zero systems.
	 * <li> The resulting graph can be analyzed by the
	 * {@link #DulmageMendelshon(BipartiteGraph, int[])} in order to discover over- under-constrained
	 * partitions
	 * </ul>
	 * 
	 * <p>It runs {@link #AugmentMatching2(BipartiteGraph, BipartiteNode, boolean)}
	 * sequentially for each V_e node trying to associate it with a differential variable.
	 * For each V_e node, if such matching is not possible the set of nodes reached by
	 * alternating paths is marked. Then, an association with any variable (including the
	 * algebraic ones) is tried, if that is not possible {@link #RET_FAILED} is returned.
	 * Finally, the marked nodes are differentiated the and the next V_e node is analyzed.
	 * 
	 * @param G the graph to be analyzed
	 * @param oneStep if <code>true</code> only one step is executed, then consecutive steps can
	 * be exported or printed for documentation or debugging purposes.
	 * 
	 * @see #SoaresSecchi2(BipartiteGraph, boolean)
	 */
	public static int SoaresSecchi(BipartiteGraph G, boolean oneStep){
		boolean singular, needDiff;
		
		// diff the last step first
		if(oneStep){
			for(BipartiteNode ve2 : G.getEqs()){
				if(ve2.hasFlag(FLAG_MARKED) ){
					G.diffEquation(ve2, false);
					ve2.removeFlag(FLAG_MARKED);
				}
			}
			G.applyDiffs();
		}
		
		while(true){
			singular = needDiff = false;
			// augment the matching one by one
			for(BipartiteNode ve : G.getEqs()){
				if(ve.getConnected()!=null)
					continue;
				if(AugmentMatching2(G, ve, false)==RET_FAILED){
					needDiff = true;
					replaceFlag(G.getEqs(), FLAG_COLORED, FLAG_MARKED);
					if(AugmentMatching2(G, ve, true)==RET_FAILED)
						singular = true;
				}
				else
					removeFlag(G.getEqs(), FLAG_COLORED);
			}
			if(singular)
				return RET_FAILED;
			if(!needDiff)
				return RET_SUCCESS;
			if(oneStep)
				return RET_STEP;
			
			// diff all MARKED equations
			for(BipartiteNode ve2 : G.getEqs()){
				if(ve2.hasFlag(FLAG_MARKED) ){
					G.diffEquation(ve2, false);
					ve2.removeFlag(FLAG_MARKED);
				}
			}
			G.applyDiffs();
		}
	}

	/**
	 * Algorithm of Soares and Secchi for the analysis of Differential-Algebraic Equation (DAE)
	 * systems (version 2).
	 * 
	 * <p>This is pretty much the same as {@link #SoaresSecchi(BipartiteGraph, boolean)}.
	 * The only difference is that it run in <i>phases</i>, the differentiations are only
	 * added to the graph at the end of a phase.
	 * A phase ends when all equations are analyzed. In the original version the
	 * differentiations are executed as they are discovered.
	 * Potential result or efficiency differences between the versions were not inspected yet. 
	 * 
	 * @param G the graph to be analyzed
	 * @param oneStep if <code>true</code> only one step is executed, then consecutive steps can
	 * be exported or printed for documentation or debugging purposes.
	 * 
	 * @see #SoaresSecchi(BipartiteGraph, boolean)
	 */
	public static int SoaresSecchi2(BipartiteGraph G, boolean oneStep){
		boolean singular, needDiff;
		
		// diff the last step first
		if(oneStep){
			for(BipartiteNode ve2 : G.getEqs()){
				if(ve2.hasFlag(FLAG_MARKED) ){
					G.diffEquation(ve2, false);
					ve2.removeFlag(FLAG_MARKED);
				}
			}
			G.applyDiffs();
		}
		
		while(true){
			singular = needDiff = false;
			// augment the matching one by one (only diff variables)
			for(BipartiteNode ve : G.getEqs()){
				if(ve.getConnected()!=null)
					continue;
				if(AugmentMatching2(G, ve, false)==RET_FAILED){
					replaceFlag(G.getEqs(), FLAG_COLORED, FLAG_MARKED);
					needDiff = true;
				}
				else
					removeFlag(G.getEqs(), FLAG_COLORED);			
			}
			if(!needDiff)
				return RET_SUCCESS;
			for(BipartiteNode ve : G.getEqs()){
				if(ve.getConnected()==null && AugmentMatching2(G, ve, true)==RET_FAILED){
					singular = true;
				}
			}
			if(singular)
				return RET_FAILED;
			if(oneStep)
				return RET_STEP;
			// diff all MARKED equations
			for(BipartiteNode ve2 : G.getEqs()){
				if(ve2.hasFlag(FLAG_MARKED) ){
					G.diffEquation(ve2, false);
					ve2.removeFlag(FLAG_MARKED);
				}
			}
			G.applyDiffs();
		}
	}
}
