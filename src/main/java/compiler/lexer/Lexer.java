package compiler.lexer;

import compiler.commons.*;
import compiler.symbols.Type;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    private Hashtable<String, Token> words = new Hashtable<String, Token>();
    private Type currentType;

    private InputStream stream;

    void reserve(Id t) {
        words.put(t.lexeme, t);
    }

    public Lexer(InputStream inputStream) {
        stream = inputStream;
        reserve(Type.Int);
        reserve(Type.Float);
    }

    public Token scan() throws IOException {
        label:
        for (; ; readChar()) {
            switch (peek) {
                case ' ':
                case '\t':
                case '\r':
                    break;
                case '\n':
                    line = line + 1;
                    break;
                default:
                    break label;
            }
        }

        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                readChar();
            } while (Character.isDigit(peek));

            if (peek != '.') {
                return new Num(v);
            }

            float x = v;

            float d = 10;
            for (; ; ) {
                readChar();
                if (!Character.isDigit(peek)) {
                    break;
                }
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            return new Real(x);
        }

        if (Character.isLetter(peek)) {
            StringBuilder b = new StringBuilder();
            do {
                b.append(peek);
                readChar();
            } while (Character.isLetterOrDigit(peek));

            String s = b.toString();
            Id w = (Id)words.get(s);
            if (w != null) {
                if (w.lexeme.equals("int")) {
                    currentType = Type.Int;
                } else if (w.lexeme.equals("float")) {
                    currentType = Type.Float;
                }
                return w;
            }
            if (currentType == null) {
                throw new Error("Identifier " + s + " is not defined");
            }

            w = new Id(Tag.ID, s);
            w.type = currentType.lexeme;
            words.put(s, w);
            return w;
        }
        Token t = new Token(peek);
        peek = ' ';
        return t;
    }

    void readChar() throws IOException {
        peek = (char) stream.read();
    }

    public void setCurrentType(Type currentType) {
        this.currentType = currentType;
    }
}