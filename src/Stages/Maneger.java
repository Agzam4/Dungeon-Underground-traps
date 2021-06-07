package Stages;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Work.LevelGenerator;

public class Maneger {

	public final static int MENU = 0;
	public final static int GAME = 1;
	
	private Stage stage;
	
	public Maneger(int stageID) {
		loadStage(stageID);
	}
	
	public void loadStage(int stageID) {
		switch (stageID) {
		case MENU:
			stage = new MenuStage();
			break;
		case GAME:
			stage = new GameStage();
			break;
		default:
			break;
		}
	}

	public void draw(Graphics2D g) {
		stage.draw(g);
	}
	
	public void update() {
		stage.update();
	}

	public void keyPressed(KeyEvent e) {
		stage.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		stage.keyReleased(e);
	}
	
	public LevelGenerator getLevelGenerator() {
		return stage.getLevelGenerator();
	}
}
