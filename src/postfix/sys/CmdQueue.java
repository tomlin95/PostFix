/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postfix.sys;

import java.io.IOException;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import postfix.syntax.PostFixLexer;
import postfix.syntax.Token;

/**
 * A CmdQueue is a  FIFO queue interface wrapping a lexer to create the illusion
 * of a literal stream of tokens.  In order to supoort PostFix execution, a
 * CmdQueue instance allows tokens to be enqueued as well as dequeued.  When 
 * tokens are enqueued, they will be dequeue before any more tokens are fetched
 * from the lexer.  A token is requested of the lexer only when the buffer 
 * backing the queue is empty.
 * @author newts
 */
public class CmdQueue extends AbstractQueue<Token> {
    
    ArrayList<Token> elements;
    PostFixLexer srcLexer;
    
    public CmdQueue(PostFixLexer lexer) {
        srcLexer = lexer;
        elements = new ArrayList<>();
        fetchOneToken();
    }

    private boolean fetchOneToken() {
        // prime the elements buffer with the first token from lexer
        try {
            elements.add(srcLexer.yylex());
            return true;
        } catch (IOException ioe) {
            // ignore.  The queue is truly empty
            return false;
        }
    }
    
    @Override
    public Iterator<Token> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean offer(Token e) {
        return elements.add(e);
    }

    /**
     * Insert the given tokens in front of the ones already in this queue.
     * @param tokens The sequence of tokens to be inserted into this queue.
     */
    public void prepend(Token[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            Token el = tokens[i];
            elements.add(i, el);
        }
    }

    /**
     * Insert the given tokens after the ones already in this queue.
     * @param tokens The sequence of tokens to be inserted into this queue.
     */
    public void append(Token[] tokens) {
        elements.addAll(Arrays.asList(tokens));
    }
   
    @Override
    public Token poll() {
        if (elements.isEmpty()) {
            fetchOneToken();
        }
        Token result = elements.remove(0);
        return result;
    }

    @Override
    public Token peek() {
        if (elements.isEmpty()) {
            fetchOneToken();
        }
        return elements.get(0);
    }
    
}
