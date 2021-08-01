package Maps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Work.LevelGenerator;
import Work.Loader;
import Work.MyFile;

public class LevelEditor extends JPanel {

	private static final long serialVersionUID = 1L;

	int w = 50, h = 50;
	int[][] map;
	
	public LevelEditor() {
		map = new int[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				map[x][y] = 2;
			}
		}
		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				map[w/2 + x][h/2 + y] = 1;
			}
		}
		
		try {
			map = ((MapSerializable) MyFile.readObject(".map")).getMap();
		} catch (ClassNotFoundException | IOException e2) {
			e2.printStackTrace();
		}
		
		setFocusable(true);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					vx -= blockSize/2;
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					vx += blockSize/2;
				}
				if(e.getKeyCode() == KeyEvent.VK_UP) {
					vy += blockSize/2;
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					vy -= blockSize/2;
				}

				if(e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
					MapSerializable mapSerializable = new MapSerializable();
					mapSerializable.setMap(map);
					MyFile.saveObject(mapSerializable, ".map");
					JLevelEditor.frame.setTitle("LevelEditor");
					System.err.println("SAVED!");
				}

				if(e.getKeyCode() == KeyEvent.VK_N && e.isControlDown() && e.isAltDown()) {
					map = new int[w][h];
					for (int y = 0; y < h; y++) {
						for (int x = 0; x < w; x++) {
							map[x][y] = 2;
						}
					}
					for (int y = -1; y < 2; y++) {
						for (int x = -1; x < 2; x++) {
							map[w/2 + x][h/2 + y] = 1;
						}
					}
				}
				

				if(e.getKeyCode() == KeyEvent.VK_L && e.isControlDown() && e.isShiftDown()) {
					try {
						BufferedImage img = ImageIO.read(new File("l.png"));
						w = img.getWidth();
						h = img.getHeight();
						map = new int[w][h];
						for (int y = 0; y < h; y++) {
							for (int x = 0; x < w; x++) {
								map[x][y] = img.getRGB(x, y) == new Color(0,0,0).getRGB() ?
										2 : 1;
							}
						}
						for (int y = -1; y < 2; y++) {
							for (int x = -1; x < 2; x++) {
								map[w/2 + x][h/2 + y] = 1;
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				replase(e);
			}	

			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				replase(e);
			}
		});
		
		Thread update = new Thread(() -> {
			while (true) {
				x += vx;
				vx *= 0.8;
				y += vy;
				vy *= 0.8;
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}
			}
		});
		update.start();
	}	
	
	private void replase(MouseEvent e) {
		try {
			map[(int) ((e.getX()-x)/blockSize)][(int) ((e.getY()-y)/blockSize)] = JLevelEditor.selected;
		} catch (ArrayIndexOutOfBoundsException e2) {
		}
		repaint();		
		JLevelEditor.frame.setTitle("LevelEditor*");		
	}
	
	
	@Override
	public void paintAll(Graphics g) {
		draw((Graphics2D) g);
	}
	@Override
	public void paint(Graphics g) {
		draw((Graphics2D) g);
	}

	double x = 0, y = 0;
	double vx = 0, vy = 0;
	int blockSize = 32;
	
	private void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.WHITE);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				g.drawImage(Loader.tileset[map[x][y]-1].getImg(),
						(int)this.x + x*blockSize, (int)this.y + y*blockSize, blockSize, blockSize, null);
			}
		}
	}
}
