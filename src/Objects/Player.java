package Objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Stages.GameStage;
import Work.LevelGenerator;
import Work.Loader;

public class Player extends GameObject {

	public static int tilesize = GameStage.tilesize;
	BufferedImage img = Loader.PLAYER;
	LevelGenerator level;
	int lw;
	int lh;
	
	int hp = 100;

	double cx = 5*GamePanel.quality;//-0.1;
	double cy = 5*GamePanel.quality;
	
	boolean gameOvers[] = new boolean[6];
	/**
	 * 0 - Победа
	 * 1 - Шипы
	 * 2 - Падающие шипы
	 * 3 - Лава
	 * 4 - Кнопка
	 * 5 - Дротик
	 */
	String gameover = "";
	
	public Player(LevelGenerator level) {
		super(Loader.PLAYER);
		isGameOver = false;
		this.level = level;
		lw = level.getWidth() * 16;
		lh = level.getHeight() * 16;
	}

	@Override
	public void draw(Graphics2D g) {
//		double k = GameStage.tilesize*GamePanel.quality;
		int q = (int) GamePanel.quality;
		int nx = (int) ((x-GameStage.mapX + GameStage.mapX2 + GamePanel.getGameWidth()/q/2)*q - cx);
		int ny = (int) (y-GameStage.mapY + GameStage.mapY2 + GamePanel.getGameHeight()/2  - cy);
		int nd = (int) (10*GamePanel.quality);
		if(c != null) {
			g.setColor(c);
			g.fillRect(nx, ny, nd, nd);
		}
		g.drawImage(img, nx, ny, nd, nd, null);

//		g.setColor(Color.RED);
//		int xx = (int)((x-GameStage.mapX + GameStage.mapX2 + GamePanel.getGameWidth()/q/2)*q - cx);
//		int yy = (int) (y-GameStage.mapY + GameStage.mapY2 + GamePanel.getGameHeight()/2 - cy);
//		g.drawLine(xx + 5*q, yy, xx + 5*q, yy+10*q);
//		g.drawLine(xx, yy + 5*q, xx+10*q, yy + 5*q);

//		int nx2 = (int) getCutX2(x+10); // -7536701444361536323
//		g.setColor(Color.GREEN);
//		g.drawString("X: " + x + " Y: " + y, GamePanel.getGameWidth()/2, GamePanel.getGameHeight()/2);
//		g.setColor(Color.RED);
//		g.drawString("X: " + getCutX2(x+10) + " Y: " + y, GamePanel.getGameWidth()/2, GamePanel.getGameHeight()/2 + 15);
//		g.setColor(Color.YELLOW);
//		g.drawString("X: " + nx2 + " TileX: " + (nx2/(double)(tilesize)), GamePanel.getGameWidth()/2, GamePanel.getGameHeight()/2 + 30);
	}

	double vx;
	double vy;
	
	private double speed = 1;

	// 1939783342040334186
	@Override
	public void update() {
		cx = 5*GamePanel.quality;//-0.1;
		cy = 5*GamePanel.quality;
		
		if(isVK_RIGHT)	vx += speed;
		if(isVK_LEFT) vx -= speed;
		
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
		
		// TODO

		if(x > level.getWidth()*16)
			x -= level.getWidth()*16;
		if(x < 0)
			x += level.getWidth()*16;
		

		if(y > level.getHeight()*16)
			y -= level.getHeight()*16;
		if(y < 0)
			y += level.getHeight()*16;

		
		if(untime > 0) {
			untime--;
		}else {
			if(checkTraps()) {//isWall(3)) {
				touchTrap();
			}
		}
		checkTouch();
	}
	
	int untime = 0;
	
	private void touchTrap() {
		if(gameOvers[0]) {
			isGameOver = true;
			return;
		}
		untime = 10;
		hp--;
		x = (int)(x/tilesize) * tilesize;
		y = (int)(y/tilesize) * tilesize;
		vx = 0;
		vy = 0;
		GameStage.radius = 0;
		if(hp < 1) {
			isGameOver = true;
		}else {
			level.deleteTraps((int)(x/tilesize), (int) (y/tilesize));
		}
		System.out.println("GameOver: " + gameover);
	}

	boolean isTrap = false;
	private boolean checkTraps() {
		gameOvers[0] = false;
		gameOvers[1] = false; // true
		gameOvers[2] = false;
		isTrap = false;
		checkTrapOne(0, 0);
		checkTrapOne(10, 0);
		checkTrapOne(0, 10);
		checkTrapOne(10, 10);
		return isTrap;
	}
	
	private void checkTrapOne(int xx, int yy) {
		if(isTrap(xx, yy)) {
			isTrap = true;
			int block = level.getBlock((int)(getCutX_x16(x+xx)/tilesize), (int)(getCutY(y+yy)/tilesize));
			if(block == level.BLOCK_CHEST) {
				gameOvers[0] = true;
			}
			if(block == level.BLOCK_SPIKY || block == level.BLOCK_SPIKY_UP) {
				gameOvers[1] = true;
			}
			if(block == level.BLOCK_SPIKY_FALL || block == level.BLOCK_SPIKY_ONE) {
				gameOvers[2] = true;
			}
			if(block == level.BLOCK_LAVA) {
				gameOvers[3] = true;
			}
		}
	}
	
	private void checkTouch() {
		Rectangle hitbox = new Rectangle((int)getCutX_x16(x), (int)getCutY(y), 10, 10);
		int x = (int)(getCutX_x16(this.x));
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
	
	private boolean isTrap(int xx, int yy) {
		Rectangle hitbox = new Rectangle((int)getCutX_x16(x), (int)getCutY(y), 10, 10);
		return hitbox.intersects(level.getHitbox((int)(getCutX_x16(x+xx)/tilesize), (int)(getCutY(y+yy)/tilesize), 3));
	}
	public Rectangle getHitbox() {
		return new Rectangle((int)getCutX_x16(x), (int)getCutY(y), 10, 10);
	}
	
	public boolean intersects(Rectangle hitbox1) {
		//level.getHitbox((int)(x/tilesize), (int)(y/tilesize));
		return hitbox1.intersects(new Rectangle((int)x, (int)y, 10, 10))
				||
				hitbox1.intersects(new Rectangle((int)x, (int)y+10, 10, 10))
				||
				hitbox1.intersects(new Rectangle((int)x+10, (int)y, 10, 10))
				||
				hitbox1.intersects(new Rectangle((int)x+10, (int)y+10, 10, 10))
				;
	}

	private boolean isWall(int type) {
		Rectangle h1 = new Rectangle((int)(x), (int)(y), 10, 10);
		Rectangle h2 = new Rectangle((int)(x+lw), (int)(y+lh), 10, 10);
		return isWall_UnCut(h1, x, y, type)
				|| isWall_UnCut(h1, x-lw, y, type)
				|| isWall_UnCut(h1, x, y-lh, type)
				|| isWall_UnCut(h1, x-lw, y-lh, type)
				
				|| isWall_UnCut(h2, x, y, type)
				|| isWall_UnCut(h2, x-lw, y, type)
				|| isWall_UnCut(h2, x, y-lh, type)
				|| isWall_UnCut(h2, x-lw, y-lh, type);
	}
	private boolean isWall_UnCut(Rectangle h, double x, double y, int type) {
		double tilesize = Player.tilesize; // TODO
		int nx = (int)((x)/tilesize);
		int nx2 = (int)((x+10)/tilesize);
		//level.getHitbox((int)(x/tilesize), (int)(y/tilesize));
		return 
				h.intersects(level.getHitbox(nx, (int)((y)/tilesize), type))
				||
				h.intersects(level.getHitbox(nx2, (int)((y)/tilesize), type))
				||
				h.intersects(level.getHitbox(nx2, (int)(((y)+10)/tilesize), type))
				||
				h.intersects(level.getHitbox(nx, (int)(((y)+10)/tilesize), type))
				;
				
//				hitbox.intersects(new Rectangle((int)x+11*tilesize, (int)y, 11*tilesize, 11*tilesize))
//				||
//				hitbox.intersects(new Rectangle((int)x+11*tilesize, (int)y+11*tilesize, 11*tilesize, 11*tilesize))
//				||
//				hitbox.intersects(new Rectangle((int)x, (int)y, 11*tilesize, 11*tilesize))
//				||
//				hitbox.intersects(new Rectangle((int)x, (int)y+11*tilesize, 11*tilesize, 11*tilesize));
	}
	
	private boolean isWall_Cut(double x, double y, int type) {
		Rectangle hitbox = new Rectangle((int) getCutX(x), (int)getCutY(y), 10, 10);
		double tilesize = Player.tilesize; // TODO
		int nx = (int)(getCutX(x)/tilesize);
		int nx2 = (int)(getCutX(x+10)/tilesize);
		//level.getHitbox((int)(x/tilesize), (int)(y/tilesize));
		return 
				hitbox.intersects(level.getHitbox(nx, (int)(getCutY(y)/tilesize), type))
				||
				hitbox.intersects(level.getHitbox(nx2, (int)(getCutY(y)/tilesize), type))
				||
				hitbox.intersects(level.getHitbox(nx2, (int)((getCutY(y)+10)/tilesize), type))
				||
				hitbox.intersects(level.getHitbox(nx, (int)((getCutY(y)+10)/tilesize), type))
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

	private double getCutX_x16(double x){ // TODO
		if(x < 0) 
			x = lw-((-x)%lw);
		if(x > lw)
			x = x%lw;
		return x;
	}
	private double getCutX2(double x){ // TODO
		if(x < 0) 
			x = lw-((-x)%lw);
		if(x > lw)
			x = x%lw;
		return x;
	}
	private double getCutX(double x){ // TODO
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
	
	public boolean isGameOver = false;

//	public boolean isGameOver() {
//		return isGameOver;
//	}
	
	
	public boolean[] getGameOvers() {
		return gameOvers;
	}

	public void setButttonGameOver() {
		gameOvers[4] = true;
	}
	
	public void setGameOver(int id) {
		gameOvers[id] = true;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	Color c = null;
	
	public void setColor(Color c) {
		this.c = c;
	}
	public void setImg(BufferedImage img) {
		this.img = img;
	}
}
