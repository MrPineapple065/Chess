import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This {@code Tile} class represents a Tile
 * on a Chess Board.
 * 
 * @version 21 March 2020
 * @author MrPineapple065
 *
 */
public final class Tile extends JButton implements MouseListener, KeyListener {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 0x851FA7B9FE81A8EDL;

	/**
	 * An {@code Array} of {@link Color} that this could be.
	 */
	private static final Color[] tileColors	= new Color[] {new Color(0xC89669), new Color(0x73463C)};

	/**
	 * The {@link ChessBoardPanel} that holds this.
	 */
	private final ChessBoardPanel boardPanel;

	/**
	 * The {@link Color} of this.
	 */
	private final Color	tileColor;

	/**
	 * The column that this is in.
	 */
	private final int column;

	/**
	 * The row that this is in.
	 */
	private final int row;

	/**
	 * The {@link Piece} on the {@link Tile}.<br>
	 * This value is {@code null} if no {@code Piece} is on the {@code Tile}.
	 */
	private Piece piece;

	/**
	 * Create {@code Tile} with all attributes defined.
	 * 
	 * @param b is the {@link ChessBoardPanel} holding this.
	 * @param col is the column that this is in.
	 * @param row is the row that this is in.
	 * 
	 * @throws IllegalArgumentException if  {@code b} or {@code color} are {@code null}.
	 * @throws IndexOutOfBoundsException if {@code Tile} trying to be created out of bounds.
	 * 
	 */
	public Tile(ChessBoardPanel b, int col, int row) throws IllegalArgumentException, IndexOutOfBoundsException {
		super(null, null);
		this.boardPanel = Objects.requireNonNull(b, "This tile must be on a ChessBoardPanel.");
		if (col < 0 || col > 7)	throw new IndexOutOfBoundsException("Illegal column: " + col);
		else					this.column	= col;
		if (row < 0 || row > 7) throw new IndexOutOfBoundsException("Illegal row: " + row);
		else					this.row = row;

		this.tileColor = (this.row % 2 == 0 && this.column % 2 == 0) || (this.row % 2 == 1 && this.column % 2 == 1) ? Tile.tileColors[0] : Tile.tileColors[1];

		//Set GUI Elements
		super.setFont(new Font("", Font.PLAIN, 40));	this.setBackground(this.tileColor);
		this.setHorizontalAlignment(JButton.CENTER);	this.setVerticalAlignment(JButton.CENTER);
		this.setFocusPainted(false);					this.setBorder(null);

		//Add Interactivity
		this.addKeyListener(this);	this.addMouseListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	/**
	 * @return {@link #piece}
	 */
	public Piece getPiece() {
		return this.piece;
	}

	/**
	 * @return {@link #column}
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * @return {@link #row}
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * @return {@link #tileColor}
	 */
	public Color getTileColor() {
		return this.tileColor;
	}

	/**
	 * Set {@link piece} to {@code piece}.
	 * 
	 * @param piece is the new {@link Piece}.
	 * 
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	/**
	 * Update GUI once a move has been completed.
	 * 
	 * @return {@code true} when updated.
	 */
	public boolean update() {
		if (this.piece != null) {
			this.setText(this.piece.toString());
			this.setForeground(this.piece.getPieceColor().color);
		} else {
			this.setText("");
			this.setForeground(null);
		} return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardPanel == null) ? 0 : boardPanel.hashCode());
		result = prime * result + column;
		result = prime * result + ((piece == null) ? 0 : piece.hashCode());
		result = prime * result + row;
		result = prime * result + ((tileColor == null) ? 0 : tileColor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)										return true;
		if (!(obj instanceof Tile)) 							return false;
		Tile other = (Tile) obj;
		if (boardPanel == null) if (other.boardPanel != null)	return false;
		else if (!boardPanel.equals(other.boardPanel))			return false;
		if (column != other.column)								return false;
		if (row != other.row)									return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf((char)('a' + this.column)) + String.valueOf(8 - this.getRow());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			this.boardPanel.getBoard().movePiece(this);
			return;
		case MouseEvent.BUTTON2:
			String tileText = this.getText();
			String coords = this.getRow() + ", " + this.getColumn();
			if ("".equals(tileText) || ! coords.equals(tileText)) {
				this.setForeground(new Color(0x333333));
				this.setText(coords);
			} else {
				if (this.getPiece() == null) {
					this.setText(null);
					this.setForeground(null);
				} else {
					this.setText(this.getPiece().toString());
					this.setForeground(this.getPiece().getPieceColor().color);
				}
			} return;
		case MouseEvent.BUTTON3:
			String tileText1 = this.getText();
			if ("".equals(tileText1) || !this.toString().equals(tileText1)) {
				this.setForeground(new Color(0x333333));
				this.setText(this.toString());
			} else {
				if (this.getPiece() == null) {
					this.setText(null);
					this.setForeground(null);
				} else {
					this.setText(this.getPiece().toString());
					this.setForeground(this.getPiece().getPieceColor().color);
				}
			} return;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {this.getModel().setPressed(true);}
	
	@Override
	public void mouseReleased(MouseEvent e) {this.getModel().setPressed(false);}
	
	@Override
	public void mouseEntered(MouseEvent e) {this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));}
	
	@Override
	public void mouseExited(MouseEvent e) {this.setBorder(null);}
	
	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()) {
		case KeyEvent.VK_ESCAPE:
			this.boardPanel.actionPerformed(null);
			return;
		case 'e':
			ChessBoard board = this.boardPanel.getBoard();
			if (board.getTiles()[1] == null) {
				board.resetTiles();
				JOptionPane.showMessageDialog(null, "Piece deselected", "", JOptionPane.PLAIN_MESSAGE, null);
			} return;
		case 's':
			this.boardPanel.scoresOption();
			return;
		case 'r':
			this.boardPanel.resetOption();
			return;
		case 'q':
			this.boardPanel.quitOption();
			return;
		case 'f':
			this.boardPanel.resignOption();
			return;
		case 'c':
			this.boardPanel.controlsOption();
			return;
		default:
			return;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {return;}
	
	@Override
	public void keyReleased(KeyEvent e) {return;}
}