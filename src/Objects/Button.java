package Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Main.GamePanel;
import Work.Loader;
import Work.MouseController;
import Work.MyAudio;

public class Button {
	
	public boolean clickable = true;
	boolean isClicked = false;
	
	String text = "";
	
	int bx, by, bw, bh;
	int maxBw = 0;
	int fontSize = 15;
	double bv = 0;
	String fontName = "Comic Sans MS";
	
	public boolean chageSize = true;
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Button(String text, int bx, int by) {
		this.text = text;
		this.bx = bx;
		this.by = by;
	}
	
	public void draw(Graphics2D gf) {
		gf.setFont(new Font(fontName, Font.PLAIN, (int) ((fontSize+(chageSize ? bv : 0))*GamePanel.scalefull)));
		bw = gf.getFontMetrics().stringWidth(text);
		bh = gf.getFont().getSize();
		maxBw = (int) (gf.getFontMetrics().stringWidth(text) + 2*GamePanel.scalefull);
		drawString(gf, text, (int) (bx - bw/2), by);
	}

	Color ufc = Color.DARK_GRAY;
	Color sc = Color.GRAY;
	Color fc = Color.WHITE;
	Color bc = Color.BLACK;
	
	private void drawString(Graphics2D gf, String str, int x, int y) {
		gf.setColor(clickable ? (!chageSize && onButton) ?  Loader.COLOR_TEXT_FG.darker() : Loader.COLOR_TEXT_FG : Loader.COLOR_TEXT_FG2);
		gf.drawString(str, x, y);
		gf.setColor(Loader.COLOR_TEXT_BG);
		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy <2; yy++) {
				gf.drawString(str, (int) (x+xx*GamePanel.scalefull/1.5), (int) (y+yy*GamePanel.scalefull/1.5));
			}
		}
	}

	boolean onButton = false;
	boolean isPressed = false;
	int s = 0;
	private static MyAudio pop = new MyAudio("/sounds/pop-1.wav");
	private static MyAudio click = new MyAudio("/sounds/pop-2.wav");
	
	public void update() {
		isClicked = false;
		if(!MouseController.isMousePressed) {
			isPressed = false;
			if(s == 2) {
				s = 1;
			}
		}
		
		int x = MouseController.getMousePointOnFrame().x;
		int y = MouseController.getMousePointOnFrame().y;
		boolean onButton = bx-bw/2 < x && bx+bw/2 > x && by-bh < y && by > y && clickable;
		
		if(!this.onButton && onButton) {
			pop.play(0);
			if(!isPressed) {
				s = 1;
			}
		}
		this.onButton = onButton;
		
		if(onButton) {
			bv = (bv - 2*GamePanel.scalefull)*0.6 + 2*GamePanel.scalefull;
			if(MouseController.isMousePressed) {
				isPressed = true;
				if(s == 1) {
					click.play(0);
					MouseController.isMousePressed = false;
					s = 2;
					isClicked = true;
				}
			}
		}else {
			bv*=0.6;
			s = 0;
		}
	}
	
	public void setPosition(int x, int y) {
		bx = x;
		by = y;
	}
	
	public int getBw() {
		return maxBw;
	}
	
	public boolean isClicked() {
		return isClicked || robotClick;
	}
	
	boolean robotClick;

	public void click() {
		robotClick = true;
	}
	
	public int getBh() {
		return (int) ((fontSize+5)*GamePanel.scalefull);
	}
}
