package Objects;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;
import Stages.GameStage;
import Work.Loader;

public class OtherPlayers {

	double x,y;
	int w,h;

	public int id = 0;
	public int players = 1;
	
	String name = null;

	public OtherPlayers(int id) {
		w = 10;
		h = 10;
		this.id = id;
	}
	
	public void draw(Graphics2D g) {
		int nx = (int) ((x - GameStage.mapX - w/2)*GamePanel.quality + GamePanel.getGameWidth()/2);
		int ny = (int) ((y - GameStage.mapY - h/2)*GamePanel.quality + GamePanel.getGameHeight()/2);
		int nw = (int) (w*GamePanel.quality);
		int nh = (int) (h*GamePanel.quality);
		if(players < 0)
			players = 1;
		g.setColor(getColor(id, players));
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
}
