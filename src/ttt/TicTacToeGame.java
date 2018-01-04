package ttt;

import java.util.Random;
class TicTacToe
{
	private static final int HUMAN        = 0;
	private static final int COMPUTER     = 1;
	public  static final int EMPTY        = 2;

	public  static final int HUMAN_WIN    = 0;
	public  static final int DRAW         = 1;
	public  static final int UNCLEAR      = 2;
	public  static final int COMPUTER_WIN = 3;

	private int [ ] [ ] board = new int[ 3 ][ 3 ];
    private Random random=new Random();
	private int side=1;
	private int position=UNCLEAR;
	private char computerChar,humanChar;

	// Constructor
	public TicTacToe( )
	{
		clearBoard( );
		initSide();
	}

	private void initSide()
	{
	    if (this.side==COMPUTER) { computerChar='X'; humanChar='O'; }
		else                     { computerChar='O'; humanChar='X'; }
    }

    public void setComputerPlays()
    {
        this.side=COMPUTER;
        initSide();
    }

    public void setHumanPlays()
    {
        this.side=HUMAN;
        initSide();
    }

	public boolean computerPlays()
	{
	    return side==COMPUTER;
	}

	public int chooseMove()
	{
	    Best best=chooseMove(COMPUTER);
	    return best.row*3+best.column;
    }

	// Find optimal move
	public Best chooseMove(int side) {
		int opp; // The other side
		Best reply; // Opponent's best reply
		int simpleEval; // Result of an immediate evaluation
		int bestRow = 0;
		int bestColumn = 0;
		int value = 0;

		if ((simpleEval = positionValue()) != UNCLEAR) {
			return new Best(simpleEval);
		} else {

			if (side == COMPUTER) {
				value = HUMAN_WIN;
				opp = getOpponent(COMPUTER);

			} else {
				value = COMPUTER_WIN;
				opp = getOpponent(side);
			}
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					if (squareIsEmpty(i, j)) {
						place(i, j, side);
						reply = chooseMove(opp);

						place(i, j, EMPTY);

						if ((side == COMPUTER && reply.val > value)
								|| (side == HUMAN && reply.val < value)) {
							value = reply.val;
							bestRow = i;
							bestColumn = j;
						}
					}
				}
			}
			return new Best(value, bestRow, bestColumn);
		}
	}

	public int getOpponent(int side) {
		if (side == COMPUTER) {
			return HUMAN;
		} else {
			return COMPUTER;
		}
	}

	//check if move ok
    public boolean moveOk(int move)
    {
 	//return ( move>=0 && move <=8 && board[move/3 ][ move%3 ] == EMPTY );
 	return true;
    }

    // play move
    public void playMove(int move)
    {
		board[move/3][ move%3] = this.side;
		if (side==COMPUTER) this.side=HUMAN;  else this.side=COMPUTER;
	}

	// Simple supporting routines
	private void clearBoard( )
	{
		// over elk vakje heen gaan en leegmaken.
		for (int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				board[x][y] = EMPTY;
			}
		}
	}

	private boolean boardIsFull( )
	{
		for (int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				if (board[x][y] == EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isAWin( int side )
	{
	    // verticaal
		for(int y = 0; y < 3; y++) {
			if (board[0][y] == board[1][y] && board[1][y] == board[2][y] && board[0][y] == side) {
				return true;
			}
		}
		// horizontaal
		for(int x = 0; x < 3; x++) {
			if (board[x][0] == board[x][1] && board[x][1] == board[x][2] && board[x][0] == side) {
				return true;
			}
		}
		// diagonalen
		if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[1][1] == side) {
			return true;
		}
		if (board[2][0] == board[1][1] && board[1][1] == board[0][2] && board[1][1] == side) {
			return true;
		}
	    return false;
    }

	// Play a move, possibly clearing a square
	private void place( int row, int column, int piece )
	{
		board[ row ][ column ] = piece;
	}

	private boolean squareIsEmpty( int row, int column )
	{
		return board[ row ][ column ] == EMPTY;
	}

	// Compute static value of current position (win, draw, etc.)
	public int positionValue() {
		{
			if (isAWin(COMPUTER)) {return COMPUTER_WIN;}
			if (isAWin(HUMAN)) {return HUMAN_WIN;}
			if (boardIsFull()) {return DRAW;}
			return UNCLEAR;
		}
	}
	public String toString() {
		String string = "";
		// for every row
		for (int x = 0; x < 3; x++) {
			string += ('\n');
			// for every col in row
			for (int y = 0; y < 3; y++) {
				if (board[x][y] == HUMAN) {string += humanChar;}
				else if (board[x][y] == COMPUTER) {string += computerChar;}
				else {string += '*';}
			}
		}
		return string;
	}

	public boolean gameOver()
	{
	    this.position=positionValue();
	    return this.position!=UNCLEAR;
    }
    public String winner()
    {
        if      (this.position==COMPUTER_WIN) return "computer";
        else if (this.position==HUMAN_WIN   ) return "human";
        else                                  return "nobody";
    }
	private class Best
    {
       int row, column, val;
       public Best( int v ) {
			 this( v, 0, 0 );
		 }

       public Best( int v, int r, int c )
        { val = v; row = r; column = c; }
    }
}

