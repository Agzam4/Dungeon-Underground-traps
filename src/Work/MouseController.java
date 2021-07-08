package Work;

import java.awt.Point;

import Main.GamePanel;

public class MouseController {

	public static boolean isMousePressed;
	public static double mouseScroll;
	
	private static int mx;
	private static int my;

	public static Point getMousePoint() {
		return new Point(mx - GamePanel.gameX, my - GamePanel.gameY);
	}
	public static Point getMousePointOnFrame() {
		return new Point(mx, my);
	}
	
	public static void setPosition(int x, int y) {
		mx = x;
		my = y;
	}
}
