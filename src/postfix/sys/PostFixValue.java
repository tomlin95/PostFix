/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postfix.sys;

import postfix.syntax.Token;

/**
 * A wrapper class for any type of value that a PostFix expression may produce.
 * All values on the stack during a PostFix evaluation must be instances of 
 * this class.
 * @author newts
 */
public class PostFixValue {

    public enum Type {
        INTEGER, BOOLEAN, EXPRESSION;
    }
    
    private Boolean booleanVal;
    private Integer intVal;
    private Token[] expVal;
    private final Type type;

    /**
     * Create a value that wraps an integer.
     * @param intVal The integer value to be wrapped.
     */
    public PostFixValue(Integer intVal) {
        this.intVal = intVal;
        type = Type.INTEGER;
    }
    
    /**
     * Create a value that wraps a boolean value;
     * @param boolVal The boolean value to be wrapped
     */
    public PostFixValue(Boolean boolVal) {
        booleanVal = boolVal;
        type = Type.BOOLEAN;
    }

    /**
     * Create a value that wraps an expression (sequence of commands)
     * @param exp The sequence of commands to be wrapped as a single value
     */
    public PostFixValue(Token[] exp) {
        this.expVal = exp;
        type = Type.EXPRESSION;
    }    
    
    /**
     *
     * @return This value as an integer.
     * @throws PostFixTypeException if this value does not represent an integer
     */
    public Integer intValue() throws PostFixTypeException {
        if (intVal == null) {
            throw new PostFixTypeException(this, Type.INTEGER);
        } else {
            return intVal;
        }
    }
    
    /**
     *
     * @return This value as an boolean.
     * @throws PostFixTypeException if this value does not represent a boolean
     */
    public Boolean boolValue() throws PostFixTypeException {
        if (booleanVal == null) {
            throw new PostFixTypeException(this, Type.BOOLEAN);
        } else {
            return booleanVal;
        }
    }

    /**
     *
     * @return this value as an expression (sequence of commands).
     * @throws PostFixTypeException if this value is not a sequence of commands
     */
    public Token[] expValue() throws PostFixTypeException {
        if (expVal == null) {
            throw new PostFixTypeException(this, Type.EXPRESSION);
        } else {
            return expVal;
        }
    }
    
    /**
     *
     * @return The type of this value
     */
    public Type getType() {
        return type;
    }
    
    @Override
    public String toString() {
        switch (type) {
            case INTEGER:
                return String.valueOf(intVal);
            case BOOLEAN:
                return String.valueOf(booleanVal);
            default:
                String result = "(";
                for (Token cmd : expVal) {
                    result = result + " " + cmd;
                }
                result = result + " )";
                return result;
        }
    }
}
