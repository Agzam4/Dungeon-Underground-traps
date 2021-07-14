package Stages;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Iterator;


import Main.GamePanel;
import Objects.Button;
import Objects.JOptionPane;
import Work.GameData;
import Work.LevelGenerator;
import Work.Loader;
import Work.MouseController;
import Work.MyAudio;

public class MenuStage extends Stage {

	private static final int PLAY = 0;
	private static final int PLAY_ON_SEED = 1;
	private static final int MULTIPLAYER = 2;
	private static final int SETTINGS = 3;
	private static final int ATCHIVMENTS = 4;
	private static final int EXIT = 5;

	MyAudio pop;
	MyAudio click;
	Maneger maneger;
	
	public MenuStage(Maneger m) {
		for (int i = 0; i < myButtons.length; i++) {
			myButtons[i] = new Button(GameData.texts[i], 0, 0);
		}
//		myButtons[MULTIPLAYER].clickable = false;
//		myButtons[SETTINGS].clickable = false;
		pop = new MyAudio("/sounds/pop-1.wav");
		click = new MyAudio("/sounds/pop-2.wav");
		maneger = m;
	}
	
	String buttons[] = new String[6];
	Button myButtons[] = new Button[buttons.length];
	
	float buttonsv[] = {0, 0, 0, 0, 0, 0};
	int buttonsW[] = {0, 0, 0, 0, 0, 0};
	boolean onButtons[] = {false, false, false, false, false, false};
	private float stroke;
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) {

		if(optionPane != null) {
			optionPane.draw(gf);
		}
		
		for (int i = 0; i < myButtons.length; i++) {
			myButtons[i].draw(gf);
		}
		
//		drawButtons(gf);

		gf.setColor(Color.WHITE);
		gf.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (25*GamePanel.scalefull))); // TODO
		drawText(gf, "Dungeon", (int) ((1.5)*dist));
		setStroke(gf, 3);
		drawLines(gf, " Dungeon ", (int) ((1.5)*dist));
		
		gf.setPaint(GameOverStage.getGradient(Loader.COLOR_GAME_BG));
		gf.fillRect(0, 0, GamePanel.frameW, GamePanel.frameH);
		
	}
	
	private void drawText(Graphics2D gf, String s, int y) {
		gf.drawString(s, (GamePanel.frameW - gf.getFontMetrics().stringWidth(s))/2, y);
	}	
	
	private void setStroke(Graphics2D gf, float v) {
		stroke = (float) (GamePanel.scalefull*v);
		gf.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	}

	private void drawLines(Graphics2D gf, String s, int yy) {
		drawDownLine(gf, gf.getFontMetrics().stringWidth(s), yy);
		drawUpLine(gf, gf.getFontMetrics().stringWidth(s), yy);
	}
	private void drawDownLine(Graphics2D gf, int w, int yy) {
		int x = (GamePanel.frameW - w)/2;
		int y =   yy + (int)(GamePanel.scalefull*stroke);
		gf.drawLine(x,  y, x+w,  y);
	}
	private void drawUpLine(Graphics2D gf, int w, int yy) {
		int x = (GamePanel.frameW - w)/2;
		int y =  yy - gf.getFont().getSize();
		gf.drawLine(x,  y, x+w,  y);
	}

	int dist = 0;
	
	private void drawButtons(Graphics2D gf) {
		for (int i = 0; i < buttons.length; i++) {
			gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) ((15+buttonsv[i])*GamePanel.scalefull))); // TODO
			buttonsW[i] = gf.getFontMetrics().stringWidth(buttons[i]);
//			drawString(gf, buttons[i], 10, (2+i)*dist);
			drawCenterString(gf, buttons[i], (3+i)*dist);
//			gf.drawString(buttons[i], );
		}
		
	}

	private void drawCenterString(Graphics2D gf, String str, int y) {
		int w = gf.getFontMetrics().stringWidth(str);
//		gf.setColor(Color.GREEN);
//		gf.drawRect((int) (GamePanel.frameW/2 - w/2), y-gf.getFont().getSize(), w, gf.getFont().getSize());
		drawString(gf, str, (int) (GamePanel.frameW/2 - w/2), y);
	}
	
	private void drawString(Graphics2D gf, String str, int x, int y) {
		gf.setColor(Loader.COLOR_TEXT_FG);
		gf.drawString(str, x, y);
		gf.setColor(Loader.COLOR_TEXT_BG);
		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy <2; yy++) {
				gf.drawString(str, (int) (x+xx*GamePanel.scalefull/1.5), (int) (y+yy*GamePanel.scalefull/1.5));
			}
		}
	}
	
	JOptionPane optionPane;
	
	int selected = -1;

	@Override
	public void update() {
		if(optionPane != null) {
			optionPane.update();
			if(optionPane.needClose) {
				if(optionPane.isOKpressed) {
					String seed = optionPane.getInput();
					try {
						maneger.setGameStage(Long.parseLong(seed));
					} catch (NumberFormatException e) {
						char[] sc = seed.toCharArray();
						long newSeed = Long.MIN_VALUE;
						for (int j = 0; j < sc.length; j++) {
							newSeed += (int) sc[j] * (j+1) * Character.MAX_CODE_POINT;
						}
						maneger.setGameStage(newSeed);
					}
				}
				optionPane = null;
			}
			return;
		}
		dist = GamePanel.frameH/(buttons.length+4);
		int x = MouseController.getMousePointOnFrame().x;
		int y = MouseController.getMousePointOnFrame().y;
		
		for (int i = 0; i < buttons.length; i++) {
			myButtons[i].update();
			int bw = buttonsW[i];
			int bh = (int) ((15+buttonsv[i])*GamePanel.scalefull);
			int bx = GamePanel.frameW/2 - bw/2;
			int by = (3+i)*dist - bh;
			myButtons[i].setPosition(GamePanel.frameW/2, (3+i)*dist);
			onButtons[i] = bx < x && bx+bw > x && by < y && by+bh > y;
				
			if(myButtons[i].isClicked()) {
//				click.play(0);

				switch (i) {
				case PLAY:
					maneger.loadStage(Maneger.GAME);
					break;
				case PLAY_ON_SEED:
					optionPane = new JOptionPane(JOptionPane.TYPE_INPUT, "Seed: ");
					break;
				case MULTIPLAYER:
					maneger.loadStage(Maneger.MULTIPLAYER);
					break;
					
				case SETTINGS:
					maneger.loadStage(Maneger.SETTINGS);
					break;
				case ATCHIVMENTS:
					maneger.loadStage(Maneger.ACHIEVEMENTS);
					break;
				case EXIT:
					GameData.save();
					System.exit(0);
					break;
				default:
					break;
				}
			}
			
			if(onButtons[i]) {
				if(selected != i) {
					selected = i;
					pop.play(0);
				}
				buttonsv[i] = (float) ((buttonsv[i] - 2*GamePanel.scalefull)*0.6 + 2*GamePanel.scalefull);
				
			}else {
				buttonsv[i] *= 0.6;
			}
		}
	}
	
	@Override
	protected void keyPressed(KeyEvent e) {
		if(optionPane != null) {
			optionPane.keyPressed(e);
		}
	}

	@Override
	protected void keyReleased(KeyEvent e) {
		
	}

	@Override
	protected LevelGenerator getLevelGenerator() {
		return null;
	}

	@Override
	protected void releasedAll() {
	}

	@Override
	protected void reloadTexts() {
		for (int i = 0; i < myButtons.length; i++) {
			myButtons[i].setText(GameData.texts[i]);
		}
	}

}
