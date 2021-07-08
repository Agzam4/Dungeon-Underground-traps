package Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Work.GameData;
import Work.Loader;
import Work.MouseController;
import Work.MyAudio;
import Work.MyFile;

public class AchievementBlock {

	static BufferedImage bg = getImage("bg");
	static BufferedImage fg = getImage("fg");
	static BufferedImage imgs = getImage("img");
	static String achievementsText[] = MyFile.readFileInResource("/text/achievements." + GameData.language).split("\n");
	MyAudio audio;
	
	int x, y, id;
	int s = (int) (GamePanel.scalefull * 15);
	BufferedImage image;
	private boolean onMouse;

	String title;
	String text;
	
	int time = 0; 

	double letterNums = 0;
	double letterNums2 = 0;
	
	boolean isNew = false;

	public AchievementBlock(int id) {
		image = imgs.getSubimage((id%4)*15, (int) (Math.floor(id/4)*15), 15, 15);
		this.id = id;
		title = achievementsText[id*2];
		text = achievementsText[id*2 + 1];
		bc =  Color.BLACK;
		fc =  Color.WHITE;
	}
	public AchievementBlock(int id, boolean n) {
		audio = new MyAudio("/music/Achievement.wav");
		image = imgs.getSubimage((id%4)*15, (int) (Math.floor(id/4)*15), 15, 15);
		this.id = id;
		title = achievementsText[id*2];
		text = achievementsText[id*2 + 1];
		isNew = n;
		if(isNew) {
			bc = new Color(255,255,200,100);
			fc = new Color(0,0,0);
		}else {
			bc =  Color.BLACK;
			fc =  Color.WHITE;
		}
		x = 0;
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
		drawImage(gf, Loader.achievement_ico[id]);
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
	
	Color fc;
	Color bc;
	
	
	
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
	
	double x2 = 1;
	
	public void draw2(Graphics2D gf) {
		x =  GamePanel.frameH/10;
		y = (int) (GamePanel.frameH/10 + x2*GamePanel.frameH/7*2 + GamePanel.frameH/7*5);

		int ry = (int) (x2*GamePanel.frameH/7*2 + GamePanel.frameH/7*5);
		int rh = GamePanel.frameH/5;

		gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (rh/5)));
		drawString(gf, title, (int) (x + 15*GamePanel.scalefull), ry + rh/4*2);
		gf.setFont(new Font("Arial", Font.PLAIN, (int) (rh/7)));
		drawString(gf, text, (int) (x + 15*GamePanel.scalefull), (int) (ry + rh/4*2.75));
		
		draw(gf);
		gf.setColor(new Color(0,0,0,150));
		gf.fillRect(0, ry, GamePanel.frameW, rh);
		gf.setColor(new Color(255,255,255,150));
		gf.drawRect(-1, ry-1, GamePanel.frameW+2, rh+1);
		
	}
	
	public void update() {
		if(isNew && time == 0) {
			audio.setVolume(GameData.audio[GameData.AUDIO_ACHIEVEMENTS]/2f);
			audio.play(0);
		}
		time++;
		if(time < 25) {
			x2 /= 2;
		}
		if(time > 25) {
			x2 = (x2 - 1)/2 + 1;
		}
		if(Math.round(x2*100) == 100) {
			needRemove = true;
		}
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
	
	boolean needRemove;
	public boolean needRemove() {
		return needRemove;
	}
}
