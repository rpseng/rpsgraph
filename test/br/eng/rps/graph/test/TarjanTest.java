package br.eng.rps.graph.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

import junit.framework.TestCase;
import br.eng.rps.graph.Algorithms;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.Tarjan;
import br.eng.rps.graph.TarjanLinkedList;
import br.eng.rps.graph.utils.Utils;

public class TarjanTest extends TestCase {
	
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
		
		G.addEdge("f_5", "x_4");
		G.addEdge("f_6", "x_3");

		boolean ret = Algorithms.MaximumMatching(G);
		
		assertTrue(ret);
		
		Tarjan tarjan = new Tarjan();
		Collection<BipartiteGraph> scc = tarjan.tarjan(G);
		
		int i = 0;
		for(BipartiteGraph comp : scc){
			System.out.println("Block " + ++i);
			System.out.println(comp.toString());
		}
	}

	public void testNLATarjan() throws Exception{
		BipartiteGraph G = loadGraph("dots/nla_tarjan.dot");

		boolean ret = Algorithms.MaximumMatching(G);
		
		assertTrue(ret);
		
		Tarjan tarjan = new Tarjan();
		Collection<BipartiteGraph> scc = tarjan.tarjan(G);
		
		int i = 0;
		for(BipartiteGraph comp : scc){
			System.out.println("Block " + ++i);
			System.out.println(comp.toString());
		}
	}

	
	public void testMaxAmonia() throws Exception{
		BipartiteGraph G = loadGraph("dots/ammonia.dot");

		boolean ret = Algorithms.MaximumMatching(G);
		
		assertTrue(ret);
		
		Tarjan tarjan = new Tarjan();
		Collection<BipartiteGraph> scc = tarjan.tarjan(G);
		
		int i = 0;
		for(BipartiteGraph comp : scc){
			System.out.println("Block " + ++i + " nv=" + comp.getVars().size());
			System.out.println(comp.toString());
		}
	}
	
	public void testMaxColumn() throws Exception{
		BipartiteGraph G = loadGraph("dots/columnSteady11.dot");

		boolean ret = Algorithms.MaximumMatching(G);
		
		assertTrue(ret);
		
		Tarjan tarjan = new Tarjan();
		Collection<BipartiteGraph> scc = tarjan.tarjan(G);
		
		int i = 0;
		for(BipartiteGraph comp : scc){
			System.out.println("Block " + ++i + " nv=" + comp.getVars().size());
			System.out.println(comp.toString());
		}
	}

	public void testTimmingAmmonia() throws Exception{
		BipartiteGraph G = loadGraph("dots/ammonia.dot");

		boolean ret = Algorithms.MaximumMatching(G);
		
		assertTrue(ret);
		
		Tarjan tarjan = new Tarjan();
		TarjanLinkedList tarjanLinked = new TarjanLinkedList();
		
		Utils.tic();
		tarjanLinked.tarjan(G);
		tarjanLinked.tarjan(G);
		tarjanLinked.tarjan(G);
		System.out.println(G.getName() + " time to partite (Linked): " + Utils.toc());

		Utils.tic();
		tarjan.tarjan(G);
		tarjan.tarjan(G);
		tarjan.tarjan(G);
		System.out.println(G.getName() + " time to partite (Array): " + Utils.toc());
	}

	public void testTimmingColumn() throws Exception{
		BipartiteGraph G = loadGraph("dots/columnSteady11.dot");

		boolean ret = Algorithms.MaximumMatching(G);
		
		assertTrue(ret);
		
		Tarjan tarjan = new Tarjan();
		TarjanLinkedList tarjanLinked = new TarjanLinkedList();
		
		Utils.tic();
		tarjanLinked.tarjan(G);
		System.out.println(G.getName() + " time to partite (Linked): " + Utils.toc());

		Utils.tic();
		tarjan.tarjan(G);
		System.out.println(G.getName() + " time to partite (Array): " + Utils.toc());
	}
}
