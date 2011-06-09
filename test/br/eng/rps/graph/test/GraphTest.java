package br.eng.rps.graph.test;

import junit.framework.TestCase;
import br.eng.rps.graph.BipartiteEdge;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.BipartiteNode;

public class GraphTest extends TestCase {

	/**
	 * Creates a Graph using addEdge.
	 */
	public void testAddEdge(){
		BipartiteGraph G = new BipartiteGraph("HeatEx");
		G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
		G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
		G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
		G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6");
		
		assertEquals(4, G.ne());
		assertEquals(6, G.nv());
	}
	
	public void testAddEdge2(){
		BipartiteGraph G = new BipartiteGraph("reactor");
		G.addEdge("f_1", "C"); G.addEdge("f_1", "R"); G.addEdge("f_1", "C'");
		G.addEdge("f_2", "T"); G.addEdge("f_2", "R"); G.addEdge("f_2", "T_c"); G.addEdge("f_2", "T'");
		G.addEdge("f_3", "C"); G.addEdge("f_3", "T"); G.addEdge("f_3", "R");
		G.addEdge("f_4", "C");
	
		System.out.println(G);
	}
	
	public void testDiffVar(){
		BipartiteGraph G = new BipartiteGraph("HeatEx");
		G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
		G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
		G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
		G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6");
		
		BipartiteNode x1 = G.getVar("x_1");
		BipartiteNode x1p = G.diffVariable(x1);
		
		assertEquals(x1.getDiff(), x1p);
		assertEquals(x1, x1p.getOrig());
		assertNull(x1p.getDiff());
		assertNull(x1.getOrig());
	}
	
	public void testDiffVarApplyDiffs(){
		BipartiteGraph G = new BipartiteGraph("HeatEx");
		G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
		G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
		G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
		G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6");
		
		int nv = G.nv();

		BipartiteNode x1 = G.getVar("x_1");
		BipartiteNode x1p = G.diffVariable(x1);

		assertEquals(x1.getDiff(), x1p);
		assertEquals(x1, x1p.getOrig());
		
		assertEquals(nv+1, G.nv());
	}
	public void testDiffEq(){
		BipartiteGraph G = new BipartiteGraph("HeatEx");
		G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
		G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
		G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
		G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6");
		
		BipartiteNode f1 = G.getEq("f_1");
		BipartiteNode f1p = G.diffEquation(f1);
		
		assertEquals(f1.getDiff(), f1p);
		assertEquals(f1, f1p.getOrig());
		assertNull(f1p.getDiff());
		assertNull(f1.getOrig());
		
		BipartiteEdge it1 = f1.getEdges();
		while(it1!=null){
			BipartiteNode x1 = it1.getNode2();
			assertTrue(f1p.findEdge(x1.getDiff()));
			it1 = it1.getNext();
		}
		it1 = f1p.getEdges();
		while(it1!=null){
			BipartiteNode x1 = it1.getNode2();
			assertTrue(f1.findEdge(x1.getOrig()));
			it1 = it1.getNext();
		}
	}
	
	public void testDiffEqApplyDiffs(){
		BipartiteGraph G = new BipartiteGraph("HeatEx");
		G.addEdge("f_1", "x_1"); G.addEdge("f_1", "x_2"); G.addEdge("f_1", "x_3");
		G.addEdge("f_2", "x_2"); G.addEdge("f_2", "x_4");
		G.addEdge("f_3", "x_3"); G.addEdge("f_3", "x_5");
		G.addEdge("f_4", "x_4"); G.addEdge("f_4", "x_5"); G.addEdge("f_4", "x_6");
		
		int ne = G.ne();
		int nv = G.nv();
		
		BipartiteNode f1 = G.getEq("f_1");
		BipartiteNode f1p = G.diffEquation(f1);
		
		assertEquals(f1.getDiff(), f1p);
		assertEquals(f1, f1p.getOrig());
		assertNull(f1p.getDiff());
		assertNull(f1.getOrig());
		
		assertEquals(ne+1, G.ne());
		assertEquals(nv+3, G.nv());
	}
}
