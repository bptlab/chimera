// $ANTLR 3.5.2 XORGrammar.g 2016-07-20 13:42:13

package de.hpi.bpt.chimera.execution.controlnodes.gateway.xorgrammarcompiler;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;


@SuppressWarnings("all")
public class XORGrammarParser extends Parser {
	public static final String[] tokenNames = new String[]{"<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMPARISON", "DOT", "NAME", "OPERATOR", "STRING"};
	public static final int EOF = -1;
	public static final int COMPARISON = 4;
	public static final int DOT = 5;
	public static final int NAME = 6;
	public static final int OPERATOR = 7;
	public static final int STRING = 8;
	public static final BitSet FOLLOW_NAME_in_expr2144 = new BitSet(new long[]{0x0000000000000010L});

	// delegators
	public static final BitSet FOLLOW_COMPARISON_in_expr2146 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_NAME_in_expr2148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr2_in_expr154 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_OPERATOR_in_expr157 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_expr2_in_expr159 = new BitSet(new long[]{0x0000000000000082L});
	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public XORGrammarParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}


	public XORGrammarParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[]{};
	}
	// $ANTLR end "expr2"

	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}

	;

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	// $ANTLR end "expr"

	// Delegated rules

	@Override
	public String[] getTokenNames() {
		return XORGrammarParser.tokenNames;
	}

	@Override
	public String getGrammarFileName() {
		return "XORGrammar.g";
	}

	// $ANTLR start "expr2"
	// XORGrammar.g:22:1: expr2 : NAME COMPARISON NAME ;
	public final XORGrammarParser.expr2_return expr2() throws RecognitionException {
		XORGrammarParser.expr2_return retval = new XORGrammarParser.expr2_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NAME1 = null;
		Token COMPARISON2 = null;
		Token NAME3 = null;

		Object NAME1_tree = null;
		Object COMPARISON2_tree = null;
		Object NAME3_tree = null;

		try {
			// XORGrammar.g:22:6: ( NAME COMPARISON NAME )
			// XORGrammar.g:22:8: NAME COMPARISON NAME
			{
				root_0 = (Object) adaptor.nil();


				NAME1 = (Token) match(input, NAME, FOLLOW_NAME_in_expr2144);
				NAME1_tree = (Object) adaptor.create(NAME1);
				adaptor.addChild(root_0, NAME1_tree);

				COMPARISON2 = (Token) match(input, COMPARISON, FOLLOW_COMPARISON_in_expr2146);
				COMPARISON2_tree = (Object) adaptor.create(COMPARISON2);
				adaptor.addChild(root_0, COMPARISON2_tree);

				NAME3 = (Token) match(input, NAME, FOLLOW_NAME_in_expr2148);
				NAME3_tree = (Object) adaptor.create(NAME3);
				adaptor.addChild(root_0, NAME3_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);
		} finally {
			// do for sure before leaving
		}
		return retval;
	}

	// $ANTLR start "expr"
	// XORGrammar.g:23:1: expr : expr2 ( OPERATOR expr2 )* ;
	public final XORGrammarParser.expr_return expr() throws RecognitionException {
		XORGrammarParser.expr_return retval = new XORGrammarParser.expr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OPERATOR5 = null;
		ParserRuleReturnScope expr24 = null;
		ParserRuleReturnScope expr26 = null;

		Object OPERATOR5_tree = null;

		try {
			// XORGrammar.g:23:5: ( expr2 ( OPERATOR expr2 )* )
			// XORGrammar.g:23:7: expr2 ( OPERATOR expr2 )*
			{
				root_0 = (Object) adaptor.nil();


				pushFollow(FOLLOW_expr2_in_expr154);
				expr24 = expr2();
				state._fsp--;

				adaptor.addChild(root_0, expr24.getTree());

				// XORGrammar.g:23:13: ( OPERATOR expr2 )*
				loop1:
				while (true) {
					int alt1 = 2;
					int LA1_0 = input.LA(1);
					if ((LA1_0 == OPERATOR)) {
						alt1 = 1;
					}

					switch (alt1) {
						case 1:
							// XORGrammar.g:23:14: OPERATOR expr2
						{
							OPERATOR5 = (Token) match(input, OPERATOR, FOLLOW_OPERATOR_in_expr157);
							OPERATOR5_tree = (Object) adaptor.create(OPERATOR5);
							adaptor.addChild(root_0, OPERATOR5_tree);

							pushFollow(FOLLOW_expr2_in_expr159);
							expr26 = expr2();
							state._fsp--;

							adaptor.addChild(root_0, expr26.getTree());

						}
						break;

						default:
							break loop1;
					}
				}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);
		} finally {
			// do for sure before leaving
		}
		return retval;
	}

	public static class expr2_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	}

	public static class expr_return extends ParserRuleReturnScope {
		Object tree;

		@Override
		public Object getTree() {
			return tree;
		}
	}
}
