import java.awt.Color;

/**
 * This <code>Player</code> class represents a
 * player in the game of Chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 */
class Player {
	public static final Color[] playerColors	= new Color[] {Piece.WHITE, Piece.BLACK};
	
	/**
	 * An <code>Array</code> of <code>Piece</code> that <code>Player</code> has.
	 */
	private Piece[]	pieces;
	
	/**
	 * The <code>Color</code> of <code>Player</code>.
	 */
	private Color	playerColor;
	
	/**
	 * The name of the <code>Player</code>.
	 */
	private String	name;
	
	/**
	 * The score of the <code>Player</code>.
	 */
	private int		score;
	
	/**
	 * Creates a player with specified name and player piece color
	 * @param n		the player's name
	 * @param color	the player's piece color
	 */
	public Player(String n, Color color) throws IllegalArgumentException {
		this.pieces	= new Piece[16];
		this.name	= n;
		
		if (color == null) {
			throw new IllegalArgumentException("Player must have a color");
		}
		
		else if (! color.equals(Piece.WHITE) && ! color.equals(Piece.BLACK)) {
			throw new IllegalArgumentException("Color is not " + Piece.WHITE.toString() + " or " + Piece.BLACK.toString() + ".");
		}
		
		else {
			this.playerColor	= color;
		}
		
		this.score	= 0;
		this.setPieces();
	}
	
	/**
	 * @return {@link #pieces}
	 */
	public Piece[] getPieces() {
		return this.pieces;
	}

	/**
	 * @return {@link #name}
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return {@link #playerColor}
	 */
	public Color getPlayerColor() {
		return this.playerColor;
	}
	
	/**
	 * @return {@link #score}
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * Set the <code>Player score</code> to <code>newScore</code>.
	 * 
	 * @param newScore is the new score.
	 */
	public void setScore(int newScore) {
		this.score = newScore;
	}
	
	/**
	 * Increment the <code>Player score</code> by <code>increment</code>.
	 * 
	 * @param increment is the increment.
	 */
	public void increaseScore(int increment) {
		this.score += increment;
	}
	
	/**
	 * Add all <code>Piece</code> to <code>Player</code>.
	 * 
	 * @throws IllegalArgumentException if <code>playerColor</code> is <code>null</code>.
	 */
	private void setPieces() throws IllegalArgumentException {
		//Add Pawns
		for (int i = 0; i < 8; i ++) {
			this.pieces[i] = new Pawn(this.playerColor);
		}
		
		//Add other pieces
		this.pieces[8]	= new Rook	(this.playerColor);
		this.pieces[9]	= new Knight(this.playerColor);
		this.pieces[10]	= new Bishop(this.playerColor);
		this.pieces[11]	= new Queen	(this.playerColor);
		this.pieces[12]	= new King	(this.playerColor);
		this.pieces[13]	= new Bishop(this.playerColor);
		this.pieces[14]	= new Knight(this.playerColor);
		this.pieces[15]	= new Rook	(this.playerColor);
	}
	
	/**
	 * @return <code>String</code> representation of <code>Player</code>.
	 */
	@Override
	public String toString() {
		return this.name + ":\t" + this.score;
	}
}