// $ANTLR : "Dot.g" -> "DotParser.java"$

	package br.eng.rps.graph.utils;
	
	import br.eng.rps.graph.*;

public interface DotTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int GRAPH = 4;
	int NODE = 5;
	int EDGE = 6;
	int STRICT_LITERAL = 7;
	int ID = 8;
	int O_BRACE = 9;
	int C_BRACE = 10;
	int STRING = 11;
	int CONN = 12;
	int O_BRACKET = 13;
	int C_BRACKET = 14;
	int COMMA = 15;
	int EQUAL = 16;
	int NUMBER = 17;
	int SEMI = 18;
	int COLON = 19;
	int WS = 20;
	int COMMENT = 21;
	int SL_COMMENT = 22;
	int ML_COMMENT = 23;
}
