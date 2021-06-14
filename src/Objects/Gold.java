package Objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Main.GamePanel;

public class Gold {

	BufferedImage img;
	float size = (float) (1f * GamePanel.scalefull*2f);

	double x;
	double y;
	
	double vx = Math.random()*10 - 5;
	double vy = Math.random()*10 - 5;
	double dir = -1;
	double tdir = 0;
	double speed = 0;
	
	double tx;
	double ty;
	
	public Gold(int type) {
//		img = GameStage.tiles[20 + type].getSubimage(7, 7, 2, 2);
	}
	
	public void update() {
//		vx += (tx - x)/Math.abs(tx - x)/(Math.random()*5);
//		x = (x-tx) * (0.5 + Math.random()/2) + tx;
//		y = (y-ty) * (0.5 + Math.random()/2) + ty;
//		x += vx;
//		y += vy;
//
////		vy *= 0.98;
////		vx *= 0.98;
//
//		vy-= vy/Math.abs(vy);
//		vx-= vx/Math.abs(vx);
//		tdir = Math.atan(Math.abs(tx-x)/Math.abs(ty-y));
//				
//		dir = tdir;//(dir - tdir)*0.25 + tdir;
//
//		System.out.println(tdir + " " + dir);
//		y += Math.cos(dir)*10;
//		x += Math.sin(dir)*10;
		double wx = tx-x;
		double wy = ty-y;
		tdir = Math.atan2(wx,wy);
//		if(dir < tdir)
//			dir += Math.toRadians(15);
//		if(dir > tdir)
//			dir -= Math.toRadians(15);
		dir = (dir-tdir) * 0.95 + tdir;
		dir = -Math.abs(dir);
		speed += (10-Math.sqrt(wx*wx + wy*wy))/-50;
		if(speed > 25)
			speed = 25;
		if(speed < -25)
			speed = -25;
		vx = Math.sin(dir)*speed;
		vy = Math.cos(dir)*speed;
		
		x+=vx;
		y+=vy;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(img, (int)x, (int)y, (int) (2*size), (int) (2*size), null);
	}

	public void setTargetPosition(int tx, int ty) {
		dir =  Math.atan((tx-x+10)/(ty-y-1));
		this.tx = tx;
		this.ty = ty;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
