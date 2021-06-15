package Stages;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Work.LevelGenerator;
import Work.MouseController;

public class Maneger {

	public final static int MENU = 0;
	public final static int GAME = 1;
	public final static int GAMEOVER = 2;
	public final static int ACHIEVEMENTS = 3;
	public final static int SETTINGS = 4;
	

	public static int selected = 0;
	
	private Stage[] stages = new Stage[5];
	
	public Maneger(int stageID) {
		loadStage(stageID);
	}
	
	public void setGameStage(long seed) {
		selected = GAME;
		stages[GAME] = new GameStage(this, seed);
	}
	
	public void loadStage(int stageID) {
		MouseController.isMousePressed = false;
		selected = stageID;
		switch (stageID) {
		case MENU:
			stages[MENU] = new MenuStage(this);
			break;
		case GAME:
			stages[GAME] = new GameStage(this, null);
			break;
		case GAMEOVER:
			stages[GAMEOVER] = new GameOverStage(null, this, null,0,0,null,null);
			break;
		case ACHIEVEMENTS:
			stages[ACHIEVEMENTS] = new AchievementStage(this);
			break;
		case SETTINGS:
			stages[SETTINGS] = new SettingsStage(this);
			break;
		default:
			break;
		}
	}

	public void draw(Graphics2D g, Graphics2D gf) {
		stages[selected].draw(g, gf);
	}
	
	public void update() {
		stages[selected].update();
	}

	public void keyPressed(KeyEvent e) {
		stages[selected].keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		stages[selected].keyReleased(e);
	}
	
	public LevelGenerator getLevelGenerator() {
		return stages[selected].getLevelGenerator();
	}

	public void releasedAll() {
		stages[selected].releasedAll();
	}
}
