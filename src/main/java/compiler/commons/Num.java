package compiler.commons;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Num extends Token {
    public final int value;
    public final String lexeme;

    public Num(int v) {
        super(Tag.NUM);
        value = v;
        lexeme = Integer.toString(value);
        type = "int";
    }

    public String tostring() {
        return "" + value;
    }
}
