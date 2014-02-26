package compiler.commons;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Id extends Token {
    public final String lexeme;
    public Object value;

    //String type;
    public Id(int t, String s) {
        super(t);
        lexeme = new String(s);
        super.lexeme = lexeme;
        value = 0;
    }
}