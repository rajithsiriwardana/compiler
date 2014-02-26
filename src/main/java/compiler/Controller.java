package compiler;

import compiler.lexer.Lexer;
import compiler.parser.Parser;
import compiler.notation.StackMachine;

import java.io.IOException;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Controller {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        StackMachine stackMachine = new StackMachine();
        Parser parser = new Parser(lexer, stackMachine);
        parser.P();
    }
}
