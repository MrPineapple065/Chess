import java.awt.Color;
import java.util.Objects;

/**
 * This {@code Queen} class represents a Queen in the game of Chess. <br>
 * This is a subclass of {@link Piece}
 * 
 * @version 21 March 2020
 * @author MrPineapple065
 *
 */
public class Queen extends Piece {
	/**
	 * {@link Bishop} to help with {@link #getLegal(Tile[][], Tile[])} and {@link #setTileCollide(Tile[][], Tile[])}
	 */
	private final Bishop bishop;
	
	/**
	 * {@link Rook} to help with {@link #getLegal(Tile[][], Tile[])} and {@link #setTileCollide(Tile[][], Tile[])}
	 */
	private final Rook rook;

	/**
	 * Creates a {@code Queen} that is {@code color}.
	 * 
	 * @param color is the {@link Color} of {@code Queen}.
	 */
	public Queen(PieceColor color) {
		super(color);
		this.bishop = new Bishop(color);
		this.rook = new Rook(color);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)								return true;
		if (!(obj instanceof Queen))					return false;
		Queen other = (Queen) obj;
		if (bishop == null) if (other.bishop != null)	return false;
		else if (!bishop.equals(other.bishop))			return false;
		if (rook == null) if (other.rook != null)		return false;
		else if (!rook.equals(other.rook))				return false;
		return true;
	}

	/**
	 * <p>Determine if move {@code Queen} makes from {@code tiles[0]} to {@code tiles[1]} is legal. </p>
	 * <p>{@code Queen} may move in any direction for any number of {@link Tile}.</p>
	 */
	@Override
	public boolean getLegal(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		Objects.requireNonNull(tiles, "Queen must move.");
		return this.rook.getLegal(board, tiles) || this.bishop.getLegal(board, tiles);
	}

	@Override
	public int getValue() throws IllegalAccessException {
		return 9;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bishop == null) ? 0 : bishop.hashCode());
		result = prime * result + ((rook == null) ? 0 : rook.hashCode());
		return result;
	}

	public void reset() {}
	
	@Override
	public Tile[] setTileCollide(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		//Queen moves like Bishop
		if (this.bishop.getLegal(board, tiles))		return this.bishop.setTileCollide(board, tiles);
		//Queen moves like Rook
		else if (this.rook.getLegal(board, tiles))	return this.rook.setTileCollide(board, tiles);
		throw new IllegalStateException("Queen cannot move like this.");
	}

	@Override
	public String toString() {
		switch (this.pieceColor) {
		case White:
			return "\u2655";
		case Black:
			return "\u265B";
		default:
			return default_name;
		}
	}
}