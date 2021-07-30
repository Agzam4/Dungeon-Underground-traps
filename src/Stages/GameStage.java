package Stages;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import Main.GamePanel;
import Multiplayer.GameClient;
import Multiplayer.StaticServer;
import Objects.Button;
import Objects.Dart;
import Objects.OtherPlayers;
import Objects.Player;
import Work.GameData;
import Work.LevelGenerator;
import Work.Loader;
import Work.MyAudio;
import Work.Painter;

public class GameStage extends Stage {

//	public BufferedImage[] tiles;
	public static LevelGenerator level;
	GameOverStage overStage;

	Maneger maneger;
	static MyAudio music;

	private static final int PLATE_DOWN = 0;
	private static final int PLATE_UP = 1;
	private static final int DOOR_OPEN = 2;
	private static final int DOOR_CLOSE = 3;
	private static final int DART = 4;
	private static final int BOOM = 5;
	
	
	ArrayList<Dart> darts = new ArrayList<Dart>();
	int dartsTime = 0;
	
	static String soundsNames[] = {"plate_down","plate_up", "door_open", "door_close", "dart", "boom"};
	static MyAudio sounds[] = new MyAudio[soundsNames.length];
	
	long times;
	
	boolean isMusicChange = false;
	
	
	boolean isLoadingOnline = false;
	
	public GameStage(Maneger maneger, Long seed) {
		music = new MyAudio("/music/Dungeon_Underground_Traps.wav");
		music.play(-1);
		init(maneger, seed, 100, 100, 0);
		setPositions();
	}
	
	private void init(Maneger maneger, Long seed, int mw, int mh, int mode) {
		times = System.nanoTime() - GameData.totalTime*1_000_000;
		this.maneger = maneger;
		music.setVolume(GameData.audio[GameData.AUDIO_MUSIC]/2f);
		
		for (int i = 0; i < sounds.length; i++) {
			sounds[i] = new MyAudio("/sounds/" + soundsNames[i] + ".wav");
			sounds[i].setVolume(GameData.audio[GameData.AUDIO_SOUNDS]/2f);
		}
		
		isGameOver = false;

		if(seed == null)
			level = new LevelGenerator();
		else
			level = new LevelGenerator(seed);
			
		level.generate(mw, mh, mode);

		pButtons[0] = new Button(GameData.texts[GameData.TEXT_RESUME], 0, 0);
		pButtons[1] = new Button(GameData.texts[GameData.TEXT_FAST_LOSE], 0, 0);
		pButtons[2] = new Button(GameData.texts[GameData.TEXT_ACHIEVEMENTS], 0, 0);
		pButtons[3] = new Button(GameData.texts[GameData.TEXT_SETTINGS], 0, 0);
		pButtons[4] = new Button(GameData.texts[GameData.TEXT_MENU], 0, 0);
	}
	
	private void setPositions() {
		mapX = level.getWidth()*tilesize/2;
		mapY = level.getHeight()*tilesize/2;
		fx = level.getFinishX()*tilesize;
		fy = level.getFinishY()*tilesize;

		player = new Player(level);
		player.setPosition(mapX, mapY-tilesize);
	}
	
	GameClient client;

	public GameStage(Maneger maneger, GameClient client) { // TODO
		this.client = null;
		isLoadingOnline = true;
		System.err.println("Starting client");
		isMusicChange = false;
		music = new MyAudio("/music/Dungeon_Underground_Traps_online_0.mp3");
		music.play(-1);
		init(maneger, null, 25, 25, 0);

		pButtons[1].clickable = false;
		pButtons[2].clickable = false;
		pButtons[3].clickable = false;
		this.client = client;
		level = null;
//		this.client.setLg(level);
		Thread loading = new Thread(() -> {
			System.err.println("Wating map...");
			while (level == null) {
				while (!this.client.isMapGet) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				System.out.println(level + " " + this.client.lg);
				level = this.client.lg;
				if(client.needExit) {
					break;
				}
			}
			System.err.println("Map load!");
			if(client.needExit) {
				maneger.setMultiplayer(client.exitMsg);
				return;
			}
			System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			setPositions();
			player.setImg(Loader.PLAYER2);
			isLoadingOnline = false;
		});
		loading.start();
	}

	double fx;
	double fy;
	public static double mapX;
	public static double mapY;
	public static double mapX2;
	public static double mapY2;
	
	public static int vw = GamePanel.getCountWidth()/2;
	public static int vh = GamePanel.getCountHeight()/2;
	
	
	public Player player;
	public static boolean isGameOver = false;
	
	int time = 0;
	int lavatime = 0;
	double lavaH = 0;
	
	int countCrushers = 0;
	public static float radius = 1;
	
	int gold = 0;
	int diamonds = 0;
	
	Button[] pButtons = new Button[5];
	
	
	int normalTime = 0;
	private static String getTime(int ts) {
		int s = ts%60;
		int m = (ts-s)/60; 
		return m + ":" + (s < 10 ? "0" + s : s);
	}
	
	Button menu2 = new Button("", 0, 0);
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) { // TODO: draw
		if(client != null && client.isGameEnd()) {
			menu2.setText(GameData.texts[GameData.TEXT_MENU]);
			menu2.draw(gf);
			String[] info = client.endINFO.split(";");
			int path = GamePanel.frameH/(info.length+6);
			if(path < 5)
				path = 5;
			menu2.setPosition(GamePanel.frameW/2, path*(info.length+5));

			gf.setFont(new Font("Comic Sans MS", Font.BOLD, path));
			if(path > GamePanel.scalefull*15) {
				gf.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (GamePanel.scalefull*15)));
			}
			int id = 0;
			int num = 0;
			for (String line : info) {
				String lineData[] = line.split(":");
				int playerID = 0;
				try {
					playerID = Integer.valueOf(lineData[0]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if(playerID == client.clientID) {
					num = id;
				}
				String nline = (id+1) + ") " + client.getName(playerID) +" (" + lineData[1] + ")";
				Painter.drawCenterString(gf, nline,
						OtherPlayers.getColor(playerID, info.length), Color.WHITE, path*(id+4));
				id++;
			}

			gf.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (path*1.5)));
			Painter.drawCenterString(gf, "You " + (num == 0 ? "win":"lose"),
					OtherPlayers.getColor(client.clientID, info.length), Color.WHITE, path*2);
		}
		if(isLoadingOnline) {
			Painter.drawGradientGF(gf, Loader.COLOR_GAME_BG);
			gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (GamePanel.frameH/15)));
			String s2 = " (" + client.loadingValue + "%)";
			if(client.loadingValue == -1) {
				s2 = "";
			}
			Painter.drawCenterString(gf, client.loadingInfo + s2, Loader.COLOR_TEXT_FG, Loader.COLOR_TEXT_BG, GamePanel.frameH/2);
			return;
		}
		GameData.drawAchievementBlock(gf);

		if(retime > 0) {
			gf.drawString("" + retime, GamePanel.frameW/2,  GamePanel.frameH/2);
		}
		
		if(paused && !isGameOver) {
			for (int i = 0; i < pButtons.length; i++) {
				pButtons[i].draw(gf);
			}
			gf.setColor(new Color(0,0,0,200));
			gf.fillRect(0, 0, GamePanel.frameW, GamePanel.frameH);
		}
		gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (5*GamePanel.scalefull)));
		
		
		countCrushers = 0;

//		int cx = (int) (GamePanel.getCountWidth()*GamePanel.quality*8 - GamePanel.getGameWidth()/2);
////		System.out.println(GamePanel.getCountWidth()*GamePanel.quality*8f);
////		System.out.println(GamePanel.getGameWidth()/2);
		
		mapX = (mapX - player.getX()) * 0.0 + player.getX();
		mapY = (mapY - player.getY()) * 0.0 + player.getY();
		if(client != null) {
			if(!client.isPlaying) {
				mapX = client.getViewX();
				mapY = client.getViewY();
			}
		}
		mapX2 *= -GameData.screenShake;
		mapY2 *= -GameData.screenShake;
		mapY2 = Math.round(mapY2*100)/100f;

		g.setColor(Loader.COLOR_GAME_BG);
		g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());
		
		for (Dart dart : darts) {
			dart.draw(g);
		}
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


		// Draw Tiles //
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
					g.drawImage(Loader.tileset[24],
							px,
							py + (int)((crusserY -16)*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(Loader.tileset[block-1],
							px,
							py + (int)(crusserY*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(Loader.tileset[level.getBlock(bx, by-1)-1],
							px,
							py + (int)(-16*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					countCrushers++;// = true;
				}else if (block == 30) {
					g.drawImage(Loader.tileset[block-1],
							px,
							py + (int)((- 16 - lavaH)*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(Loader.tileset[block+2],
							px,
							py + (int)(-lavaH*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
					g.drawImage(Loader.tileset[block+1],
							px,
							py,
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);

				}else {
					g.drawImage(Loader.tileset[block-1],
							px,
							py,
							(int)(tilesize*GamePanel.quality),
							(int)(tilesize*GamePanel.quality),
							null);
				}
				
			}
		}
		
		
		// Draw Border //
		tileX = (int) -(GamePanel.getGameWidth()/GamePanel.quality/2/tilesize);
		tileY = (int) -(GamePanel.getGameHeight()/GamePanel.quality/2/tilesize);
		for (int y = (int) (-mapY%tilesize); y < GamePanel.getGameHeight()/GamePanel.quality; y+=tilesize) {
			for (int x = (int) (-mapX%tilesize); x < GamePanel.getGameWidth()/GamePanel.quality; x+=tilesize) {
				int bx = (int) ((mapX+x)/tilesize) + tileX;
				int by = (int) ((y+mapY)/tilesize) + tileY;
				int px = (int) ((x - 5 + mapX2)*GamePanel.quality + GamePanel.quality*8);
				int py = (int) Math.ceil((y+3 + mapY2)*GamePanel.quality);
				int lineSize = (int)(tilesize*GamePanel.quality);
				int borderSize = (int)(Loader.borderSize*GamePanel.quality/2d);
				
				int borderV = borderSize*Loader.borderType;
				if(level.isVoidBlock(bx, by)) {
					if(Loader.isVisibleBorderUp) {
						if(level.isNotVoidBlock(bx, by-1)) {
							g.setColor(Loader.COLOR_BORDER_UP);
							g.fillRect(px, py-borderSize + borderV, lineSize, borderSize*2);
						}
					}
					if(Loader.isVisibleBorderLeft) {
						if(level.isNotVoidBlock(bx-1, by)) {
							g.setColor(Loader.COLOR_BORDER_LEFT);
							g.fillRect(px-borderSize+borderV, py, borderSize*2, lineSize);
						}
					}
				}
				if(level.isNotVoidBlock(bx, by)) {
					if(Loader.isVisibleBorderDown) {
						if(level.isVoidBlock(bx, by-1)) {
							g.setColor(Loader.COLOR_BORDER_DOWN);
							g.fillRect(px, py-borderSize-borderV, lineSize, borderSize*2);
						}
					}
					if(Loader.isVisibleBorderRight) {
						if(level.isVoidBlock(bx-1, by)) {
							g.setColor(Loader.COLOR_BORDER_RIGHT);
							g.fillRect(px-borderSize-borderV, py, borderSize*2, lineSize);
						}
					}
				}
			}
		}

		if(isGameOver && client == null) {
			if(overStage != null)
				overStage.draw(g, gf);
			return;
		}

		if(client != null) {
			client.drawPlayers(g);
			player.setColor(OtherPlayers.getColor(client.clientID, client.getPlayersCount()));
		}
		
		if(client == null || client.isPlaying) {
			player.draw(g);
		}
		
		
		
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

		if(client == null) {
		gf.setColor(new Color(37,107,142));//240,235,16));
		gf.drawImage(Loader.tileset[level.BLOCK_DIAMOND-1],
				(int) ((0)*GamePanel.scalefull),
				(int) ((0)*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				null);
		gf.drawString("" + diamonds,
				(int) ((11)*GamePanel.scalefull),
				(int) ((10)*GamePanel.scalefull));//+gf.getFont().getSize()/2
		
		gf.drawImage(Loader.tileset[level.BLOCK_GOLD-1],
				(int) ((0)*GamePanel.scalefull),
				(int) ((8)*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				(int) (16*GamePanel.scalefull),
				null);
		gf.setColor(new Color(178,172,0));
		gf.drawString("" + gold,
				(int) ((11)*GamePanel.scalefull),
				(int) ((18)*GamePanel.scalefull));//+gf.getFont().getSize()/2
		} else if(!client.isGameEnd()){
			gf.setColor(client.needChangeMusic ? new Color(255,100,100) : new Color(94,249,156));
			String time = "Time: " + getTime(client.normalTime);

			int defsize = gf.getFont().getSize();
			gf.setFont(new Font("Comic Sans MS", Font.BOLD, gf.getFont().getSize()));
			
//			if(client.isPlaying) {
//				gf.drawString(time,
//						(GamePanel.frameW-gf.getFontMetrics().stringWidth(time))/2,
//						(int) ((2)*GamePanel.scalefull) + gf.getFont().getSize());
				Color cc = client.needChangeMusic ? new Color(255,100,100) : new Color(94,249,156);
				Painter.drawCenterString(gf, time, cc, cc.darker().darker(),
						(int) ((2)*GamePanel.scalefull) + gf.getFont().getSize());
//			}
			gf.setColor(Color.WHITE);
			Painter.k = 0.75;
			Painter.drawString(gf, "Score: " + client.score + "/" + (client.scoreLast+client.score) + " | Players: " + client.getPlayersCount(), Color.WHITE, Color.DARK_GRAY, (int) ((2)*GamePanel.scalefull),
					(int) ((2)*GamePanel.scalefull) + gf.getFont().getSize());
			
			try {
				int y = (int) (((4)*GamePanel.scalefull) + gf.getFont().getSize()*2);
				String lines[] = client.leadBoard.split(";");
				for (int i = 0; i < client.getPlayersCount(); i++) {
					if(i > lines.length-1) {
						break;
					}
					String data[] = lines[i].split("/");
					int id = Integer.valueOf(data[0]);
					gf.setFont(new Font("Comic Sans MS", Font.BOLD, gf.getFont().getSize()));
					Color c = OtherPlayers.getColor(id, client.getPlayersCount());
					if(id == client.clientID) {
						c = OtherPlayers.getColorLight(id, client.getPlayersCount());
						gf.setFont(new Font("Comic Sans MS", Font.BOLD+2, gf.getFont().getSize()));
					}
//					gf.drawString(data[1], (int) ((2)*GamePanel.scalefull), y);
					Painter.drawString(gf, (i+1) + ") " + data[1], c, c.darker().darker(), (int) ((3)*GamePanel.scalefull), y);
					y += gf.getFont().getSize()*1.5;
				}

//				gf.drawString("isPlaying: " + client.isPlaying,
//						(int) ((2)*GamePanel.scalefull),
//						y + gf.getFont().getSize());
			} catch (NumberFormatException e) {
			}
			
			
			gf.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
			gf.setColor(new Color(255,100,100));
			if(retime > 0) {
				int timeW = gf.getFontMetrics().stringWidth(retime+"");
				gf.drawString(retime+"", (GamePanel.frameW-timeW)/2, GamePanel.frameH/3);
			}
			if(!isReady) {
				radius = 0.01f;
				int tw = gf.getFontMetrics().stringWidth("Press ENTER if you ready");
				gf.drawString("Press ENTER if you ready", (GamePanel.frameW-tw)/2, GamePanel.frameH/3);
			}else if (!client.isAllReady) {
				radius = 0.1f;
				int tw = gf.getFontMetrics().stringWidth("Wating other players");
				gf.drawString("Wating other players", (GamePanel.frameW-tw)/2, GamePanel.frameH/3);
			}
			if(!client.isPlaying) {
				int txtW = gf.getFontMetrics().stringWidth("You Lose");
				int y =  gf.getFont().getSize()+ 20 + defsize;
				gf.drawString("You Lose", (GamePanel.frameW-txtW)/2, y);
				gf.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
				String txt = "Use " + 
				KeyEvent.getKeyText(GameData.control[GameData.KEY_RIGHT]) 
				+ "/" + KeyEvent.getKeyText(GameData.control[GameData.KEY_LEFT]) + " to change view";
				txtW = gf.getFontMetrics().stringWidth(txt);
				y+= gf.getFont().getSize()+5;
				gf.drawString(txt, (GamePanel.frameW-txtW)/2, y);
			}
		}
		
		if(win > 0) {
			g.setColor(new Color(0,0,0,win));
			g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());
		}
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
		int newR = (int) (GamePanel.getGameHeight()/2f * GameData.visionRadius * radius + (int)(4*GamePanel.quality*Math.cos(gradientT/10d)));
		if(newR < 0)
			newR = 1;
		int g = (int)(5 + 5*Math.cos(gradientT/10d));
		return new RadialGradientPaint(
				new Point(GamePanel.getGameWidth()/2,GamePanel.getGameHeight()/2),
				newR,
				new float[] { .0f,1f},
				GameData.lavaLight ? new Color[] {new Color(g,g,255), new Color(0,255,0,10)} : 
					new Color[] {new Color(0,0,0,0), new Color(g,g,g)});
	}
	

	final AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1f);
	final AlphaComposite normal = AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1f);

	public static double crusserY = 0;
	static int gradientT = 0;

	int boom_time = 0;
	int boom_x = -1;
	int boom_y = -1;
	
	int retime = 0;
	
	int win = 0;
	
	@Override
	public void update() { // TODO: update
		if(client != null && client.isGameEnd()) {
			if(win < 245)
				win+=10;

			retime = 0;
			isGameOver = false;
			menu2.update();
			
			if(win > 100 && menu2.isClicked()) {
				try {
					client.exit();
				} catch (IOException e) {
					e.printStackTrace();
				}
					music.stop();
					music.close();
					maneger.loadStage(Maneger.MULTIPLAYER);
			}
		}
		if(isLoadingOnline) return;
		GameData.updateAchievementBlock();
		if(client != null) {
			if(!isMusicChange) {
				if(client.needChangeMusic) {
					isMusicChange = true;
					music.stop();
					Thread startMusic = new Thread(() -> {
						music = new MyAudio("/music/Dungeon_Underground_Traps_online_1.mp3");
						music.setVolume(GameData.audio[GameData.AUDIO_MUSIC]/2f);
						music.play(-1);
					});
					startMusic.start();
				}
			}
		}

		if(client != null) {
//			if(client.isPlaying)
				client.sendPosition(mapX, mapY);
		}
		
		if(isGameOver) {
			if(client != null) {
				retime = 100; // TODO
				mapX = level.getWidth()*tilesize/2;
				mapY = level.getHeight()*tilesize/2;
				player.setPosition(mapX, mapY-10);
				isGameOver = false;
			}else {
				if(overStage != null)
					overStage.update();
				radius = (radius-1)*0.8f + 1;
				gradientT = 0;
				return;
			}
		}
		retime--;
		if(retime > 0) {
			return;
		}else if(retime == 0) {
			player.isGameOver = false;
			mapX = level.getWidth()*tilesize/2;
			mapY = level.getHeight()*tilesize/2;
			player.setPosition(mapX, mapY-tilesize+6);
			retime = -1;
		}
		if(boom_time > 0) { // -1014327005907606213
			boom_time--;
			if(boom_time < 1) {
				level.setTile(boom_x, boom_y, level.BLOCK_AIR);
				level.setTile(boom_x, boom_y-1, level.BLOCK_AIR);
				level.setTile(boom_x, boom_y-2, level.BLOCK_AIR);
				level.setTile(boom_x-1, boom_y+1, level.BLOCK_AIR);
				level.setTile(boom_x+1, boom_y+1, level.BLOCK_AIR);
				for (int y = boom_y-1; y < boom_y+2; y++) {
					for (int x = boom_x-1; x < boom_x+2; x++) {
						if(level.isStone(x, y)) {
							level.setTile(x, y, level.BLOCK_AIR);
						}
					}
				}
				sounds[BOOM].play(0);
			}
		}
		
		if(paused && (client == null || !client.isGameEnd())) {
			updateVolume();
			int dist = GamePanel.frameH/(pButtons.length + 4);
			for (int i = 0; i < pButtons.length; i++) {
				pButtons[i].setPosition(GamePanel.frameW/2, dist*(i+2));
				pButtons[i].update();
				if(pButtons[i].isClicked()) {
					switch (i) {
					case 0:
						paused = false;
						break;
					case 1:
						paused = false;
						player.setButttonGameOver();
						player.isGameOver = true;
//						radius = (radius-1)*0.8f + 1;
//						gradientT = 0;
						break;
					case 2:
						maneger.loadStage(Maneger.ACHIEVEMENTS);
						break;
					case 3:
						maneger.loadStage(Maneger.SETTINGS);
						break;
					case 4:
						music.stop();
						music.close();
						isGameOver = true;
						radius = (radius-1)*0.8f + 1;
						gradientT = 0;
						if(client != null) {
							try {
								client.exit();
								client = null;
							} catch (IOException e) {
								e.printStackTrace();
								System.exit(0);
							}
								maneger.loadStage(Maneger.MENU);
						}else {
							maneger.loadStage(Maneger.MENU);
						}
						break;
					default:
						break;
					}
				}
			}
			if(client == null)
			return;
		}
		
		GameData.totalTime = (System.nanoTime()-times)/1_000_000;
		
		
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
		
		
		for (int i = 0; i < darts.size(); i++) {
			darts.get(i).update();
			if(darts.get(i).isHit()) {
				darts.remove(i);
			}
		}
		if(dartsTime > 0)
			dartsTime--;
		
		if(client == null || !client.isGameEnd())
		player.update();
		if(player.isGameOver && !isGameOver) {
			isGameOver = true;
			player.isGameOver = false;
			if(client == null) {
				overStage = new GameOverStage(music, maneger, player, gold, diamonds, level, this);
				if(!player.getGameOvers()[0])
					music.stop();
			}
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
					if(level.getBlock(x+tileX-5, y+tileY) == level.BLOCK_DART) {
						if(dartsTime < 1) {
							sounds[DART].play(0);
							dartsTime = 50;
							darts.add(new Dart(this, x+tileX-5, y+tileY));
						}
					}
					if(level.getBlock(x+tileX, y+tileY+2) == level.BLOCK_TNT && boom_time == 0) {
						boom_time = 5;
						boom_x = x+tileX;
						boom_y = y+tileY+2;
					}
				}
				if(level.BLOCK_PLATE_ACTIVATE == t && !isHit) {
					level.setTile(x+tileX, y+tileY, level.BLOCK_PLATE);
					sounds[PLATE_UP].play(0);
				}
				if(isHit && t == level.BLOCK_GOLD) {
					if(client == null) {
						gold++;
						level.setTile(x+tileX, y+tileY, level.BLOCK_AIR);
						GameData.complitedAchievements(GameData.ACHIEVEMENTS_GOLD);
					}else {
						client.sendRemoveTreasures(x+tileX, y+tileY);
					}
				}
				if(isHit && t == level.BLOCK_DIAMOND) {
					if(client == null) {
						diamonds++;
						level.setTile(x+tileX, y+tileY, level.BLOCK_AIR);
						GameData.complitedAchievements(GameData.ACHIEVEMENTS_DIAMOND);
					}else {
						client.sendRemoveTreasures(x+tileX, y+tileY);
					}
				}
				
				level.setHit(x+tileX, y+tileY, false);
			}
		}
		

	}
	
	private void setKeys(int key, boolean b) {
		if(client == null || client.isAllReady) {
			if(key == GameData.control[GameData.KEY_JUMP])
				player.setVK_UP(b);
			if(key == GameData.control[GameData.KEY_DOWN])
				player.setVK_DOWN(b);
			if(key == GameData.control[GameData.KEY_RIGHT])
				player.setVK_RIGHT(b);
			if(key == GameData.control[GameData.KEY_LEFT])
				player.setVK_LEFT(b);
		}
		if(key == KeyEvent.VK_ENTER && client != null) {
			if(!isReady)
				client.sendReady();
			isReady = true;
		}
	}

	boolean isReady = false;

	@Override
	protected void keyPressed(KeyEvent e) {
		if(client != null) {
			if(client.isPlaying) {
				setKeys(e.getKeyCode(),true);
			} else {

				if(e.getKeyCode() == GameData.control[GameData.KEY_RIGHT]) {
					client.nextView(0);
				}
				if(e.getKeyCode() == GameData.control[GameData.KEY_LEFT]) {
					client.nextLast(0);
				}
			}
		}else {
			setKeys(e.getKeyCode(),true);
		}
	}

	@Override
	protected void keyReleased(KeyEvent e) {
		setKeys(e.getKeyCode(),false);
		if(e.getKeyCode() == GameData.control[GameData.KEY_PAUSE]) {
			paused = !paused;
		}
	}
	
	@Override
	protected LevelGenerator getLevelGenerator() {
		return level;
	}
	
	public void deleteTraps(int x, int y) {
		level.deleteTraps(x, y);
	}

	boolean paused = false;

	@Override
	protected void releasedAll() {
		if(player != null) {
			player.setVK_UP(false);
			player.setVK_DOWN(false);
			player.setVK_RIGHT(false);
			player.setVK_LEFT(false);
		}
	}
	
	public static void updateMusicVolume() {
		if(music != null)
		music.setVolume(GameData.audio[GameData.AUDIO_MUSIC]/2f);
	}
	public void updateVolume() {
		updateMusicVolume();
		for (int ii = 0; ii < sounds.length; ii++) {
			sounds[ii].setVolume(GameData.audio[GameData.AUDIO_SOUNDS]/2f);
		}
	}
	
	public int getDarts() {
		return darts.size();
	}

	@Override
	protected void reloadTexts() {
		pButtons[0].setText(GameData.texts[GameData.TEXT_RESUME]);
		pButtons[1].setText(GameData.texts[GameData.TEXT_FAST_LOSE]);
		pButtons[2].setText(GameData.texts[GameData.TEXT_ACHIEVEMENTS]);
		pButtons[3].setText(GameData.texts[GameData.TEXT_SETTINGS]);
		pButtons[4].setText(GameData.texts[GameData.TEXT_MENU]);
	}
	
	
	public static void reloadMusic() {
		if(music == null)
			return;
		boolean isPlaying = music.isPlaying();
		music.stop();
		music.close();
		music = new MyAudio("/music/Dungeon_Underground_Traps.wav");
		if(isPlaying)
			music.play(-1);

		if(sounds == null)
			return;
		for (int i = 0; i < sounds.length; i++) {
			sounds[i] = new MyAudio("/sounds/" + soundsNames[i] + ".wav");
		}
	}
}
