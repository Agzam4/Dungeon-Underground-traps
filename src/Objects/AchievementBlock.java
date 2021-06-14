package Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Work.GameData;
import Work.MouseController;
import Work.MyFile;

public class AchievementBlock {

	static BufferedImage bg = getImage("bg");
	static BufferedImage fg = getImage("fg");
	static BufferedImage imgs = getImage("img");
	static String achievementsText[] = MyFile.readFileInResource("/text/achievements." + System.getProperty("user.language")).split("\n");
	
	int x, y, id;
	int s = (int) (GamePanel.scalefull * 15);
	BufferedImage image;
	private boolean onMouse;

	String title;
	String text;

	double letterNums = 0;
	double letterNums2 = 0;
	
	public AchievementBlock(int id) {
		image = imgs.getSubimage((id%4)*15, (int) (Math.floor(id/4)*15), 15, 15);
		this.id = id;
		title = achievementsText[id*2];
		text = achievementsText[id*2 + 1];
	}

	public void draw(Graphics2D gf) {
		if(onMouse) {
			int w = (int) (15*GamePanel.scalefull);
			gf.setColor(Color.WHITE);
			gf.drawRect(x-w/2, y-w/2, w-1, w-1);
			gf.setColor(new Color(255,255,255,100));
			gf.fillRect(x-w/2, y-w/2, w, w);
		}
		if(!GameData.achievements[id]) {
			int w = (int) (15*GamePanel.scalefull);
			gf.setColor(new Color(0,0,0,200));
			gf.fillRect(x-w/2, y-w/2, w-1, w-1);
			gf.setColor(Color.DARK_GRAY);
			gf.drawRect(x-w/2, y-w/2, w-1, w-1);
		}
		drawImage(gf, fg);
		drawImage(gf, image);
		drawImage(gf, bg);
	}
	
	public void drawInfo(Graphics2D gf) {
		int newX = (int) (x+(8*GamePanel.scalefull));
		String newText = text + "     ";
		try {
			newText = text.substring(0, (int) letterNums2) + "     ";
		} catch (StringIndexOutOfBoundsException e) {
		}
		String newTitle = title;
		try {
			newTitle = title.substring(0, (int) letterNums);
		} catch (StringIndexOutOfBoundsException e) {
		}
		gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) ((10)*GamePanel.scalefull)));
		if(newX + gf.getFontMetrics().stringWidth(newText) + 3*GamePanel.scalefull> GamePanel.frameW) {
			newX = (int) (GamePanel.frameW-gf.getFontMetrics().stringWidth(newText) - 3*GamePanel.scalefull);
		}
		gf.setFont(new Font("Arial", Font.PLAIN, (int) ((7)*GamePanel.scalefull)));
		if(newX + gf.getFontMetrics().stringWidth(newText) + 3*GamePanel.scalefull> GamePanel.frameW) {
			newX = (int) (GamePanel.frameW-gf.getFontMetrics().stringWidth(newText) - 3*GamePanel.scalefull);
		}
		if(newX < 5) {
			newX = 5;
		}
		
		gf.setFont(new Font("Arial", Font.PLAIN, (int) ((7)*GamePanel.scalefull)));
		drawString(gf, "    " + newText, newX, y + gf.getFont().getSize());
		gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) ((10)*GamePanel.scalefull)));
		drawString(gf, "  " + newTitle, newX, y);
	}
	
	Color fc = Color.WHITE;
	Color bc = Color.BLACK;
	
	private void drawString(Graphics2D gf, String str, int x, int y) {
		gf.setColor(fc);
		gf.drawString(str, x, y);
		gf.setColor(bc);
		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy <2; yy++) {
				gf.drawString(str, (int) (x+xx*GamePanel.scalefull/1.5), (int) (y+yy*GamePanel.scalefull/1.5));
			}
		}
	}
	
	private void drawImage(Graphics2D gf, BufferedImage img) {
		int w = (int) (img.getWidth()*GamePanel.scalefull);
		int h = (int) (img.getHeight()*GamePanel.scalefull);
		gf.drawImage(img, x-w/2, y-h/2, w, h, null);
	}
	
	public void update() {
		int mx = MouseController.getMousePointOnFrame().x;
		int my = MouseController.getMousePointOnFrame().y;
		int w = (int) (bg.getWidth()*GamePanel.scalefull);
		int h = (int) (bg.getHeight()*GamePanel.scalefull);
		onMouse = mx < x+w/2 && mx > x-w/2 &&  my < y+h/2 && my > y-h/2;
		s = (int) (GamePanel.scalefull * 15);
		
		if(onMouse) {
			letterNums += title.length()/10d;
			if(letterNums > title.length()) {
				letterNums = title.length();
			}
			letterNums2 += text.length()/10d;
			if(letterNums2 > text.length()) {
				letterNums2 = text.length();
			}
		}else {
			letterNums = 0;
			letterNums2 = 0;
		}
	}

	private static BufferedImage getImage(String string) {
		try {
			return ImageIO.read(AchievementBlock.class.getResource("/img/ico/achievement_" + string + ".png"));
		} catch (IOException e) {
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
