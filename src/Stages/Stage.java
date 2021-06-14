package Stages;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Work.LevelGenerator;

public abstract class Stage {
	
	public static final int tilesize = 16;

	public abstract void draw(Graphics2D g, Graphics2D gf);
	public abstract void update();
	protected abstract void keyPressed(KeyEvent e);
	protected abstract void keyReleased(KeyEvent e);
	protected abstract void releasedAll();
	protected abstract LevelGenerator getLevelGenerator();
}
