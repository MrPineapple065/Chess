import java.util.ArrayList;
import java.util.Objects;

/**
 * This {@code Rook} class represents a
 * Rook in the game of Chess. <br>
 * This is a subclass of {@link Piece}
 * 
 * @version 28 August 2020
 * @since 21 March 2020
 * @author MrPineapple065
 */
public class Rook extends Piece {
	/**
	 * A boolean determining if the {@code Rook} has moved.
	 * 
	 * @see King#castle(Piece, Tile[], ChessBoard)
	 */
	private boolean	hasMoved;

	/**
	 * Create a {@code Rook} with {@code color}.
	 * 
	 * @param color is the {@link PieceColor} of {@code Rook}.
	 */
	public Rook(PieceColor color) {
		super(color);
		this.hasMoved = false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)				return true;
		if (!(obj instanceof Rook))		return false;
		Rook other = (Rook) obj;
		if (hasMoved != other.hasMoved)	return false;
		return true;
	}

	/**
	 * @see #getLegal(Tile[][], Tile[])
	 * @param tiles	are the original and new positions of {@code Rook}.
	 * @return true / false
	 */
	private boolean getLegal(Tile[] tiles) {
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		return ((oldX - newX == 0) && (oldY != newY)) || ((oldX != newX) && (oldY - newY == 0));
	}

	/**
	 * Determine if move {@code Rook} makes from {@code tiles[0]} to {@code tiles[1]} is legal. <br>
	 * A {@code Rook} can move <b>horizantally</b> or <b>vertically</b>.
	 */
	@Override
	public boolean getLegal(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		Objects.requireNonNull(tiles, "Rook must move.");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles");

		if (this.getLegal(tiles)) {
			this.setHasMoved(true);
			return true;
		}
		return false;
	}

	@Override
	public int getValue() throws IllegalAccessException {
		return 5;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hasMoved ? 1231 : 1237);
		return result;
	}

	/**
	 * Determine if <code>Rook</code> has moved.
	 * 
	 * @return <code>hasMoved</code>
	 */
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	@Override
	public void reset() {
		this.hasMoved = false;
	}
	
	/**
	 * Set {@link hasMoved} to {@code bool}.
	 * 
	 * @param bool is the new value.
	 */
	public void setHasMoved(boolean bool ) {
		this.hasMoved = bool;
	}

	@Override
	public Tile[] setTileCollide(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		ArrayList<Tile> temp = new ArrayList<Tile>();
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();

		if (newX < oldX) {			//North
			for (int row = oldX; row >= newX; row--) {
				temp.add(board[row][oldY]);
			}
		} else if (newX > oldX) {	//South
			for (int row = oldX; row <= newX; row++) {
				temp.add(board[row][oldY]);
			}
		} else if (newY > oldY) {	//East
			for (int col = oldY; col <= newY; col++) {
				temp.add(board[oldX][col]);
			}
		} else {					//West
			for (int col = oldY; col >= newY; col--) {
				temp.add(board[oldX][col]);
			}
		}

		//Cannot collide with self
		temp.remove(board[oldX][oldY]);

		/**Cannot collide when capturing*/
		if (temp.get(temp.size() - 1).getPiece() != null) {
			if (! this.isAlly(temp.get(temp.size() - 1).getPiece())) {
				temp.remove(temp.size() - 1);
			}
		}

		Tile[] t = new Tile[temp.size()];
		for (int k = 0; k < t.length; k++) {
			t[k] = temp.get(k);
		} return t;
	}

	@Override
	public String toString() {
		switch (this.pieceColor) {
		case White:
			return "\u2656";
		case Black:
			return "\u265C";
		default:
			return "\uFFFD";
		}
	}
}