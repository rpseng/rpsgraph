package rpsgraph;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;

import br.eng.rps.graph.Algorithms;
import br.eng.rps.graph.BipartiteGraph;
import br.eng.rps.graph.BipartiteNode;
import br.eng.rps.graph.utils.Utils;

public class Tool {

	// the actions
	static final int MAXMATCHING = 0;
	static final int PANTELIDES = 1;
	static final int SOARES = 2;
	static final int SOARES2 = 3;

	// the export formats
	static final int GRAPHVIZ = 0;
	static final int TIKZ = 1;
	static final int GRAPHVIZ2 = 2;

	static void print(BipartiteGraph G, int format, PrintStream out, boolean grayAlgebraic){
		switch(format){
		case TIKZ:
			Utils.ExportTikz(G, out, grayAlgebraic);
			break;
		case GRAPHVIZ:
		default:
			Utils.ExportGraphViz(G, out);
		break;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String outfile = null;
		int action = MAXMATCHING;
		int format = GRAPHVIZ;
		int maxsteps = 4;
		boolean verbose = false, grayAlgebraic = false, dm = false;
		double exectime = 0.0;

		int argc = args.length;

		if(argc < 1){
			System.err.println(
					"RPSGraph usage: java -jar rpsgraph.jar dotfile [-options...]\n\n" +
					"where options include:\n" +
					"-o outfile    to select an output file (default stdout)\n\n" +
					"-m            to execute a maximum matching (default)\n" +
					"-p            to execute the Pantelides algorithm\n" +
					"-s            to execute the Soares and Secchi algorithm\n" +
					"-u            to execute the Soares and Secchi algorithm (version 2)\n" +
					"-d            execute a DM decompostion on final graph\n\n" +
					"-z            set the output format to modified GraphViz\n" +
					"-t            set the output format to LaTeX Tikz\n" +
					"-x steps      change maximum number of steps (default 4)\n" +
					"-v            enable verbose output");
			return;
		}

		// loop for the arguments
		for(int i=1; i<argc; ++i){
			if(args[i].charAt(0) == '-'){
				switch(args[i].charAt(1)){
				case 'o':
					if(++i < argc) outfile = args[i];
					break;
				case 'x':
					if(++i < argc) maxsteps = Integer.parseInt(args[i]);
					break;
				case 'm':
					action = MAXMATCHING;
					break;
				case 'p':
					action = PANTELIDES;
					grayAlgebraic = false;
					break;
				case 's':
					action = SOARES;
					grayAlgebraic = true;
					break;
				case 'u':
					action = SOARES2;
					grayAlgebraic = true;
					break;
				case 'd':
					dm = true;
					break;
				case 'v':
					verbose = true;
					break;
				case 'z':
					format = GRAPHVIZ;
					break;
				case 't':
					format = TIKZ;
					break;
				}
			}
		}

		PrintStream out = System.out;
		BipartiteGraph G = null;
		try{
			if(outfile!=null)
				out = new PrintStream(new File(outfile));

			G = new BipartiteGraph("unnamed");
			InputStream in = new FileInputStream(args[0]);
			Utils.tic();
			Utils.ImportGraphViz(in, G);
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
		Utils.toc();
		System.out.println("// Time for parse the graph: " + Utils.toc());
		System.out.println("// Number of variables:" + G.nv());
		System.out.println("// Number of equations:" + G.ne());

		// add the differential version of all variables if missing
		if(action == SOARES || action == SOARES2){
			Iterator<BipartiteNode> it = G.getVars().iterator();
			while(it.hasNext()){
				BipartiteNode vv = it.next();
				if(vv.getDiff()==null && vv.getOrig()==null){
					G.diffVariable(vv);
					it = G.getVars().iterator();
				}
			}
		}

		if(verbose){
			out.println("// Input graph: " + G.getName());
			print(G, format, out, grayAlgebraic);
		}

		int ret = 0;
		switch(action){
		case MAXMATCHING:
			Utils.tic();
			boolean perfect = Algorithms.MaximumMatching(G);
			exectime = Utils.toc();
			if(perfect)
				out.println("// Perfect V_e matching found");
			break;
		case PANTELIDES:
			if(verbose || maxsteps!=0){
				int i=1;
				while(true){
					Utils.tic();
					ret = Algorithms.Pantelides(G, true);
					exectime += Utils.toc();
					if(ret != Algorithms.RET_STEP)
						break;
					if(verbose){
						out.println("// Pantelides step:");
						print(G, format, out, false);
					}
					if(maxsteps!=0 && i++ >= maxsteps){
						out.println("// Maximum number of steps (use -x option)");
						break;
					}
				}
			}
			else{
				Utils.tic();
				ret = Algorithms.Pantelides(G, false);
				exectime = Utils.toc();
			}				
			break;
		case SOARES:
			if(verbose || maxsteps!=0){
				int i=1;
				while(true){
					Utils.tic();
					ret = Algorithms.SoaresSecchi(G, true);
					exectime += Utils.toc();
					if(ret != Algorithms.RET_STEP)
						break;
					if(verbose){
						out.println("// DAEAnalysis step:");
						print(G, format, out, grayAlgebraic);
					}
					if(maxsteps!=0 && i++ >= maxsteps){
						out.println("// Maximum number of steps (use -x option)");
						return;
					}
				}
			}
			else{
				Utils.tic();
				ret = Algorithms.SoaresSecchi(G, false);
				exectime = Utils.toc();
			}
			if(ret != Algorithms.RET_SUCCESS)
				out.println("// DAEAnalysis found a singular system!");
			break;
		case SOARES2:
			if(verbose || maxsteps!=0){
				int i=1;
				while(true){
					Utils.tic();
					ret = Algorithms.SoaresSecchi2(G, true);
					exectime += Utils.toc();
					if(ret != Algorithms.RET_STEP)
						break;
					if(verbose){
						out.println("// DAEAnalysis step:");
						print(G, format, out, grayAlgebraic);
					}
					if(maxsteps!=0 && i++ >= maxsteps){
						out.println("// Maximum number of steps (use -x option)");
						return;
					}
				}
			}
			else{
				Utils.tic();
				ret = Algorithms.SoaresSecchi2(G, false);
				exectime = Utils.toc();
			}
			if(ret != Algorithms.RET_SUCCESS)
				out.println("// DAEAnalysis found a singular system!");
			break;
		}

		out.println("// Resulting graph:");
		out.println("// Number of variables:" + G.nv());
		out.println("// Number of equations:" + G.ne());
		out.println("// Execution time: " + exectime);
		out.println("// Return code: " + ret);
		print(G, format, out, grayAlgebraic);

		if(dm){
			int parts[] = { 0, 0};
			Utils.tic();
			Algorithms.DulmageMendelshon(G, parts);
			exectime = Utils.toc();
			out.println("// DM time: " + exectime);
			out.println("// Over-constrained partition: " + parts[0]);
			out.println("// Under-constrained partition: " + parts[1]);
		}
		if(outfile!=null)
			out.close();
	}
}
