/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postfix.sys;

import postfix.syntax.PostFixException;

/**
 *
 * @author newts
 */
public class PostFixRunTimeException extends PostFixException {

    public PostFixRunTimeException(String msg) {
        super(msg);
    }
    
    public PostFixRunTimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
