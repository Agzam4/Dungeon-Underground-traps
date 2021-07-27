package Objects;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;
import Stages.GameStage;
import Work.Loader;

public class OtherPlayers {

	// 6745331085857661477;
	
	double x,y;
	int w,h;

	public int id = 0;
	public int players = 1;
	
	boolean isVisible = true;
	
	int lw, lh;
	
	String name = null;

	public OtherPlayers(int id) {
		w = 10;
		h = 10;
		this.id = id;
	}
	
	public void draw(Graphics2D g) {
		if(!isVisible) return;
		int nw = (int) (w*GamePanel.quality);
		int nh = (int) (h*GamePanel.quality);
		if(players < 0)
			players = 1;
		g.setColor(getColor(id, players));

		drawPlayer(g, x, y, nw, nh);

		drawPlayer(g, x+lw, y, nw, nh);
		drawPlayer(g, x-lw, y, nw, nh);

		drawPlayer(g, x, y+lh, nw, nh);
		drawPlayer(g, x, y-lh, nw, nh);

		drawPlayer(g, x-lw, y-lh, nw, nh);
		drawPlayer(g, x+lw, y-lh, nw, nh);
		drawPlayer(g, x-lw, y+lh, nw, nh);
		drawPlayer(g, x+lw, y+lh, nw, nh);
	}
	
	private void drawPlayer(Graphics2D g, double x, double y, int nw, int nh) {
		int nx = (int) ((x - GameStage.mapX - w/2)*GamePanel.quality + GamePanel.getGameWidth()/2);
		int ny = (int) ((y - GameStage.mapY - h/2)*GamePanel.quality + GamePanel.getGameHeight()/2);
		g.fillRect(nx, ny, nw, nh);
		g.drawImage(Loader.PLAYER2, nx, ny, nw, nh, null);
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static Color getColor(int id, int players) {
		return new Color(Color.HSBtoRGB((1/(float)(players))*id, 1f, 1f));
	}
	
	public static Color getColorLight(int id, int players) {
		return new Color(Color.HSBtoRGB((1/(float)(players))*id, 0.5f, 1f));
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		if(name == null) return "Player" + id;
		return name;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public void hide() {
		isVisible = false;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setLB(int lw, int lh) {
		this.lw = lw*16;
		this.lh = lh*16;
	}
	
}
