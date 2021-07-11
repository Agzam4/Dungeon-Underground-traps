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
	public static final int PACKS = 5;
	

	public int selected = 0;
	public int lastSelected = 0;
	
	private Stage[] stages = new Stage[6];
	
	public Maneger(int stageID) {
		MouseController.isMousePressed = false;
		loadStage(stageID);
	}
	
	public void setGameStage(long seed) {
		MouseController.isMousePressed = false;
		selected = GAME;
		stages[GAME] = new GameStage(this, seed);
	}
	
	public void setLast() {
		MouseController.isMousePressed = false;
		selected = lastSelected;
	}
	public void setSelected(int selected) {
		MouseController.isMousePressed = false;
		lastSelected = selected;
		this.selected = selected;
	}
	
	public void loadStageNoLast(int stageID) {
		loadStagee(stageID);
	}
	public void loadStage(int stageID) {
		lastSelected = selected;
		loadStagee(stageID);
	}
	
	private void loadStagee(int stageID) {
		MouseController.isMousePressed = false;
		releasedAll();
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
		case PACKS:
			stages[PACKS] = new PacksStage(this);
			break;
		default:
			break;
		}
	}

	public void draw(Graphics2D g, Graphics2D gf) {
		if(stages[selected] != null)
		stages[selected].draw(g, gf);
	}
	
	public void update() {
		if(stages[selected] != null)
		stages[selected].update();
	}

	public void keyPressed(KeyEvent e) {
		if(stages[selected] != null)
		stages[selected].keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		if(stages[selected] != null)
		stages[selected].keyReleased(e);
	}
	
	public LevelGenerator getLevelGenerator() {
		return stages[selected].getLevelGenerator();
	}

	public void releasedAll() {
		if(stages[selected] != null)
		stages[selected].releasedAll();
	}
	
	
	public void reloadTexts() {
		for (Stage stage : stages) {
			if(stage != null)
				stage.reloadTexts();
		}
	}
}
