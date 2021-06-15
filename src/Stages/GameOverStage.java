package Stages;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import Objects.Button;
import Objects.Player;
import Work.GameData;
import Work.LevelGenerator;
import Work.MyAudio;

public class GameOverStage extends Stage {

	Maneger maneger;
	
	Player player;
	String gameOver = "";
	
	int gold;
	int diamonds;
	
	boolean isWin = false;
	
	LevelGenerator generator;
	GameStage gameStage;
	
	MyAudio audio;

	Button menu = new Button(GameData.texts[GameData.TEXT_MENU], 0, 0);
	Button next = new Button(GameData.texts[GameData.TEXT_NEXT], 0, 0);
	
	MyAudio audio2;
//	ArrayList<Gold> golds = new ArrayList<Gold>();
	
	public GameOverStage(MyAudio audio, Maneger maneger, Player p, int g, int d, LevelGenerator generator, GameStage gameStage) {
		this.maneger = maneger;
		this.audio2 = audio;
		gold = g;
		diamonds = d;
		this.generator = generator;
		this.gameStage = gameStage;
		
		d = 0;
		player = p;
		boolean[] go = player.getGameOvers();
		isWin = go[0];
		audio = new MyAudio("/music/GameOver.wav");
		if(!isWin)
			audio.play(0);
		if(go[0]) {
//		for (int i = 0; i < g + 25; i++) {
//			golds.add(new Gold(0));
//			golds.get(i).setPosition(GamePanel.frameW/3, (int) (15*GamePanel.scalefull));
//			golds.get(i).setTargetPosition(GamePanel.frameW/2, GamePanel.frameH/2);
//		}
			gameOver = "You win!";
			GameData.consecutive_wins++;
			if(GameData.consecutive_wins > 9)
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_REWARD10);
			if(GameData.consecutive_wins > 49)
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_REWARD50);
			if(GameData.consecutive_wins > 99)
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_REWARD100);
			if(GameData.consecutive_wins > 999)
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_REWARD1000);
			
			GameData.complitedAchievements(GameData.ACHIEVEMENTS_CHEST);

			if(gold == generator.gold)
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_BAG_OF_GOLD);
			if(diamonds == generator.diamonds)
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_BAG_OF_DIAMONDS);
		}else {
			GameData.consecutive_wins = 0;
			gameOver = "You lost from ";
			if(go[1]) {
				gameOver += "spikes";
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_SPIKES);
			}
			if(go[2]) {
				gameOver += "crusher";
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_CRUSHER);
			}
			if(go[3]) {
				gameOver += "lava";
				GameData.complitedAchievements(GameData.ACHIEVEMENTS_LAVA);
			}
		}
		
		
	}
	
	double d = 0;

	@Override
	public void draw(Graphics2D g, Graphics2D gf) {
		menu.draw(gf);
		next.draw(gf);
//		g.setColor(Color.DARK_GRAY);
//		g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());
//		g.setPaint(gameStage.getGradient());
//		g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());

		
		gf.setFont(new Font("Arial", Font.PLAIN, (int) (15*GamePanel.scalefull))); // TODO
		gf.setColor(Color.WHITE);
		setStroke(gf, 1);
		
		gf.setPaint(getGradient(isWin ? new Color(255,255,0):Color.WHITE));
		drawText(gf, gameOver, 88);

		fillRect(gf, (int)(d-15*GamePanel.scalefull));
		
		if(GamePanel.frameW - d < 5) {
			int a = (int) ((GamePanel.frameW - d)*2)/2;
			if(a < 0) a = 0;
			if(a > 256) a = 256;
			gf.setColor(new Color(200,200,200, a));
		}else {
			gf.setColor(new Color(200,200,200));
		}
		
		int myX = (int) (GamePanel.frameW/2 - d/2 + 8*GamePanel.scalefull);
		gf.drawLine(myX, (int) (GamePanel.scalefull*100),myX, (int) (GamePanel.scalefull*160));
		gf.setPaint(getGradient(isWin ? new Color(255,255,0):Color.WHITE));
		
		drawLines(gf, (int)(d), 88);
		
		gf.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (25*GamePanel.scalefull))); // TODO
		drawText(gf, "Dungeon", 45);
		setStroke(gf, 3);
		drawLines(gf, " Dungeon ", 45);

//		for (int i = 0; i < golds.size(); i++) {
//			golds.get(i).draw(gf);
//		}

		
		gf.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (7*GamePanel.scalefull)));
		
		if(!isWin) {
			gf.setColor(new Color(255, 0, 0, (int) (125 + 125*Math.cos(time/5f))));
			int maxX = gf.getFontMetrics().stringWidth(diamonds + "/" + generator.diamonds);
			int goldW = gf.getFontMetrics().stringWidth(gold + "/" + generator.gold);
			if(goldW > maxX) maxX = goldW;
			setStroke(gf, 1);
			drawX(gf,
					(int) (20*GamePanel.scalefull), (int) (118*GamePanel.scalefull) - gf.getFont().getSize(),
					(int) (24*GamePanel.scalefull) + maxX, (int) (134*GamePanel.scalefull));
		}
		gf.setColor(new Color(52,161,216));
		gf.drawImage(gameStage.tiles[generator.BLOCK_DIAMOND-1],
				(int) (0*GamePanel.scalefull),
				(int) (100*GamePanel.scalefull),
				(int) (32*GamePanel.scalefull),
				(int) (32*GamePanel.scalefull),
				null);
		gf.drawString(diamonds + "/" + generator.diamonds,
				(int) ((22)*GamePanel.scalefull),
				(int) (118*GamePanel.scalefull));//
		
		
		gf.drawImage(gameStage.tiles[generator.BLOCK_GOLD-1],
				(int) (0*GamePanel.scalefull),
				(int) (116*GamePanel.scalefull),
				(int) (32*GamePanel.scalefull),
				(int) (32*GamePanel.scalefull),
				null);
		gf.setColor(new Color(178,172,0));
		gf.drawString(gold + "/" + generator.gold,
				(int) (22*GamePanel.scalefull),
				(int) (134*GamePanel.scalefull));

		gf.setColor(new Color(0,255,178));
		gf.drawString("Seed: " + generator.getSeed(),
				(int) (14*GamePanel.scalefull),
				(int) (150*GamePanel.scalefull));

		
		//+gf.getFont().getSize()/2
//		drawLines(gf, "  You lose of" + gameOver, 100);

		gf.setPaint(getGradient(Color.DARK_GRAY));
		gf.fillRect(0, 0, GamePanel.frameW, GamePanel.frameH);
		gf.setPaint(Color.WHITE);
		gf.dispose();
	}
	
	private void drawX(Graphics2D gf, int x1, int y1, int x2, int y2) {
		gf.drawLine(x1, y1, x2, y2);
		gf.drawLine(x1, y2, x2, y1);
	}
	
	final AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1f);
	final AlphaComposite normal = AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1f);
	private void fillRect(Graphics2D gf, int w) {
//		gf.setComposite(alpha);
//		gf.setColor(new Color(0,0,0,0));
		gf.setPaint(getGradient(Color.DARK_GRAY));
		gf.fillRect(GamePanel.frameW/2 + w/2, 0, GamePanel.frameW, GamePanel.frameH);
		gf.fillRect(0, 0, GamePanel.frameW/2 - w/2, GamePanel.frameH);
//		gf.setComposite(normal);
	}
	
	private void setStroke(Graphics2D gf, float v) {
		stroke = (float) (GamePanel.scalefull*v);
		gf.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	}

	private void drawLines(Graphics2D gf, String s, int yy) {
		drawDownLine(gf, gf.getFontMetrics().stringWidth(s), yy);
		drawUpLine(gf, gf.getFontMetrics().stringWidth(s), yy);
	}
	private void drawLines(Graphics2D gf, int v, int yy) {
		drawDownLine(gf, v, yy);
		drawUpLine(gf, v, yy);
	}
	private void drawDownLine(Graphics2D gf, int w, int yy) {
		int x = (GamePanel.frameW - w)/2;
		int y =  (int) (GamePanel.scalefull*(yy+stroke));
		gf.drawLine(x,  y, x+w,  y);
	}
	private void drawUpLine(Graphics2D gf, int w, int yy) {
		int x = (GamePanel.frameW - w)/2;
		int y =  (int) (GamePanel.scalefull*(yy)) - gf.getFont().getSize();
		gf.drawLine(x,  y, x+w,  y);
	}
	
	float stroke = 1;
	
	private void drawText(Graphics2D gf, String s, int y) {
		gf.drawString(s, (GamePanel.frameW - gf.getFontMetrics().stringWidth(s))/2, (int) (GamePanel.scalefull*y));
		
	}

	
	int time = 0;
	
	@Override
	public void update() {
		next.update();
		menu.update();
		menu.setPosition(GamePanel.frameW/5*2,
				(int) (GamePanel.frameH -menu.getBh()*2));
		next.setPosition(GamePanel.frameW/5*3,
				(int) (GamePanel.frameH -menu.getBh()*2));
		time++;
		if(menu.isClicked()) {
			audio2.stop();
			audio2.close();
			maneger.loadStage(Maneger.MENU);
		}else if(next.isClicked()) {
			audio2.stop();
			audio2.close();
			maneger.loadStage(Maneger.GAME);
		}
		int targerW = (int) (GamePanel.frameW);
		d = (d-targerW)*0.9 + targerW;
//		for (int i = 0; i < golds.size(); i++) {
//			golds.get(i).update();
//		}
	}

	@Override
	protected void keyPressed(KeyEvent e) {
		
	}

	@Override
	protected void keyReleased(KeyEvent e) {
		
	}

	@Override
	protected LevelGenerator getLevelGenerator() {
		return null;
	}
	
	protected static RadialGradientPaint getGradient(Color in) {
		return new RadialGradientPaint(
				new Point(GamePanel.frameW/2,GamePanel.frameH/2),
				GamePanel.frameH/2f * 1.5f,
				new float[] { .0f,1f},
				new Color[] {in, new Color(5,5,5)});
	}

	@Override
	protected void releasedAll() {
		// TODO Auto-generated method stub
		
	}

}
