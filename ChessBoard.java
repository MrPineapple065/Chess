import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.swing.JOptionPane;

/**
 * This {@code ChessBoard} class represents a chess board. <br>
 * It includes all the {@link Tile} on the board as well as all {@link Piece} on the board.
 * 
 * @version 28 August 2020
 * @since 21 March 2020
 * @author MrPineapple065
 */
public final class ChessBoard {
	/**
	 * A 2D array containing all {@link Tile}.
	 */
	private final Tile[][] board;
	
	/**
	 * An array of {@link Tile} containing the original and new positions of a {@link Piece}.
	 */
	private final Tile[] tiles;
	
	/**
	 * An array of {@link Player} to play that game.
	 */
	private final Player[] players;
	
	/**
	 * The {@link ChessBoardPanel} holding this.
	 */
	private final ChessBoardPanel boardPanel;
	
	/**
	 * The {@link Piece} attacking {@link King}
	 */
	private Piece attackPiece;
	
	/**
	 * A {@code boolean} used to determine if the game is over.
	 */
	private boolean	gameOver;
	
	/**
	 * A reference to the current {@link Player}.
	 */
	private Player currentPlayer;
	
	/**
	 * A reference to the next {@link Player}.
	 */
	private Player nextPlayer;
	
	/**
	 * An {@code int} keeping track of the current {@link Player}.
	 */
	private int	currentIndex;
	
	/**
	 * An {@code int} keeping track of the next {@link Player}.
	 */
	private int	nextIndex;
	
	/**
	 * Creates a {@code ChessBoard} with {@code players} and {@code bp}.
	 * 
	 * @param bp is the {@link ChessBoardPanel} holding this.
	 * @param players is an {@code Array} containing all {@link Player}.
	 * 
	 * @throws IllegalArgumentException if the {@code length} of {@code players} is not 2. 
	 * @throws NullPointerException if {@code bp} is null or {@code players} is null.
	 */
	ChessBoard(ChessBoardPanel bp, Player[] players) throws IllegalArgumentException, NullPointerException {
		this.boardPanel = Objects.requireNonNull(bp, "ChessBoard must be on a ChessBoardPanel.");
		Objects.requireNonNull(players, "There must be players playing.");
		if (players.length != 2) throw new IllegalArgumentException("The number of Players is not 2.");
		this.players	= players;
		this.board	= new Tile[8][8];
		this.tiles	= new Tile[2];
		this.createBoard(); this.reset();
	}
	
	/**
	 * Find the {@link King} on the {@link board}.
	 * 
	 * @param piece is the {@link Piece} used to find ally or enemy {@code King}.
	 * @param opponent	{@code true} for opponent. {@code false} for ally.
	 * 
	 * @return the {@link Tile} that the {@code King} occupies.
	 * 
	 * @throws IllegalStateException if no {@code King} is found.
	 */
	public Tile findKing(Piece piece, boolean opponent) throws IllegalStateException {
		Objects.requireNonNull(piece, "piece must be nonnull.");
		for (Tile[] row : this.board) {
			for (Tile tile : row) {
				Piece tilePiece = tile.getPiece();
				if (!(tilePiece instanceof King)) continue;
				if (!opponent && piece.isAlly(tilePiece))		return tile;
				else if (opponent && !piece.isAlly(tilePiece))	return tile;
			}
		} throw new IllegalStateException("King must always be on the board.");
	}
	
	/**
	 * Find all {@link Rook} on the board.<br>
	 * This also includes {@code Rook} that have been promoted from {@link Pawn}.
	 * 
	 * @param piece is the {@link Piece} used to find ally or enemy {@code Rook}.
	 * @param opponent	{@code true} for opponent. {@code false} for ally.
	 * 
	 * @return	an {@code Array} of {@link Tile} containing all {@code Rook} on {@code board}.
	 */
	public Tile[] findRooks(Piece piece, boolean opponent) {
		Objects.requireNonNull(piece, "piece must be nonnull.");
		List<Tile> rooks = new ArrayList<Tile>(10);
		for (Tile[] row : this.board) {
			for (Tile tile : row) {
				Piece tilePiece = tile.getPiece();
				if (!(tilePiece instanceof Rook))				continue;
				if (!opponent && piece.isAlly(tilePiece))		rooks.add(tile);
				else if (opponent && !piece.isAlly(tilePiece))	rooks.add(tile);
			}
		} return rooks.toArray(new Tile[rooks.size()]);
	}
 	
 	/**Move a {@link Piece} to its new position and update GUI*/
	private void advance() {
		this.tiles[1].setPiece(this.tiles[0].getPiece());
		this.tiles[0].setPiece(null);
		this.tiles[0].update();
		this.tiles[1].update();
		
		this.currentIndex	= ++this.currentIndex % 2;
		this.nextIndex		= ++this.nextIndex % 2;
		this.currentPlayer	= this.players[this.currentIndex];
		this.nextPlayer		= this.players[this.nextIndex];
	}
 	
 	/**
	 * Initialize and add {@link Tile} to the {@link #board}.
	 */
	private void createBoard() {
		for (int row = 0; row < this.board.length; row ++) {
			for (int column = 0; column < this.board[row].length; column ++) {
				this.board[row][column] = new Tile(this.boardPanel, column, row);
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)											return true;
		if (!(obj instanceof ChessBoard))							return false;
		ChessBoard other = (ChessBoard) obj;
		if (attackPiece == null) if (other.attackPiece != null)		return false;
		else if (!attackPiece.equals(other.attackPiece))			return false;
		if (!Arrays.deepEquals(board, other.board))					return false;
		if (boardPanel == null) if (other.boardPanel != null)		return false;
		else if (!boardPanel.equals(other.boardPanel))				return false;
		if (currentIndex != other.currentIndex)						return false;
		if (currentPlayer == null) if (other.currentPlayer != null)	return false;
		else if (!currentPlayer.equals(other.currentPlayer))		return false;
		if (gameOver != other.gameOver)								return false;
		if (nextIndex != other.nextIndex)							return false;
		if (nextPlayer == null) if (other.nextPlayer != null)		return false;
		else if (!nextPlayer.equals(other.nextPlayer))				return false;
		if (!Arrays.equals(players, other.players))					return false;
		if (!Arrays.equals(tiles, other.tiles))						return false;
		return true;
	}
	
	/**
	 * @return {@link #board}.
	 */
	public Tile[][] getBoard() {
		return this.board;
	}
	
	/**
	 * @return {@link #gameOver}
	 */
	public boolean getGameOver() {
		return this.gameOver;
	}
	
	/**
	 * @return {@link #currentPlayer}
	 */
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	/**
	 * @return {@link #nextPlayer}
	 */
	public Player getNextPlayer() {
		return this.nextPlayer;
	}
	
	/**
 	 * @return {@link #tiles}.
 	 */
	public Tile[] getTiles() {
		return this.tiles;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attackPiece == null) ? 0 : attackPiece.hashCode());
		result = prime * result + Arrays.deepHashCode(board);
		result = prime * result + ((boardPanel == null) ? 0 : boardPanel.hashCode());
		result = prime * result + currentIndex;
		result = prime * result + ((currentPlayer == null) ? 0 : currentPlayer.hashCode());
		result = prime * result + (gameOver ? 1231 : 1237);
		result = prime * result + nextIndex;
		result = prime * result + ((nextPlayer == null) ? 0 : nextPlayer.hashCode());
		result = prime * result + Arrays.hashCode(players);
		result = prime * result + Arrays.hashCode(tiles);
		return result;
	}
	
	/**
	 * Help determine if any move puts the <b>opponent</b> {@link King} in <b>check</b>.
	 * 
	 * @param piece that is moving.
	 * @param tiles are the original new positions of {@code piece}.
	 */
	private void kingHasBeenCheck(Piece piece, Tile[] tiles) {
		Objects.requireNonNull(piece, "There must be a piece moving.");
		Objects.requireNonNull(tiles, "The piece must be moving.");
		if (tiles.length != 2) throw new IllegalArgumentException("Illegal number of elements in tiles.");
		
		//Store original Piece positions
		Piece tiles0OrigPiece = tiles[0].getPiece(), tiles1OrigPiece = tiles[1].getPiece();
		
		//temporarily move <code>Piece</code>.
		tiles[1].setPiece(tiles0OrigPiece);
		tiles[0].setPiece(null);
		
		//Find both King
		Tile opponentKingTile = this.findKing(piece, true), allyKingTile = this.findKing(piece, false);
		King opponentKing = (King)opponentKingTile.getPiece(), allyKing = (King)allyKingTile.getPiece();
		
		this.attackPiece = null;
		if (piece.checkKing(this.board, this.tiles, opponentKingTile)) {			//Piece has moved to check the Opponent's King.
			if (!piece.canUnCheck(this, this.tiles)) {						//Determine if the King can move itself to safety
				if (piece.protect(opponentKingTile, this.board, this.tiles)) {		//Determine if a Piece can move to protect the King
					JOptionPane.showMessageDialog(null, "Check!", "Check!", JOptionPane.INFORMATION_MESSAGE);
					attackPiece = piece;
					tiles[0].setPiece(tiles0OrigPiece);
					tiles[1].setPiece(tiles1OrigPiece);
					this.advance();
					this.gameOver = false;
				} else { //No Piece can protect the King
					JOptionPane.showMessageDialog(null, "Check Mate!\n" + this.currentPlayer.getName() + ", wins!", "Check Mate", JOptionPane.INFORMATION_MESSAGE);
					tiles[0].setPiece(tiles0OrigPiece);
					tiles[1].setPiece(tiles1OrigPiece);
					this.advance();
					this.gameOver = true;
				}
			} else {	//Piece is able to protect the King.
				JOptionPane.showMessageDialog(null, "Check!", "Check", JOptionPane.INFORMATION_MESSAGE);
				opponentKing.setIsCheck(true);
				attackPiece = piece;
				tiles[0].setPiece(tiles0OrigPiece);
				tiles[1].setPiece(tiles1OrigPiece);
				this.advance();
				this.gameOver = false;
			}
		} else if (allyKing.isCheck() && attackPiece != null) { //Piece has moved to protect the Ally's King
			if (attackPiece.protecting(this, tiles)) {
				allyKing.setIsCheck(false);
				tiles[0].setPiece(tiles0OrigPiece);
				tiles[1].setPiece(tiles1OrigPiece);
				this.advance();
			} else JOptionPane.showMessageDialog(null, "You must protect the King!", "Carefull!", JOptionPane.WARNING_MESSAGE);
		} else { //Piece has moved
			tiles[0].setPiece(tiles0OrigPiece);
			tiles[1].setPiece(tiles1OrigPiece);
			this.advance();
		}
	}
	
	/**
	 * Move {@link Piece} from {@link #tiles}{@code [0]} to {@code tiles[1]}.
	 */
	private void movePiece() {
		Piece tile0Piece = this.tiles[0].getPiece(), tile1Piece = this.tiles[1].getPiece();
		if (this.currentPlayer.getPlayerColor() != tile0Piece.getPieceColor()) return;
		if (!tile0Piece.getLegal(this.board, this.tiles)) {
			JOptionPane.showMessageDialog(null, tile0Piece.toString() + " can not move like this!", "Careful!", JOptionPane.PLAIN_MESSAGE);
			return;
		} if (tile0Piece.determineKingisCheck(this, this.tiles)) {	//Determine if Pawn moves away from Ally King.
			JOptionPane.showMessageDialog(null, "This move will put the King in check!", "Careful!", JOptionPane.PLAIN_MESSAGE);
			return;
		} if (Piece.collide(tile0Piece.setTileCollide(board, tiles))) {	//Determine if a Pawn collides with any other Pawn.
			JOptionPane.showMessageDialog(null, tile0Piece.toString() + " cannot jump!", "Careful!", JOptionPane.PLAIN_MESSAGE);
			return;
		} if (tile1Piece == null) {	//Capturing
			this.tempAdvance();
			this.kingHasBeenCheck(tile0Piece, tiles);
			return;
		} if (tile1Piece.isAlly(tile0Piece)) { //Allies can't capture each other.
			JOptionPane.showMessageDialog(null, "You cannot capture Allies", "Careful!", JOptionPane.PLAIN_MESSAGE);
			return;
		} try {
			this.currentPlayer.increaseScore(tile1Piece.getValue());
		} catch (IllegalAccessException iae) {
			throw new IllegalStateException("King somehow got captured.");
		} this.tempAdvance();
		this.kingHasBeenCheck(tile0Piece, tiles);
	}
	
	/**
	 * Assist {@link #movePiece()}
	 * 
	 * @param tile is a {@link Tile} clicked on.
	 */
	public void movePiece(Tile tile) {
		Objects.requireNonNull(tile, "Piece must move to a new Tile.");
		this.setTiles(tile);
			
		if (this.tiles[0] != null && this.tiles[1] != null) {
			this.movePiece();
			this.resetTiles();
		}
	}
	
	/**Place the {@link Piece} on {@link #board}*/
	private void placePieces() {
		int[][] row		= {{6,7}, {1,0}};	//Put piece at top or bottom of board
		int indexInList = 0;
		Tile tile;
		Piece piece;
		
		for (int player : new int[] {0, 1}) {						//Black and White players
			for (int i : row[player]) {								//Loop through their respective piece
				for (int j : new int[] {0, 1, 2, 3, 4, 5, 6, 7}) {	//the column to put piece in	
					tile = this.board[i][j];
					piece = this.players[player].getPieces()[indexInList];
					piece.reset();
					tile.setPiece(piece);							//Set piece on Tile
					tile.setText(tile.getPiece().toString());		//Set GUI elements
					tile.setForeground(players[player].getPlayerColor().color);
					indexInList ++;
				}
			} indexInList = 0;
		}
	}
	
	/**Reset the Game*/
	public void reset() {
		this.resetBoard();
		this.resetTiles();
		this.gameOver		= false;
		this.currentIndex	= 0;
		this.nextIndex		= 1;
		this.currentPlayer	= this.players[this.currentIndex];
		this.nextPlayer		= this.players[this.nextIndex];
	}
	
	/**Reset the {@link #board}*/
	private void resetBoard() {
		for (Tile[] row : this.board) {
			for (Tile tile : row) {
				tile.setPiece(null);
				tile.setForeground(null);
				tile.setText(null);
			}
		} for (Player player : this.players) {
			player.setScore(0);
		} this.placePieces();
	}
	
	/**Empty {@link #tiles}.*/
	public void resetTiles() {
		Arrays.fill(this.tiles, null);
	}
	
	/**
	 * Change {@link #gameOver} to {@code bool}.
	 * 
	 * @param bool is the new value of {@link #gameOver}.
	 */
	public void setGameOver(boolean bool) {
		this.gameOver = bool;
	}
	
	/**
	 * Place a {@link Tile} clicked on in the first available place.
	 * 
	 * @param tile is the {@code Tile} clicked on.
	 */
	private void setTiles(Tile tile) {
		if (this.tiles[0] == null)
			if (tile.getPiece() == null) return;
			else this.tiles[0] = tile;
		else if (this.tiles[1] == null)
			if (tile.equals(tiles[0])) this.resetTiles();
			else this.tiles[1] = tile;
	}
	
	/**temporary moving*/
	private void tempAdvance() {
		this.tiles[1].setPiece(this.tiles[0].getPiece());
	}
	
	@Override
	public String toString() {
		String str = "";
		for (Tile[] row : this.board) {
			for (Tile tile : row) {
				str += tile.getPiece() == null ? tile.toString() + "\t" : tile.getPiece().toString();
			} str += "\n";
		} return str;
	}
}