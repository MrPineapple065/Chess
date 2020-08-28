import java.awt.Color;

/**
 * An enumerated type the helps determine the color of each {@link Piece}.
 */
public enum PieceColor {
	White(new Color(0xd3d3d3)),
	Black(new Color(0x2c2c2c));
	
	public final Color color;
	
	private PieceColor(Color color) {
		this.color = color;
	}
}