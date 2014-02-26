package compiler.commons;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Token {
    public final int tag;
    public String type;
    public String lexeme;

    public Token(int t) {
        tag = t;
    }

    public String tostring() {
        return "" + (char) tag;
    }
}
