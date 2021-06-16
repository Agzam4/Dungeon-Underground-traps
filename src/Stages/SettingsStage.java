package Stages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.TimeZone;

import javax.swing.JOptionPane;

import Main.DungeonJFrame;
import Main.GamePanel;
import Objects.Button;
import Work.GameData;
import Work.LevelGenerator;
import Work.MyFile;

public class SettingsStage extends Stage {

	Maneger maneger;
	Button button = new Button(GameData.texts[GameData.TEXT_BACK], 0, 0);
	
	int selected = 0;
	
	Button settings[] = new Button[4];
	String txt[] = {"Game", "Graphics", "Audio", "Control"};
	
	public SettingsStage(Maneger maneger) {
		this.maneger = maneger;
		for (int i = 0; i < settings.length; i++) {
			settings[i] = new Button(txt[i], 0, 0);
			settings[i].chageSize = false;
		}
		for (int i = 0; i < audioButtons.length; i++) {
			audioButtons[i] = new Button("", 0, 0);
			audioButtons[i].chageSize = false;
		}
		for (int i = 0; i < setControl.length; i++) {
			setControl[i] = new Button("", 0, 0);
		}
		minus.chageSize = false;
		plus.chageSize = false;
		deleteprogress.chageSize = false;
		fullscreen.chageSize = false;
	}
	
	int dist = 0;
	int sh = 0;
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) {
		button.draw(gf);
		for (int i = 0; i < settings.length; i++) {
			settings[i].draw(gf);
		}
		
		gf.setColor(Color.WHITE);
		sh = (GamePanel.frameH/6*4 - 5)/150;
		gf.drawRect(sx+5, 5, GamePanel.frameW - sx - 30, sh*150);
		setSize(gf, 15);
		switch (selected) {
		case 0:
			drawGame(gf);
			break;
		case 1:
			drawGraphics(gf);;
			break;
		case 2:
			drawAudio(gf);
			break;
		case 3:
			drawControl(gf);
			break;
		default:
			break;
		}

		gf.setPaint(GameOverStage.getGradient(Color.DARK_GRAY));
		gf.fillRect(0, 0, GamePanel.frameW, GamePanel.frameH);
	}
	
	Button setControl[] = new Button[5];
	int maxXControl = 0;
	
	Button deleteprogress = new Button("Delete progress", 0, 0);
	
	private void drawControl(Graphics2D gf) {
		drawCenterString(gf, "Control", 1, Color.WHITE, Color.BLACK);
		setSize(gf, 10);
		maxXControl = 0;

//		updateMaxXControl(gf.getFontMetrics().stringWidth("Pause: ")+ gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[0]))/2);
//		updateMaxXControl(gf.getFontMetrics().stringWidth("Jump: ") 	+ gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[1]))/2);
//		updateMaxXControl(gf.getFontMetrics().stringWidth("Down: ") + gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[2]))/2);
//		updateMaxXControl(gf.getFontMetrics().stringWidth("Right: ")+ gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[3]))/2);
//		updateMaxXControl(gf.getFontMetrics().stringWidth("Left: ") + gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[4]))/2);

		updateMaxXControl(gf.getFontMetrics().stringWidth("Pause: ") + gf.getFontMetrics().stringWidth("Press key to set")/2);
		updateMaxXControl(gf.getFontMetrics().stringWidth("Pause: ") + gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[0]))/2);
		updateMaxXControl(gf.getFontMetrics().stringWidth("Pause: ") + gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[1]))/2);
		updateMaxXControl(gf.getFontMetrics().stringWidth("Pause: ") + gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[2]))/2);
		updateMaxXControl(gf.getFontMetrics().stringWidth("Pause: ") + gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[3]))/2);
		updateMaxXControl(gf.getFontMetrics().stringWidth("Pause: ") + gf.getFontMetrics().stringWidth(KeyEvent.getKeyText(GameData.control[4]))/2);
		
		drawString(gf, "Pause: ", 15, 			25, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, "Up: ", 15, 				35, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, "Left: ", 15, 			45, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, "Down: ", 15, 			55, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, "Right: ", 15, 			65, Color.LIGHT_GRAY, Color.BLACK);
		for (int i = 0; i < setControl.length; i++) {
			setControl[i].draw(gf);
		}
	}
	private void updateMaxXControl(int nx) {
		if(nx > maxXControl) {
			maxXControl = nx;
		}
	}

	private void setSize(Graphics2D gf, double s) {
		gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (s*sh)));
	}

	Button plus = new Button("+", 0, 0);
	Button minus = new Button("-", 0, 0);
	private void drawGraphics(Graphics2D gf) {
		drawCenterString(gf, "Graphics", 1, Color.WHITE, Color.BLACK);
		setSize(gf, 10);
		sgqt = gf.getFontMetrics().stringWidth("Game quality: " + GameData.quality) + 25 + sgqm;
		drawString(gf, "Game quality: " + GameData.quality, 25 + sgqm, 25, Color.LIGHT_GRAY, Color.BLACK);
		plus.draw(gf);
		minus.draw(gf);
		setSize(gf, 7);
		drawString(gf, "All the textures in the game are drawn by me", 15, 45, Color.GRAY, Color.BLACK);
	}
	
	String audioV[] = {"off", "quietly", "on"};
	Button audioButtons[] = new Button[3];
	int audioW[] = new int[3];
	
	private void drawAudio(Graphics2D gf) {
		drawCenterString(gf, "Audio", 1, Color.WHITE, Color.BLACK);
		setSize(gf, 10);
		audioW[0] = gf.getFontMetrics().stringWidth("Music: ");
		audioW[1] = gf.getFontMetrics().stringWidth("Sounds: ");
		audioW[2] = gf.getFontMetrics().stringWidth("Sound of achievement: ");
		audioW[0] += gf.getFontMetrics().stringWidth(audioV[GameData.audio[0]])/2;
		audioW[1] += gf.getFontMetrics().stringWidth(audioV[GameData.audio[1]])/2;
		audioW[2] += gf.getFontMetrics().stringWidth(audioV[GameData.audio[2]])/2;
		drawString(gf, "Music: ", 15, 			25, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, "Sounds: ", 15, 			35, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, "Sound of achievement: ", 15, 			45, Color.LIGHT_GRAY, Color.BLACK);
		for (int i = 0; i < audioButtons.length; i++) {
			audioButtons[i].draw(gf);
		}

		setSize(gf, 7);
		drawString(gf, "This game uses natural sounds recorded by me using a voice recorder.", 15, 65, Color.GRAY, Color.BLACK);
		drawString(gf, "Music & Sound achievements made by me in jummbus.bitbucket.io & beepbox.co", 15, 75, Color.GRAY, Color.BLACK);
		
	}
	
	Button fullscreen = new Button("Fullscreen", 0, 0);
	
	private void drawGame(Graphics2D gf) {
		Date date = new Date(GameData.totalTime*1);
		TimeZone.setDefault(TimeZone.getTimeZone("UTS"));  
		String formattedDate = new SimpleDateFormat("HH:mm:ss").format(date);
		
		int achievements = 0;
		for (int i = 0; i < GameData.achievements.length; i++) {
			if(GameData.achievements[i]) {
				achievements++;
			}
		}
		String p = "";
		if(GameData.totalLevels != 0)
			p = " (" + GameData.totalWins*100/GameData.totalLevels + "%)";
		drawCenterString(gf, "Game", 1, Color.WHITE, Color.BLACK);
		setSize(gf, 10);
		drawString(gf, " Time: " + formattedDate, 5, 			25, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, " Levels: " + GameData.totalLevels, 5, 		35, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, " Wins: " + GameData.totalWins + p, 5, 			45, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, " Gold: " + GameData.totalGold, 5, 			55, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, " Diamonds: " + GameData.totalDiamonds, 5, 	65, Color.LIGHT_GRAY, Color.BLACK);
		drawString(gf, " Achievements: " + achievements + "/" + GameData.achievements.length, 5, 75, Color.LIGHT_GRAY, Color.BLACK);
		fullscreen.draw(gf);
		deleteprogress.draw(gf);
	}
	
	public void drawCenterString(Graphics2D gf, String str, int y, Color c1, Color c2) {
		int w = (int) (gf.getFontMetrics().stringWidth(str) + 2*GamePanel.scalefull);
		drawString(gf, str, (int) ((GamePanel.frameW - sx - 30)/2 - w/2), y, c1, c2);
	}

	private void drawString(Graphics2D gf, String str, int x, int y, Color c1, Color c2) {
		y = y*sh;
		y += 5  + gf.getFont().getSize();
		x += sx + 5;
		gf.setColor(c1);
		gf.drawString(str, x, y);
		gf.setColor(c2);
		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy <2; yy++) {
				gf.drawString(str, (int) (x+xx*GamePanel.scalefull/1.5), (int) (y+yy*GamePanel.scalefull/1.5));
			}
		}
	}
	
	int sx = 0;
	int sgqm = 0;
	int sgqt = 0;
	
	int setControlID = -1;

	@Override
	public void update() {
		GameStage.updateMusicVolume();
		dist = GamePanel.frameH/(settings.length+2);
		
		sx = 0;
		for (int i = 0; i < settings.length; i++) {
			settings[i].setPosition(10 + settings[i].getBw()/2, (i+1)*dist);
			settings[i].update();
			if(sx < 10 + settings[i].getBw())
			sx = 10 + settings[i].getBw();
			if(settings[i].isClicked()) {
				selected = i;
			}
		}

		if(selected == 0) {
			fullscreen.setText(GameData.fullscreen ? "unfullscreen" : "fullscreen");
			fullscreen.setPosition(sx + 15 + fullscreen.getBw()/2, 105*sh + 5);
			fullscreen.setFontSize(10);
			fullscreen.update();
			if(fullscreen.isClicked()) {
				GameData.fullscreen = !GameData.fullscreen;
				JOptionPane.showMessageDialog(null, "Reload game");
				GameData.save();
				System.exit(0);
			}
			deleteprogress.setPosition(sx + 15 + deleteprogress.getBw()/2, 125*sh + 5);
			deleteprogress.setFontSize(10);
			deleteprogress.update();
			if(deleteprogress.isClicked()) {
				String input = JOptionPane.showInputDialog(null, "Write \"Yes\" if you really want delete progress");
				if(input != null) {
					if(input.toUpperCase().equals("YES")){
						GameData.deleteProgress();
					}
				}
			}
		}
		

		if(selected == 2) {
			for (int i = 0; i < audioButtons.length; i++) {
				audioButtons[i].setText(audioV[GameData.audio[i]]);
				audioButtons[i].setFontSize(10);
				audioButtons[i].setPosition(sx + 35 + audioW[i], (25 + i*10 + 10)*sh + 5);
				audioButtons[i].update();
				if(audioButtons[i].isClicked()) {
					GameData.audio[i] = (GameData.audio[i]+1)%3;
				}
			}
		}
		

		if(selected == 3) {
			for (int i = 0; i < setControl.length; i++) {
				setControl[i].chageSize = false;
				setControl[i].clickable = setControlID == -1;
				setControl[i].setText(setControlID == i ? "Press key to set" : KeyEvent.getKeyText(GameData.control[i]));
				setControl[i].setFontSize(10);
				setControl[i].setPosition(sx + 35 + maxXControl, (25 + i*10 + 10)*sh + 5);
				setControl[i].update();
				if(setControl[i].isClicked() && setControlID == -1) {
					setControlID = i;
					break;
				}
			}
		}

		sgqm = minus.getBw();
		if(selected == 1) {
			minus.setPosition(25 + sx + sgqm/2, sh*36);
			plus.setPosition(25 + sx + sgqt, sh*36);
			minus.update();
			plus.update();
		}
		
		plus.clickable = GameData.quality < 10;
		minus.clickable = GameData.quality > 1;
		if(plus.isClicked()) {
			GameData.quality++;
		}
		if(minus.isClicked()) {
			GameData.quality--;
		}
		
		button.setPosition(GamePanel.frameW/2, GamePanel.frameH/6*5);
		button.update();
		if(button.isClicked()) {
			maneger.setLast();
		}
	}
	

	@Override
	protected void keyPressed(KeyEvent e) {
		if(setControlID != -1) {
			GameData.control[setControlID] = e.getKeyCode();
			setControlID = -1;
		}
	}

	@Override
	protected void keyReleased(KeyEvent e) {
	}

	@Override
	protected void releasedAll() {
		
	}

	@Override
	protected LevelGenerator getLevelGenerator() {
		return null;
	}
}
