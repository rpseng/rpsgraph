// $ANTLR : "Dot.g" -> "DotParser.java"$

	package br.eng.rps.graph.utils;
	
	import br.eng.rps.graph.*;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

 class DotParser extends antlr.LLkParser       implements DotTokenTypes
 {

protected DotParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public DotParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected DotParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public DotParser(TokenStream lexer) {
  this(lexer,1);
}

public DotParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final void graph(
		BipartiteGraph G
	) throws RecognitionException, TokenStreamException {
		
		Token  name = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case STRICT_LITERAL:
			{
				match(STRICT_LITERAL);
				break;
			}
			case GRAPH:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(GRAPH);
			{
			switch ( LA(1)) {
			case ID:
			{
				name = LT(1);
				match(ID);
				break;
			}
			case O_BRACE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			G.setName(name.getText());
			match(O_BRACE);
			{
			_loop5:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					stmt(G);
				}
				else {
					break _loop5;
				}
				
			} while (true);
			}
			match(C_BRACE);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
	}
	
	protected final void stmt(
		BipartiteGraph G
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case EDGE:
			{
				edge_stmt();
				break;
			}
			case NODE:
			{
				node_stmt();
				break;
			}
			case ID:
			case STRING:
			{
				conn_stmt(G);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	protected final void edge_stmt() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(EDGE);
			{
			switch ( LA(1)) {
			case O_BRACKET:
			{
				attr_list();
				break;
			}
			case NODE:
			case EDGE:
			case ID:
			case C_BRACE:
			case STRING:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	protected final void node_stmt() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(NODE);
			{
			switch ( LA(1)) {
			case O_BRACKET:
			{
				attr_list();
				break;
			}
			case NODE:
			case EDGE:
			case ID:
			case C_BRACE:
			case STRING:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	protected final void conn_stmt(
		BipartiteGraph G
	) throws RecognitionException, TokenStreamException {
		
		Token  f = null;
		Token  fs = null;
		Token  v = null;
		Token  vs = null;
		String v1, v2;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ID:
			{
				f = LT(1);
				match(ID);
				v1=f.getText();
				break;
			}
			case STRING:
			{
				fs = LT(1);
				match(STRING);
				v1=fs.getText();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(CONN);
			{
			switch ( LA(1)) {
			case ID:
			{
				v = LT(1);
				match(ID);
				v2=v.getText();
				break;
			}
			case STRING:
			{
				vs = LT(1);
				match(STRING);
				v2=vs.getText();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			G.addEdge(v1, v2);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	protected final void attr_list() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(O_BRACKET);
			{
			switch ( LA(1)) {
			case ID:
			{
				a_list();
				break;
			}
			case C_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(C_BRACKET);
			{
			switch ( LA(1)) {
			case O_BRACKET:
			{
				attr_list();
				break;
			}
			case NODE:
			case EDGE:
			case ID:
			case C_BRACE:
			case STRING:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	protected final void a_list() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			arg();
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case ID:
			case C_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ID:
			{
				a_list();
				break;
			}
			case C_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
	}
	
	protected final void arg() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(ID);
			{
			switch ( LA(1)) {
			case EQUAL:
			{
				match(EQUAL);
				{
				switch ( LA(1)) {
				case NUMBER:
				{
					match(NUMBER);
					break;
				}
				case ID:
				{
					match(ID);
					break;
				}
				case STRING:
				{
					match(STRING);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case ID:
			case C_BRACKET:
			case COMMA:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"graph\"",
		"\"node\"",
		"\"edge\"",
		"STRICT_LITERAL",
		"identifier",
		"O_BRACE",
		"C_BRACE",
		"STRING",
		"CONN",
		"O_BRACKET",
		"C_BRACKET",
		"COMMA",
		"EQUAL",
		"NUMBER",
		"SEMI",
		"COLON",
		"WS",
		"COMMENT",
		"SL_COMMENT",
		"ML_COMMENT"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2400L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 3424L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 16384L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 49408L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	
	}
