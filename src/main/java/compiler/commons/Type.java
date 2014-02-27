package compiler.commons;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Type extends Id {

    public int width = 0;
// width is used for storage allocation

    public Type(String s, int tag, int w) {
        super(tag, s);
        width = w;
    }

    public static final Type Int = new Type("int", Tag.BASIC, 4), Float = new Type("float", Tag.BASIC, 8),
            Char = new Type("char", Tag.BASIC, 1);


    public static boolean numeric(Type p) {

        if (p == Type.Char || p == Type.Int || p == Type.Float) {
            return true;
        } else {
            return false;
        }
    }

    public static Type max(Type p1, Type p2) {
        if (p1 == Type.Float || p2 == Type.Float) {
            return Type.Float;
        } else {
            return Type.Int;
        }
    }

}