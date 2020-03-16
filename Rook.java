import java.awt.Color;
import java.util.ArrayList;

/**
 * This <code>Rook</code> class represents a
 * Rook in the game of Chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 *
 */
public class Rook extends Piece {
	/**
	 * The amount of points that the <code>Rook</code> is worth.
	 */
	private static final int	VALUE	= 0x05;
	
	/**
	 * A boolean determining if the <code>Pawn</code> has moved.
	 * 
	 * @see King.castle();
	 */
	private boolean	hasMoved;
	
	/**
	 * Create a <code>Rook</code> with <code>color</code>.
	 * 
	 * @param color is the color of <code>Rook</code>.
	 * 
	 * @throws IllegalClassFormatException if <code>color</code> is <code>null</code>.
	 */
	public Rook(Color color) throws IllegalArgumentException {
		super(color);
		this.hasMoved = false;
	}
	
	/**
	 * Determine the value of <code>Rook</code>.
	 * 
	 * @return <b><i>VALUE</i></b>
	 */
	public static int getValue() {
		return VALUE;
	}
	
	/**
	 * Determine if <code>Rook</code> has moved.
	 * 
	 * @return <code>hasMoved</code>
	 */
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	/**
	 * Set <code>hasMoved</code> to <code>bool</code>.
	 * 
	 * @param bool is the new value.
	 */
	public void setHasMoved(boolean bool ) {
		this.hasMoved = bool;
	}
	
	/**
	 * Determine if move <code>Rook</code> makes from <code>tiles[0]</code> to <code>tiles[1]</code> is legal. </br>
	 * A <code>Rook</code> can <i>only</i> move <b>horizantally</b> or <b>vertically</b>.
	 * 
	 * @param tiles are the original and new positions of <code>Rook</code>.
	 * @return  <code>true</code> if the move is legal. <br>
	 * 			<code>false</code> if the move is illegal.
	 * 
	 * @throws IllegalArgumentException if any <code>parameter</code> is <code>null</code>.
	 */
	public static boolean getLegal(Rook rook, Tile[] tiles) throws IllegalArgumentException {
		try {
			Rook.iae(rook, new Tile[0][0], tiles);
		}
		
		catch (IllegalArgumentException iae) {
			throw iae;
		}
		
		if (Rook.getLegal(tiles)) {
			rook.setHasMoved(true);
			return true;
		}
		return false;
	}
	
	/**
	 * @see #getLegal(Rook, Tile[])
	 * @param tiles
	 * @return true / false
	 */
	public static boolean getLegal(Tile[] tiles) {
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		return ((oldX - newX == 0) && (oldY != newY)) || ((oldX != newX) && (oldY - newY == 0));
	}
	
	/**
	 * Determine all <code>Tile</code> that the <code>Rook</code>
	 * will take on its journey from <code>tiles[0]</code> to <code>tiles[1]</code>.
	 * 
	 * @param piece is the piece moving.
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>Rook</code>.
	 * 
	 * @return	An <code>Array</code> of <code>Tile</code> that the <code>Rook</code>
	 * 			will take on its journey from <code>tiles[0]</code> to <code>tiles[1]</code>.
	 * 
	 * @throws IllegalArgumentException if any {@link #iae(Piece, Tile[][], Tile[])} throws its exception.
	 */
	public static Tile[] setTilesCollide(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		try {
			Rook.iae(piece, board, tiles);
		}
		
		catch (IllegalArgumentException iae) {
			throw iae;
		}
		
		ArrayList<Tile> temp = new ArrayList<Tile>();
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		/**Going <b>North</b>*/
		if (newX < oldX) {
			for (int row = oldX; row >= newX; row--) {
				temp.add(board[row][oldY]);
			}
		}
		
		/**Going <b>South</b>*/
		else if (newX > oldX) {
			for (int row = oldX; row <= newX; row++) {
				temp.add(board[row][oldY]);
			}
		}
		
		/**Going <b>East</b>*/
		else if (newY > oldY) {
			for (int col = oldY; col <= newY; col++) {
				temp.add(board[oldX][col]);
			}
		}
		
		/**Going <b>West</b>*/
		else {
			for (int col = oldY; col >= newY; col--) {
				temp.add(board[oldX][col]);
			}
		}
      
		/**Cannot collide with <code>self</code>*/
		if (piece.equals(temp.get(0).getPiece())) {
			temp.remove(0);
		}
		
		/**Cannot collide when capturing*/
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
	 * Determine if any <code>parameter</code> is <code>null</code>
	 * 
	 * @param piece is the rook.
	 * @param board is the board.
	 * @param tiles are the original and new positisions of <code>Rook</code>
	 * 
	 * @throws IllegalArgumentException if any <code>parameter</code> is <code>null</code>.
	 */
	private static void iae(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		if (piece == null) {
			throw new IllegalArgumentException("There must be a Rook.");
		}
		
		if (board == null) {
			throw new IllegalArgumentException("There must be a board");
		}
		
		if (tiles == null) {
			throw new IllegalArgumentException("Rook must move.");
		}
	}

	/**
	 * @return <code>String</code> representation of <code>Rook</code>
	 */
	@Override
	public String toString() {
		if (super.getPieceColor().equals(Piece.WHITE)) {
			return "\u2656";
		}
		
		return "\u265C";
	}
}