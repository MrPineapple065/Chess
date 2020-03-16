import java.awt.Color;
import java.util.ArrayList;

/**
 * This <code>Bishop</code> class represents a
 * Bishop in the game of chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 *
 */
public class Bishop extends Piece {
	/**
	 * The amount of points that <code>Bishop</code> is worth
	 */
	private static final int 	VALUE	= 3;
	
	/**
	 * Create a <code>Bishop</code> that is <code>color</code>.
	 * 
	 * @param color is the color of <code>Piece</code>.
	 * 
	 * @throws IllegalArgumentException if <code>color</code> is <code>null</code>.
	 */
	public Bishop(Color color) throws IllegalArgumentException {
		super(color);
	}
	
	/**
	 * Determine the value of <code>Bishop</code>
	 * @return <i><b>{@link #VALUE}</b></i>
	 */
	public static int getValue() {
		return VALUE;
	}
	
	/**
	 * Determine if move <code>Bishop</code> makes from <code>tiles[0]</code> to <code>tiles[1]</code> is legal. </br>
	 * <code>Bishop</code> may <i>only</i> move <b>diagonally</b>.
	 * 
	 * @param tiles are the original and new positions of <code>Bishop</code>.
	 * @return	<code>true</code> if the move is legal. </br>
	 * 			<code>false</code> if the move is illegal.
	 * 
	 * @throws IllegalArgumentException if <code>tiles</code> is <code>null</code>
	 */
	public static boolean getLegal(Tile[] tiles) throws IllegalArgumentException {
		if (tiles == null) {
			throw new IllegalArgumentException("Bishop must move.");
		}
		
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		return (Math.abs(oldX - newX) == Math.abs(oldY - newY));
	}

	/**
	 * Determine all <code>Tile</code> from <code>tiles[0]</code> to
	 * <code>tiles[1]</code> that <code>Bishop</code> travels over in its journey
	 * 
	 * @param piece is the piece moving.
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>Bishop</code>.
	 * 
	 * @return an <code>Array</code> of <code>Tile</code> that <code>Bishop</code> takes along its journey.
	 * 
	 * @throws IllegalArgumentException if any of the <code>parameters</code> are <code>null<code>.
	 */
	public static Tile[] setTilesCollide(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		try {
			Bishop.iae(piece, board, tiles);
		}
		
		catch (IllegalArgumentException iae) {
			throw iae;
		}
		
		ArrayList<Tile> temp = new ArrayList<Tile>();
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		int i = oldX, j = oldY;
		
		if (newX < oldX) {
			if (newY < oldY) {
				//Going NW
				for (int count = newX; count <= oldX; count++) {
					temp.add(board[i][j]);
					i--; j--;
				}
			}
			
			else {
				//Going NE
				for (int count = newX; count <= oldX; count++) {
					temp.add(board[i][j]);
					i--; j++;
				}
			}
		}
		
		else {
			if (newY < oldY) {
				//Going SW
				for (int count = oldX; count <= newX; count++) {
					temp.add(board[i][j]);
					i++; j--;
				}
			}
			
			else {
				//Going SE
				for (int count = oldX; count <= newX; count++) {
					temp.add(board[i][j]);
					i++; j++;
				}
			}
		}
		
		//Cannot collide with self
		if (piece.equals(temp.get(0).getPiece())) {
			temp.remove(0);
		}
		
		//Cannot collide when capturing
		if (temp.get(temp.size() - 1).getPiece() != null) {
			if (! piece.isAlly(temp.get(temp.size() - 1).getPiece())) {
				temp.remove(temp.size() - 1);
			}
		}
		
		
		Tile[] t = new Tile[temp.size()];
		for (int k = 0; k < t.length; k++) {
			t[k] = temp.get(k);
		}
		
		return t;

	}
	
	/**
	 * Determine if any <code>parameters</code> are <code>null</code>.
	 * 
	 * @param piece is the Bishop
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>Bishop</code>
	 * 
	 * @throws IllegalArgumentException with the correct information.
	 */
	private static void iae(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		if (piece == null) {
			throw new IllegalArgumentException("There must be a Bishop.");
		}
		
		if (board == null) {
			throw new IllegalArgumentException("There must be a board.");
		}
		
		if (tiles == null) {
			throw new IllegalArgumentException("Bishop must move.");
		}
	}

	/**
	 * @return <code>String</code> representation of <code>Bishop</code>
	 */
	@Override
	public String toString() {
		if (super.getPieceColor().equals(Piece.WHITE)) {
			return "\u2657";
		}
		
		return "\u265D";
	}
}
