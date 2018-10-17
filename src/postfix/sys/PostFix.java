package postfix.sys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Stack;
import postfix.syntax.PostFixException;
import postfix.syntax.Token;
import postfix.syntax.PostFixLexer;
import postfix.syntax.PostFixSyntaxException;
import postfix.syntax.TokenType;

public class PostFix {

    Stack<PostFixValue> stack;
    Integer a1, a2, a3;    // temporary operands (like registers here)
    Token [] a;

    public PostFix() {
        stack = new Stack<>();
    }

    public Stack<PostFixValue> evaluate(PostFixLexer lexer) throws PostFixException {
        Token t;
        // setup fresh token stream (queue) from lexer
        CmdQueue nextTokens = new CmdQueue(lexer);
        t = nextTokens.poll();
        while (!t.isA(TokenType.EOF)) {
            System.out.println(t);    // command being executed
            dispatch(t, nextTokens);  // do it
            // now display the current machine state
            System.out.println(String.format("\tstack: %s", stack));
            System.out.println(String.format("\tqueue: %s", nextTokens));
            t = nextTokens.poll();    // grab the next command
        }
        return stack;
    }

    protected void pop2Operands() throws PostFixTypeException {
        a2 = stack.pop().intValue();
        a1 = stack.pop().intValue();
    }

    protected void dispatch(Token t, CmdQueue cmds) throws PostFixRunTimeException {
        PostFixValue top;
        PostFixValue x;

        switch (t.getType()) {
            case INT:
                stack.push(new PostFixValue(new Integer(t.getLexeme())));
                break;
            case BOOL:
		// complete this
                Token seq = cmds.poll();
                seq.isA(TokenType.BOOL);
                break;
            case ADD:
                pop2Operands();
                stack.push(new PostFixValue(a1 + a2));
                break;
            case SUB:
                pop2Operands();
                stack.push(new PostFixValue(a1 - a2));
                break;
            case MUL:
		pop2Operands();                     // complete this
                stack.push(new PostFixValue(a1 * a2));
                break;
		// add cases for missing arithmetic operators
            case DIV:
                pop2Operands();
                stack.push(new PostFixValue(a2 / a1));
                break;
            case MOD:
                pop2Operands();
                stack.push(new PostFixValue(a2 % a1));
                break;
            case EQ:
                pop2Operands();
                stack.push(new PostFixValue(a1.equals(a2)));
                break;
		// add cases for missing logical operators
            case GT:
                pop2Operands();
                stack.push(new PostFixValue(a1 > a2));
                break;
            case LT:
                pop2Operands();
                stack.push(new PostFixValue(a1 < a2));
                break;
            case LE:
                pop2Operands();
                stack.push(new PostFixValue(a1 <= a2));
                break;
            case GE:
                pop2Operands();
                stack.push(new PostFixValue(a1 >= a2));
                break;
            case POP:
                stack.pop();
                break;
		// add cases for other stack operations
            case SWAP:
                PostFixValue a = stack.pop();
                PostFixValue b = stack.pop();
                stack.push(a);
                stack.push(b);
                break;
            case ROLL3:
                a = stack.pop();
                b = stack.pop();
                PostFixValue c = stack.pop();
                stack.push(a);
                stack.push(c);
                stack.push(b);
                break;
            case DUP:
                a = stack.pop();
                stack.push(a);
                stack.push(a);
                break;
            case IF:
                PostFixValue alt = stack.pop();
                PostFixValue cons = stack.pop();
                PostFixValue pred = stack.pop();
		// finish this
                // pred.isA(TokenType.BOOL);
                if (pred.boolValue() == true)
                {
                    stack.push(cons);
                }
                else
                {
                    stack.push(alt);
                }
                break;
            case LPAREN:
                Token[] tokenArr = fetchCmdSeq(cmds);  // complete this below
                stack.push(new PostFixValue(tokenArr));
                //x = stack.peek();
                break;
            case EXEC:
                // implement this
                Token[] topTokens = stack.pop().expValue();
                cmds.prepend(topTokens);
                break;
            case PRINT:
                top = stack.peek();
                System.out.println("Output: " + top);
                break;
            default:
                throw new PostFixRunTimeException(
			String.format("Unimplemented operation %s", t));
        }
    }

    /**
     * Retrieve tokens in the given command queue (token stream) up to the next
     * matching closing parenthesis (RPAREN type), storing them in a collection
     * and returning it as an array of tokens.
     * @param cmds The token stream
     * @return The command sequence found (including any nested command 
     * sequences) in an array of tokens.
     * @throws PostFixRunTimeException If no matching closing parenthesis is 
     * found before the input ends.
     */
    private Token[] fetchCmdSeq(CmdQueue cmds) throws PostFixRunTimeException {
	// Use a list to collect tokens while examining them for matching RPAREN
        ArrayList<Token> tokenList = new ArrayList<>();

	// We use "levels" to keep track of the matching parentheses
	// because we want to include nested expression sequences in this one
        int level = 1;  	// to account for LPAREN already seen

        Token seqToken = cmds.poll();
        while (!seqToken.isA(TokenType.EOF) && level > 0) {
	    // Implement the body of this loop
	    // Hint: Increment level for each LPAREN, decrement for RPAREN
            if(seqToken.isA(TokenType.LPAREN)){
                level++;
            }else if(seqToken.isA(TokenType.RPAREN)){
                level--;
            }
            if(level==0){
                break;
            }
            
            tokenList.add(seqToken);
            seqToken=cmds.poll();
        }
	
        if (level == 0) {  // matching RPAREN was found
            return tokenList.toArray(new Token[tokenList.size()]);
        } else {
            throw new PostFixRunTimeException(
                    String.format("Command sequence missing closing parenthesis: %s", 
                            tokenList));
        }
    }

    public void reset() {
        stack = new Stack<>();
    }

    /**
     * Read PostFix program(s) from stdin and from provided filenames in right
     * to left order, evaluate each and compose the results to finally print the
     * result. This way, functions can be defined in files and invoked on
     * arguments provided at runtime.
     *
     * @param args The list of file names to read from
     */
    public static void main(String args[]) {
        Stack<PostFixValue> result = null;
        boolean debug = false;

        Reader inputReader;
        PostFixLexer plex;

        // Treat multiple arguments as files to be read.  
        // Treat "-" as stdin standard input.  This will allow user to provide
        // arguments to programs stored in files, and to compose functions
        // saved in files.
        PostFix eval = new PostFix();

        // now evaluate commands in files in right to left order (so that
        // it feels like function composition, as results from right passed
        // to operations on the left)
        for (int i = args.length - 1; i >= 0; i--) {
            String fname = args[i];
            try {
                if (fname.equals("-")) {
                    inputReader = new InputStreamReader(System.in);
                } else {
                    inputReader = new FileReader(new File(fname));
                }
                // setup lexer
                plex = new PostFixLexer(inputReader);
                try {
                    // evaluate the token stream
                    result = eval.evaluate(plex);
                } catch (PostFixSyntaxException pfse) {
                    System.out.println(String.format("Syntax error in %s at line/char: %d / %d",
                            fname, plex.getLine(), plex.getChar()));
                    System.out.println("Last token read was: " + pfse.getLastToken());
                } catch (PostFixRunTimeException pfte) {
                    System.out.println(String.format("Runtime Error in %s at line/char: %d / %d",
                            fname, plex.getLine(), plex.getChar()));
                    System.out.println(pfte.getMessage());
                } catch (PostFixException pfe) {
                    System.out.println(String.format("Error in %s at line/char: %d / %d",
                            fname, plex.getLine(), plex.getChar()));
                    System.out.println(pfe.getMessage());
                }
            } catch (FileNotFoundException ex) {
                System.err.println(String.format("Warning: Ignoring missing file: %s", fname));
            }
        }
        System.out.println(String.format("Result: %s", result));
    }
}
