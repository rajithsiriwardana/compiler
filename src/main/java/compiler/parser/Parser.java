package compiler.parser;

import compiler.commons.*;
import compiler.lexer.Lexer;
import compiler.notation.StackMachine;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Parser {
    public StringBuffer postFix = new StringBuffer();
    private Lexer lexer;
    private StackMachine stackMachine;

    private Token look;
    private Id currentAssigneeSymbol;
    private Id skipId;
    private int skipFlag = -1;
    private ThreeAddressCodeGenerator threeAddressCodeGenerator;

    public Parser(Lexer lex, StackMachine stackMachine, BufferedWriter writer)
            throws IOException {
        lexer = lex;
        this.stackMachine = stackMachine;
        threeAddressCodeGenerator = new ThreeAddressCodeGenerator(writer);
        move();
    }

    void move() throws IOException {
        look = lexer.scan();
    }

    void error(String s) {
        throw new Error("near line " + Lexer.line + ":" + s);
    }

    void match(int t) throws IOException {
        if (look.tag == t && skipFlag == -1) {
            move();
        } else if (skipFlag == 1) {
            skipFlag = -1;
        } else {
            error("syntax error");
        }
    }


    public void P() throws IOException {
        D();
        L();
    }

    public void D() throws IOException {
        B();
        N();
        lexer.setCurrentType(null);
        match(';');
        D1();
    }

    public Type B() throws IOException {
        Type b = (Type) look;
        match(Tag.BASIC);
        return b;
    }

    public void D1() throws IOException {
        if (look.tag == Tag.BASIC) {
            D();
        } else {

        }
    }

    public void N() throws IOException {
        match(Tag.ID);
        N1();
    }

    public void N1() throws IOException {
        if (look.tag == ',') {
            match(',');
            match(Tag.ID);
            N1();
        } else {

        }

    }

    public void L() throws IOException {
        S();
        stackMachine.evaluate(null);
        if (currentAssigneeSymbol != null) {
            currentAssigneeSymbol.value = stackMachine.value;

            postFix.append("\n");
            if (currentAssigneeSymbol.type.equals("int") && stackMachine.value instanceof Float) {

                postFix.append("Warning : Stack Machine-Time mismatch (Narrowing convention) between assignee id type =")
                        .append(currentAssigneeSymbol.type).append(" calculated value type=").append(Type.Float.tostring())
                        .append("\n");

            } else if (currentAssigneeSymbol.type.equals("float") && stackMachine.value instanceof Integer) {
                postFix.append("Warning : Stack Machine-Time mismatch (Widening convention) between assignee id type =")
                        .append(currentAssigneeSymbol.type).append(" calculated value type=").append(Type.Int.tostring())
                        .append("\n");

            }

            postFix.append(currentAssigneeSymbol.lexeme).append("=").append(currentAssigneeSymbol.value).append("\n");
        } else {

            postFix.append("\n");
            postFix.append(stackMachine.value);
        }
        currentAssigneeSymbol = null;

        AbstractNode.processedSymbols = new ArrayList<AbstractNode>(); // new set of nodes for new stmt
        AbstractNode.tempVal = 0;
        System.out.println("Postfix notation and value of the statement");
        System.out.println(postFix);
        System.out.println();
        postFix = new StringBuffer();
        match(';');
        L1();
    }

    public void L1() throws IOException {
        if (look.tag == Tag.ID || look.tag == Tag.NUM || look.tag == Tag.FLOAT || look.tag == '(') {
            L();
        } else {

        }
    }

    public void S() throws IOException {
        AbstractNode exprn;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            E();
        } else if (look.tag == Tag.ID) {
            currentAssigneeSymbol = (Id) look;
            match(Tag.ID);
            if (look.tag == '=') {
                match('=');
                exprn = E();
                threeAddressCodeGenerator
                        .generateCodeForNode(threeAddressCodeGenerator.
                                insertAndGetLeaf(currentAssigneeSymbol), exprn, "=");
                                ////generate code for assignment
            } else {
                skipId = currentAssigneeSymbol;
                currentAssigneeSymbol = null;
                skipFlag = 1;
                E();
            }
        } else {
            throw new Error("Syntax Error");
        }
    }

    public AbstractNode E() throws IOException {
        AbstractNode node;
        AbstractNode termnode;
        termnode = T();
        node = E1(termnode);
        return node;
    }


    public AbstractNode E1(AbstractNode termnodeinh) throws IOException {
        AbstractNode node; // node which rerpesents the operation so far
        AbstractNode snode;  // synthesised attribute which gives the full answer
        AbstractNode pretn = termnodeinh; //previous term nonterminal is inherited
        AbstractNode curtn;
        if (look.tag == '+') {
            match('+');
            curtn = T();
            postFix.append("+");
            stackMachine.evaluate("+");
            node = threeAddressCodeGenerator.generateCodeForNode(pretn, curtn, "+");
            //generate code for node
            snode = E1(node);
        } else {
            snode = termnodeinh;
        }
        return snode;
    }

    public AbstractNode T() throws IOException {
        AbstractNode node;
        AbstractNode factnode;
        factnode = F();
        node = T1(factnode);
        return node;
    }

    public AbstractNode T1(AbstractNode factnodeinh) throws IOException {
        AbstractNode node; // node which rerpesents the operation so far
        AbstractNode snode;  // synthesised attribute which gives the full answer
        AbstractNode prefn = factnodeinh; //previous factor nonterminal is inherited
        AbstractNode curfn;
        if (look.tag == '*') {
            match('*');
            curfn = F();
            //System.out.print("*");
            postFix.append("*");
            stackMachine.evaluate("*");
            node = threeAddressCodeGenerator.generateCodeForNode(prefn, curfn, "*");
            //generate code for node

            snode = T1(node);
        } else {
            snode = factnodeinh;
        }
        return snode;
    }

    public AbstractNode F() throws IOException {
        AbstractNode abstractNode;
        if (look.tag == '(') {
            match('(');
            abstractNode = E();
            match(')');
        } else if (look.tag == Tag.ID) {
            Id word = (Id) look;
            String workLex = word.lexeme;
            stackMachine.postfixTokenStack.push(word);
            match(Tag.ID);
            abstractNode = threeAddressCodeGenerator.insertAndGetLeaf(word);
            //insert leaf to  processed symbols if not exists
            postFix.append(workLex);

        } else if (look.tag == Tag.NUM) {
            Num num = (Num) look;
            String IntNum = num.tostring();
            match(Tag.NUM);
            stackMachine.postfixTokenStack.push(num);
            abstractNode = threeAddressCodeGenerator.insertAndGetLeaf(num);
            //insert leaf to  processed symbols if not exists
            postFix.append(IntNum);

        } else if (look.tag == Tag.FLOAT) {
            Real real = (Real) look;
            String floatNum = real.tostring();
            match(Tag.FLOAT);
            stackMachine.postfixTokenStack.push(real);
            abstractNode = threeAddressCodeGenerator.insertAndGetLeaf(real);
            //insert leaf to  processed symbols if not exists
            postFix.append(floatNum);

        } else if (skipId != null && skipFlag == 1) {
            Id word = skipId;
            String workLex = word.lexeme;
            stackMachine.postfixTokenStack.push(word);
            match(Tag.ID);
            abstractNode = threeAddressCodeGenerator.insertAndGetLeaf(word);
            //insert leaf to  processed symbols if not exists
            postFix.append(workLex);
            skipId = null;

        } else {
            throw new Error("Syntax Error");
        }
        return abstractNode;
    }
}
