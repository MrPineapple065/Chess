import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * This <code>Pawn</code> represents a
 * Pawn in the game of Chess.
 * 
 * @version 16 March 2020
 * @author MrPineapple065
 *
 */
class Pawn extends Piece {
	/**
	 * The amount of points <code>Pawn</code> is worth
	 */
	private static final int VALUE = 0x01;
	
	/**
	 * A boolean determining if the <code>Pawn</code> has moved.
	 * 
	 * @see #getLegal(Pawn, Tile[][], Tile[]);
	 */
	private boolean	hasMoved;
	
	/**
	 * Creates a <code>Pawn</code> that is <code>color</code>.
	 * 
	 * @param color is the color of the <code>Pawn</code>.
	 * 
	 * @throws IllegalArgumentException if <code>color</code> is <code>null</code>.
	 */
	public Pawn(Color color) throws IllegalArgumentException {
		super(color);
		this.hasMoved = false;
	}
	
	/**
	 * Determine the value of <code>Pawn</code>.
	 * 
	 * @return {@link #VALUE}
	 */
	public static int getValue() {
		return VALUE;
	}
	
	/**
	 * Determine if move <code>Pawn</code> makes from <code>tiles[0]</code> to <code>tiles[1]</code> is legal. </br>
	 * A <code>Pawn</code> can <b>only</b> move <b>forward</b> <i>one</i> <code>Tile</code> at a time. </br>
	 * Only the first move may <code>Pawn</code> move <i>two</i> <code>Tile<code> forward. </br>
	 * It also can <b>only</b> capture <b>diagonally</b>.
	 * 
	 * @param pawn is the pawn moving.
	 * @param board is the board.
	 * @param tiles are the original and new positions of <code>Pawn</code>.
	 * 
	 * @return  <code>true</code> if move is legal. </br>
	 * 			<code>false</code> if move is illegal.
	 * 
	 * @throws IllegalArgumentException if any <code>paremeter</code> is <code>null</code>.
	 */
	public static boolean getLegal(Pawn pawn, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		try {
			Pawn.iae(pawn, board, tiles);
		}
		
		catch (IllegalArgumentException iae) {
			throw iae;
		}
		
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		//White
		if (pawn.getPieceColor().equals(Piece.WHITE)) {
			//First move can move two forward
			if (! pawn.hasMoved()) {
				if ((oldX - newX <= 2) && (oldY == newY)) {
					if (board[newX][newY].getPiece() != null) {
						return false;
					}
					
					pawn.setFirstMove(false);
					return true;
				}
				pawn.setFirstMove(true);
				
				//Capturing
				if ((oldX - newX == 1) && (oldY - newY == 1)) {
					if (board[newX][newY].getPiece() != null) {
						pawn.setFirstMove(false);
						return true;
					}
				}
				
				else if ((oldX - newX == 1) && (oldY - newY == -1)) {
					if (board[newX][newY].getPiece() != null) {
						pawn.setFirstMove(false);
						return true;
					}
				}
			}
			
			else {
				//moving
				if ((oldX - newY == 1) && (newY == oldY)) {
					return (board[newX][newY].getPiece() == null);
				}
				
				//Capturing
				if ((oldX - newX == 1) && (Math.abs(oldY - newY) == 1)) {
					return (board[newX][newY].getPiece() != null);
				}
			}
		}
		
		//Black
		else {
			//First move can move two forward
			if (! pawn.hasMoved()) {
				//Moving
				if ((oldX - newX >= -2) && (oldY == newY)) {
					if (board[newX][newY].getPiece() != null) {
						return false;
					}
					pawn.setFirstMove(false);
					return true;
				}
				
				//Capturing
				if ((oldX - newX == -1) && (Math.abs(oldY - newY) == 1)) {
					if (board[newX][newY].getPiece() != null) {
						pawn.setFirstMove(false);
						return true;
					}
				}
			}
				
			else {
				//Moving
				if ((oldX - newX == -1) && (newY == oldY)) {
					return (board[newX][newY].getPiece() == null);
				}
				
				//Capturing
				if ((oldX - newY == -1) && (Math.abs(oldY - newX) == 1)) {
					return (board[newX][newY].getPiece() != null);
				}
			}
		}
		
		return false;
	}

	/**
	 * Determine all <code>Tile</code> from <code>tiles[0]</code> to
	 * <code>tiles[1]</code> that a <code>Pawn</code> travels over in its journey.
	 * 
	 * @param piece is the piece moving
	 * @param board is the board
	 * @param tiles are the original and new positions of <code>Piece</code>.
	 * 
	 * @return	an <code>Array</code> of <code>Tile</code> from <code>tiles[0]</code> to
	 * 			<code>tiles[1]</code> that a <code>Pawn</code> travels over in its journey.
	 * 
	 * @throws IllegalArgumentException if any <code>parameter</code> is <code>null</code>.
	 */
	public static Tile[] setTilesCollide(Piece piece, Tile[][] board, Tile[] tiles) throws IllegalArgumentException {
		try {
			Pawn.iae(piece, board, tiles);
		}

		catch (IllegalArgumentException iae) {
			throw iae;
		}
		
		ArrayList<Tile> temp = new ArrayList<Tile>(2);
		int oldX = tiles[0].getRow(), oldY = tiles[0].getColumn(), newX = tiles[1].getRow(), newY = tiles[1].getColumn();
		
		//White
		if (piece.getPieceColor().equals(Piece.WHITE)) {
			if (oldY != newY) {
				temp.add(board[oldX][oldY]);
				temp.add(board[newX][newY]);
			}
			
			else {
				for (int i = oldX; i >= newX; i--) {
					temp.add(board[i][newY]);
				}
			}
		}
		
		//Black
		else {
			if (oldY != newY) {
				temp.add(board[oldX][oldY]);
				temp.add(board[newX][newY]);
			}
			
			else {
				for (int i = oldX; i <= newX; i++) {
					temp.add(board[i][newY]);
				}
			}
		}
		
		//Cannot collide with self
		if (temp.get(0).equals(board[oldX][oldY])) {
			temp.remove(0);
		}
		
		//Cannot collide when capturing
		if (temp.get(temp.size() - 1).getPiece() != null) {
			if (! piece.isAlly(temp.get(temp.size() - 1).getPiece())) {
				temp.remove(temp.size() - 1);
			}
		}
		
		Tile[] t = new Tile[temp.size()];
		for (int k = 0; k < t.length; k++) {
			t[k] = temp.get(k);
		}
		return t;
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
			throw new IllegalArgumentException("There must be a Pawn.");
		}
		
		if (! (piece instanceof Pawn)) {
			throw new IllegalArgumentException("Piece must be a Pawn.");
		}
		
		if (board == null) {
			throw new IllegalArgumentException("There must be a board.");
		}
		
		if (tiles == null) {
			throw new IllegalArgumentException("Pawn must move.");
		}
	}
	
	/**
	 * Determine if <code>Pawn<code> has moved.
	 * 
	 * @return hasMoved
	 */
	private boolean hasMoved() {
		return this.hasMoved;
	}
	
	/**
	 * Change the value of <code>hasMoved</code> to <code>bool</code>.
	 * 
	 * @param bool is the new value.
	 */
	public void setFirstMove(boolean bool) {
		this.hasMoved = bool;
	}
	
	/**
	 * Promote a <code>Pawn</code> that has made it to the other side of <code>board</code>.
	 * 
	 * @param tiles are the original and new positions of <code>Pawn</code>.
	 * 
	 * @throws IllegalArgumentException is {@link #getPieceColor()} return <code>null</code>.
	 */
	public void promote(Tile[] tiles) throws IllegalArgumentException {
		switch (JOptionPane.showOptionDialog(null, "Which piece would you like to promote the pawn to?", "Promotion!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {"\u2655", "\u2656", "\u2657", "\u2658"}, "\u2655")) {
		
		case 0:
			Queen queen = new Queen(this.getPieceColor());
			tiles[0].setPiece(queen);
			break;
			
		case 1:
			Bishop bishop = new Bishop(this.getPieceColor());
			tiles[0].setPiece(bishop);
			break;
			
		case 2:
			Rook rook = new Rook(this.getPieceColor());
			tiles[0].setPiece(rook);
			break;
			
		case 3:
			Knight knight = new Knight(this.getPieceColor());
			tiles[0].setPiece(knight);
			break;
		}
	}
	
	/**
	 * @return <code>String</code> representation of <code>Pawn</code>
	 */
	@Override
	public String toString() {
		if (super.getPieceColor().equals(Piece.WHITE)) {
			return "\u2659";
		}
		
		return "\u265F";
	}
}