package Stages;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Objects.Player;
import Work.GameData;
import Work.LevelGenerator;
import Work.MyAudio;

public class GameStage extends Stage {

	public BufferedImage[] tiles;
	public static LevelGenerator level;
	GameOverStage overStage;

	Maneger maneger;
	MyAudio music;

	private static final int PLATE_DOWN = 0;
	private static final int PLATE_UP = 1;
	private static final int DOOR_OPEN = 2;
	private static final int DOOR_CLOSE = 3;
	
	String soundsNames[] = {"plate_down","plate_up", "door_open", "door_close"};
	MyAudio sounds[] = new MyAudio[soundsNames.length];
	
	
	
	public GameStage(Maneger maneger, Long seed) {
		this.maneger = maneger;
		music = new MyAudio("/music/Dungeon_Underground_Traps.wav");
		music.play(-1);
		
		for (int i = 0; i < sounds.length; i++) {
			sounds[i] = new MyAudio("/sounds/" + soundsNames[i] + ".wav");
		}
		
		isGameOver = false;

		if(seed == null)
			level = new LevelGenerator();
		else
			level = new LevelGenerator(seed);
			
		level.generate(100, 100);
		mapX = level.getWidth()*tilesize/2;
		mapY = level.getHeight()*tilesize/2;
		fx = level.getFinishX()*tilesize;
		fy = level.getFinishY()*tilesize;

		player = new Player(level);
		player.setPosition(mapX, mapY-tilesize);
		
		BufferedImage tileset = null;
		try {
			tileset = ImageIO.read(GameStage.class.getResourceAsStream("/img/tileset/Dungeon_Underground_traps.png"));
		} catch (IOException e) {
		}
		int w = tileset.getWidth()/tilesize;
		int h = tileset.getHeight()/tilesize;
		
		tiles = new BufferedImage[w*h];
		int id = 0;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				tiles[id] = tileset.getSubimage(x*tilesize, y*tilesize, tilesize, tilesize);
				id++;
			}
		}
		
		
	}

	double fx;
	double fy;
	public static double mapX;
	public static double mapY;
	public static double mapX2;
	public static double mapY2;
	
	public static int vw = GamePanel.getCountWidth()/2;
	public static int vh = GamePanel.getCountHeight()/2;
	
	
	Player player;
	public static boolean isGameOver = false;
	
	int time = 0;
	int lavatime = 0;
	double lavaH = 0;
	
	int countCrushers = 0;
	public static float radius = 1;
	
	int gold = 0;
	int diamonds = 0;
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) {
		GameData.drawAchievementBlock(gf);
		if(isGameOver) {
			overStage.draw(g, gf);
			return;
		}
		
		countCrushers = 0;

//		int cx = (int) (GamePanel.getCountWidth()*GamePanel.quality*8 - GamePanel.getGameWidth()/2);
////		System.out.println(GamePanel.getCountWidth()*GamePanel.quality*8f);
////		System.out.println(GamePanel.getGameWidth()/2);
		mapX = (mapX - player.getX()) * 0.0 + player.getX();
		mapY = (mapY - player.getY()) * 0.0 + player.getY();
		mapX2 *= -GameData.screenShake;
		mapY2 *= -GameData.screenShake;
		mapY2 = Math.round(mapY2*100)/100f;

		g.setColor(new Color(43,43,43));
		g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());
//
//		int tileX = (int) (mapX/tilesize);
//		int tileY = (int) (mapY/tilesize);
//
//		for (int y = -vh-1; y < GamePanel.getCountWidth()-vh+1; y++) {
//			for (int x = -vw-1; x < GamePanel.getCountWidth()-vw+1; x++) {
//				int block = level.getBlock(x+tileX, y+tileY);
//				int px = (int) (((x*16-mapX%tilesize) -5 + GameStage.mapX2 + GamePanel.getGameWidth()/GamePanel.quality/2)*GamePanel.quality)
//						;
//						
////						(int)(((x+vw)*16 - mapX%tilesize + mapX2 - 1.5)*GamePanel.quality);
//				int py = (int)(((y+vh)*16 - mapY%tilesize + mapY2 + 7 + GamePanel.gameY)*GamePanel.quality);
		

		int tileX = (int) -(GamePanel.getGameWidth()/GamePanel.quality/2/tilesize);
		int tileY = (int) -(GamePanel.getGameHeight()/GamePanel.quality/2/tilesize);
		//int cx = (int) ((int)(GamePanel.getGameWidth()/GamePanel.quality) - (int) (-mapX%tilesize));
		for (int y = (int) (-mapY%tilesize); y < GamePanel.getGameHeight()/GamePanel.quality; y+=tilesize) {
			for (int x = (int) (-mapX%tilesize); x < GamePanel.getGameWidth()/GamePanel.quality; x+=tilesize) {
				int bx = (int) ((mapX+x)/tilesize) + tileX;
				int by = (int) ((y+mapY)/tilesize) + tileY;
				int block = level.getBlock(bx, by);
				int px = (int) ((x - 5 + mapX2)*GamePanel.quality + GamePanel.quality*8);
//						(int) (-GamePanel.getGameWidth()/2 + x*GamePanel.quality + cx/2*GamePanel.quality);
//						(int) ((x - 5)*GamePanel.quality ) + GamePanel.getUntileDistanse();
//						(int) ((x-5)*GamePanel.quality) - GamePanel.getUntileDistanse();
				
						
//						(int) ((x * GamePanel.quality /* + tileX / + GameStage.mapX + GameStage.mapX2 - 5*GamePanel.quality));
						
						//player.getWDebug() + (int) Math.ceil((x)*GamePanel.quality);//(int)(((x+vw)*16 - mapX%tilesize + mapX2 - 1.5)*GamePanel.quality);
				int py = (int) Math.ceil((y+3 + mapY2)*GamePanel.quality);//(int)(((y+vh)*16 - mapY%tilesize + mapY2 + 7 + GamePanel.gameY)*GamePanel.quality);
				
				
				if(block == 26){
					g.drawImage(tiles[24],
							px,
							py + (int)((crusserY -16)*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(tiles[block-1],
							px,
							py + (int)(crusserY*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(tiles[level.getBlock(bx, by-1)-1],
							px,
							py + (int)(-16*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					countCrushers++;// = true;
				}else if (block == 30) {
					g.drawImage(tiles[block-1],
							px,
							py + (int)((- 16 - lavaH)*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(tiles[block+2],
							px,
							py + (int)(-lavaH*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(tiles[block+1],
							px,
							py,
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);

				}else {
					g.drawImage(tiles[block-1],
							px,
							py,
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
				}
				
			}
		}
		
		
		player.draw(g);
		
		
		if(GameData.lavaLight) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());
			g.setComposite(alpha);
//			g.setPaint(getGradient());
			g.setColor(new Color(0,255,0,10));
			g.fillOval(GamePanel.getGameWidth()/2-GamePanel.getGameHeight()/2, 0, GamePanel.getGameHeight(), GamePanel.getGameHeight());
			g.setComposite(normal);
		} else {
			g.setPaint(getGradient());
			g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());
		}

		g.setColor(new Color(255,0,180));
//		g.drawLine(GamePanel.getGameWidth()/2, 0, GamePanel.getGameWidth()/2, GamePanel.getGameHeight());
//		g.drawLine(0, GamePanel.getGameHeight()/2, GamePanel.getGameWidth(), GamePanel.getGameHeight()/2);

		gf.setColor(new Color(37,107,142));//240,235,16));
		gf.drawImage(tiles[level.BLOCK_DIAMOND-1],
				(int) ((0)*GamePanel.scalefull),
				(int) ((0)*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				null);
		gf.drawString("" + diamonds,
				(int) ((11)*GamePanel.scalefull),
				(int) ((10)*GamePanel.scalefull));//+gf.getFont().getSize()/2
		
		gf.drawImage(tiles[level.BLOCK_GOLD-1],
				(int) ((0)*GamePanel.scalefull),
				(int) ((8)*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				null);
		gf.setColor(new Color(178,172,0));
		gf.drawString("" + gold,
				(int) ((11)*GamePanel.scalefull),
				(int) ((18)*GamePanel.scalefull));//+gf.getFont().getSize()/2
	}
	
	RadialGradientPaint radialGradientPaint = new RadialGradientPaint(
			new Point(GamePanel.getGameWidth()/2,GamePanel.getGameHeight()/2),
			GamePanel.getGameHeight()/2f * GameData.visionRadius,
			new float[] { .0f,1f},
			new Color[] {new Color(0,0,0,0), new Color(0,0,0,255)});

	protected static RadialGradientPaint getGradient() {
		if(!(radius > 0)) {
			radius = 0.1f;
		}
		int g = (int)(5 + 5*Math.cos(gradientT/10d));
		return new RadialGradientPaint(
				new Point(GamePanel.getGameWidth()/2,GamePanel.getGameHeight()/2),
				GamePanel.getGameHeight()/2f * GameData.visionRadius * radius + (int)(4*GamePanel.quality*Math.cos(gradientT/10d)),
				new float[] { .0f,1f},
				GameData.lavaLight ? new Color[] {new Color(g,g,255), new Color(0,255,0,10)} : 
					new Color[] {new Color(0,0,0,0), new Color(g,g,g)});
	}
	

	final AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1f);
	final AlphaComposite normal = AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1f);

	public static double crusserY = 0;
	static int gradientT = 0;
	
	
	@Override
	public void update() {
		GameData.updateAchievementBlock();
		
		if(isGameOver) {
			overStage.update();
			radius = (radius-1)*0.8f + 1;
			gradientT = 0;
			return;
		}
		
		
		radius = (radius-1)*0.8f + 1;
//		System.out.println(time + ": " + crusserY);
		lavatime++;
		gradientT++;
		lavaH = 2+Math.cos(lavatime/10f)*2;
		if(time == 0) {
			crusserY = (crusserY - 16) / 2 + 16;
			if(Math.round(crusserY) == 16) {
				time = 1;
				crusserY = 16;
				mapY2 +=countCrushers;
			}
		}
		if(time == 1) {
			crusserY *= .8;
			if(Math.round(crusserY) == 0) {
				time+=2;
				crusserY = 0;
			}
		}
		if(time > 1) {
			time++;
			if(time > 10) {
				time = 0;
			}
		}
		

		player.update();
		if(player.isGameOver && !isGameOver) {
			isGameOver = true;
			player.isGameOver = false;
			overStage = new GameOverStage(music, maneger, player, gold, diamonds, level, this);
			if(!player.getGameOvers()[0])
				music.stop();
		}
		
		
		int tileX = (int) (mapX/tilesize);
		int tileY = (int) (mapY/tilesize);
		for (int y = -vh-1; y < GamePanel.getCountWidth()-vh+1; y++) {
			for (int x = -vw-1; x < GamePanel.getCountWidth()-vw+1; x++) {
				int t = level.getBlock(x+tileX, y+tileY);
				boolean isHit = level.isHit(x+tileX, y+tileY);
				if(
						level.getBlock(x+tileX, y+tileY+1) == level.BLOCK_PLATE &&
						level.getBlock(x+tileX+1, y+tileY) == level.BLOCK_DOOR_OPEN_UP &&
						level.getBlock(x+tileX+1, y+tileY+1) == level.BLOCK_DOOR_OPEN_DOWN &&

						!level.isHit(x+tileX, y+tileY+1) &&
						!level.isHit(x+tileX+1, y+tileY) &&
						!level.isHit(x+tileX+1, y+tileY+1)
				) {
					level.setTile(x+tileX+1, y+tileY, level.BLOCK_STONE_DOOR);
					level.setTile(x+tileX+1, y+tileY+1, level.BLOCK_STONE_DOOR);
					sounds[DOOR_OPEN].stop();
					sounds[DOOR_CLOSE].play(0);
				}
				if(5 == t && isHit) {
					level.setTile(x+tileX, y+tileY, level.BLOCK_PLATE_ACTIVATE);
					if(level.BLOCK_STONE_DOOR == level.getBlock(x+tileX+1, y+tileY)
							&& level.BLOCK_STONE_DOOR == level.getBlock(x+tileX+1, y+tileY-1)) {
						sounds[DOOR_OPEN].play(0);
						level.setTile(x+tileX+1, y+tileY, level.BLOCK_DOOR_OPEN_DOWN);
						level.setTile(x+tileX+1, y+tileY-1, level.BLOCK_DOOR_OPEN_UP);
					}else {
						sounds[PLATE_DOWN].play(0);
					}
				}
				if(level.BLOCK_PLATE_ACTIVATE == t && !isHit) {
					level.setTile(x+tileX, y+tileY, level.BLOCK_PLATE);
					sounds[PLATE_UP].play(0);
				}
				if(isHit && t == level.BLOCK_GOLD) {
					gold++;
					level.setTile(x+tileX, y+tileY, level.BLOCK_AIR);
					GameData.complitedAchievements(GameData.ACHIEVEMENTS_GOLD);
				}
				if(isHit && t == level.BLOCK_DIAMOND) {
					diamonds++;
					level.setTile(x+tileX, y+tileY, level.BLOCK_AIR);
					GameData.complitedAchievements(GameData.ACHIEVEMENTS_DIAMOND);
				}
				
				level.setHit(x+tileX, y+tileY, false);
			}
		}
	}
	
	@Override
	protected void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.setVK_UP(true);
			break;
		case KeyEvent.VK_DOWN:
			player.setVK_DOWN(true);
			break;
		case KeyEvent.VK_RIGHT:
			player.setVK_RIGHT(true);
			break;
		case KeyEvent.VK_LEFT:
			player.setVK_LEFT(true);
			break;
		default:
			break;
		}
	}

	@Override
	protected void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.setVK_UP(false);
			break;
		case KeyEvent.VK_DOWN:
			player.setVK_DOWN(false);
			break;
		case KeyEvent.VK_RIGHT:
			player.setVK_RIGHT(false);
			break;
		case KeyEvent.VK_LEFT:
			player.setVK_LEFT(false);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected LevelGenerator getLevelGenerator() {
		return level;
	}
	
	public void deleteTraps(int x, int y) {
		level.deleteTraps(x, y);
	}

	@Override
	protected void releasedAll() {
		player.setVK_UP(false);
		player.setVK_DOWN(false);
		player.setVK_RIGHT(false);
		player.setVK_LEFT(false);
	}
}
