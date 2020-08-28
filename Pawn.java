import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

/**
 * This {@code Pawn} represents a Pawn in the game of Chess. <br>
 * This is a subclass of {@link Piece}
 * 
 * @version 28 August 2020
 * @since 21 March 2020
 * @author MrPineapple065
 */
class Pawn extends Piece {
	/**
	 * A boolean determnining is the {@code Pawn} taken advantage of its first move.
	 * Used in {@link #getLegal(Tile[][], Tile[])}.
	 */
	private boolean advantage;
	
	/**
	 * A boolean determining if the {@code Pawn} has moved.
	 * Used in {@link #getLegal(Tile[][], Tile[])}
	 */
	private boolean	hasMoved;
	
	/**
	 * Creates a {@code Pawn} that is {@code color}.
	 * 
	 * @param color is the {@link Color} of the {@code Pawn}.
	 */
	public Pawn(PieceColor color) {
		super(color);
		this.hasMoved = false;
		this.advantage = false;
	}
	
	/**
	 * Determine if {@code Pawn} has taken advantage of first move. <br>
	 * Used in {@link #getLegal(Tile[][], Tile[])}
	 * 
	 * @return {@link #advantage}
	 */
	public boolean advantage() {
		return this.advantage;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)					return true;
		if (!(obj instanceof Pawn))			return false;
		Pawn other = (Pawn) obj;
		if (advantage != other.advantage)	return false;
		if (hasMoved != other.hasMoved)		return false;
		return true;
	}
	
	/**
	 * <p>Determine if move {@code Pawn} makes from {@code tiles[0]} to {@code tiles[1]} is legal.</p>
	 * <p>A {@code Pawn} can must move <b>forward</b> <i>one</i> {@link Tile} at a time.<br>
	 * Only the first move may {@code Pawn} move <i>two</i> {@code Tile} forward.</p>
	 * <p>It must capture <b>diagonally</b>.<br>
	 * En passant is a special pawn capture that can only occur <b>immediately</b> 
	 * after {@code Pawn} makes a move of <i>two</i> {@code Tile} from its starting square.<br>
	 * It can be captured by an <b><i>enemy</i></b> {@code Pawn} had it advanced <b><i>only one</i></b> {@code Tile}</p>
	 */
	@Override
	public boolean getLegal(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		Objects.requireNonNull(board, "Pawn must be on a board");
		Objects.requireNonNull(tiles, "Pawn must move");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");
		
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		Piece newPosPiece = board[newX][newY].getPiece();
		if (this.isWhite()) {
			if (!this.hasMoved) {
				if ((oldX - newX <= 2) && (oldY == newY)) {
					if (newPosPiece != null) return false;
					this.hasMoved = true;
					this.advantage = oldX - newX == 2;
					return true;
				} if ((oldX - newX == 1) && (Math.abs(oldY - newY) == 1)) {
					if (newPosPiece != null) {
						this.hasMoved = true;
						this.advantage = false;
						return true;
					}
				}
			} else {
				if ((oldX - newX == 1) && (newY == oldY)) {
					this.advantage = false;
					return (newPosPiece == null);
				} if ((oldX - newX == 1) && (Math.abs(oldY - newY) == 1)) {
					if (newPosPiece != null) return true;
					Piece behind = board[newX][oldY].getPiece();
					if (behind instanceof Pawn) {
						if (((Pawn)behind).advantage()) {
							board[newX][oldY].setPiece(null);
							return true;
						}
					}
				}
			}
		} else {
			if (!this.hasMoved) {
				if (oldX - newX >= -2 && oldY == newY) {
					if (newPosPiece != null) return false;
					this.hasMoved = true;
					this.advantage = oldX - newX == -2;
					return true;
				} if (oldX - newX == -1 && Math.abs(oldY - newY) == 1) {
					if (newPosPiece != null) {
						this.hasMoved = true;
						this.advantage = false;
						return true;
					}
				}
			} else {
				if (oldX - newX == -1 && newY == oldY) {
					this.setAdvantage(false);
					return (newPosPiece == null);
				} if (oldX - newX == -1 && Math.abs(oldY - newY) == 1) {
					if (newPosPiece != null) return true;
					else {
						Piece behind = board[newX][oldY].getPiece();
						if (behind instanceof Pawn) {
							if (((Pawn)behind).advantage()) {
								board[newX][oldY].setPiece(null);
								return true;
							}
						}
					}
				}
			}
		} return false;
	}

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (advantage ? 1231 : 1237);
		result = prime * result + (hasMoved ? 1231 : 1237);
		return result;
	}
	
	/**
	 * Determine if {@code Pawn} has moved.
	 * 
	 * @return {@link #hasMoved}
	 */
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	@Override
	public void reset() {
		this.hasMoved = false;
		this.advantage = false;
	}
	
	/**
	 * Promote a {@code Pawn} that has made it to the other side of {@code board}.
	 * 
	 * @param tiles are the original and new positions of {@code Pawn}.
	 */
	public void promote(Tile[] tiles) {
		Queen	queen	= new Queen	(this.pieceColor);
		Bishop	bishop	= new Bishop(this.pieceColor);
		Rook	rook	= new Rook	(this.pieceColor);
		Knight	knight	= new Knight(this.pieceColor);
		
		switch (JOptionPane.showOptionDialog(null, "Which piece would you like to promote the pawn to?", "Promotion!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {queen.toString(), bishop.toString(), rook.toString(), knight.toString()}, 0)) {
		case 0:
			tiles[1].setPiece(queen);
			return;
		case 1:
			tiles[1].setPiece(bishop);
			return;
		case 2:
			tiles[1].setPiece(rook);
			return;
		case 3:
			tiles[1].setPiece(knight);
			return;
		}
	}

	/**
	 * Change {@link #advantage} to {@code bool}.
	 * @param bool is the new value.
	 */
	public void setAdvantage(boolean bool) {
		this.advantage = bool;
	}

	/**
	 * Change {@link #hasMoved} to {@code bool}.
	 * 
	 * @param bool is the new value.
	 */
	public void setFirstMove(boolean bool) {
		this.hasMoved = bool;
	}

	@Override
	public Tile[] setTileCollide(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		Objects.requireNonNull(board, "Pawn must be on a board");
		Objects.requireNonNull(tiles, "Pawn must move");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");
		
		ArrayList<Tile> temp = new ArrayList<Tile>(2);
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		if (this.isWhite())
			if (oldY != newY) {
				temp.add(board[oldX][oldY]);
				temp.add(board[newX][newY]);
			} else for (int i = oldX; i >= newX; i--) {
				temp.add(board[i][newY]);
			}
		else
			if (oldY != newY) {
				temp.add(board[oldX][oldY]);
				temp.add(board[newX][newY]);
			} else for (int i = oldX; i <= newX; i++) {
				temp.add(board[i][newY]);
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
			return "\u2659";
		case Black:
			return "\u265F";
		default:
			return default_name;
		}
	}
}