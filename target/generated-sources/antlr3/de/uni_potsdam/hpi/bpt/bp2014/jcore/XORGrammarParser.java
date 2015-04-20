// $ANTLR 3.5 de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g 2015-04-20 15:37:33

package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class XORGrammarParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "CHARAC", "COMPARISON", "DOT", 
		"NAME", "OPERATOR"
	};
	public static final int EOF=-1;
	public static final int CHARAC=4;
	public static final int COMPARISON=5;
	public static final int DOT=6;
	public static final int NAME=7;
	public static final int OPERATOR=8;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public XORGrammarParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public XORGrammarParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return XORGrammarParser.tokenNames; }
	@Override public String getGrammarFileName() { return "de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g"; }


	public static class expr2_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "expr2"
	// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:22:1: expr2 : NAME COMPARISON NAME ;
	public final XORGrammarParser.expr2_return expr2() throws RecognitionException {
		XORGrammarParser.expr2_return retval = new XORGrammarParser.expr2_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NAME1=null;
		Token COMPARISON2=null;
		Token NAME3=null;

		Object NAME1_tree=null;
		Object COMPARISON2_tree=null;
		Object NAME3_tree=null;

		try {
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:22:6: ( NAME COMPARISON NAME )
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:22:8: NAME COMPARISON NAME
			{
			root_0 = (Object)adaptor.nil();


			NAME1=(Token)match(input,NAME,FOLLOW_NAME_in_expr2138); 
			NAME1_tree = (Object)adaptor.create(NAME1);
			adaptor.addChild(root_0, NAME1_tree);

			COMPARISON2=(Token)match(input,COMPARISON,FOLLOW_COMPARISON_in_expr2140); 
			COMPARISON2_tree = (Object)adaptor.create(COMPARISON2);
			adaptor.addChild(root_0, COMPARISON2_tree);

			NAME3=(Token)match(input,NAME,FOLLOW_NAME_in_expr2142); 
			NAME3_tree = (Object)adaptor.create(NAME3);
			adaptor.addChild(root_0, NAME3_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expr2"


	public static class expr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:23:1: expr : expr2 ( OPERATOR expr2 )* ;
	public final XORGrammarParser.expr_return expr() throws RecognitionException {
		XORGrammarParser.expr_return retval = new XORGrammarParser.expr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OPERATOR5=null;
		ParserRuleReturnScope expr24 =null;
		ParserRuleReturnScope expr26 =null;

		Object OPERATOR5_tree=null;

		try {
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:23:5: ( expr2 ( OPERATOR expr2 )* )
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:23:7: expr2 ( OPERATOR expr2 )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_expr2_in_expr148);
			expr24=expr2();
			state._fsp--;

			adaptor.addChild(root_0, expr24.getTree());

			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:23:13: ( OPERATOR expr2 )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==OPERATOR) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:23:14: OPERATOR expr2
					{
					OPERATOR5=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_expr151); 
					OPERATOR5_tree = (Object)adaptor.create(OPERATOR5);
					adaptor.addChild(root_0, OPERATOR5_tree);

					pushFollow(FOLLOW_expr2_in_expr153);
					expr26=expr2();
					state._fsp--;

					adaptor.addChild(root_0, expr26.getTree());

					}
					break;

				default :
					break loop1;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expr"

	// Delegated rules



	public static final BitSet FOLLOW_NAME_in_expr2138 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_COMPARISON_in_expr2140 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NAME_in_expr2142 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr2_in_expr148 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_OPERATOR_in_expr151 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_expr2_in_expr153 = new BitSet(new long[]{0x0000000000000102L});
}
