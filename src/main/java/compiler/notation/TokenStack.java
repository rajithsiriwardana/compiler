package compiler.notation;

import compiler.commons.Token;

import java.util.Stack;

/**
 * @author rajith
 * @version ${Revision}
 */
public class TokenStack {
    public Stack<Token> stack;

    public TokenStack() {
        stack = new Stack<Token>();
    }

    public void push(Token token) {
        stack.push(token);
    }

    public Token pop() {
        return stack.pop();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
