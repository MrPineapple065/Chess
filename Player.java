import java.util.Objects;

/**
 * This {@code Player} class represents a
 * player in the game of Chess.
 * 
 * @version 21 March 2020
 * @since 21 March 2020
 * @author MrPineapple065
 */
class Player {
	/**
	 * An {@code array} of {@link Piece} that {@code Player} has.
	 */
	private final Piece[] pieces;
	
	/**
	 * The {@link PieceColor} of this.
	 */
	private final PieceColor playerColor;
	
	/**
	 * The name of the this.
	 */
	private final String name;
	
	/**
	 * The score of the this.
	 */
	private int	 score;
	
	/**
	 * Creates a {@code Player} with specified name and player piece color
	 * 
	 * @param n		is the player's name.
	 * @param color	is the player's piece color.
	 */
	public Player(String n, PieceColor color) {
		this.pieces	= new Piece[16];
		this.name	= Objects.requireNonNull(n, "Player's name must be non null");
		this.playerColor = Objects.requireNonNull(color, "Player must have PieceColor.");
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
	public PieceColor getPlayerColor() {
		return this.playerColor;
	}
	
	/**
	 * @return {@link #score}
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * Set {@link #score} to {@code newScore}.
	 * 
	 * @param newScore is the new score.
	 */
	public void setScore(int newScore) {
		this.score = newScore;
	}
	
	/**
	 * Increment {@link #score} by {@code increment}.
	 * 
	 * @param increment is the increment.
	 */
	public void increaseScore(int increment) {
		this.score += increment;
	}
	
	/**
	 * Add all {@link Piece} to {@link pieces}.
	 * 
	 * @throws IllegalArgumentException if construction of {@link Piece} fails.
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
	
	@Override
	public String toString() {
		return String.format("%-10s:%d", this.name, this.score);
	}
}