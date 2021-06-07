package Objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Main.GamePanel;
import Stages.GameStage;
import Work.LevelGenerator;

public class Player extends GameObject {

	public static int tilesize = GameStage.tilesize;
	LevelGenerator level;
	int lw;
	int lh;
	
	int hp = 99;

	double cx = 5*GamePanel.quality;//-0.1;
	double cy = 5*GamePanel.quality;
	
	public Player(LevelGenerator level) {
		super(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
		this.level = level;
		lw = level.getWidth() * 16;
		lh = level.getHeight() * 16;
	}

	@Override
	public void draw(Graphics2D g) {
		double k = GameStage.tilesize*GamePanel.quality;
		int q = (int) GamePanel.quality;
		g.drawImage(main,
				(int)((x-GameStage.mapX + GameStage.mapX2 + GamePanel.getGameWidth()/q/2)*q - cx),
				(int) (y-GameStage.mapY + GameStage.mapY2 + GamePanel.getGameHeight()/2  - cy),
				(int)(main.getWidth()*GamePanel.quality),
				(int) (main.getHeight()*GamePanel.quality), null);

		g.setColor(Color.RED);
		int xx = (int)((x-GameStage.mapX + GameStage.mapX2 + GamePanel.getGameWidth()/q/2)*q - cx);
		int yy = (int) (y-GameStage.mapY + GameStage.mapY2 + GamePanel.getGameHeight()/2 - cy);
		g.drawLine(xx + 5*q, yy, xx + 5*q, yy+10*q);
		g.drawLine(xx, yy + 5*q, xx+10*q, yy + 5*q);
		
		g.drawString("" + hp, xx, yy);
	}

	double vx;
	double vy;

	@Override
	public void update() {
		if(isVK_RIGHT)	vx += 1;
		if(isVK_LEFT) vx -= 1;
		
//		x += vx;
		for (int i = 0; i < Math.abs(vx)-1; i++) {
			x += vx/Math.abs(vx);
			if(isWall(0)) {
				x -= vx/Math.abs(vx);
				vx = 0;
				break;
			}
		}
//		if(isWall()) {
//			x -= vx/2;
//			if(isWall()) {
//				x -= vx/2;
//				vx = vx/2;
//			}
//		}

		for (int i = 0; i < Math.abs(vy)-1; i++) {
			y += vy/Math.abs(vy);
			if(isWall(0)) {
				y -= vy/Math.abs(vy);
				vy = 0;
				break;
			}
		}

		if(isWall(1)) { // Лестница
			if(isVK_UP)	vy = -4;
			if(isVK_DOWN) vy += 1;
			vy*=0.8;
			if(vy > 4) {
				vy = 4;
			}
			if(vy < -4) {
				vy = -4;
			}
		}else {
			vy+=1;
			y+=1;
			if(isWall(0)) {
				if(isVK_UP)	vy = -10;
			}
			y-=1;
		}
		vx*=0.8;

		if(isWall(3)) {
			touchTrap();
		}
		
		checkTouch();
	}
	
	private void touchTrap() {
		hp--;
		level.deleteTraps((int)(x/tilesize), (int) (y/tilesize));
	}
	
	private void checkTouch() {
		Rectangle hitbox = new Rectangle((int)getCutX(x), (int)getCutY(y), 10, 10);
		int x = (int)(getCutX(this.x));
		int y = (int)(getCutY(this.y));
		checkTouchOne(hitbox, x, y);
		checkTouchOne(hitbox, x+10, y);
		checkTouchOne(hitbox, x, y+10);
		checkTouchOne(hitbox, x+10, y+10);
	}
	
	private void checkTouchOne(Rectangle hitbox, int x, int y) {
		if(hitbox.intersects(GameStage.level.getHitbox(x/tilesize,y/tilesize,2)))
			GameStage.level.setHit(x/tilesize, y/tilesize, true);
	}

	private boolean isWall(int type) {
		Rectangle hitbox = new Rectangle((int)getCutX(x), (int)getCutY(y), 10, 10);
		//level.getHitbox((int)(x/tilesize), (int)(y/tilesize));
		return hitbox.intersects(level.getHitbox((int)(getCutX(x)/tilesize), (int)(getCutY(y)/tilesize), type))
				||
				hitbox.intersects(level.getHitbox((int)((getCutX(x)+10)/tilesize), (int)(getCutY(y)/tilesize), type))
				||
				hitbox.intersects(level.getHitbox((int)((getCutX(x)+10)/tilesize), (int)((getCutY(y)+10)/tilesize), type))
				||
				hitbox.intersects(level.getHitbox((int)(getCutX(x)/tilesize), (int)((getCutY(y)+10)/tilesize), type))
				;
				
//				hitbox.intersects(new Rectangle((int)x+11*tilesize, (int)y, 11*tilesize, 11*tilesize))
//				||
//				hitbox.intersects(new Rectangle((int)x+11*tilesize, (int)y+11*tilesize, 11*tilesize, 11*tilesize))
//				||
//				hitbox.intersects(new Rectangle((int)x, (int)y, 11*tilesize, 11*tilesize))
//				||
//				hitbox.intersects(new Rectangle((int)x, (int)y+11*tilesize, 11*tilesize, 11*tilesize));
	}
//	private boolean isWall2() {
//		return 1 != level.getBlock((int)Math.floor(x/16), (int)Math.floor(y/16));
//	}

	boolean isVK_UP;
	boolean isVK_DOWN;

	boolean isVK_RIGHT;
	boolean isVK_LEFT;

	

	public void setVK_UP(boolean b) {
		isVK_UP = b;
	}
	public void setVK_DOWN(boolean b) {
		isVK_DOWN = b;
	}
	public void setVK_RIGHT(boolean b) {
		isVK_RIGHT = b;
	}
	public void setVK_LEFT(boolean b) {
		isVK_LEFT = b;
	}

	private double getCutX(double x){
		if(x < 0) x = lw-((-x)%lw);
		x = x%lw;
		return x;
	}
	
	private double getCutY(double y){
		if(y < 0) y = lh-((-y)%lh);
		y = y%lh;
		return y;
	}
	
	public double getX() {
		return x;//tilesize*16/11;
	}
	public double getY() {
		return y;
	}
	
	
	public int getWDebug() {
		return (int) (x - GameStage.mapX + GameStage.mapX2 /*+ GamePanel.getGameWidth()/2 */- cx);
	}
}
