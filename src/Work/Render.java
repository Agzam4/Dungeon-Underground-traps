package Work;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Render {

	private static RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	private static RenderingHints alpha = new RenderingHints(
			RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
	
	private static RenderingHints color = new RenderingHints(
			RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);

	// KEY_DITHERING
	// KEY_FRACTIONALMETRICS
	// KEY_INTERPOLATION
	
	private static RenderingHints rendering = new RenderingHints(
			RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
	
	// KEY_STROKE_CONTROL
	
	private static RenderingHints textAntialiasing = new RenderingHints(
			RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
	// KEY_TEXT_LCD_CONTRAST range 100 to 250;

	public static void setDefault() {
		color = new RenderingHints(
				RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
		rendering = new RenderingHints(
				RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		alpha = new RenderingHints(
				RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
	}

	public static void setSpeed() {
		alpha = new RenderingHints(
				RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		color = new RenderingHints(
				RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		rendering = new RenderingHints(
				RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	}
	public static void setQuality() {
		alpha = new RenderingHints(
				RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		color = new RenderingHints(
				RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		rendering = new RenderingHints(
				RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}
	
	public static void addRenderingHints(Graphics2D g) {
		g.setRenderingHints(antialiasing);
//		g.setRenderingHints(alpha);
//		g.setRenderingHints(color);
//		g.setRenderingHints(rendering);
//		g.setRenderingHints(textAntialiasing);
	}
	
	
}
