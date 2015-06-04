package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 */
public class XORGrammarCompiler {
    /**
     * @param expression
     * @return
     */
    public CommonTree compile(String expression) {
        try {
            //lexer splits input into tokens
            ANTLRStringStream input = new ANTLRStringStream(expression);
            TokenStream tokens = new CommonTokenStream(new XORGrammarLexer(input));

            //parser generates abstract syntax tree
            XORGrammarParser parser = new XORGrammarParser(tokens);
            XORGrammarParser.expr_return ret = parser.expr();

            //acquire parse result
            //printTree(ast);
            return (CommonTree) ret.tree;
        } catch (RecognitionException e) {
            throw new IllegalStateException("Recognition exception is never thrown, only declared.");
        }
    }

    /**
     * @param ast
     */
    private void printTree(CommonTree ast) {
        print(ast, 0);
    }

    /**
     * @param tree
     * @param level
     */
    private void print(CommonTree tree, int level) {
        //indent level
        for (int i = 0; i < level; i++)
            System.out.print("--");

        //print node description: type code followed by token text
        System.out.println(" " + tree.getType() + " " + tree.getText());

        //print all children
        if (tree.getChildren() != null)
            for (Object ie : tree.getChildren()) {
                print((CommonTree) ie, level + 1);
            }
    }

}