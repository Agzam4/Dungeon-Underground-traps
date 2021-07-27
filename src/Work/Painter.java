package Work;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;

public class Painter {
	
	public static double k = 1;

	public static void drawString(Graphics2D gf, String str, Color c1, Color c2, int x, int y) {
		gf.setColor(c1);
		gf.drawString(str, x, y);
		gf.setColor(c2);
		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy <2; yy++) {
				gf.drawString(str, (int) (x+xx*GamePanel.scalefull/1.5), (int) (y+yy*GamePanel.scalefull/1.5));
			}
		}
	}

	public static void drawCenterString(Graphics2D gf, String str, Color c1, Color c2, int y) {
		int tw = gf.getFontMetrics().stringWidth(str);
		drawString(gf, str, c1, c2, (GamePanel.frameW-tw)/2, y);
	}
}
