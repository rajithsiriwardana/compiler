package compiler.parser;

import java.util.ArrayList;

/**
 * @author rajith
 * @version ${Revision}
 */
public class AbstractNode {
    public static ArrayList<AbstractNode> processedSymbols = new ArrayList<AbstractNode>();
    public int value = 0;
    public static int tempVal = 0;
    public String type;
}
