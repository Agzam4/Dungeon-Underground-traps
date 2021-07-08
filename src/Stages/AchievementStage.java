package Stages;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Objects.AchievementBlock;
import Objects.Button;
import Work.GameData;
import Work.LevelGenerator;
import Work.Loader;

public class AchievementStage extends Stage {

	AchievementBlock test;
	
	AchievementBlock achievements[] = new AchievementBlock[16];
	
	Button back = new Button(GameData.texts[GameData.TEXT_BACK], GamePanel.frameW/2, 0);
	
	BufferedImage bg;
	
	Maneger maneger;
	
	public AchievementStage(Maneger maneger) {
		for (int i = 0; i < achievements.length; i++) {
			achievements[i] = new AchievementBlock(i);
		}
		try {
			bg = ImageIO.read(AchievementBlock.class.getResource("/img/bg/achievement_bg.png"));
		} catch (IOException e) {
			bg = new BufferedImage(GamePanel.getGameWidth(), GamePanel.getGameHeight(), BufferedImage.TYPE_INT_RGB);
		}
		this.maneger = maneger;
	}
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) {

		for (int y = 0; y < GamePanel.getGameHeight(); y+= Loader.ACHIEVEMENTS_BG.getHeight()) {
			for (int x = 0; x < GamePanel.getGameWidth(); x+= Loader.ACHIEVEMENTS_BG.getWidth()) {
				g.drawImage(Loader.ACHIEVEMENTS_BG, x, y, null);
			}
		}

		back.draw(gf);
		for (int i = 0; i < achievements.length; i++) {
			achievements[i].drawInfo(gf);
		}
		for (int i = 0; i < achievements.length; i++) {
			achievements[i].draw(gf);
		}
		gf.setPaint(GameOverStage.getGradient(new Color(0,0,0,0)));
		gf.fillRect(0, 0, GamePanel.frameW, GamePanel.frameH);
	}

	@Override
	public void update() {
		back.setPosition(GamePanel.frameW/2, GamePanel.frameH/6*5);
		back.update();
		if(back.isClicked()) {
			maneger.setLast();
		}
		for (int i = 0; i < achievements.length; i++) {
			achievements[i].update();
			achievements[i].setPosition((int) (GamePanel.frameW/4*(i%4 + 0.5)),
					(int) (GamePanel.frameH/6*((Math.floor(i/4)+1.0))));
		}
	}

	@Override
	protected void keyPressed(KeyEvent e) {
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
