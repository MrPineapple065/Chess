import java.util.Objects;

/**
 * This {@code Knight} class represents a Knight in the game of chess.
 * 
 * @version 21 March 2020
 * @author MrPineapple
 *
 */
public class Knight extends Piece {
	/**
	 * Creates a {@code Knight} that is {@code color}.
	 * 
	 * @param color is the {@link PieceColor} of {@code Knight}.
	 */
	public Knight(PieceColor color) {
		super(color);
	}

	@Override
	public int hashCode() {
		return this.pieceColor.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)				return true;
		if (!(obj instanceof Knight))	return false;
		return true;
	}

	/**
	 * <p>Determine if move {@code Knight} makes from {@code tiles[0]} to {@code tiles[1]} is legal.</p>
	 * <p>{@code Knight} must move in an <i>"L"</i> shape.</p>
	 */
	@Override
	public boolean getLegal(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		Objects.requireNonNull(tiles, "Knight must move.");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");

		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();

		//Vertical L
		if ((Math.abs(newX - oldX) == 1) && (Math.abs(newY - oldY) == 2)) 		return true;
		//Horizontal L
		else if ((Math.abs(newX - oldX) == 2) && (Math.abs(newY - oldY) == 1))	return true;
		return false;
	}

	@Override
	public int getValue() {
		return 3;
	}
	
	public void reset() {}

	@Override
	public Tile[] setTileCollide(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		return new Tile[0];
	}

	@Override
	public String toString() {
		switch (this.pieceColor) {
		case White:
			return "\u2658";
		case Black:
			return "\u265E";
		default:
			return default_name;
		}
	}
}