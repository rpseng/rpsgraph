//;-*- mode: antlr -*-

header {
	package br.eng.rps.graph.utils;
	
	import br.eng.rps.graph.*;
}

options
{
}

class DotParser extends Parser;
options
{
    buildAST=false;
    exportVocab=Dot;
    defaultErrorHandler = true;
    classHeaderPrefix = ""; // make the parser classes not public
}
tokens {
GRAPH	= "graph" ;
NODE	= "node" ;
EDGE	= "edge" ;
}

graph [BipartiteGraph G]
    :  (STRICT_LITERAL)?
       GRAPH (name:ID)? {G.setName(name.getText());}
       O_BRACE! (stmt[G])* C_BRACE!
    ;

protected stmt [BipartiteGraph G]
    :
    	edge_stmt
    	| node_stmt
    	| conn_stmt[G]
    ;

protected conn_stmt [BipartiteGraph G] {String v1, v2;}
    :	(f:ID {v1=f.getText();} | fs:STRING {v1=fs.getText();})
    	CONN (v:ID {v2=v.getText();} | vs:STRING {v2=vs.getText();})
    		{G.addEdge(v1, v2); }
    ;

protected edge_stmt
    :  EDGE (attr_list)?
    ;

protected node_stmt
    :  NODE (attr_list)?
    ;

protected attr_list
    :  O_BRACKET! (a_list)? C_BRACKET! (attr_list)?
    ;

protected a_list
    :  (arg) (COMMA!)? (a_list)?
    ;

protected arg
    :  ID^ (EQUAL! (NUMBER | ID | STRING) )?
    ;

class DotLexer extends Lexer;
options
{
    k=1;
    charVocabulary = '\3'..'\377';
    exportVocab=Dot;
    // classHeaderPrefix = "@SuppressWarnings(\"unchecked\")"; // make the parser classes not public
}

O_BRACE: '{';
C_BRACE: '}';
O_BRACKET: '[';
C_BRACKET: ']';
SEMI	: ';' ;
EQUAL	: '=' ;
COMMA	: ',' ;
COLON	: ':' ;
CONN    : "--";
ID	options {testLiterals=true; paraphrase = "identifier";}
		: ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'\'')+ ;
STRING : '"'! (~ '"') * '"'! ;

WS
    :
       (   ' '
        |  '\t'
        |  '\r' '\n' { newline(); }
        |  '\n'      { newline(); }
       ) { _ttype = Token.SKIP; } //ignore this token
    ;

// Single-line comments
COMMENT
    :  (  ("/*") => ML_COMMENT
        | ("//") => SL_COMMENT)
       {_ttype = Token.SKIP; newline();}
    ;

// Taken from ANTLR's Java grammar.
// Single-line comments
protected SL_COMMENT
    :  "//"
       (~('\n'|'\r'))* ('\n'|'\r'('\n')?)
       {_ttype = Token.SKIP; newline();}
    ;

// multiple-line comments
protected ML_COMMENT
    :  "/*"
       (   /*
              '\r' '\n' can be matched in one alternative or by matching
              '\r' in one iteration and '\n' in another.  I am trying to
              handle any flavor of newline that comes in, but the language
              that allows both "\r\n" and "\r" and "\n" to all be valid
              newline is ambiguous.  Consequently, the resulting grammar
              must be ambiguous.  I'm shutting this warning off.
            */
            options
            {
                generateAmbigWarnings=false;
            }
	:
               {LA(2)!='/'}? '*'
             | '\r' '\n' {newline();}
             | '\r' {newline();}
             | '\n' {newline();}
             | ~('*'|'\n'|'\r')
       )*
       "*/"
       {_ttype = Token.SKIP;}
    ;
    