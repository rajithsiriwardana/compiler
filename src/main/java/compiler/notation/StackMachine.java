package compiler.notation;

import compiler.commons.Id;
import compiler.commons.Num;
import compiler.commons.Real;
import compiler.commons.Token;

import java.util.Stack;

/**
 * @author rajith
 * @version ${Revision}
 */
public class StackMachine {
    public Stack<Token> postfixTokenStack;
    public Object value;

    public StackMachine() {
        postfixTokenStack = new Stack<Token>();
    }

    public void evaluate(String operator) {
        Token top = postfixTokenStack.pop();
        Token nextToTop = null;
        if (!postfixTokenStack.isEmpty()) {
            nextToTop = postfixTokenStack.pop();
        }
        Object value1 = null;
        Object value2 = null;
        Object result = null;
        if (top != null && nextToTop != null) {
            if (top instanceof Id) {
                Id word = (Id) top;
                value1 = word.value;
            } else if (top instanceof Num) {
                Num num = (Num) top;
                value1 = num.value;
            } else if (top instanceof Real) {
                Real real = (Real) top;
                value1 = real.value;
            }

            if (nextToTop != null) {
                if (nextToTop instanceof Id) {
                    Id word = (Id) nextToTop;
                    value2 = word.value;
                } else if (nextToTop instanceof Num) {
                    Num num = (Num) nextToTop;
                    value2 = num.value;
                } else if (nextToTop instanceof Real) {
                    Real real = (Real) nextToTop;
                    value2 = real.value;
                }
            }

            if (operator != null) {
                if (operator.equals("*")) {
                    if (value1 instanceof Integer && value2 instanceof Integer) {
                        result = (Integer) value1 * (Integer) value2;
                    } else if (value1 instanceof Integer && value2 instanceof Float) {
                        result = (Integer) value1 * (Float) value2;
                    } else if (value1 instanceof Float && value2 instanceof Integer) {
                        result = (Float) value1 * (Integer) value2;
                    } else if (value1 instanceof Float && value2 instanceof Float) {
                        result = (Float) value1 * (Float) value2;
                    }
                } else if (operator.equals("+")) {
                    if (value1 instanceof Integer && value2 instanceof Integer) {
                        result = (Integer) value1 + (Integer) value2;
                    } else if (value1 instanceof Integer && value2 instanceof Float) {
                        result = (Integer) value1 + (Float) value2;
                    } else if (value1 instanceof Float && value2 instanceof Integer) {
                        result = (Float) value1 + (Integer) value2;
                    } else if (value1 instanceof Float && value2 instanceof Float) {
                        result = (Float) value1 + (Float) value2;
                    }
                }

            }

            if (result instanceof Integer) {
                Num num = new Num((Integer) result);
                postfixTokenStack.push(num);
            } else {
                Real real = new Real((Float) result);
                value = result;
                postfixTokenStack.push(real);
            }
        } else {
            if (top instanceof Id) {
                Id word = (Id) top;
                value = word.value;
            } else if (top instanceof Num) {
                Num num = (Num) top;
                value = num.value;
            } else if (top instanceof Real) {
                Real real = (Real) top;
                value = real.value;
            }
        }
    }
}
