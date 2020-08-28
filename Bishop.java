import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This {@code Bishop} class represents a Bishop in the game of chess.
 * This is a subclass of {@link Piece}.
 * 
 * @version 28 August 2020
 * @since 21 March 2020
 * @author MrPineapple065
 */
public class Bishop extends Piece {
	/**
	 * Create a {@code Bishop} that is {@code color}.
	 * 
	 * @param color is the {@link Color} of {@code Bishop}.
	 */
	public Bishop(PieceColor color) {
		super(color);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)				return true;
		if (!(obj instanceof Bishop))	return false;
		return true;
	}
	
	/**
	 * <p>Determine if move {@code Bishop} makes from {@code tiles[0]} to {@code tiles[1]} is legal.</p>
	 * <p>{@code Bishop} may must move <b>diagonally</b>.</p>
	 */
	@Override
	public boolean getLegal(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		Objects.requireNonNull(tiles, "Bishop must move");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");
		return Math.abs(tiles[0].getRow() - tiles[1].getRow()) == Math.abs(tiles[0].getColumn() - tiles[1].getColumn());
	}

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + pieceColor.hashCode();
		result = prime * result + 3;
		return result;
	}
	
	public void reset() {}
	
	@Override
	public Tile[] setTileCollide(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		Objects.requireNonNull(board, "Bishop must be on a board");
		Objects.requireNonNull(tiles, "Bishop must move");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");
		
		ArrayList<Tile> temp = new ArrayList<Tile>();
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		int i = oldX, j = oldY;
		int dx = 0, dy = 0;
		
		if (newX < oldX)
			if (newY < oldY) {	//North West
				dx = -1; dy = -1;
			} else {			//North East
				dx = -1; dy = 1;
			}
		else
			if (newY < oldY) {	//South West
				dx = 1; dy = -1;
			} else {			//South East
				dx = 1; dy = 1;
			}
		
		for (int count = 0; count <= Math.abs(oldX - newX); count++) {
			temp.add(board[i][j]);
			i += dx; j += dy;
		}
		
		//Cannot collide with self
		temp.remove(board[oldX][oldY]);
		
		//Cannot collide when capturing
		if (!this.isAlly(temp.get(temp.size() - 1).getPiece())) temp.remove(temp.size() - 1);
		return temp.toArray(new Tile[temp.size()]);
	}

	@Override
	public String toString() {
		switch (this.pieceColor) {
		case White:
			return "\u2657";
		case Black:
			return "\u265D";
		default:
			return default_name;
		}
	}
}