import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

/**
 * This </code>ChessBoard</code> class represents a chess board.
 * It includes all the <code>Tile</code> on the board
 * as well as all <code>Piece</code> on the board.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 */
class ChessBoard {
	/**
	 * A 2D {@link Arrays} containing all {@link Tile}.
	 */
	private Tile[][]		board;
	
	/**
	 * An {@link Arrays} of {@link Tile} containing the original and new positions of a {@link Piece}.
	 */
	private Tile[]			tiles;
	
	/**
	 * An {@link Arrays} of {@link Player} to play that game.
	 */
	private Player[]		players;
	
	/**
	 * The {@link ChessBoardPanel} holding <code>this</code>.
	 */
	private ChessBoardPanel boardPanel;
	
	/**
	 * The {@link Piece} attacking {@link King}
	 */
	private Piece attackPiece;
	
	/**
	 * A <code>boolean</code> used to determine if the game is over.
	 */
	private boolean			gameOver;
	
	/**
	 * An <code>int</code> keeping track of the current <code>Player</code>.
	 */
	private int				currentPlayer;
	
	/**
	 * An <code>int</code> keeping track of the next <code>Player</code>.
	 */
	private int				nextPlayer;
	
	/**
	 * Creates a <code>ChessBoard</code> with <code>Player[] p</code>
	 * and <code>ChessBoardPanel cb</code>.
	 * 
	 * @throws IllegalArgumentException if the <code>length</code> of
	 * <code>players</code> is greater than 2 or 
	 * if <code>bp</code> is <code>null</code>.
	 * 
	 * @param bp is the <code>JPanel</code> holding <code>this</code>.
	 * @param players is an <code>Array</code> containing all <code>Player</code>.
	 */
	ChessBoard(ChessBoardPanel bp, Player[] players) throws IllegalArgumentException {
		if (bp == null) {
			throw new IllegalArgumentException("There must be a ChessBoardPanel.");
		}
		
		else {
			this.boardPanel = bp;
		}
		
		if (players.length != 2) {
			throw new IllegalArgumentException("The number of Players is not 2.");
		}
		
		else {
			this.players	= players;
		}
		
		this.board			= new Tile[8][8];
		this.tiles			= new Tile[2];
		this.gameOver		= false;
		this.currentPlayer	= 0;
		this.nextPlayer		= 1;
		this.createBoard(); this.reset();
	}
	
	
	
	/**
	 * Return a 2D {@link Arrays} of all {@link Tile}.
	 * 
	 * @return {@link #board}.
	 */
	public Tile[][] getBoard() {
		return this.board;
	}
	
	/**
	 * Determine the {@link Tile} at (<code>row</code>, <code>col</code>).
	 * 
	 * @param row is the row to access
	 * @param col is the column to access
	 * 
	 * @return {@link #board}<code>[row][col]</code>
	 */
 	public Tile getTile(int row, int col) {
 		return this.board[row][col];
	}
	
 	/**
 	 * Determine the original and new position of any {@link Piece} movement.
 	 * 
 	 * @return {@link #tiles}.
 	 */
	public Tile[] getTiles() {
		return this.tiles;
	}
	
	/**
	 * Determine if the game if over.
	 * 
	 * @return <code>{@link #gameOver}</code>
	 */
	public boolean getGameOver() {
		return this.gameOver;
	}
	
	/**
	 * Determine the current {@link Player}.
	 * 
	 * @return <code>{@link#currentPlayer}</code>
	 */
	public int getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	/**
	 * Determine the next {@link Player}.
	 * 
	 * @return <code>{@link#nextPlayer}</code>
	 */
	public int getNextPlayer() {
		return this.nextPlayer;
	}
	
	
	
	/**
	 * Change {@link #gameOver} to <code>bool</code>.
	 * 
	 * @param bool is the new value of {@link #gameOver}.
	 */
	public void setGameOver(boolean bool) {
		this.gameOver = bool;
	}
	
	/**Empty {@link #tiles}.*/
	public void resetTiles() {
		Arrays.fill(this.tiles, null);
	}
	
	/**
	 * Assist {@link #movePiece()}
	 * 
	 * @param tile is a <code>Tile</code> clicked on.
	 */
	public void movePiece(Tile tile) {
		this.setTiles(tile);
			
		if (this.tiles[0] != null && this.tiles[1] != null) {
			this.movePiece();
			this.resetTiles();
		}
	}
		
	/**Reset the Game*/
	public void reset() {
		this.resetBoard();
		this.resetTiles();
		this.gameOver		= false;
		this.currentPlayer	= 0;
		this.nextPlayer		= 1;
	}
	
	/**
	 * remove {@link MouseListener} and {@link KeyListener} from {@link Tile}.
	 */
	public void removeTileListeners() {
		for (Tile[] row : this.board) {
			for (Tile tile : row) {
				tile.removeMouseListener(tile);
				tile.removeKeyListener(tile);
			}
		}
	}
	
	/**
	 * Move {@link Piece} from current {@link Tile} to new {@link Tile}.
	 * 
	 */
	private void movePiece() {
		Piece tile0Piece = this.tiles[0].getPiece(), tile1Piece = this.tiles[1].getPiece();
		
		/**Check if <i>White</i> tries to move <i>Black</i> and vice versa.*/
		if (this.players[this.currentPlayer].getPlayerColor().equals(tile0Piece.getPieceColor())) {
			
			/**Determine if move is legal*/
			if (Piece.getLegal(tile0Piece, this.board, this.tiles)) {
				
				/**Determine if {@link Pawn} moves away from <b>Ally</b> {@link King}.*/
				if (! Piece.determineKingisCheck(tile0Piece, this.board, this.tiles)) {
					
					/**A possible {@link Pawn} pomotion has took place and values need to be updated*/
					tile0Piece = this.tiles[0].getPiece(); tile1Piece = this.tiles[1].getPiece();
					Color tile1Color;
					
					try {
						tile1Color = tiles[1].getPiece().getPieceColor();
					}
					
					catch (NullPointerException npe) {
						tile1Color = null;
					}
					/**Determine if a {@link Pawn} <i>collides</i> with any other {@link Pawn}*/
					if (! Piece.collide(Piece.setTileCollide(tile0Piece, board, tiles))) {
						
						/**Capturing*/
						if (tile1Color != null) {
							
							/**Allies can't capture each other*/
							if (! tile1Piece.isAlly(tile0Piece)) {
								try {
									this.players[this.currentPlayer].increaseScore(Piece.getValue(tile1Piece));
								}
								
								catch (IllegalAccessException iae) {
									this.players[this.currentPlayer].increaseScore(0);;
								}
								
								this.tempAdvance();
								
								/**Determine if moving a <code>Piece</code> to capture another piece puts the king in check*/
								this.kingHasBeenCheck(tile0Piece, tiles);
							}
							
							else {
								JOptionPane.showMessageDialog(null, "You cannot capture Allies", "Careful!", JOptionPane.PLAIN_MESSAGE);
							}
						}
						
						/**Moving*/
						else {
							this.tempAdvance();
							this.kingHasBeenCheck(tile0Piece, tiles);
						}
					}
					
					else {
						JOptionPane.showMessageDialog(null, tile0Piece.toString() + " cannot jump!", "Careful!", JOptionPane.PLAIN_MESSAGE);
					}
					
				}
				
				else {
					JOptionPane.showMessageDialog(null, "This move will put the King in check!", "Careful!", JOptionPane.PLAIN_MESSAGE);
				}
			}
			
			else {
				JOptionPane.showMessageDialog(null, tile0Piece.toString() + " can not move like this!", "Careful!", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	/**
	 * Help determine if any move puts the
	 * opponent {@link King} in <b>check</b>.
	 * 
	 * @param piece that is moving.
	 * @param tiles are the original new positions of <code>piece</code>.
	 * 
	 * @throws	IllegalArgumentException if </br>
	 * 			{@link Piece#checkKing(Piece, Tile[][], Tile[], Tile)}, </br>
	 * 			{@link Piece#canUnCheck(Piece, Tile[][], Tile[])}, or </br>
	 * 			{@link Piece#protect(Piece, Tile, Tile[][], Tile[])} throw their exceptions.
	 * @throws	IllegalStateException if {@link #findKing(Piece, Tile[][], boolean)} throws its exception.
	 */
	private void kingHasBeenCheck(Piece piece, Tile[] tiles) {
		/**Store original <code>Piece</code> positions*/
		Piece tiles0OrigPiece = tiles[0].getPiece(), tiles1OrigPiece = tiles[1].getPiece();
		
		/**temporarily move <code>Piece</code>.*/
		tiles[1].setPiece(tiles0OrigPiece);
		tiles[0].setPiece(null);
		
		/**Find both <code>Kings</code>*/
		Tile opponentKingTile = ChessBoard.findKing(piece, board, true), allyKingTile = ChessBoard.findKing(piece, board, false);
		
		this.attackPiece = null;
		/**<code>Piece</code> has moved to check the Opponent's <code>King</code>.*/
		if (Piece.checkKing(piece, this.board, this.tiles, opponentKingTile)) {
			/**Determine if the <code>King</code> can move itself to safety*/
			if (! Piece.canUnCheck(piece, this.board, this.tiles)) {
				
				/**Determine if a <code>Piece</code> can move to protect the <code>King</code>*/
				if (Piece.protect(piece, opponentKingTile , this.board, this.tiles)) {
					JOptionPane.showMessageDialog(null, "Check!", "Check!", JOptionPane.INFORMATION_MESSAGE);
					attackPiece = piece;
					tiles[0].setPiece(tiles0OrigPiece);
					tiles[1].setPiece(tiles1OrigPiece);
					this.advance();
					this.gameOver = false;
				}
				
				/**No <code>Piece</code> can protect the <code>King</code>*/
				else {
					JOptionPane.showMessageDialog(null, "Check Mate!\n" + this.players[this.currentPlayer].getName() + ", wins!", "Check Mate", JOptionPane.INFORMATION_MESSAGE);
					tiles[0].setPiece(tiles0OrigPiece);
					tiles[1].setPiece(tiles1OrigPiece);
					this.advance();
					this.gameOver = true;
				}
			}
			
			else {
				JOptionPane.showMessageDialog(null, "Check!", "Check", JOptionPane.INFORMATION_MESSAGE);
				((King)(opponentKingTile.getPiece())).setIsCheck(true);
				attackPiece = piece;
				tiles[0].setPiece(tiles0OrigPiece);
				tiles[1].setPiece(tiles1OrigPiece);
				this.advance();
				this.gameOver = false;
			}
		}
		
		
		/**<code>Piece</code> has moved to protect the Ally's <code>King</code>*/
		else if (((King)(allyKingTile.getPiece())).isCheck() && attackPiece != null) {
			if (Piece.protecting(attackPiece, this.getBoard(), tiles)) {
				((King)(allyKingTile.getPiece())).setIsCheck(false);
				tiles[0].setPiece(tiles0OrigPiece);
				tiles[1].setPiece(tiles1OrigPiece);
				this.advance();
			}
			
			else {
				JOptionPane.showMessageDialog(null, "You must protect the King!", "Carefull!", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		/**<code>Piece</code> has moved*/
		else {
			tiles[0].setPiece(tiles0OrigPiece);
			tiles[1].setPiece(tiles1OrigPiece);
			this.advance();
		}
	}
	
	/**Move a {@link Piece} to its new position*/
	private void advance() {
		this.tiles[1].setPiece(this.tiles[0].getPiece());
		this.tiles[0].setPiece(null);
		
		this.tiles[0].update();
		this.tiles[1].update();
		
		this.currentPlayer	= (this.currentPlayer	+ 1) % 2;
		this.nextPlayer		= (this.nextPlayer		+ 1) % 2;
	}
	
	/**temporary moving*/
	private void tempAdvance() {
		this.tiles[1].setPiece(this.tiles[0].getPiece());
	}
	
	/**Reset the {@link ChessBoard}*/
	private void resetBoard() {
		for (Tile[] row : this.board) {
			for (Tile tile : row) {
				tile.setPiece(null);
				tile.setForeground(null);
				tile.setText(null);
			}
		}
		
		for (Player player : this.players) {
			player.setScore(0);
		}
		
		this.placePieces();
	}
	
	/**Place the {@link Piece} on {@link board}*/
	private void placePieces() {
		int[][] row		= new int[][]{{6,7}, {1,0}};	//Put piece at top or bottom of board
		int indexInList = 0;
		Tile tile;		//Ease of typing
		Piece piece;	//Ease of typing
		
		for (int player : new int[]{0, 1}) {						//Black and White players
			for (int i : row[player]) {								//Loop through their respective piece
				for (int j : new int[]{0, 1, 2, 3, 4, 5, 6, 7}) {	//the column to put piece in	
					tile = this.board[i][j];
					piece = this.players[player].getPieces()[indexInList];
					
					if (piece instanceof Pawn) {			//Set pawn firstMove to true
						((Pawn)(piece)).setFirstMove(false);
					}
					
					if (piece instanceof King) {			//Set king isCheck to false
						((King)(piece)).reset();
					}
					
					tile.setPiece(piece);							//Set piece on Tile
					tile.setText(tile.getPiece().toString());		//Set GUI elements
					tile.setForeground(players[player].getPlayerColor());
					
					indexInList ++;
				}
			}
			indexInList = 0;
		}
	}
	
	/**
	 * Add {@link Tile} to the {@link #board}.
	 */
	private void createBoard() {
		for (int row = 0; row < board.length; row ++) {
			for (int column = 0; column < board[row].length; column ++) {
				board[row][column] = new Tile(this.boardPanel, column, row);
			}
		}
	}
	
	/**Place a {@link Tile} clicked on in the first available place.*/
	private void setTiles(Tile tile) {
		if (this.tiles[0] == null) {
			if (tile.getPiece() == null) {
				return;
			}
			
			this.tiles[0] = tile;
		}
		
		else if (this.tiles[1] == null) {
			if (tile.equals(tiles[0])) {
				this.resetTiles();
			}
			
			else {
				this.tiles[1] = tile;
			}
		}
	}
	
	/**
	 * Find the {@link King} on the {@link board}.
	 * 
	 * @param piece is the piece
	 * @param board is the board
	 * @param opponent is if looking for <i>opponent</i> or <i>ally</i>.
	 * 
	 * @return the {@link Tile} that the {@link King} occupies.
	 * 
	 * @throws IllegalStateException if no {@link King} is found.
	 */
	public static Tile findKing(Piece piece, Tile[][] board, boolean opponent) throws IllegalStateException {
		for (Tile[] row : board) {
			for (Tile tile : row) {
				Piece tilePiece = tile.getPiece();
				if (tilePiece instanceof King) {
					if (piece.isAlly(tilePiece)) {
						if (! opponent) {
							return tile;
						}
					}
					
					else {
						if (opponent) {
							return tile;
						}
					}
				}
			}
		}
		throw new IllegalStateException("King must always be on the board.");
	}
	
	/**
	 * Find all {@link Rook} on the board
	 * 
	 * @param piece is the piece
	 * @param board is the board
	 * @param opponent is if looking for <i>opponent</i> or <i>ally</i>.
	 * 
	 * @return	an <code>Array</code> of {@link Tile} containing all {@link Rook} on {@link board}.
	 * 
	 * @implNote Note this also includes {@link Rook} promoted from {@link Pawn}.
	 */
	public static Tile[] findRooks(Piece piece, Tile[][] board, boolean opponent) {
		ArrayList<Tile> rooks = new ArrayList<Tile>(10);
		for (Tile[] row : board) {
			for (Tile tile : row) {
				Piece tilePiece = tile.getPiece();
				if (tilePiece instanceof Rook) {
					if (piece.isAlly(tilePiece)) {
						if (! opponent) {
							rooks.add(tile);
						}
					}
					
					else {
						if (opponent) {
							rooks.add(tile);
						}
					}
				}
			}
		}
		
		Tile[] rook = new Tile[rooks.size()];
		for (int i = 0; i < rook.length; i++) {
			rook[i] = rooks.get(i);
		}
		return rook;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attackPiece == null) ? 0 : attackPiece.hashCode());
		result = prime * result + Arrays.deepHashCode(board);
		result = prime * result + ((boardPanel == null) ? 0 : boardPanel.hashCode());
		result = prime * result + currentPlayer;
		result = prime * result + (gameOver ? 1231 : 1237);
		result = prime * result + nextPlayer;
		result = prime * result + Arrays.hashCode(players);
		result = prime * result + Arrays.hashCode(tiles);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChessBoard other = (ChessBoard) obj;
		if (attackPiece == null) {
			if (other.attackPiece != null)
				return false;
		} else if (!attackPiece.equals(other.attackPiece))
			return false;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		if (boardPanel == null) {
			if (other.boardPanel != null)
				return false;
		} else if (!boardPanel.equals(other.boardPanel))
			return false;
		if (currentPlayer != other.currentPlayer)
			return false;
		if (gameOver != other.gameOver)
			return false;
		if (nextPlayer != other.nextPlayer)
			return false;
		if (!Arrays.equals(players, other.players))
			return false;
		if (!Arrays.equals(tiles, other.tiles))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String str = "";
		
		for (Tile[] row : this.board) {
			for (Tile tile : row) {
				if (tile.getPiece() == null) {
					str += tile.toString() + "\t";
				}
				
				else {
					str += tile.getPiece() + "\t";
				}
			}
			
			str += "\n";
		}
		
		return str;
	}
}