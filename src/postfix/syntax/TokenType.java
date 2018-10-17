/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postfix.syntax;

/**
 *
 * @author newts
 */
public enum TokenType {
    ADD("add"), 
    SUB("sub"), 
    MUL("mul"),
    DIV("div"),
    MOD("mod"),
    EQ("eq"),
    GT("gt"),
    LT("lt"),
    GE("ge"),
    LE("le"),
    
    IF("if"),
    POP("pop"),
    SWAP("swap"),
    ROLL3("roll3"),
    DUP("dup"),
    LPAREN("("),
    RPAREN(")"),
    EXEC("exec"),
    PRINT("print"),
    
    INT("int", true), 
    BOOL("bool", true),
    EOF("eof"),
    ;
    
    String symbol;
    boolean hasLexeme;
    
    private TokenType(String name) {
	this.symbol = name;
        hasLexeme = false;
    }
    
    private TokenType(String name, boolean hasLexeme) {
        this.symbol = name;
        this.hasLexeme = hasLexeme;
    }

    public String getSymbol() {
        return symbol;
    }
 
}
