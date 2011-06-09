package br.eng.rps.graph.test;

import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.utils.Utils;


public class UtilsTest extends TestCase {
	
	public void testImportStatic() throws Exception{
		String file = "dots/ammonia.dot";
		BipartiteGraph G = new BipartiteGraph();
		
		InputStream in = new FileInputStream(file);
		Utils.ImportGraphViz(in, G);
		
		assertTrue(G.nv() > 0);
		assertTrue(G.ne() > 0);
	}
	
	public void testImportDynamic() throws Exception{
		String file = "dots/batch_column.dot";
		BipartiteGraph G = new BipartiteGraph();
		
		InputStream in = new FileInputStream(file);
		Utils.ImportGraphViz(in, G);
		
		assertTrue(G.nv() > 0);
		assertTrue(G.ne() > 0);
	}
	
	public void testImportBig() throws Exception{
		String file = "dots/columnSteady11.dot";
		BipartiteGraph G = new BipartiteGraph();
		
		InputStream in = new FileInputStream(file);
		Utils.ImportGraphViz(in, G);
		
		assertTrue(G.nv() > 0);
		assertTrue(G.ne() > 0);
	}
}
