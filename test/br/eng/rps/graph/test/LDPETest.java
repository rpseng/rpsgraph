package br.eng.rps.graph.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;

import junit.framework.TestCase;
import br.eng.rps.graph.Algorithms;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.Tarjan;
import br.eng.rps.graph.utils.Utils;

public class LDPETest extends TestCase {
	
	BipartiteGraph loadGraph(String file) throws Exception{
		BipartiteGraph G = new BipartiteGraph();
		InputStream in = new FileInputStream(file);
		
		double time;
		Utils.tic();
		Utils.ImportGraphViz(in , G);
		time = Utils.toc();
		
		System.out.println(file + " time to load:" + time + " nv:" + G.nv());
		
		return G;
	}

	public void testFixed() throws Exception{
		BipartiteGraph G = loadGraph("dots/ldpe_fixed.dot");

		boolean ret = Algorithms.MaximumMatching(G);
		
		PrintStream out = new PrintStream("ldpd_fixed.out");
		
		assertTrue(ret);
		
		Tarjan tarjan = new Tarjan();
		Collection<BipartiteGraph> scc = tarjan.tarjan(G);
		
		int i = 0;
		for(BipartiteGraph comp : scc){
			out.println("Block " + ++i);
			out.println(comp.toString());
		}
	}
}
