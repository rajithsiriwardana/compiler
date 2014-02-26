package compiler.parser;

import compiler.commons.Token;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Leaf extends AbstractNode {

    public Token token;

    public Leaf(Token token) {

        super.value = AbstractNode.processedSymbols.size() + 1;
        this.token = token;
    }

}
