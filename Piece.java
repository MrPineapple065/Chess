import java.awt.Color;

/**
 * This <code>Piece</code> class represents
 * all pieces in the game of Chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 */
public class Piece {
	/**
	 * <code>Color</code> to represent the <i>White <code>Player</code></i>.
	 */
	public static final Color WHITE		= new Color(0xD3D3D3);
	
	/**
	 * <code>Color</code> to represent the <i>Black <code>Player</code></i>
	 */
	public static final Color BLACK 	= new Color(0x565352);
	
	/**
	 * <code>Color</code> to represent the <code>Piece</code> color.
	 */
	private Color pieceColor;	//the color of the piece
	
	/**
	 * Create a <code>Piece</code> that is <code>color</code>.
	 * 
	 * @param color is color of <code>this</code>
	 * 
	 * @throws IllegalArgumentException if <code>color</code> is <code>null</code>
	 */
	public Piece(Color color) throws IllegalArgumentException {
		if (color == null) {
			throw new IllegalArgumentException("Piece must have a color");
		}
		
		if (! (color.equals(WHITE)) && ! (color.equals(BLACK))) {
			throw new IllegalArgumentException("Color must be " + WHITE.toString() + " or " + BLACK.toString());
		}
		
		this.pieceColor	= color;
	}
	
	/**
	 * Get the score if each piece's respective value
	 * 
	 * @param piece to get score for.
	 * @return <code>int</code> that is the <code>Piece</code> score.
	 * 
	 * @throws IllegalAccessException if <code>piece</code> is <code>instanceof</code> <code>King</code>. 
	 */
	public static int getValue(Piece piece) throws IllegalAccessException {
		if (piece instanceof Pawn) {
			return Pawn.getValue();
		}
		
		else if (piece instanceof Knight) {
			return Knight.getValue();
		}
		
		else if (piece instanceof Bishop) {
			return Bishop.getValue();
		}
		
		else if (piece instanceof Rook) {
			return Rook.getValue();
		}
		
		else if (piece instanceof Queen) {
			return Queen.getValue();
		}
		
		throw new IllegalAccessException("The King does not have a value");
	}
	
	/**
	 * Determine if <code>piece</code> moving from <code>tiles[0]</code>
	 * to <code>tiles[0]</code> is a legal move.
	 * 
	 * @param piece that is moving.
	 * @param board is the board.
	 * @param tiles that holds original position and new position.
	 * 
	 * @return	<code>false</code> if move is illegal. </br>
	 * 			<code>true</code> if move is legal.
	 *  
	 * @throws	IllegalArgumentException if </br>
	 * 			{@link Pawn#getLegal(Pawn, Tile[][], Tile[])}, </br> 
	 * 			{@link Bishop#getLegal(Tile[])}, </br>
	 * 			{@link Rook#getLegal(Rook, Tile[])}, </br>
	 * 			{@link Knight#getLegal(Tile[])}, </br>
	 * 			{@link Queen#getLegal(Tile[])}, and </br>
	 * 			{@link King#getLegal(King, Tile[])} throw their exceptions
	 * 
	 */
	public static boolean getLegal(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		/**Determine the type of <code>Piece</code>*/
		if (piece instanceof Pawn) {
			return Pawn.getLegal((Pawn)piece, board, tiles);
		}
		
		else if (piece instanceof Knight) {
			return Knight.getLegal(tiles);
		}
		
		else if (piece instanceof Bishop) {
			return Bishop.getLegal(tiles);
		}
		
		else if (piece instanceof Rook) {
			return Rook.getLegal((Rook)piece, tiles);
		}
		
		else if (piece instanceof Queen) {
			return Queen.getLegal(tiles);
		}
		
		else if (!King.getLegal((King)piece, tiles)) {
			if (King.hasCastled((King)piece, board, tiles)) {
				King.castle(piece, tiles, board);
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Determine if <code>piece</code> <i>collides</i> with other <code>piece</code> along its journey.
	 * 
	 * @param tilesCollide an <code>Array</code> of <code>Tile</code> a </code>Piece</code> takes on its journey.
	 * 
	 * @return	<code>true</code> if <code>piece</code> collides. </br>
	 * 			<code>false</code> if <code>piece</code> does not collide.
	 * 
	 * @throws IllegalArgumentException if <code>tilesCollide</code> is <code>null</code>.
	 */
	public static boolean collide(Tile[] tilesCollide) throws IllegalArgumentException {
		if (tilesCollide == null) {
			throw new IllegalArgumentException("Piece must move.");
		}
		
		for (Tile tile : tilesCollide) {
			if (tile.getPiece() != null) {	//find a piece in this list
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determine if the <code>King</code> can move out of a <b>check</b>.
	 * 
	 * @param piece that is <i>moving</i>.
	 * @param board is the board.
	 * @param tiles are the original and new positions of <code>piece</code>.
	 * 
	 * @return	<code>true</code> if <code>King</code> can move. </br>
	 * 			<code>false</code> if <code>King</code> cannot move.
	 *  
	 * @throws IllegalArgumentException if {@link #checkKing(Piece, Tile[][], Tile[], Tile)} throws its exception
	 * @throws IllegalStateException if ChessBoard.findKing(Piece, Tile[][], boolean) throws its exception
	 */
	public static boolean canUnCheck(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException, IllegalStateException {
		Tile kingTile;
		try {
			kingTile = ChessBoard.findKing(piece, board, true);	//King Tile
		}
		
		catch (IllegalStateException ise) {
			throw ise;
		}
		
		Tile tempKingTile;									//The Tile to check for check
		Piece tempKingTilePiece;
		boolean[] check = new boolean[9];					//The list to return
		int row		= kingTile.getRow(), column = kingTile.getColumn();
		int index = 0;										//index in check
		
		for (int x : new int[] {-1, 0, 1}) {
			for (int y : new int[] {-1, 0, 1}) {
				try {
					/**set the <code>tempKingTile</code> to a surrounding <code>Tile</code>*/
					tempKingTile = board[row + x][column + y];
					tempKingTilePiece = tempKingTile.getPiece();
					
					/**A <code>Piece</code> is on a potential tile to move to*/
					if (tempKingTilePiece != null) {
						
						//Determine if this new tile has an Ally or an Enemy
						if (! tempKingTilePiece.getPieceColor().equals(kingTile.getPiece().getPieceColor())) {
							//tile has an enemy piece on it
							check[index] = ! Piece.checkKing(piece, board, tiles, tempKingTile);
						}
						
						else {
							//tile has an ally piece on it
							check[index] = false;
						}
					}
					
					else {
						//If a piece can attack this new tile add false
						//else add true
						check[index] = ! Piece.checkKing(piece, board, tiles, tempKingTile);
					}
				}
				
				catch (ArrayIndexOutOfBoundsException aiooe) {
					check[index] = false;	//can't move off the board
				}	
				index++;
			}
		}
		
		check[5] = false; //the king doesn't move from original position.
		
		//return if there is a true in the list
		for (boolean element : check) {
			if (element) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determine if any <code>Piece</code> can <i>protect</i> the <code>King</code> from <b>check</b>
	 * 
	 * @param piece is the piece protecting.
	 * @param kingTile is the <code>Tile</code> the <code>King</code> is on.
	 * @param board is the board.
	 * @param tiles are the original and new position of <code>Piece</code>. 
	 * 
	 * @return	<code>true</code> if a Piece can <i>protect</i> the <code>King</code>. </br>
	 * 			<code>false</code> if a Piece cannot <i>protect</i> the <code>King</code>.
	 * 
	 * @throws	IllegalArgumentException if {@link #setTileCollide(Piece, Tile[][], Tile[])},
	 * 			{@link #getLegal(Piece, Tile[][], Tile[])}, or {@link #collide(Tile[])} throw their exceptions
	 */
	public static boolean protect(Piece piece, Tile kingTile, Tile[][] board, Tile[] tiles) {
		Piece tempPiece;		//piece in the board
		King king = (King)kingTile.getPiece();
		Tile[] path = Piece.setTileCollide(piece, board, new Tile[] {tiles[1], kingTile});
		
		for (Tile[] row : board) {	//Find an piece
			for (Tile tile : row) {
				tempPiece = tile.getPiece();
				
				if (tempPiece != null) {
					//Determine if the piece is the same color as oponent King
					if (tempPiece.getPieceColor().equals(king.getPieceColor())) {
						//path is empty means that attacking piece is next to King
						if (path.length == 0) {
							return true;
						}
						
						else {
						//check all tiles from attacking piece to King to see if any piece can move there
							for (Tile tileCheck : path) {
								//determine if an ally can move into this path
								if (Piece.getLegal(tempPiece, board, new Tile[]{tile, tileCheck})) {
									//determine if the movement causes colliding
									if (! Piece.collide(Piece.setTileCollide(tempPiece, board, new Tile[] {tileCheck, kingTile}))) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Determine if a piece moves to protect the King from check
	 * 
	 * @param piece is the piece protecting.
	 * @param board is the board.
	 * @param tiles are the original and new positions of <code>piece</code>.
	 * 
	 * @return	<code>true</code> if <code>Piece</code> moves to <i>protect</i> the <code>King</code>. </br>
	 * 			<code>false</code> if <code>Piece</code> does not moves to <i>protect</i> the <code>King</code>.
	 * 
	 * @throws	IllegalArgumentException if {@link #setTileCollide(Piece, Tile[][], Tile[])} or
	 * 			{@link #getLegal(Piece, Tile[][], Tile[])} throw their exceptoins.
	 */
	public static boolean protecting(Piece piece, Tile[][] board, Tile[] tiles) {
		//A non-King piece does the protecting
		if (! (piece instanceof King)) {
			Tile[] tilesCollide = Piece.setTileCollide(piece, board, new Tile[]{tiles[1], ChessBoard.findKing(piece, board, false)});
			boolean[] canProtect = new boolean[tilesCollide.length];
			int index = 0;
			for (Tile tile : tilesCollide) {
				canProtect[index] = Piece.getLegal(piece, board, new Tile[]{tiles[1], tile});
				index++;
			}
			
			for (boolean isProtect : canProtect) {
				if (isProtect) {
					return true;
				}
			}
			return false;
		}
		
		/** 
		 * The <code>King</code> can always protect itself
		 * Determination if new location is safe is done elseware
		 * @see #ChessBoard.kingHasBeenCheck(Piece, Tiles[])
		 */
		return true;
	}

	/**
	 * Determine, when <i>any</i> {@link Piece} moves, if the {@link King} becomes <b>check</b>.
	 * 
	 * @param piece is the piece moving
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>Piece</code>.
	 * 
	 * @return	<code>true</code> if the <code>King</code> is <b>check</b>. </br>
	 * 			<code>false</code> if the <code>King</code> is <b>not check</b>.
	 *
	 * @throws IllegalArgumentException if {@link King#determineCheck(Piece, Tile[][], Tile[])} throws its exception.
	 * @throws IllegalStateException if {@link #findKing(Piece, Tile[][], boolean)} throws its exception.
	 */
	public static boolean determineKingisCheck(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException, IllegalStateException {
		//Store the original values of pieces in tiles
		//if the new position is unsafe, the piece moves back to its original position
		Piece tile0OrigPiece = tiles[0].getPiece();
		Piece tile1OrigPiece = tiles[1].getPiece();
		
		//temporarily advance the Piece
		tiles[1].setPiece(tiles[0].getPiece());
		tiles[0].setPiece(null);
		
		//Determine if the King is moving
		if (piece instanceof King) {
			if(King.determineCheck(piece, board, tiles[1])) {
				tiles[0].setPiece(tile0OrigPiece);
				tiles[1].setPiece(tile1OrigPiece);
				return true;
			}
			
			else {
				tiles[0].setPiece(tile0OrigPiece);
				tiles[1].setPiece(tile1OrigPiece);
				return false;
			}
		}
		
		/**<code>Piece</code> is not <code>King</code>.*/
		
		/**<code>Piece</code> might be <code>Pawn</code> getting promoted*/
		if (tiles[1].getPiece() instanceof Pawn) {
			int row = tiles[1].getRow();
			if (row == 7 || row == 0) {
				((Pawn)tiles[1].getPiece()).promote(tiles);
				tile0OrigPiece = tiles[0].getPiece();
				tile1OrigPiece = tiles[1].getPiece();
				piece = tile1OrigPiece;
			}
		}
		
		Tile kingTile = ChessBoard.findKing(piece, board, false);
		if (King.determineCheck(kingTile.getPiece(), board, kingTile)) {
			tiles[0].setPiece(tile0OrigPiece);
			tiles[1].setPiece(tile1OrigPiece);
			return true;
		}
		
		else {
			tiles[0].setPiece(tile0OrigPiece);
			tiles[1].setPiece(tile1OrigPiece);
			return false;
		}
	}
	
	/**
	 * Determine if <i>any</i> move puts the <code>King</code> in <b>check</b>.
	 * 
	 * @param piece is the piece moving
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>Piece</code>.
	 * @param kingTile is the <code>Tile</code> where the <code>King</code> is.
	 * 
	 * @return	<code>true</code> if a move puts the <code>King</code> in <b>check</b>. </br>
	 * 			<code>false</code> if a move does not put the <code>King</code> in <b>check</b>.
	 * 
	 * @throws	IllegalArgumentException if {@link #getLegal(Piece, Tile[][], Tile[])},
	 * 			{@link #collide(Tile[])}, or
	 * 			{@link #setTileCollide(Piece, Tile[][], Tile[])} throws their exceptions.
	 */
	public static boolean checkKing(Piece piece, Tile[][] board, Tile[] tiles, Tile kingTile) throws IllegalArgumentException {
		//Determine if a piece can move from its current position to the King
		if (Piece.getLegal(piece, board, new Tile[]{tiles[1], kingTile})) {
			
			//Determine if other pieces block its path
			return (!Piece.collide(Piece.setTileCollide(piece, board, new Tile[] {tiles[1], kingTile})));
		}
		
		return false;
	}
	
	//set tileCollide and return tileCollide
	/**
	 * Determine all <code>Tile</code> that <code>Piece</code> takes from
	 * <code>tiles[0]</code> to <code>tiles[1]</code>.
	 * 
	 * @param piece is a piece.
	 * @param board is the board.
	 * @param tiles are the original and new position of the <code>piece</code>.
	 * 
	 * @return	an <code>Array</code> of <code>Tile</code> containing
	 * 			all <code>Tile<code> from <code>tiles[0]</code> to <code>tiles[1]</code>.
	 * 
	 * @throws IllegalArgumentException if any subclass.{@link #setTileCollide(Piece, Tile[][], Tile[])} throw their exceptions.
	 */
	public static Tile[] setTileCollide(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException{
		if (piece instanceof Pawn) {
			return Pawn.setTilesCollide(piece, board, tiles);
		}
		
		else if (piece instanceof Knight) {
			return Knight.setTilesCollide();
		}
		
		else if (piece instanceof Bishop) {
			return Bishop.setTilesCollide(piece, board, tiles);
		}
		
		else if (piece instanceof Rook) {
			return Rook.setTilesCollide(piece, board, tiles);
		}
		
		else if (piece instanceof Queen) {
			return Queen.setTilesCollide(piece, board, tiles);
		}
		
		return King.setTilesCollide(piece, board, tiles);
	}
	
	/**
	 * Determine if a <code>Piece</code> is an <i>ally</i>.
	 * 
	 * @param piece is the piece to compare.
	 * 
	 * @return	<code>true</code> if <code>piece</code> is an <i>ally</i>.</br>
	 * 			<code>false</code> if <code>piece</code> is not an <i>ally</i>.
	 */
	public boolean isAlly(Piece piece) {
		return this.getPieceColor().equals(piece.getPieceColor());
	}
	
	/**
	 * Determine the <code>Color</code> of <code>this</code>.
	 * 
	 * @return {@link #pieceColor}.
	 */
	public Color getPieceColor() {
		return this.pieceColor;
	}
}