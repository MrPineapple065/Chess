import java.awt.Color;
import java.util.Objects;

/**
 * This {@code King} class represents a King in the game of Chess. <br>
 * This is a subclass of {@link Piece}.
 * 
 * @version 28 August 2020
 * @since 16 March 2020
 * @author MrPineapple065
 */
public class King extends Piece {
	/**
	 * boolean determining if {@code King} has moved. <br>
	 * Used in {@link #getLegal(Tile[][], Tile[])}
	 */
	private boolean hasMoved;
	
	/**
	 * boolean determining if {@code King} is <b>check</b>.
	 */
	private boolean isCheck;
	
	/**
	 * Creates a {@code King} that is {@code code}.
	 * 
	 * @param color is the {@link Color} of {@code King}.
	 */
	public King(PieceColor color) {
		super(color);
		this.isCheck = false;
		this.hasMoved = false;
	}
	
	/**
	 * Castle the {@code King}.
	 * 
	 * @param piece is the {@code Piece} moving.
	 * @param tiles are the original and new posisitions of {@code King}.
	 * @param board is the board.
	 */
	public static void castle(Piece piece, Tile[] tiles, ChessBoard board) {
		Tile[] rooks = board.findRooks(piece, false);
		Tile[][] b = board.getBoard();
		if (tiles[0].getColumn() < tiles[1].getColumn()) {
			Tile newTile = b[tiles[1].getRow()][tiles[1].getColumn() - 1];
			newTile.setPiece(rooks[1].getPiece());
			newTile.update();
			
			rooks[1].setPiece(null);
			rooks[1].update();
		} else {
			Tile newTile = b[tiles[1].getRow()][tiles[1].getColumn() + 1];
			newTile.setPiece(rooks[0].getPiece());;
			newTile.update();
			
			rooks[0].setPiece(null);
			rooks[0].update();
		} return;
	}
	
	/**
	 * <p>Assist {@link #determineCheck(Piece, Tile[][], Tile)} in determining its information.</p>
	 * <p>Travel x by dx and y by dy (either -1, 0, or 1).<br>
	 * Check each {@link Tile} for {@link Piece} and determine if the piece can move back to the original position.</p>
	 * 
	 * @param piece is the piece
	 * @param board is the board
	 * @param kingTile is positions of {@code King}
	 * @param dx is the horizantal direction of travel
	 * @param dy is the vertical direction of travel
	 * 
	 * @return	{@code true} if {@code Piece} found is an <i>enemy</i>.<br>
	 *			{@code false} if {@code Piece} found is an <i>ally</i> or <i>no piece</i>.
	 */
	private boolean checkLineforCheck(Piece piece, Tile[][] board, Tile kingTile, int dx, int dy) {		
		int row = kingTile.getRow(), col = kingTile.getColumn();	//coordinates for King
		Piece pieceCheck = null;									//piece at checking coordinates
		while (pieceCheck == null) {								//Check in a direction for a piece
			row += dx; col += dy;
			try {
				pieceCheck = board[row][col].getPiece();
			} catch (ArrayIndexOutOfBoundsException aiooe) {		//No pieces exist outside of the board
				return false;
			}
		} if (piece.isAlly(pieceCheck)) return false;
		return pieceCheck.getLegal(board, new Tile[]{board[row][col], kingTile});
	}
	
	/**
	 * Determine if the new {@link Tile} {@code King} moves to is safe.
	 * 
	 * @param piece is the piece.
	 * @param board is the board.
	 * @param kingTile is the position of {@code King}.
	 * 
	 * @return	{@code true} if new {@code Tile} is safe. <br>
	 * 			{@code false} if new {@code Tile} is unsafe.
	 */
	public boolean determineCheck(Piece piece, Tile[][] board, Tile kingTile) {
		int[] dx = {1, 1, 0, -1, -1, -1, 0, 1}, dy = {0, -1, -1, -1, 0, 1, 1, 1};
		
		//check each direction in a counterclock-wise direction (E NE N NW W SW S SE)
		for (int doF = 0; doF < 8; doF++) {
			if (this.checkLineforCheck(piece, board, kingTile, dx[doF], dy[doF])) return true;
		} return this.determineKnight(board, kingTile);
	}
	
	/**
	 * Determine if {@link Knight} can <b>check</b> the {@code King}.
	 * 
	 * @param board is the board
	 * @param kingTile is the positions of {@code King}.
	 * 
	 * @return	{@code true} if {@code Knight} can <b>check</b> the {@code King}. <br>
	 * 			{@code false} if {@code Knight} is found.
	 */
	private boolean determineKnight(Tile[][] board, Tile kingTile) {
		Objects.requireNonNull(board, "King must be on a board.");
		Objects.requireNonNull(kingTile, "King must be on a Tile.");
		int[] x = {-2,-1, 1, 2, 2, 1, -1, -2}, y = {1, 2, 2, 1, -1, -2, -2, -1};
		int row = kingTile.getRow(), col = kingTile.getColumn();
		
		//loop through all possible positions of a Knight
		for (int i = 0; i < 8; i++) {
			Piece piece;	//Piece at the position
			try {
				piece = board[row + x[i]][col + y[i]].getPiece();
				if (piece instanceof Knight && !piece.isAlly(this)) return true;
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				continue;
			}
		} return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)				return true;
		if (!(obj instanceof King))		return false;
		King other = (King) obj;
		if (hasMoved != other.hasMoved)	return false;
		if (isCheck != other.isCheck)	return false;
										return true;
	}
	
	/**
	 * <p>Determine if move {@code King} makes from {@code tiles[0]} to {@code tiles[1]} is legal.</p>
	 * <p>Like the {@link Queen} the {@code King} can move in any direction.<br>
	 * However, {@code King} must only move <b>one</b> {@link Tile} at a time.</p>
	 * 
	 * <p>Checking if {@code King} has <b>castled</b> is done in {@link #hasCastled(ChessBoard, Tile[])}.</p>
	 */
	@Override
	public boolean getLegal(Tile[][] board, Tile[] tiles) {
		Objects.requireNonNull(tiles, "King must move");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		if ((Math.abs(newX - oldX) <= 1) && (Math.abs(newY - oldY) <= 1)) {
			this.setHasMoved(true);
			return true;
		} return false;
	}
	
	@Override
	public int getValue() throws IllegalAccessException {
		throw new IllegalAccessException("King does not have a value.");
	}
	
	
	/**
	 * Determine if {@code King} has castled.<br>
	 * Actual castling is done in {@link #castle(Piece, Tile[], ChessBoard)}.
	 * 
	 * @param board is the {@link ChessBoard}.
	 * @param tiles is the original and new posisions of this.
	 * 
	 * @return	{@code true} if {@code King} has castled.<br>
	 * 			{@code false} if {@code King} has not castled.
	 */
	public boolean hasCastled(ChessBoard board, Tile[] tiles) {
		if (this.hasMoved()) return false;
		for (Tile tile : board.findRooks(this, false)) {
			if (((Rook)tile.getPiece()).hasMoved())
				return false;
		} int col0 = tiles[0].getColumn(), col1 = tiles[1].getColumn();
		
		Tile[][] b = board.getBoard();
		if (tiles[0].getRow() == tiles[1].getRow())
			if (col0 < col1)
				for (int i = col0 + 1; i < col1; i++) {
					if (b[tiles[0].getRow()][i].getPiece() != null) return false;
				}
			else if (col0 > col1)
				for (int i = col1; i < col0; i++) {
					if (b[tiles[0].getRow()][i].getPiece() != null) return false;
				}
		else return false;
		return Math.abs(col0 - col1) == 2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hasMoved ? 1231 : 1237);
		result = prime * result + (isCheck ? 1231 : 1237);
		return result;
	}
	
	/**
	 * @return {@link #hasMoved}
	 */
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	/**
	 * @return {@link #isCheck}.
	 */
	public boolean isCheck() {
		return this.isCheck;
	}

	@Override
	public void reset() {
		this.isCheck = false;
		this.hasMoved = false;
	}

	/**
	 * Set {@link #hasMoved} to {@code bool}.
	 * 
	 * @param bool is the new value of {@code hasMoved}
	 */
	public void setHasMoved(boolean bool) {
		this.hasMoved = bool;
	}
	
	/**
	 * Set {@link #isCheck} to {@code bool}.
	 * 
	 * @param bool is the new value of {@link #isCheck};
	 */
	public void setIsCheck(boolean bool) {
		this.isCheck = bool;
	}

	@Override
	public Tile[] setTileCollide(Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		return new Tile[0];
	}

	@Override
	public String toString() {
		switch (this.pieceColor) {
		case White:
			return "\u2654";
		case Black:
			return "\u265A";
		default:
			return default_name;
		}
	}
}