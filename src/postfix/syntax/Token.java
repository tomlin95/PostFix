package postfix.syntax;

public class Token {

    String lexeme;
    TokenType type;

    public Token(TokenType typ) {
	this.type = typ;
    }
    
    public Token(TokenType typ, String lexeme) {
        this.type = typ;
        this.lexeme = lexeme;
    }
    
    /**
     *
     * @param typ The type of token to be tested for.
     * @return <code>true</code> if this token is of the same type as the 
     * argument
     */
    public boolean isA(TokenType typ) {
        return type == typ;
    }

    public String getLexeme() {
        return lexeme;
    }

    public TokenType getType() {
        return type;
    }    
    
    @Override
    public String toString() {
	if (lexeme != null) {
            return lexeme;
        } else {
            return type.getSymbol();
        }
    }
}