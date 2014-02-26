package compiler.commons;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Real extends Token {
    public final float value;
    public final String lexeme;

    public Real(float v) {
        super(Tag.FLOAT);
        value = v;
        lexeme = Float.toString(value);
        type = "float";
    }

    public String tostring() {
        return "" + value;
    }
}