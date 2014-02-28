package compiler;

import compiler.lexer.Lexer;
import compiler.parser.Parser;
import compiler.notation.StackMachine;

import java.io.*;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Controller {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("./src/main/resources/inputs/input.txt");
        BufferedWriter outWrite=new BufferedWriter(new FileWriter("./src/main/resources/code/3AC.txt"));
        Lexer lexer = new Lexer(inputStream);
        StackMachine stackMachine = new StackMachine();
        Parser parser = new Parser(lexer, stackMachine, outWrite);
        parser.P();
        inputStream.close();
        outWrite.close();
    }
}
