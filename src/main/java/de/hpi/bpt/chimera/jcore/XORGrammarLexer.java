// $ANTLR 3.5.2 XORGrammar.g 2016-07-20 13:42:14

package de.hpi.bpt.chimera.jcore;


import org.antlr.runtime.*;

@SuppressWarnings("all")
public class XORGrammarLexer extends Lexer {
	public static final int EOF = -1;
	public static final int COMPARISON = 4;
	public static final int DOT = 5;
	public static final int NAME = 6;
	public static final int OPERATOR = 7;
	public static final int STRING = 8;
	static final String DFA6_eotS = "\2\uffff\1\3\2\uffff";
	static final String DFA6_eofS = "\5\uffff";
	static final String DFA6_minS = "\1\43\1\60\1\56\2\uffff";
	static final String DFA6_maxS = "\3\172\2\uffff";
	static final String DFA6_acceptS = "\3\uffff\1\1\1\2";
	static final String DFA6_specialS = "\5\uffff}>";
	// $ANTLR end "STRING"
	static final String[] DFA6_transitionS = {"\1\1\14\uffff\12\2\7\uffff\32\2\6\uffff\32\2", "\12\2\7\uffff\32\2\6\uffff\32\2", "\1\4\1\uffff\12\2\7\uffff\32\2\6\uffff\32\2", "", ""};
	// $ANTLR end "COMPARISON"
	static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
	// $ANTLR end "DOT"
	static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
	// $ANTLR end "OPERATOR"
	static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
	// $ANTLR end "NAME"
	static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
	static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
	static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
	static final short[][] DFA6_transition;

	static {
		int numStates = DFA6_transitionS.length;
		DFA6_transition = new short[numStates][];
		for (int i = 0; i < numStates; i++) {
			DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
		}
	}

	protected DFA6 dfa6 = new DFA6(this);
	public XORGrammarLexer() {
	}
	public XORGrammarLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public XORGrammarLexer(CharStream input, RecognizerSharedState state) {
		super(input, state);
	}

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[]{};
	}

	@Override
	public String getGrammarFileName() {
		return "XORGrammar.g";
	}

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			// XORGrammar.g:17:16: ( ( '#' )? ( 'A' .. 'Z' | 'a' .. 'z' | '0' .. '9' )+ )
			// XORGrammar.g:17:18: ( '#' )? ( 'A' .. 'Z' | 'a' .. 'z' | '0' .. '9' )+
			{
				// XORGrammar.g:17:18: ( '#' )?
				int alt1 = 2;
				int LA1_0 = input.LA(1);
				if ((LA1_0 == '#')) {
					alt1 = 1;
				}
				switch (alt1) {
					case 1:
						// XORGrammar.g:17:18: '#'
					{
						match('#');
					}
					break;

				}

				// XORGrammar.g:17:22: ( 'A' .. 'Z' | 'a' .. 'z' | '0' .. '9' )+
				int cnt2 = 0;
				loop2:
				while (true) {
					int alt2 = 2;
					int LA2_0 = input.LA(1);
					if (((LA2_0 >= '0' && LA2_0 <= '9') || (LA2_0 >= 'A' && LA2_0 <= 'Z') || (LA2_0 >= 'a' && LA2_0 <= 'z'))) {
						alt2 = 1;
					}

					switch (alt2) {
						case 1:
							// XORGrammar.g:
						{
							if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
								input.consume();
							} else {
								MismatchedSetException mse = new MismatchedSetException(null, input);
								recover(mse);
								throw mse;
							}
						}
						break;

						default:
							if (cnt2 >= 1) {
								break loop2;
							}
							EarlyExitException eee = new EarlyExitException(2, input);
							throw eee;
					}
					cnt2++;
				}

			}

		} finally {
			// do for sure before leaving
		}
	}

	// $ANTLR start "COMPARISON"
	public final void mCOMPARISON() throws RecognitionException {
		try {
			int _type = COMPARISON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// XORGrammar.g:18:11: ( ( '!' )? ( '=' | '<' | '>' | '<=' | '>=' ) )
			// XORGrammar.g:18:13: ( '!' )? ( '=' | '<' | '>' | '<=' | '>=' )
			{
				// XORGrammar.g:18:13: ( '!' )?
				int alt3 = 2;
				int LA3_0 = input.LA(1);
				if ((LA3_0 == '!')) {
					alt3 = 1;
				}
				switch (alt3) {
					case 1:
						// XORGrammar.g:18:13: '!'
					{
						match('!');
					}
					break;

				}

				// XORGrammar.g:18:17: ( '=' | '<' | '>' | '<=' | '>=' )
				int alt4 = 5;
				switch (input.LA(1)) {
					case '=': {
						alt4 = 1;
					}
					break;
					case '<': {
						int LA4_2 = input.LA(2);
						if ((LA4_2 == '=')) {
							alt4 = 4;
						} else {
							alt4 = 2;
						}

					}
					break;
					case '>': {
						int LA4_3 = input.LA(2);
						if ((LA4_3 == '=')) {
							alt4 = 5;
						} else {
							alt4 = 3;
						}

					}
					break;
					default:
						NoViableAltException nvae = new NoViableAltException("", 4, 0, input);
						throw nvae;
				}
				switch (alt4) {
					case 1:
						// XORGrammar.g:18:18: '='
					{
						match('=');
					}
					break;
					case 2:
						// XORGrammar.g:18:24: '<'
					{
						match('<');
					}
					break;
					case 3:
						// XORGrammar.g:18:30: '>'
					{
						match('>');
					}
					break;
					case 4:
						// XORGrammar.g:18:36: '<='
					{
						match("<=");

					}
					break;
					case 5:
						// XORGrammar.g:18:43: '>='
					{
						match(">=");

					}
					break;

				}

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
			// do for sure before leaving
		}
	}

	// $ANTLR start "DOT"
	public final void mDOT() throws RecognitionException {
		try {
			// XORGrammar.g:19:13: ( '.' )
			// XORGrammar.g:19:15: '.'
			{
				match('.');
			}

		} finally {
			// do for sure before leaving
		}
	}

	// $ANTLR start "OPERATOR"
	public final void mOPERATOR() throws RecognitionException {
		try {
			int _type = OPERATOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// XORGrammar.g:20:9: ( ' & ' | ' | ' | '&' | '|' )
			int alt5 = 4;
			switch (input.LA(1)) {
				case ' ': {
					int LA5_1 = input.LA(2);
					if ((LA5_1 == '&')) {
						alt5 = 1;
					} else if ((LA5_1 == '|')) {
						alt5 = 2;
					} else {
						int nvaeMark = input.mark();
						try {
							input.consume();
							NoViableAltException nvae = new NoViableAltException("", 5, 1, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				break;
				case '&': {
					alt5 = 3;
				}
				break;
				case '|': {
					alt5 = 4;
				}
				break;
				default:
					NoViableAltException nvae = new NoViableAltException("", 5, 0, input);
					throw nvae;
			}
			switch (alt5) {
				case 1:
					// XORGrammar.g:20:11: ' & '
				{
					match(" & ");

				}
				break;
				case 2:
					// XORGrammar.g:20:19: ' | '
				{
					match(" | ");

				}
				break;
				case 3:
					// XORGrammar.g:20:27: '&'
				{
					match('&');
				}
				break;
				case 4:
					// XORGrammar.g:20:33: '|'
				{
					match('|');
				}
				break;

			}
			state.type = _type;
			state.channel = _channel;
		} finally {
			// do for sure before leaving
		}
	}

	// $ANTLR start "NAME"
	public final void mNAME() throws RecognitionException {
		try {
			int _type = NAME;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// XORGrammar.g:21:5: ( STRING | STRING DOT STRING )
			int alt6 = 2;
			alt6 = dfa6.predict(input);
			switch (alt6) {
				case 1:
					// XORGrammar.g:21:7: STRING
				{
					mSTRING();

				}
				break;
				case 2:
					// XORGrammar.g:21:16: STRING DOT STRING
				{
					mSTRING();

					mDOT();

					mSTRING();

				}
				break;

			}
			state.type = _type;
			state.channel = _channel;
		} finally {
			// do for sure before leaving
		}
	}

	@Override
	public void mTokens() throws RecognitionException {
		// XORGrammar.g:1:8: ( COMPARISON | OPERATOR | NAME )
		int alt7 = 3;
		switch (input.LA(1)) {
			case '!':
			case '<':
			case '=':
			case '>': {
				alt7 = 1;
			}
			break;
			case ' ':
			case '&':
			case '|': {
				alt7 = 2;
			}
			break;
			case '#':
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
			case 'z': {
				alt7 = 3;
			}
			break;
			default:
				NoViableAltException nvae = new NoViableAltException("", 7, 0, input);
				throw nvae;
		}
		switch (alt7) {
			case 1:
				// XORGrammar.g:1:10: COMPARISON
			{
				mCOMPARISON();

			}
			break;
			case 2:
				// XORGrammar.g:1:21: OPERATOR
			{
				mOPERATOR();

			}
			break;
			case 3:
				// XORGrammar.g:1:30: NAME
			{
				mNAME();

			}
			break;

		}
	}

	protected class DFA6 extends DFA {

		public DFA6(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 6;
			this.eot = DFA6_eot;
			this.eof = DFA6_eof;
			this.min = DFA6_min;
			this.max = DFA6_max;
			this.accept = DFA6_accept;
			this.special = DFA6_special;
			this.transition = DFA6_transition;
		}

		@Override
		public String getDescription() {
			return "21:1: NAME : ( STRING | STRING DOT STRING );";
		}
	}

}
