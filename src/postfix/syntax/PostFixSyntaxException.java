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
public class PostFixSyntaxException extends PostFixException {

    private static final long serialVersionUID = 1L;

    private final Token lastToken;
    
    public PostFixSyntaxException(Token last) {
        this(last, "PostFix Error");
    }

    public PostFixSyntaxException(Token last, String message) {
        super(message);
        lastToken = last;
    }
    
    public Token getLastToken() {
        return lastToken;
    }
    
}
