package compiler;

import compiler.lexer.Lexer;
import compiler.parser.Parser;
import compiler.notation.StackMachine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Controller {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("./src/main/resources/inputs/input.txt");
        Lexer lexer = new Lexer(inputStream);
        StackMachine stackMachine = new StackMachine();
        Parser parser = new Parser(lexer, stackMachine);
        parser.P();
    }
}
