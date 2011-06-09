package br.eng.rps.graph.test;


import java.io.FileInputStream;
import java.io.InputStream;

import rpsgraph.Tool;

import junit.framework.TestCase;
import br.eng.rps.graph.Algorithms;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.BipartiteNode;
import br.eng.rps.graph.utils.Utils;

/**
 * Tests to generate the graphs on Rafael de Pelegrini Soares Dr. Thesis (2007)
 * 
 * @author rafael
 *
 */
public class ThesisTest extends TestCase {
	
	public void testReissigPantelides(){
		String args[] = {"dots/reissig.dot", "-p", "-v", "-t"};
		Tool.main(args);
	}
	public void testReissigPantelides_b(){
		String args[] = {"dots/reissig_b.dot", "-p", "-v", "-t"};
		Tool.main(args);
	}
	public void testReissigSoares(){
		String args[] = {"dots/reissig.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}
	public void testReissigSoares_b(){
		String args[] = {"dots/reissig_b.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}
	public void testReactorPantelides(){
		String args[] = {"dots/reactor.dot", "-p", "-v", "-t"};
		Tool.main(args);
	}
	public void testReactorSoares(){
		String args[] = {"dots/reactor.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}
	public void testUncontrollableFullSoares(){
		String args[] = {"dots/uncontrollable_full.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}
	public void testUncontrollableFixedSoares(){
		String args[] = {"dots/uncontrollable_fixed.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}
	public void testPend(){
		String args[] = {"dots/pend.dot", "-s", "-v"};
		Tool.main(args);
	}
	public void testPside(){
		String args[] = {"dots/pside.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}
	
	public void testPendPaths() throws Exception{
		BipartiteGraph g = new BipartiteGraph();
		InputStream file = new FileInputStream("dots/pend.dot");
		Utils.ImportGraphViz(file, g);
		
		assertEquals(Algorithms.RET_SUCCESS, Algorithms.SoaresSecchi(g, false));
		
		System.out.print("\n\nAlternating paths for not connected variables of " + g.getName());
		for(BipartiteNode v : g.getVars()){
			if(v.getConnected() == null){
				Algorithms.markAlternating(v, Algorithms.FLAG_MARKED);
				System.out.print("\nStarting at " + v.toString() + ':');
				for(BipartiteNode v2 : g.getVars()){
					if(v2.hasFlag(Algorithms.FLAG_MARKED)){
						System.out.print(v2.toString() + ' ');
						v2.removeFlag(Algorithms.FLAG_MARKED);
					}
				}
			}
		}
	}

	public void testCondenserPaths() throws Exception{
		BipartiteGraph g = new BipartiteGraph();
		InputStream file = new FileInputStream("dots/condenser.dot");
		Utils.ImportGraphViz(file, g);
		
		assertEquals(Algorithms.RET_SUCCESS, Algorithms.SoaresSecchi(g, false));
		
		System.out.print("\n\nAlternating paths for not connected variables of " + g.getName());
		for(BipartiteNode v : g.getVars()){
			if(v.getConnected() == null){
				Algorithms.markAlternating(v, Algorithms.FLAG_MARKED);
				System.out.print("\nStarting at " + v.toString() + ':');
				for(BipartiteNode v2 : g.getVars()){
					if(v2.hasFlag(Algorithms.FLAG_MARKED)){
						System.out.print(v2.toString() + ' ');
						v2.removeFlag(Algorithms.FLAG_MARKED);
					}
				}
			}
		}
	}
	public void testPistonPaths() throws Exception{
		BipartiteGraph g = new BipartiteGraph();
		InputStream file = new FileInputStream("dots/piston.dot");
		Utils.ImportGraphViz(file, g);
		
		assertEquals(Algorithms.RET_SUCCESS, Algorithms.SoaresSecchi(g, false));
		
		System.out.print("\n\nAlternating paths for not connected variables of " + g.getName());
		for(BipartiteNode v : g.getVars()){
			if(v.getConnected() == null){
				Algorithms.markAlternating(v, Algorithms.FLAG_MARKED);
				System.out.print("\nStarting at " + v.toString() + ':');
				for(BipartiteNode v2 : g.getVars()){
					if(v2.hasFlag(Algorithms.FLAG_MARKED)){
						System.out.print(v2.toString() + ' ');
						v2.removeFlag(Algorithms.FLAG_MARKED);
					}
				}
			}
		}
	}
}
