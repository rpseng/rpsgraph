package br.eng.rps.graph.test;

import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import br.eng.rps.graph.Algorithms;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.BipartiteNode;
import br.eng.rps.graph.utils.Utils;

public class AlgorithmsTest extends TestCase {
	
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

	public void testMaxMatchingSimple(){
		BipartiteGraph G = new BipartiteGraph("HeatEx");
		G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
		G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
		G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
		G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6");

		boolean ret = Algorithms.MaximumMatching(G);
		
		assertTrue(ret);
	}
	public void testDMSimple(){
		BipartiteGraph G = new BipartiteGraph("HeatEx");
		G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
		G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
		G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
		G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6");

		boolean ret = Algorithms.MaximumMatching(G);
		assertTrue(ret);
		
		// check a DM decomposition
		int parts[] = {0, 0};
		Algorithms.DulmageMendelshon(G, parts);
		
		// all unconstrainted
		assertEquals(0, parts[0]);
		assertEquals(G.nv(), parts[1]);
	}
	
	/**
	 * Test for a maximum maching when the given file name should have a perfect matching.
	 */
	public void maxMatching(String file) throws Exception{
		BipartiteGraph G = loadGraph(file);
		
		double time;
		boolean ret;
		
		Utils.tic();
		ret = Algorithms.MaximumMatching(G);
		time = Utils.toc();
		
		assertTrue(ret);
		
		// all nodes should be unmarked
		for(BipartiteNode ve : G.getEqs()){
			assertFalse(ve.hasFlag(Algorithms.FLAG_COLORED|Algorithms.FLAG_MARKED));
		}
		
		System.out.println(file + " time:" + time + " nv:" + G.nv());
	}
	
	/**
	 * DAE analysis using Soares algorithm
	 */
	public void daeAnalysis(String file) throws Exception{
		BipartiteGraph G = loadGraph(file);
		
		double time;
		int ret;
		
		Utils.tic();
		ret = Algorithms.SoaresSecchi(G, false);
		time = Utils.toc();
		
		assertEquals(Algorithms.RET_SUCCESS, ret);
		
		System.out.println(file + " time:" + time + " nv:" + G.nv());
	}
	
	public void testMaxMatchingAmmonia() throws Exception{
		maxMatching("dots/ammonia.dot");
	}
	public void testMaxMatchingColumnSteady5() throws Exception{
		maxMatching("dots/columnSteady5.dot");
	}
	public void testMaxMatchingColumnSteady11() throws Exception{
		maxMatching("dots/columnSteady11.dot");
	}
	public void testUgav20() throws Exception{
		daeAnalysis("dots/ugav20.dot");
	}
	public void testUgav40() throws Exception{
		daeAnalysis("dots/ugav40.dot");
	}
	public void testUgav80() throws Exception{
		daeAnalysis("dots/ugav80.dot");
	}
}
