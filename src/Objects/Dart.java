package Objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import Game.BufferedImageS;
import Main.GamePanel;
import Stages.GameStage;
import Work.Loader;

public class Dart implements Serializable {

	private static final long serialVersionUID = 1L;
	
	double x,y;
	int w,h;
	GameStage gameStage;
	BufferedImageS img;
	
	public Dart(GameStage gameStage) {
		this.gameStage = gameStage;
		
		w = Loader.DART.getImg().getWidth();
		h = Loader.DART.getImg().getHeight();
	}
	
	public void setPos(int x, int y) {
		this.x = x*16;// + GamePanel.getGameWidth()/2;
		this.y = y*16;// + GamePanel.getGameHeight()/2;
	}
	
	public void draw(Graphics2D g) {
		if(isHit)
			return;
		g.drawImage(Loader.DART.getImg(),
				(int) ((x - GameStage.mapX + w/2)*GamePanel.quality + GamePanel.getGameWidth()/2),
				(int) ((y - GameStage.mapY + h/2)*GamePanel.quality + GamePanel.getGameHeight()/2),
				(int) (w*GamePanel.quality),
				(int) (h*GamePanel.quality), null);
	}
	
	Rectangle hitbox;
	
	boolean isHit = false;
	int time = 0;
	
	public void update() {
		x += 16/2.5f;
		if(isHit)
			return;
		time++;
		hitbox = new Rectangle((int)(x+8-w/2), (int)(y+8+h), w, h);
		if(gameStage.player.intersects(hitbox)) {
			gameStage.player.setGameOver(5);
			gameStage.player.isGameOver = true;
		}
		if(time > 3) {
			if(hitbox.intersects(gameStage.level.getHitbox((int) (x/16), (int) (y/16), 0))) {
				isHit = true;
			}
		}
	}
	
	public boolean isHit() {
		return isHit;
	}
}
