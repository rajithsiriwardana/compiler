package compiler.parser;

import compiler.commons.Id;
import compiler.commons.Num;
import compiler.commons.Real;
import compiler.commons.Tag;
import compiler.commons.Token;

import java.io.IOException;

/**
 * @author rajith
 * @version ${Revision}
 */
public class ThreeAddressCodeGenerator {

    Node generateCodeForNode(AbstractNode left, AbstractNode right, String op) throws IOException {

        Node n = null, temp;

        for (AbstractNode abstractNode : AbstractNode.processedSymbols) {
            if (abstractNode instanceof Node) {
                n = (Node) abstractNode;
                if (n.op.equals(op) && left == n.left && right == n.right) {
                    return n;
                }

            }
        }

        if (!op.equals("=")) { // widen convention is done if current operation is not the assignment
            if (widen(left, left, right)) {
                AbstractNode.tempVal++;
                left = new Node("(float)", left, null); //using (float) as a unary operator for casting integer
                left.type = "float";
                generateCode(left); //after casting temporary operator will be generated
            }
            if (widen(right, left, right)) {
                AbstractNode.tempVal++;
                right = new Node("(float)", right, null); //using (float) as a unary operator for casting integer
                right.type = "float";
                generateCode(right); //after casting temporary operator will be generated
            }
        } else { // if assignment then only right side will be casting
            if (widen(right, left, right)) {
                AbstractNode.tempVal++;
                right = new Node("(float)", right, null); //using (float) as a unary operator for casting integer
                right.type = "float";
                generateCode(right); //after casting temporary operator will be generated
            }
        }
        AbstractNode.tempVal++; //give a new number to the temperory
        n = new Node(op, left, right);
        if (!n.op.equals("=")) {
            if (left.type == "float" || right.type == "float") {
                n.type = "float";  // in assignment we can't change type
            } else {
                n.type = "int";
            }
        }
        generateCode(n);
        AbstractNode.processedSymbols.add(n);
        return n;
    }

    Leaf insertAndGetLeaf(Token token) throws IOException {

        Leaf leaf;
        for (AbstractNode abstractNode : AbstractNode.processedSymbols) {
            if (abstractNode instanceof Leaf) {
                leaf = (Leaf) abstractNode;
                if (token == (leaf.token))
                    return leaf;
            }
        }
        leaf = new Leaf(token);
        leaf.type = token.type;
        AbstractNode.processedSymbols.add(leaf);
        return leaf;
    }

    /**
     * Generate the three address code for the given node
     *
     * @param inNode
     * @throws java.io.IOException
     */
    private void generateCode(AbstractNode inNode) throws IOException {
        Leaf l;
        Node node;
        Id id = null, assId = null;
        Num num;
        String leftSym, rightSym = null;
        node = (Node) inNode;
        if (node.right != null) {
            if (!node.op.equals("=")) {
                leftSym = "t" + node.left.value;
                rightSym = "t" + node.right.value;
                if (node.left instanceof Leaf) { //if the child is leaf print its lexeme
                    l = (Leaf) node.left;
                    if (l.token.tag == Tag.ID) {
                        id = (Id) l.token;
                        leftSym = id.lexeme;
                    } else {

                        if (l.token instanceof Num) {
                            num = (Num) l.token;
                            leftSym = num.lexeme;
                        }

                        if (l.token instanceof Real) {
                            Real real = (Real) l.token;
                            leftSym = real.lexeme;
                        }

                    }
                }

                if (node.right instanceof Leaf) {
                    l = (Leaf) node.right;
                    if (l.token.tag == Tag.ID) {
                        id = (Id) l.token;
                        rightSym = id.lexeme;
                    } else {


                        if (l.token instanceof Num) {
                            num = (Num) l.token;
                            rightSym = num.lexeme;
                        }

                        if (l.token instanceof Real) {
                            Real real = (Real) l.token;
                            rightSym = real.lexeme;
                        }

                    }
                }
                System.out.println("t" + node.value + "= " + leftSym + node.op + rightSym);
            } else {  //an assignment
                l = (Leaf) node.left;
                assId = (Id) l.token; // left of assignment is definitely id
                if (assId.type.equals("int") && node.right.type.equals("float")) {
                    throw new Error("Narrowing conversion is not allowed ");
                }
                if (!(node.right instanceof Leaf)) //if the right side is node
                {
                    rightSym = "t" + node.right.value;
                } else {

                    l = (Leaf) node.right;
                    if (l.token.tag == Tag.ID) {
                        id = (Id) l.token;
                        rightSym = id.lexeme;
                    } else {
                        if (l.token instanceof Num) {
                            num = (Num) l.token;
                            rightSym = num.lexeme;
                        }

                        if (l.token instanceof Real) {
                            Real real = (Real) l.token;
                            rightSym = real.lexeme;
                        }
                    }
                }
                System.out.println(assId.lexeme + "= " + rightSym);
            }
            System.out.println();
        } else {
            if (node.left instanceof Leaf) {
                l = (Leaf) node.left;
                System.out.println("t" + node.value + "= " + node.op + l.token.lexeme);
            } else {
                System.out.println("t" + node.value + "= " + node.op + "t" + node.left.value);
            }
            System.out.println();
        }
    }

    /**
     * Check for widening conventions
     *
     * @param n
     * @param left
     * @param right
     * @return
     * @throws java.io.IOException
     */
    private boolean widen(AbstractNode n, AbstractNode left, AbstractNode right) throws IOException {
        String maxtype = "int";
        if (right.type.equals("float") || left.type.equals("float")) {
            maxtype = "float";
        }
        if (maxtype.equals(n.type)) {
            return false;
        } else {
            return true;
        }
    }

}
