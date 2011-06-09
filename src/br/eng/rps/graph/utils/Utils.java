package br.eng.rps.graph.utils;

import java.io.InputStream;
import java.io.PrintStream;

import br.eng.rps.graph.Algorithms;
import br.eng.rps.graph.BipartiteEdge;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.BipartiteNode;

/**
 * Utility functions for importing/exporting bipartite graphs.
 * 
 * @author rafael
 *
 */
public class Utils {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private Utils(){}

	/**
	 * Export the graph to be included as a LaTeX Tikz figure.
	 * 
	 * <p>This function will produce the commands to generate a figure in
	 * LaTeX using the package Tikz.
	 * But in order to get it working, the commands in file tikzgraph.tex
	 * (accompanying this package) should be in the preamble of your document.
	 * 
	 * @param G the graph to be exported
	 * @param out the stream to print into
	 * @param grayAlgebraic if <code>true</code> the algebraic variables are grayed out
	 */
	public static void ExportTikz(BipartiteGraph G, PrintStream out, boolean grayAlgebraic){
		int width = Math.max(G.ne(), G.nv());
		out.println("\\begin{tikzpicture}");

		out.println("\t%% equations");
		int i = 1;
		for(BipartiteNode ve : G.getEqs()){
			out.print("\t\\AddEq{" + ve.toString() + "}{" + (i + (width-G.ne())/2) + "}");
			if(ve.getOrig()!=null){
				out.print("\\AddDiff{" + ve.getOrig().toString() + "}{" + ve.toString() + '}');
			}
			if(ve.hasFlag(Algorithms.FLAG_COLORED|Algorithms.FLAG_MARKED))
				out.print("\\MarkNode{" + ve.toString() + '}');
			out.print('\n');
			++i;
		}
		out.println("\t%% variables");
		int j = 1;
		for(BipartiteNode vv : G.getVars()){
			if(vv.hasFlag(Algorithms.FLAG_DELETED))
				continue;
			out.println("\t\\AddVar{" + vv.toString() + "}{" + (j + (width-G.nv())/2) + "}");
			++j;
		}

		out.println("\t% edges");
		for(BipartiteNode ve : G.getEqs()){
			out.print("\n\t");
			for(BipartiteEdge ed=ve.getEdges(); ed!=null; ed=ed.getNext()){
				BipartiteNode vv = ed.getNode2();
				if(vv.hasFlag(Algorithms.FLAG_DELETED))
					continue;
				if(vv.getConnected() == ve)
					out.print("\\AddConn");
				else
					out.print("\\AddEdge");
				if(grayAlgebraic && vv.getDiff()!=null)
					out.print("Gray");
				out.print("{" + ve.toString() + "}{" + vv.toString() + '}');
			}
		}
		if(grayAlgebraic){
			out.print("\n\t% gray algebraic variables\n\t");
			for(BipartiteNode vv : G.getVars()){
				if(vv.getDiff()!=null)
					out.print("\\GrayNode{" + vv.toString() + '}');
			}
		}
		out.print("\n\\end{tikzpicture}\n");
	}


	/**
	 * Export the graph as a <a href="http://www.graphviz.org">GraphViz</a> DOT language.
	 * @param G the graph to be exported
	 * @param out the stream to print into
	 * 
	 * @see #ImportGraphViz(InputStream, BipartiteGraph)
	 */
	public static void ExportGraphViz(BipartiteGraph G, PrintStream out){
		out.println("graph " + G.getName() + '{');
		out.println("node[shape=circle,fixedsize=1]");
		out.println("edge[color=\"gray\", style=\"setlinewidth(2)\"]");

		// not connected edges
		for(BipartiteNode ve : G.getEqs()){
			out.print("\n\t");
			for(BipartiteEdge ed=ve.getEdges(); ed!=null; ed=ed.getNext()){
				BipartiteNode vv = ed.getNode2();
				if(vv.hasFlag(Algorithms.FLAG_DELETED) )
					continue;
				if(vv.getConnected() != ve)
					out.print('"' + ve.toString() + "\"--\"" + vv.toString() + "\" ");
			}
		}

		// connected edges
		out.println("\nedge[color=\"black\", style=\"setlinewidth(4)\"]");
		for(BipartiteNode ve : G.getEqs()){
			for(BipartiteEdge ed=ve.getEdges(); ed!=null; ed=ed.getNext()){
				BipartiteNode vv = ed.getNode2();
				if(vv.hasFlag(Algorithms.FLAG_DELETED) )
					continue;
				if(vv.getConnected() == ve)
					out.println("\t\"" + ve.toString() + "\"--\"" + vv.toString() + '"');
			}
		}
		out.println('}');
	}

	/**
	 * Import the graph from the given input stream in the
	 * <a href="http://www.graphviz.org">GraphViz</a> DOT language.
	 * 
	 * <p>Actually, a subset of the DOT language is supported.
	 * A simple valid example follows:
	 * <blockquote><pre>
	 * graph InitialSet{
	 *  node[shape=circle,fixedsize=1]
	 *  edge[color="gray"]
	 *  
	 *  f_1--"x_1'"  f_1--"x_2'"
	 *  f_2--x_2
	 * }
	 * </pre></blockquote>
	 * In the above example some optional formating attributes are given (<code>shape</code>,
	 * <code>color</code>, etc). These flags are ignored when the graph is imported.
	 * Note that if one node label contains symbols like <code>',+,-</code>, etc it needs to be
	 * quoted.
	 * 
	 * @param in the input stream containing a <a href="http://www.graphviz.org">GraphViz</a> DOT language graph
	 * @param G the graph were to put the contents
	 * @throws Exception if a invalid graph is found
	 * 
	 * @see #ExportGraphViz(BipartiteGraph, PrintStream)
	 */
	public static void ImportGraphViz(InputStream in, BipartiteGraph G) throws Exception {
		DotLexer lexer = new DotLexer(in);
		DotParser parser = new DotParser(lexer);

		parser.graph(G);
	}

	private static long tstart;
	private static long tend;
	private static boolean needToc;

	/**
	 * Starts the time counting.
	 * 
	 * <p>Usage:
	 * <blockquote><pre>
	 * Utils.tic();
	 * 
	 * // some time consuming task ...
	 * 
	 * Utils.toc();
	 * 
	 * System.out.println("Time for the execution of the task: " + Utils.toc());
	 * </pre></blockquote>
	 * 
	 * @see #toc()
	 */
	public static void tic(){
		tstart = tend = System.currentTimeMillis();
		needToc = true;
	}

	/**
	 * Returns the system time since the last call to {@link #tic()}.
	 * 
	 * <p>Multiple calls to this function without {@link #tic()} again will return
	 * always the same value.
	 * @see #tic()
	 */
	public static double toc(){
		if(needToc){
			tend = System.currentTimeMillis();
			needToc = false;
		}
		return (double)(tend - tstart)/1000.0;
	}
}
