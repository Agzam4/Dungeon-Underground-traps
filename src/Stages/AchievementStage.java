package Stages;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import Objects.AchievementBlock;
import Objects.Button;
import Work.GameData;
import Work.LevelGenerator;

public class AchievementStage extends Stage {

	AchievementBlock test;
	
	AchievementBlock achievements[] = new AchievementBlock[16];
	
	Button back = new Button(GameData.texts[GameData.TEXT_BACK], GamePanel.frameW/2, 0);
	
	Maneger maneger;
	
	public AchievementStage(Maneger maneger) {
		for (int i = 0; i < achievements.length; i++) {
			achievements[i] = new AchievementBlock(i);
		}
		this.maneger = maneger;
	}
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) {
		back.draw(gf);
		for (int i = 0; i < achievements.length; i++) {
			achievements[i].drawInfo(gf);
		}
		for (int i = 0; i < achievements.length; i++) {
			achievements[i].draw(gf);
		}
		gf.setColor(Color.GRAY);
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
