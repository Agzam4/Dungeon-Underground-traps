package Objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Work.LevelGenerator;

public abstract class GameObject {

	protected double x;
	protected double y;
	
	protected BufferedImage main;
	
	protected Rectangle hitbox;// = new Rectangle(x, y, width, height)
	
	public GameObject(BufferedImage main) {
		this.main = main;
	}

	public abstract void draw(Graphics2D g);
	public abstract void update();
	
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static boolean createIntersection(Rectangle r1, Rectangle r2) {

	     // Left x
	     int leftX = Math.max(r1.x, r2.x);

	     // Right x
	     int rightX = (int) Math.min(r1.getMaxX(), r2.getMaxX());

	     // TopY
	     int topY = Math.max(r1.y,r2.y);

	     // Bottom y
	     int botY =  (int) Math.min(r1.getMaxY(), r2.getMaxY());

	     return (rightX > leftX) && (botY > topY);
	 }

	boolean isLadder = false;
	boolean isWall = false;
	
	public void detect(LevelGenerator generator, int x, int y) {
		isLadder = false;
		isWall = false;

		detectOne(generator, x, y+11);
		detectOne(generator, x, y-11);
		detectOne(generator, x+11, y);
		detectOne(generator, x-11, y);
		detectOne(generator, x+11, y+11);
		detectOne(generator, x-11, y+11);;
		detectOne(generator, x+11, y-11);
		detectOne(generator, x-11, y-11);
	}
	
	private void detectOne(LevelGenerator generator, int x, int y) {
		if(0 == generator.getType(x/16, y/16)) {
			isWall = true;
		}
		if(9 == generator.getType(x, y)) {
			isLadder = true;
		}
	}
}
