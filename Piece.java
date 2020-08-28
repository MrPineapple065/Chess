import java.util.Objects;

/**
 * This {@code Piece} class represents all pieces in the game of Chess.
 * 
 * @author MrPineapple065
 * @version 27 August 2020
 * @since 21 March 2020
 */
public abstract class Piece {
	/**
	 * Used when this has an invalid {@link #pieceColor}.
	 * @see #toString()
	 */
	protected static final String default_name = "\uFFFD";
	
	/**
	 * {@link PieceColor} to represent the {@code Piece} color.
	 */
	protected PieceColor pieceColor;
	
	/**
	 * Create a {@code Piece}.
	 * 
	 * @param color is {@link PieceColor} of this.
	 */
	protected Piece(PieceColor color){
		this.pieceColor	= Objects.requireNonNull(color, "Piece must have a color.");;
	}
	
	/**
	 * Determine if {@code piece} <i>collides</i> with other {@code piece} along its journey.
	 * 
	 * @param tilesCollide an {@code Array} of {@link Tile} a {@code piece} takes on its journey.
	 * 
	 * @return	{@code true} if {@code piece} collides. <br>
	 * 			{@code false} if {@code piece} does not collide.
	 */
	public static boolean collide(Tile[] tilesCollide) {		
		Objects.requireNonNull(tilesCollide, "Piece must move.");
		for (Tile tile : tilesCollide) {
			if (tile.getPiece() != null) return true;
		} return false;
	}
	
	/**
	 * Determine if the {@link King} can move out of a <b>check</b>.
	 * 
	 * @param board is the {@link ChessBoard}.
	 * @param tiles are the original and new positions of this.
	 * 
	 * @return	{@code true} if {@code King} can move. <br>
	 * 			{@code false} if {@code King} cannot move.
	 */
	public boolean canUnCheck(ChessBoard board, Tile[] tiles) {
		Objects.requireNonNull(board, "There must be a board.");
		Objects.requireNonNull(tiles, "Piece must have moved");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");
		
		Tile kingTile;
		try {
			kingTile = board.findKing(this, true);
		} catch (IllegalStateException ise) {
			throw ise;
		}
		
		Tile[][] b = board.getBoard(); //Board
		Tile tempKingTile;		//The Tile to check for check
		Piece tempKingTilePiece;//The piece on tempKingtile
		int row	= kingTile.getRow(), column = kingTile.getColumn();
		
		for (int x : new int[] {-1, 0, 1}) {
			for (int y : new int[] {-1, 0, 1}) {
				if (x == 0 && y == 0) continue;
				try {
					//set the tempKingTile to a surroundingTile.
					tempKingTile = b[row + x][column + y];
					tempKingTilePiece = tempKingTile.getPiece();
					if (!kingTile.getPiece().isAlly(tempKingTilePiece))
						if (!this.checkKing(b, tiles, tempKingTile)) return true;
				} catch (ArrayIndexOutOfBoundsException aiooe) {
					continue;
				}
			}
		} return false;
	}

	/**
	 * Determine if <i>any</i> move puts the {@link King} in <b>check</b>.
	 * 
	 * @param board is the board
	 * @param tiles are the original and new positions of {@link Piece}.
	 * @param kingTile is the {@link Tile} where the {@link King} is.
	 * 
	 * @return	{@code true} if a move puts the {@code King} in <b>check</b>. <br>
	 * 			{@code false} if a move does not put the {@code King} in <b>check</b>.
	 */
	public boolean checkKing(Tile[][] board, Tile[] tiles, Tile kingTile) {
		//Determine if a piece can move from its current position to the King
		if (this.getLegal(board, new Tile[]{tiles[1], kingTile}))
			return (!Piece.collide(this.setTileCollide(board, new Tile[] {tiles[1], kingTile})));
		return false;
	}
	
	/**
	 * Determine, when <i>any</i> {@link Piece} moves, if the {@link King} becomes <b>check</b>.<br>
	 * This ensures that a piece cannot move <i>away</i> from protecting the {@code King}.<br>
	 * This also ensures that the {@code King} cannot move itself into <b>check</b>.
	 * 
	 * @param board is the {@link ChessBoard}
	 * @param tiles are the original and new positions of {@code piece}.
	 * 
	 * @return	{@code true} if the {@code King} is <b>check</b>. <br>
	 * 			{@code false} if the {@code King} is <b>not check</b>.
	 */
	public boolean determineKingisCheck(ChessBoard board, Tile[] tiles) {
		//Store the original values of pieces in tiles.
		//if the new position is unsafe, the piece moves back to its original position.
		Piece tile0OrigPiece = this;
		Piece tile1OrigPiece = tiles[1].getPiece();
		
		//temporarily advance the Piece.
		tiles[1].setPiece(this);
		tiles[0].setPiece(null);
		
		//Determine if the King is moving.
		if (this instanceof King) {
			if(((King)this).determineCheck(this, board.getBoard(), tiles[1])) {
				tiles[0].setPiece(tile0OrigPiece);
				tiles[1].setPiece(tile1OrigPiece);
				return true;
			} else {
				tiles[0].setPiece(tile0OrigPiece);
				tiles[1].setPiece(tile1OrigPiece);
				return false;
			}
		}
		//Piece is not King.
		//Piece might be Pawn getting promoted.
		if (this instanceof Pawn) {
			int row = tiles[1].getRow();
			if (row == 7 || row == 0) {
				((Pawn)this).promote(tiles);
				tile0OrigPiece = tiles[1].getPiece();
				tile1OrigPiece = tiles[0].getPiece();
			}
		}
		
		Tile kingTile = board.findKing(this, false);
		King king = (King)kingTile.getPiece();
		if (king.determineCheck(king, board.getBoard(), kingTile)) {
			tiles[0].setPiece(tile0OrigPiece);
			tiles[1].setPiece(tile1OrigPiece);
			return true;
		} else {
			tiles[0].setPiece(tile0OrigPiece);
			tiles[1].setPiece(tile1OrigPiece);
			return false;
		}
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	/**
	 * Determine if this moving from {@code tiles[0]} to {@code tiles[1]} is a legal move.
	 * 
	 * @param board is the board.
	 * @param tiles are the original and new positions of {@code piece}
	 * 
	 * @return	{@code true} if a move is legal.<br>
	 * 			{@code false} if move is illegal.<br>
	 *  
	 * @throws	IllegalArgumentException if tiles does not have 2 elements.
	 */
	public abstract boolean getLegal(Tile[][] board, Tile[] tiles) throws IllegalArgumentException;

	/**
	 * @return {@link #pieceColor}.
	 */
	public PieceColor getPieceColor() {
		return this.pieceColor;
	}
	
	/**
	 * Get the value of this.
	 * 
	 * @return The value of this.
	 * 
	 * @throws IllegalAccessException if this is a {@link King}. 
	 */
	public abstract int getValue() throws IllegalAccessException;
	
	@Override
	public abstract int hashCode();
	
	/**
	 * Determine if a {@link Piece} is an <i>ally</i>.
	 * 
	 * @param piece is the {@code Piece} to compare.
	 * 
	 * @return	{@code true} if {@code Piece} is an <i>ally</i>.<br>
	 * 			{@code false} if {@code Piece} is not an <i>ally</i> or is null.
	 */
	public boolean isAlly(Piece piece) {
		if (piece == null) return false;
		return this.pieceColor == piece.getPieceColor();
	}
	
	/**
	 * Determine if this is {@link PieceColor#Black}
	 * 
	 * @return {@code true} if {@link PieceColor#Black}.<br>{@code false} otherwise.
	 */
	public boolean isBlack() {
		return PieceColor.Black == this.pieceColor;
	}
	
	/**
	 * Determine if this is {@link PieceColor#White}
	 * 
	 * @return {@code true} if {@link PieceColor#White}.<br>{@code false} otherwise.
	 */
	public boolean isWhite() {
		return PieceColor.White == this.pieceColor;
	}
	
	/**
	 * Determine if any {@code Piece} can <i>protect</i> the {@link King} from <b>check</b>
	 * 
	 * @param kingTile is the {@link Tile} the {@code King} is on.
	 * @param board is the board.
	 * @param tiles are the original and new position of {@code piece}. 
	 * 
	 * @return	{@code true} if a {@code piece} can <i>protect</i> the {@code King}. <br>
	 * 			{@code false} if a {@code piece} cannot <i>protect</i> the {@code King}.
	 */
	public boolean protect(Tile kingTile, Tile[][] board, Tile[] tiles) {
		Piece tempPiece;		//piece in the board
		King king = (King)kingTile.getPiece();
		Tile[] path = this.setTileCollide(board, new Tile[] {tiles[1], kingTile});
		
		//path is empty means that attacking piece is next to King
		if (path.length == 0) return true;
		
		for (Tile[] row : board) {	//Find a piece
			for (Tile tile : row) {
				tempPiece = tile.getPiece();
				if (tempPiece == null)			continue;
				if (tempPiece instanceof King)	continue;
				if (!tempPiece.isAlly(king))	continue;
				for (Tile tileCheck : path) {
					//determine if an ally can move into this path
					if (!tempPiece.getLegal(board, new Tile[] {tile, tileCheck})) continue;
					if (!Piece.collide(tempPiece.setTileCollide(board, new Tile[] {tileCheck, kingTile}))) return true;
				}
			}
		} return false;
	}
	
	/**
	 * Determine if {@code piece} moves to <i>protect</i> the {@link King} from <b>check</b>.
	 * 
	 * @param board is the board.
	 * @param tiles are the original and new positions of {@code piece}.
	 * 
	 * @return	{@code true} if {@code Piece} moves to <i>protect</i> the {@code King}.<br>
	 * 			{@code false} if {@code Piece} does not moves to <i>protect</i> the {@code King}.
	 */
	public boolean protecting(ChessBoard board, Tile[] tiles) {
		Tile[][] b = board.getBoard();
		//A non-King piece does the protecting
		if (!(this instanceof King)) {
			Tile[] tilesCollide = this.setTileCollide(b, new Tile[]{tiles[1], board.findKing(this, false)});
			for (Tile tile : tilesCollide) {
				if (this.getLegal(b, new Tile[]{tiles[1], tile})) return true;
			} return false;
		}
		
		/** 
		 * The {@code King} can always protect itself
		 * Determination if new location is safe is done elseware
		 * @see ChessBoard#kingHasBeenCheck(Piece, Tiles[])
		 */
		return true;
	}
	
	/**
	 * Makes all attributes, if any, to their default values.
	 */
	public abstract void reset();
	
	/**
	 * Determine all {@link Tile} from {@code tiles[0]} to {@code tiles[1]} that this travels over in its journey.
	 * 
	 * @param board is the board.
	 * @param tiles are the original and new position of the this.
	 * 
	 * @return	an Array of {@code Tile} that this will take along its journey.
	 * 
	 * @throws	IllegalArgumentException if {@code tiles} does not have 2 elements.
	 */
	public abstract Tile[] setTileCollide(Tile[][] board, Tile[] tiles) throws IllegalArgumentException;

	@Override
	public abstract String toString();
}