// $ANTLR 3.5 de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g 2015-04-21 10:20:18

package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class XORGrammarLexer extends Lexer {
	public static final int EOF=-1;
	public static final int CHARAC=4;
	public static final int COMPARISON=5;
	public static final int DOT=6;
	public static final int NAME=7;
	public static final int OPERATOR=8;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public XORGrammarLexer() {} 
	public XORGrammarLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public XORGrammarLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g"; }

	// $ANTLR start "CHARAC"
	public final void mCHARAC() throws RecognitionException {
		try {
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:17:16: ( ( '$' )? ( 'A' .. 'Z' | 'a' .. 'z' | '0' .. '9' )+ )
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:17:18: ( '$' )? ( 'A' .. 'Z' | 'a' .. 'z' | '0' .. '9' )+
			{
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:17:18: ( '$' )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0=='$') ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:17:18: '$'
					{
					match('$'); 
					}
					break;

			}

			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:17:22: ( 'A' .. 'Z' | 'a' .. 'z' | '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CHARAC"

	// $ANTLR start "COMPARISON"
	public final void mCOMPARISON() throws RecognitionException {
		try {
			int _type = COMPARISON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:18:11: ( '=' | '<' | '>' | '<=' | '>=' )
			int alt3=5;
			switch ( input.LA(1) ) {
			case '=':
				{
				alt3=1;
				}
				break;
			case '<':
				{
				int LA3_2 = input.LA(2);
				if ( (LA3_2=='=') ) {
					alt3=4;
				}

				else {
					alt3=2;
				}

				}
				break;
			case '>':
				{
				int LA3_3 = input.LA(2);
				if ( (LA3_3=='=') ) {
					alt3=5;
				}

				else {
					alt3=3;
				}

				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:18:13: '='
					{
					match('='); 
					}
					break;
				case 2 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:18:19: '<'
					{
					match('<'); 
					}
					break;
				case 3 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:18:25: '>'
					{
					match('>'); 
					}
					break;
				case 4 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:18:31: '<='
					{
					match("<="); 

					}
					break;
				case 5 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:18:38: '>='
					{
					match(">="); 

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMPARISON"

	// $ANTLR start "DOT"
	public final void mDOT() throws RecognitionException {
		try {
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:19:13: ( '.' )
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:19:15: '.'
			{
			match('.'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOT"

	// $ANTLR start "OPERATOR"
	public final void mOPERATOR() throws RecognitionException {
		try {
			int _type = OPERATOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:20:9: ( ' & ' | ' | ' | '&' | '|' )
			int alt4=4;
			switch ( input.LA(1) ) {
			case ' ':
				{
				int LA4_1 = input.LA(2);
				if ( (LA4_1=='&') ) {
					alt4=1;
				}
				else if ( (LA4_1=='|') ) {
					alt4=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case '&':
				{
				alt4=3;
				}
				break;
			case '|':
				{
				alt4=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}
			switch (alt4) {
				case 1 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:20:11: ' & '
					{
					match(" & "); 

					}
					break;
				case 2 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:20:19: ' | '
					{
					match(" | "); 

					}
					break;
				case 3 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:20:27: '&'
					{
					match('&'); 
					}
					break;
				case 4 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:20:33: '|'
					{
					match('|'); 
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPERATOR"

	// $ANTLR start "NAME"
	public final void mNAME() throws RecognitionException {
		try {
			int _type = NAME;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:21:5: ( CHARAC | CHARAC DOT CHARAC )
			int alt5=2;
			alt5 = dfa5.predict(input);
			switch (alt5) {
				case 1 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:21:7: CHARAC
					{
					mCHARAC(); 

					}
					break;
				case 2 :
					// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:21:16: CHARAC DOT CHARAC
					{
					mCHARAC(); 

					mDOT(); 

					mCHARAC(); 

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NAME"

	@Override
	public void mTokens() throws RecognitionException {
		// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:1:8: ( COMPARISON | OPERATOR | NAME )
		int alt6=3;
		switch ( input.LA(1) ) {
		case '<':
		case '=':
		case '>':
			{
			alt6=1;
			}
			break;
		case ' ':
		case '&':
		case '|':
			{
			alt6=2;
			}
			break;
		case '$':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
			{
			alt6=3;
			}
			break;
		default:
			NoViableAltException nvae =
				new NoViableAltException("", 6, 0, input);
			throw nvae;
		}
		switch (alt6) {
			case 1 :
				// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:1:10: COMPARISON
				{
				mCOMPARISON(); 

				}
				break;
			case 2 :
				// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:1:21: OPERATOR
				{
				mOPERATOR(); 

				}
				break;
			case 3 :
				// de/uni_potsdam/hpi/bpt/bp2014/jcore/XORGrammar.g:1:30: NAME
				{
				mNAME(); 

				}
				break;

		}
	}


	protected DFA5 dfa5 = new DFA5(this);
	static final String DFA5_eotS =
		"\2\uffff\1\3\2\uffff";
	static final String DFA5_eofS =
		"\5\uffff";
	static final String DFA5_minS =
		"\1\44\1\60\1\56\2\uffff";
	static final String DFA5_maxS =
		"\3\172\2\uffff";
	static final String DFA5_acceptS =
		"\3\uffff\1\1\1\2";
	static final String DFA5_specialS =
		"\5\uffff}>";
	static final String[] DFA5_transitionS = {
			"\1\1\13\uffff\12\2\7\uffff\32\2\6\uffff\32\2",
			"\12\2\7\uffff\32\2\6\uffff\32\2",
			"\1\4\1\uffff\12\2\7\uffff\32\2\6\uffff\32\2",
			"",
			""
	};

	static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
	static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
	static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
	static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
	static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
	static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
	static final short[][] DFA5_transition;

	static {
		int numStates = DFA5_transitionS.length;
		DFA5_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
		}
	}

	protected class DFA5 extends DFA {

		public DFA5(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 5;
			this.eot = DFA5_eot;
			this.eof = DFA5_eof;
			this.min = DFA5_min;
			this.max = DFA5_max;
			this.accept = DFA5_accept;
			this.special = DFA5_special;
			this.transition = DFA5_transition;
		}
		@Override
		public String getDescription() {
			return "21:1: NAME : ( CHARAC | CHARAC DOT CHARAC );";
		}
	}

}
