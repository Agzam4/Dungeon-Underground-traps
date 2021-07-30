package Main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Stages.Maneger;
import Work.GameData;
import Work.MouseController;
import Work.Render;

public class GamePanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	public static double quality = 5;
//	public double moveX;
//	public double moveY;

	public static int frameW;
	public static int frameH;
	public static double scale;
	public static double scalefull;

	public static BufferedImage all;
	public static BufferedImage game;
	public static BufferedImage gamefull;
	public static int gameX;
	public static int gameY;
	public static boolean running;
	
	public static boolean stop = false;

	private static int FPS = 60;
	private static long sleep = 50;//1000 / FPS;
	
	static Maneger maneger = new Maneger(Maneger.MENU);
	
	public GamePanel() {
		setBackground(Color.BLACK);
		setFocusable(true);
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
//				GamePanel.stop = true; TODO
				pausedIsDrawed = false;
				maneger.releasedAll();
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
//				GamePanel.stop = false;
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				System.out.println("getPreciseWheelRotation: " + e.getPreciseWheelRotation());
				System.out.println("getScrollAmount: " + e.getScrollAmount());
				System.out.println("getScrollType: " + e.getScrollType());
				System.out.println("getUnitsToScroll: " + e.getUnitsToScroll());
				System.out.println("getWheelRotation: " + e.getWheelRotation());
				System.out.println("getWhen: " + e.getWhen());
				MouseController.mouseScroll -= e.getPreciseWheelRotation();
			}
		});
	}
	
	public void run(DungeonJFrame frame) {
		running = true;
		calculateScreen(frame);

		Thread update = new Thread() {
			public void run() {
				long start;
				long wait;
				while (running) {
					start = System.nanoTime();
					updtae();
					if(!GameData.skipFrames) {
						draw((Graphics2D)getGraphics());
					}
					wait = sleep - (System.nanoTime() - start)/1000000;
					if(wait < 0) wait = 5;
					try {
						Thread.sleep(wait);
					} catch (InterruptedException e) {
					}
					if(quality != GameData.quality) {
						quality = GameData.quality;
						calculateScreen(frame);
					}
				}
			}
		};
		update.start();
		Thread draw = new Thread() {
			public void run() {
				long start;
				long wait;
				while (running) {
					if(GameData.skipFrames) {
						start = System.nanoTime();
						draw((Graphics2D)getGraphics());
						wait = sleep - (System.nanoTime() - start)/1000000;
						if(wait < 0) wait = 1;
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
		
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {
				System.out.println("componentShown");
				
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				System.out.println("componentResized");
				calculateScreen(frame);
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				System.out.println("componentMoved");
				
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
				
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				MouseController.setPosition(e.getX(), e.getY());
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				MouseController.setPosition(e.getX(), e.getY());
			}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				MouseController.isMousePressed = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				MouseController.isMousePressed = true;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
	}
	
	public static int getUntileDistanse() {
		return (int) ((getGameWidth() - getCountWidth()*16*quality)/2);
	}


	private void updtae() {
//		System.out.println(MouseController.getMousePoint().x + "x" + MouseController.getMousePoint().y);
		if(!stop)
		maneger.update();
	};

	final AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1f);
	final AlphaComposite normal = AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1f);
	
	boolean pausedIsDrawed = false;
	
	@Override
	public void paint(Graphics g) {
		draw((Graphics2D) g);
	}
	
	private void draw(Graphics2D thisG) {
		if(thisG == null) {
			return;
		}
//		Graphics2D thisG = (Graphics2D) getGraphics();
		if(stop) {
			if(!pausedIsDrawed) {
				Graphics2D a = (Graphics2D) all.getGraphics();
				a.setColor(new Color(0,0,0,200));
				a.fillRect(0, 0, frameW, frameH);
				a.setFont(new Font("Arial", Font.PLAIN, (int) (scalefull*15)));
				a.setColor(new Color(255,255,255,200));
				a.drawString("Paused", (frameW-a.getFontMetrics().stringWidth("Paused"))/2, frameH/2);
				pausedIsDrawed = true;
			}
			thisG.drawImage(all, 0, 0, null);
			return;
		}
		Graphics2D g = (Graphics2D) game.getGraphics();
		Graphics2D gf = (Graphics2D) gamefull.getGraphics();
		

		Render.addRenderingHints(g);
		Render.addRenderingHints(gf);

		
		gf.setComposite(alpha);
		gf.setColor(new Color(0,0,0,0));
		gf.fillRect(0, 0, frameW, frameH);
		gf.setComposite(normal);
		
		gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (5*scalefull))); // TODO
		
		maneger.draw(g, gf);
//		g.setColor(Color.WHITE);
//		for (int y = 0; y < 25; y++) {
//			for (int x = 0; x < 25; x++) {
//				g.drawRect(x*16, y*16, 15, 15);
//			}
//		}
		g.dispose();
		

		Graphics2D a = (Graphics2D) all.getGraphics();
		a.drawImage(game, gameX, gameY,
				(int)(game.getWidth()*scale), (int) (game.getHeight()*scale), null);
		a.drawImage(gamefull, 0, 0, null);
		a.dispose();
		
		thisG.drawImage(all, 0, 0, null);
		
//		thisG.setColor(new Color(0,255,180));
//		thisG.drawLine(frameW/2, 0, frameW/2, frameH);
//		thisG.drawLine(0, frameH/2, frameW, frameH/2);
//		thisG.setColor(new Color(0,255,180));
//		thisG.drawString("v0.0.0", (int) (15 - GamePanel.gameX), 50);
//		thisG.drawString("Seed: " + maneger.getLevelGenerator().getSeed(), -GamePanel.gameX - 15, getGameHeight()-25+gameY);
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
		if(e.isControlDown() && e.isAltDown()) {
			switch (e.getKeyCode()) {
			case  KeyEvent.VK_1:
				Render.setSpeed();
				System.out.println("Render.speed");
				break;
			case  KeyEvent.VK_2:
				Render.setQuality();
				System.out.println("Render.quality");
				break;
			case  KeyEvent.VK_0:
				Render.setDefault();
				System.out.println("Render.default");
				break;
			default:
				break;
			}
		}else {
			maneger.keyPressed(e);
		}
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
	
	public void calculateScreen(DungeonJFrame frame) {
		frameW = frame.getWidth();
		frameH = frame.getHeight();
		double tq = quality*16;
		scalefull = (int) Math.floor(frameH/200);
		scale = (int) Math.ceil(frameH/16f/11f)/quality;
		game = new BufferedImage((int) ((int) Math.ceil(frameW/scale/tq/2f)*tq*2f + tq),
				(int) (Math.ceil(16*11*quality/tq)*tq), BufferedImage.TYPE_INT_RGB);
		/**
		 * TYPE_INT_RGB
		 * 
		 * TODO
		 * 
		 * TYPE_USHORT_555_RGB	// ROUND
		 * TYPE_BYTE_INDEXED	// PIXEL
		 * TYPE_BYTE_GRAY		// GRAY
		 * TYPE_BYTE_BINARY		// W&B
		 */
				
		gamefull = new BufferedImage(frameW, frameH, BufferedImage.TYPE_INT_ARGB);
		all = new BufferedImage(frameW, frameH, BufferedImage.TYPE_INT_RGB);
		gameX = (int) (frameW - Math.floor(game.getWidth()/16)*scale*16)/2;
		gameY = (int) (frameH - Math.floor(game.getHeight()/16)*scale*16)/2;

		System.out.println("Frame: " + frameW + "x" + frameH);
		System.out.println("Game: " + getGameWidth() + "x" + getGameHeight());
		System.out.println("GameTiles: " + getCountWidth() + "x" + getCountHeight());
		System.out.println("Scale: " + scale);
		System.out.println("ScaleFull: " + scalefull);
		System.out.println("Quality: " + quality);
	}
	
	
	public static void reloadTexts() {
		maneger.reloadTexts();
	}
}
