package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Stages.Maneger;
import Work.GameData;

public class GamePanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	public static final double quality = 5;
//	public double moveX;
//	public double moveY;

	public static int frameW;
	public static int frameH;
	public static double scale;
	
	public static BufferedImage game;
	public static int gameX;
	public static int gameY;

	private static int FPS = 60;
	private static long sleep = 50;//1000 / FPS;
	
	Maneger maneger;
	
	public GamePanel() {
		setBackground(Color.BLACK);
		setFocusable(true);
	}
	
	public void run(DungeonJFrame frame) {
		
		frameW = frame.getWidth();
		frameH = frame.getHeight();
		double tq = quality*16;
		scale = (int) Math.ceil(frameH/16f/11f)/quality;
		game = new BufferedImage((int) ((int) Math.ceil(frameW/scale/tq)*tq),
				(int) (Math.ceil(16*11*quality/tq)*tq), BufferedImage.TYPE_INT_RGB);
		gameX = (int) (frameW - Math.floor(game.getWidth()/16)*scale*16)/2;
		gameY = (int) (frameH - Math.floor(game.getHeight()/16)*scale*16)/2;
		
		maneger = new Maneger(Maneger.GAME);

		Thread update = new Thread() {
			public void run() {
				long start;
				long wait;
				while (true) {
					start = System.nanoTime();
					updtae();
					if(!GameData.skipFrames) {
						draw();
					}
					wait = sleep - (System.nanoTime() - start)/1000000;
					if(wait < 0) wait = 5;
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		update.start();
		Thread draw = new Thread() {
			public void run() {
				long start;
				long wait;
				while (true) {
					if(GameData.skipFrames) {
						start = System.nanoTime();
						draw();
						wait = sleep - (System.nanoTime() - start)/1000000;
						if(wait < 0) wait = 5;
						try {
							Thread.sleep(wait);
						} catch (InterruptedException e) {
						}
					}else {
						try {
							Thread.sleep(sleep);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		};
		draw.start();
		
		addKeyListener(this);

		System.out.println("Frame: " + frameW + "x" + frameH);
		System.out.println("Game: " + getGameWidth() + "x" + getGameHeight());
		System.out.println("GameTiles: " + getCountWidth() + "x" + getCountHeight());
		System.out.println("Scale: " + scale);
		System.out.println("Quality: " + quality);
	}
	
	public static int getUntileDistanse() {
		return (int) ((getGameWidth() - getCountWidth()*16*quality)/2);
	}


	private void updtae() {
		maneger.update();
	};
	
	private void draw() {
		Graphics2D g = (Graphics2D) game.getGraphics();
		
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (5*quality)));
		
		maneger.draw(g);
//		g.setColor(Color.WHITE);
//		for (int y = 0; y < 25; y++) {
//			for (int x = 0; x < 25; x++) {
//				g.drawRect(x*16, y*16, 15, 15);
//			}
//		}
		g.dispose();
		
		Graphics2D thisG = (Graphics2D) getGraphics();
		thisG.drawImage(game, gameX, gameY,
				(int)(game.getWidth()*scale), (int) (game.getHeight()*scale), null);
		thisG.setColor(new Color(0,255,180));
//		thisG.drawLine(frameW/2, 0, frameW/2, frameH);
//		thisG.drawLine(0, frameH/2, frameW, frameH/2);
		thisG.setColor(new Color(0,255,180));
//		thisG.drawString("v0.0.0", (int) (15 - GamePanel.gameX), 50);
		thisG.drawString("Seed: " + maneger.getLevelGenerator().getSeed(), -GamePanel.gameX - 15, 25);
		thisG.dispose();
	}
	

	public static int getGameWidth() {
		return game.getWidth();
	}
	
	public static int getGameHeight() {
		return game.getHeight();
	}

	public static int getCountWidth() {
		return (int) (Math.floor(game.getWidth()/16f)/quality);
	}
	public static int getCountHeight() {
		return (int) (Math.floor(game.getHeight()/16f)/quality);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		maneger.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		maneger.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	
	public static double getScale() {
		return scale;
	}
}
