package br.eng.rps.graph.test;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import rpsgraph.Tool;

import junit.framework.TestCase;
import br.eng.rps.graph.Algorithms;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.BipartiteNode;
import br.eng.rps.graph.utils.Utils;

/**
 * Tests to generate the graphs on the article for SIAM Journal on Scientific Computing
 * 
 * @author rafael
 *
 */
public class SJSCTest extends TestCase {
	
	public void testReissigPantelides(){
		String args[] = {"dots/reissig.dot", "-p", "-v", "-t"};
		Tool.main(args);
	}
	public void testReissig_bPantelides(){
		String args[] = {"dots/reissig_b.dot", "-p", "-v", "-t"};
		Tool.main(args);
	}
	public void testReissigSoares(){
		String args[] = {"dots/reissig.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}
	
	public void testPistonPantelides(){
		String args[] = {"dots/piston.dot", "-p", "-v", "-t"};
		Tool.main(args);
	}
	public void testPistonSoares(){
		String args[] = {"dots/piston.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}

	public void testPistonPaths() throws Exception{
		BipartiteGraph g = new BipartiteGraph();
		InputStream file = new FileInputStream("dots/piston.dot");
		Utils.ImportGraphViz(file, g);
		
		Iterator<BipartiteNode> it = g.getVars().iterator();
		while(it.hasNext()){
			BipartiteNode vv = it.next();
			if(vv.getDiff()==null && vv.getOrig()==null){
				g.diffVariable(vv);
				it = g.getVars().iterator();
			}
		}
		
		assertEquals(Algorithms.RET_SUCCESS, Algorithms.SoaresSecchi(g, false));
		
		Utils.ExportTikz(g, System.out, true);
		
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

	public void testReissigPaths() throws Exception{
		BipartiteGraph g = new BipartiteGraph();
		InputStream file = new FileInputStream("dots/reissig.dot");
		Utils.ImportGraphViz(file, g);
		
		Iterator<BipartiteNode> it = g.getVars().iterator();
		while(it.hasNext()){
			BipartiteNode vv = it.next();
			if(vv.getDiff()==null && vv.getOrig()==null){
				g.diffVariable(vv);
				it = g.getVars().iterator();
			}
		}
		
		assertEquals(Algorithms.RET_SUCCESS, Algorithms.SoaresSecchi(g, false));
		
		Utils.ExportTikz(g, System.out, true);
		
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
	
	public void testPend2orderPaths() throws Exception{
		BipartiteGraph g = new BipartiteGraph();
		InputStream file = new FileInputStream("dots/pend2order.dot");
		Utils.ImportGraphViz(file, g);
		
		Iterator<BipartiteNode> it = g.getVars().iterator();
		while(it.hasNext()){
			BipartiteNode vv = it.next();
			if(vv.getDiff()==null && vv.getOrig()==null){
				g.diffVariable(vv);
				it = g.getVars().iterator();
			}
		}
		
		System.out.println("\n\nInitial graph for " + g.getName());
		Utils.ExportTikz(g, System.out, true);
		
		assertEquals(Algorithms.RET_SUCCESS, Algorithms.SoaresSecchi(g, false));
		
		Utils.ExportTikz(g, System.out, true);
		
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
	
	public void testchowdhryESoares(){
		String args[] = {"dots/chowdhryE.dot", "-s", "-v", "-t"};
		Tool.main(args);
	}

}
