import java.awt.Color;

/**
 * This <code>King</code> class represents a
 * King in the game of Chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 *
 */
public class King extends Piece {
	/**
	 * boolean determining if <code>King</code> is <b>check</b>.
	 */
	private boolean isCheck;
	
	/**
	 * boolean determining if <code>King</code> has moved.
	 * 
	 * @see {@link #(King, Tile[][], Tile[])}
	 */
	private boolean hasMoved;
	
	/**
	 * Creates a <code>King</code> that is <code>color</code>.
	 * 
	 * @param color is the color of <code>King</code>
	 * 
	 * @throws IllegalClassFormatException if <code>color</code> is <code>null</code>
	 */
	public King(Color color) throws IllegalArgumentException {
		super(color);
		this.isCheck = false;
		this.hasMoved = false;
	}

	/**
	 * Determine if move <code>King</code> makes from <code>tiles[0]</code> to <code>tiles[1]</code> is legal.
	 * Like the <code>Queen</code> the <code>King</code> can move in any direction.</br>
	 * However, <code>King</code> can only move <b>one</b> <code>Tile</code> at a time.</br>
	 * 
	 * Checking if <code>King</code> has <b>castled</b> is done elsewhere.</br>
	 * See:&nbsp&nbsp&nbsp&nbsp&nbsp{@link #hasCastled(King, Tile[][], Tile[])}
	 * 
	 * @param tiles are the original and new positions of <code>King</code>.
	 * 
	 * @return	<code>true</code> if the move is legal. </br>
	 * 			<code>false</code> if the move is illegal.
	 */
	public static boolean getLegal(King king, Tile[] tiles) throws IllegalArgumentException {
		if (tiles == null) {
			throw new IllegalArgumentException("King must move.");
		}
		
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		if ((Math.abs(newX - oldX) <= 1) && (Math.abs(newY - oldY) <= 1)) {
			king.setHasMoved(true);
			return true;
		}
		return false;
	}
	
	/**
	 * Determine if <code>King</code> has castled
	 * 
	 * @param king is the King
	 * @param board is the board
	 * @param tiles is the original and new Posisions of <code>King</code>
	 * 
	 * @return	<code>true</code> if <code>King</code> has castled. </br>
	 * 			<code>false</code> if <code>King</code> has not castled.
	 * 
	 * @implNote actual castling is done in {@link #castle(Tile, Tile[], Tile[][])}.
	 */
	public static boolean hasCastled(King king, Tile[][] board, Tile[] tiles) {
		if (king.hasMoved()) {
			return false;
		}
		
		for (Tile tile : ChessBoard.findRooks(king, board, false)) {
			if (((Rook)tile.getPiece()).hasMoved()) {
				return false;
			}
		}
		
		int col0 = tiles[0].getColumn(), col1 = tiles[1].getColumn();
		
		if (tiles[0].getRow() == tiles[1].getRow()) {
			if (col0 < col1) {
				for (int i = col0 + 1; i < col1; i++) {
					if (board[tiles[0].getRow()][i].getPiece() != null) {
						break;
					}
				}
			}
			
			else if (col0 > col1) {
				for (int i = col1; i < col0; i++) {
					if (board[tiles[0].getRow()][i].getPiece() != null) {
						break;
					}
				}
			}
		}

		else {
			return false;
		}
		
		return Math.abs(col0 - col1) == 2;
	}
	
	/**
	 * Determine if the new <code>Tile</code> <code>King</code> moves to is safe.
	 * 
	 * @param piece is the piece.
	 * @param board is the board.
	 * @param kingTile is the position of <code>King</code>.
	 * 
	 * @return	<code>true</code> if new <code>Tile</code> is safe. </br>
	 * 			<code>false</code> if new <code>Tile</code> is unsafe.
	 * 
	 * @throws IllegalArgumentException if {@link #checkLineforCheck(Piece, Tile[][], Tile[], int, int)} throws its exception.
	 */
	public static boolean determineCheck(Piece piece, Tile[][] board, Tile kingTile) throws IllegalArgumentException {
		int[] dx = new int[] {1, 1, 0, -1, -1, -1, 0, 1}, dy = new int[] {0, -1, -1, -1, 0, 1, 1, 1};
		boolean[] dCheck = new boolean[8];
		
		//check each direction in a counterclock-wise direction (E NE N NW W SW S SE)
		for (int doF = 0; doF < 8; doF++) {
			dCheck[doF] = King.checkLineforCheck(piece, board, kingTile, dx[doF], dy[doF]);
		}
		
		for (boolean bool : dCheck) {
			if (bool) {
				return true;
			}
		}
		return King.determineKnight(board, kingTile);
	}
	
	/**
	 * Assist {@link #determineCheck(Piece, Tile[][], Tile[])} in determining its information.</br>
	 * Travel x by dx and y by dy (either -1, 0, or 1).</br>
	 * Check each <code>Tile</code> for <code>Piece</code> and determine if the piece can move back to the original position.
	 * 
	 * @param piece is the piece
	 * @param board is the board
	 * @param kingTile is positions of <code>King</code>
	 * @param dx is the horizantal direction of travel
	 * @param dy is the vertical direction of travel
	 * 
	 * @return	<code>true</code> if <code>Piece<code> found is an <i>enemy</i>.</br>
	 *			<code>false</code> if <code>Piece</code> found is an <i>ally</i> or <i>no piece</i>.
	 * 
	 * @throws IllegalArgumentException if {@link #getLegal(Piece, Tile[][], Tile[])} throws its exception.
	 */
	private static boolean checkLineforCheck(Piece piece, Tile[][] board, Tile kingTile, int dx, int dy) throws IllegalArgumentException {		
		int row = kingTile.getRow(), col = kingTile.getColumn();	//coordinates for King
		Piece pieceCheck = null;									//piece at checking coordinates
		
		while (pieceCheck == null) {								//Check in a direction for a piece
			row += dx; col += dy;
			try {
				pieceCheck = board[row][col].getPiece();
			}
			
			catch (ArrayIndexOutOfBoundsException aiooe) {	//No pieces exist outside of the board
				break;
			}
		}
		
		//determine if a piece is in this line
		try {
			//piece is an ally
			if (piece.isAlly(pieceCheck)) {
				return false;
			}
			
			else {
				return Piece.getLegal(pieceCheck, board, new Tile[]{board[row][col], board[kingTile.getRow()][kingTile.getColumn()]});
			}
		}
		
		catch (NullPointerException npe) {
			return false;
		}
	}
	
	/**
	 * Determine if <code>Knight</code> can <b>check</b> the <code>King</code>.
	 * 
	 * @param board is the board
	 * @param kingTile is the positions of <code>King</code>.
	 * 
	 * @return	<code>true</code> if <code>Knight</code> can <b>check</b> the <code>King</code>. </br>
	 * 			<code>false</code> if <code>Knight</code> is found.
	 * 
	 * @throws IllegalArgumentException if <code>board</code> or <code>tiles</code> are <code>null</code>.
	 */
	private static boolean determineKnight(Tile[][] board, Tile kingTile) throws IllegalArgumentException {
		int[] x = {-2,-1, 1, 2, 2, 1, -1, -2}, y = {1, 2, 2, 1, -1, -2, -2, -1};
		int row = kingTile.getRow(), col = kingTile.getColumn();
		
		//loop through all possible positions of a Knight
		for (int i = 0; i < 8; i++) {
			Piece piece;	//Piece at the position
			
			//Attempt to Find a Piece
			try {
				piece = board[row + x[i]][col + y[i]].getPiece();
				
				if (piece instanceof Knight) {
					if (! piece.getPieceColor().equals(kingTile.getPiece().getPieceColor())) {
						return true;
					}
				}
			}
			
			//Looking off the board
			catch (ArrayIndexOutOfBoundsException aioobe) {
				continue;
			}
		}
		return false;
	}
	
	/**
	 * Determine all <code>Tile</code> from <code>tiles[0]</code> to <code>tiles[1]</code> that <code>King</code> travels over in its journey.
	 * Since <code>King</code> can only move one <code>Tile</code> at a time, it is either capturing or moving.
	 * 
	 * @param piece is the piece
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>King</code>.
	 * 
	 * @return	an <code>Array</code> of <code>Tile<code> that the <code>King</code>
	 *			will take on its journey from <code>tiles[0]</code> to <code>tiles[1]</code>.
	 * 
	 * @throws IllegalArgumentException if any parameters are <code>null</code>;
	 */
	public static Tile[] setTilesCollide(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		try {
			King.iae(piece, board, tiles);
		}
		
		catch (IllegalArgumentException iae) {
			throw iae;
		}
		
		return new Tile[0];
	}
	
	/**
	 * Castle the <code>King</code>
	 * 
	 * @param tiles are the original and new posisitions of <code>King</code>
	 * @param board is the board
	 */
	public static void castle(Piece piece, Tile[] tiles, Tile[][] board) {
		Tile[] rooks = ChessBoard.findRooks(piece, board, false);
		
		/**King-side Castle*/
		if (tiles[0].getColumn() < tiles[1].getColumn()) {
			Tile newTile = board[tiles[1].getRow()][tiles[1].getColumn() - 1];
			newTile.setPiece(rooks[1].getPiece());
			newTile.update();
			
			rooks[1].setPiece(null);
			rooks[1].update();
		}
		
		/**Queen-side Caslte*/
		else {
			Tile newTile = board[tiles[1].getRow()][tiles[1].getColumn() + 1];
			newTile.setPiece(rooks[0].getPiece());;
			newTile.update();
			
			rooks[0].setPiece(null);
			rooks[0].update();
		}
		return;
		
	}
	
	/**
	 * Determine if any of its <code>parameters</code> are <code>null</code>.
	 * 
	 * @param piece is the piece
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>Piece</code>.
	 * 
	 * @throws IllegalArgumentException if any <code>parameters</code> are <code>null</code>.
	 */
	private static void iae(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		if (piece == null) {
			throw new IllegalArgumentException("There must be a King.");
		}
		
		/**if (! (piece instanceof Pawn)) {
			throw new IllegalArgumentException("Piece must be a King.");
		}*/
		
		if (board == null) {
			throw new IllegalArgumentException("There must be a board.");
		}
		
		if (tiles == null) {
			throw new IllegalArgumentException("King must move.");
		}
	}
	
	/**
	 * Determin if <code>King</code> is <b>checl<b>.
	 * 
	 * @return {@link #isCheck}.
	 */
	public boolean isCheck() {
		return this.isCheck;
	}
	
	/**
	 * Determine if <code>King</code> has moved.
	 *  
	 * @return	<code>true</code> if </code>King</code> has moved. </br>
	 * 			<code>false</code> if <code>King</code> has not moved.
	 */
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	
	/**
	 * Set <code>{@link #isCheck}</code> to <code>bool</code>.
	 * 
	 * @param bool is the new value of <code>{@link #isCheck}</code>;
	 */
	public void setIsCheck(boolean bool) {
		this.isCheck = bool;
	}
	
	/**
	 * Set <code>{@link #hasMoved}</code> to <code>bool</code>.
	 * 
	 * @param bool is the new value of <code>{@link #hasMoved}</code>.
	 */
	public void setHasMoved(boolean bool) {
		this.hasMoved = bool;
	}
	
	/**
	 * Reset the <code>King</code>.
	 */
	public void reset() {
		this.setIsCheck(false);
		this.setHasMoved(false);
	}
	
	/**
	 * @return <code>String</code> representation of <code>King</code>.
	 */
	@Override
	public String toString() {
		if (super.getPieceColor().equals(Piece.WHITE)) {
			return "\u2654";
		}
		return "\u265A";
	}
}