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
public class PostFixException extends Exception {

    private static final long serialVersionUID = 1L;

    public PostFixException() {
        super();
    }
    
    public PostFixException(String msg) {
        super(msg);
    }
    
    public PostFixException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
