package compiler.notation;

import compiler.commons.Token;

import java.util.Stack;

/**
 * @author rajith
 * @version ${Revision}
 */
public class PostfixTokenStack {
    public Stack<Token> stack;

    public PostfixTokenStack() {
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
