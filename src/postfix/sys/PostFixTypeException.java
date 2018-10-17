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
public class PostFixTypeException extends PostFixRunTimeException {
    PostFixValue val;

    public PostFixTypeException(PostFixValue val, PostFixValue.Type expectedType) {
        super(String.format("Wrong type argument: expected %s, received %s", expectedType, val.getType()));
        this.val = val;
    }

    public PostFixValue getVal() {
        return val;
    }
    
    
    
}
