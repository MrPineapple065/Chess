import java.awt.Color;

/**
 * This <code>Queen</code> class represents a
 * Queen in the game of Chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 *
 */
public class Queen extends Piece {
	/**
	 * The amount of points that <code>Queen</code> is worth.
	 */
	private static final int	VALUE	= 0x09;
	
	/**
	 * Creates a <code>Queen</code> that is <code>color</code>.
	 * 
	 * @param color is the color of <code>Queen</code>.
	 * @throws IllegalArgumentException if <code>color</code> is <code>null</code>
	 */
	public Queen(Color color) throws IllegalArgumentException {
		super(color);
	}
	
	/**
	 * Determine the value of <code>Queen</code>
	 * 
	 * @return <b><i>{@link #VALUE}</i></b>
	 */
	public static int getValue() {
		return VALUE;
	}
	
	/**
	 * Determine if move <code>Queen</code> makes from <code>tiles[0]</cod>< to <code>tiles[1]</code> is legal. </br>
	 * <code>Queen</code> may move in any direction for any number of <code>Tile</code>.
	 * 
	 * @param tiles are the original and new positions of <code>Queen</code>
	 * 
	 * @return an <code>Array</code> of <code>Tile<code> that the <code>Queen</code>
	 *			will take on its journey from <code>tiles[0]</code> to <code>tiles[1]</code>.
	 *
	 * @throws IllegalArgumentException
	 */
	public static boolean getLegal(Tile[] tiles) throws IllegalArgumentException {
		if (tiles == null) {
			throw new IllegalArgumentException("Queen must move.");
		}
		return (Rook.getLegal(tiles) || Bishop.getLegal(tiles));
	}
	
	/**
	 * Determine all <code>Tile</code> from <code>tiles[0]</code> to <code>tiles[1]</code> that <code>Queen</code> travels over in its journey.
	 * 
	 * @param piece is a piece.
	 * @param board is the board.
	 * @param tiles are the original and new positions of <code>Queen</code>.
	 * 
	 * @return	an <code>Array</code> of <code>Tile<code> that the <code>Queen</code>
	 *			will take on its journey from <code>tiles[0]</code> to <code>tiles[1]</code>.
	 *
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 */
	public static Tile[] setTilesCollide(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalStateException, IllegalArgumentException {
		if (tiles == null) {
			throw new IllegalArgumentException("Queen must move.");
		}
		
		//Queen moves like Bishop
		if (Bishop.getLegal(tiles)) {
			return Bishop.setTilesCollide(piece, board, tiles);
		}
		
		//Queen moves like Rook
		else if (Rook.getLegal(tiles)) {
			return Rook.setTilesCollide(piece, board, tiles);
		}
		
		throw new IllegalStateException("Queen cannot move like this.");
	}

	/**
	 * @return <code>String</code> representation of <code>Queen</code>.
	 */
	@Override
	public String toString() {
		if (super.getPieceColor().equals(Piece.WHITE)) {
			return "\u2655";
		}
		
		return "\u265B";
	}
}