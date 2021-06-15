package Stages;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import Objects.Button;
import Work.GameData;
import Work.LevelGenerator;

public class SettingsStage extends Stage {

	Maneger maneger;
	Button button = new Button(GameData.texts[GameData.TEXT_BACK], 0, 0);
	
	public SettingsStage(Maneger maneger) {
		this.maneger = maneger;
	}

	@Override
	public void draw(Graphics2D g, Graphics2D gf) {
		button.draw(gf);
	}

	@Override
	public void update() {
		button.setPosition(GamePanel.frameW/2, GamePanel.frameH/6*5);
		button.update();
		if(button.isClicked()) {
			maneger.loadStage(Maneger.MENU);
		}
	}

	@Override
	protected void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void releasedAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected LevelGenerator getLevelGenerator() {
		// TODO Auto-generated method stub
		return null;
	}
}
