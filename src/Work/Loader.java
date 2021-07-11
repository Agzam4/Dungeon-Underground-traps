package Work;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Loader {

	public static int tilesize = 16;
	public static BufferedImage[] tileset;
	public static BufferedImage[] achievement_ico;
	
	public static BufferedImage PLAYER;
	public static BufferedImage DART;

	public static Color COLOR_GAME_BG;
	public static Color COLOR_TEXT_BG;
	public static Color COLOR_TEXT_FG;
	public static Color COLOR_TEXT_FG1;
	public static Color COLOR_TEXT_FG2;
	
	public static Color COLOR_BORDER_UP = Color.BLACK;
	public static Color COLOR_BORDER_DOWN = Color.BLACK;
	public static Color COLOR_BORDER_RIGHT = Color.BLACK;
	public static Color COLOR_BORDER_LEFT = Color.BLACK;

	public static boolean isVisibleBorderUp = false;
	public static boolean isVisibleBorderDown = false;
	public static boolean isVisibleBorderRight = false;
	public static boolean isVisibleBorderLeft = false;

	public static double borderSize = 2;

	public static final int BORDER_TYPE_IN = -1;
	public static final int BORDER_TYPE_CENTER = 0;
	public static final int BORDER_TYPE_OUT = 1;
	public static int borderType = BORDER_TYPE_IN;

	public static BufferedImage ACHIEVEMENTS_BG;

	public static int minQuality = 1;

	private static Color[] colors;
	
	public static void reload(String pack) {
		MyFile.pack = pack;
		PLAYER =  MyFile.readImage("tileset/player");
		DART =  MyFile.readImage("tileset/dart");
		ACHIEVEMENTS_BG =  MyFile.readImage("bg/achievement_stage");
		loadTileSet();
		loadAchievementIco();
		loadColors();
		loadBorders();
		
		minQuality = 1;
		if(16 < tilesize) {
			minQuality = (int) Math.ceil(tilesize/16d);
		}
	}
	
	private static void loadBorders() {
		String data[];
		try {
			data = (MyFile.readPackFile("border.border")+"&&&&&&&&&&&&&&&").split("&");
		} catch (NullPointerException e) {
			borderSize = 0;
			isVisibleBorderDown = false;
			isVisibleBorderLeft = false;
			isVisibleBorderRight = false;
			isVisibleBorderUp = false;
			return;
		}
		String data1[] = data[0].replaceAll("\r", "").split(" ");
		try {
			System.out.println("Data2: [" + data1[0] + "]");
			borderType = Integer.parseInt(data1[0]);
			System.out.println("D2: " + borderType);
			if(borderType < -1) borderType = -1;
			if(borderType > 1) borderType = 1;
		} catch (NumberFormatException e) {
			borderType = BORDER_TYPE_IN;
			borderSize = 0;
		}
		try {
			System.out.println("Data1: [" + data1[1] + "]");
			borderSize = Double.parseDouble(data1[1]);
			System.out.println("D1: " + borderSize);
		} catch (NumberFormatException e) {
			borderType = BORDER_TYPE_IN;
			borderSize = 0;
		}
		System.out.println("borderType: " + borderType + "\tborderSize: " + borderSize);
		Color[] cs = new Color[4];
		for (int i = 1; i < 5; i++) {
			boolean b = false;
			try {
				cs[i-1] = Color.decode("#" + data[i]);
				b = true;
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			}
			switch (i) {
			case 1:
				isVisibleBorderUp = b;
				COLOR_BORDER_UP = cs[0];
				break;
			case 2:
				isVisibleBorderDown = b;
				COLOR_BORDER_DOWN = cs[1];
				break;
			case 3:
				isVisibleBorderRight = b;
				COLOR_BORDER_RIGHT = cs[2];
				break;
			case 4:
				isVisibleBorderLeft = b;
				COLOR_BORDER_LEFT = cs[3];
				break;
			default:
				break;
			}
			
			System.out.println(i + ") " + b + "\t" + cs[i-1]);
		}
	}

	private static void loadColors() {
		String ttt = "";
		for (int i = 0; i < 4; i++) {
			ttt+= "\n~";
		}
		String[] scolors = (MyFile.loadTextFile("colors/colors.colors")+ttt).split("\n");
		Color[] defColors = {new Color(43,43,43), Color.BLACK, Color.WHITE, Color.GRAY, Color.DARK_GRAY};
		colors = new Color[defColors.length];
		for (int i = 0; i < defColors.length; i++) {
			System.out.println(scolors[i]);
			try {
				colors[i] = Color.decode("#" + (scolors[i] + "000000").substring(0, 6));
			} catch (NumberFormatException e) {
				colors[i] = defColors[i];
				System.out.println("DEF: " + colors[i]);
				e.printStackTrace();
			}
		}

		COLOR_GAME_BG = colors[0];
		COLOR_TEXT_BG = colors[1];
		COLOR_TEXT_FG = colors[2];
		COLOR_TEXT_FG1 = colors[3];
		COLOR_TEXT_FG2 = colors[4];
	}
	
	private static void loadAchievementIco() {
		BufferedImage tileset = MyFile.readImage("ico/achievement_img");
		Loader.achievement_ico = new BufferedImage[4 * 4];
		int size = tileset.getWidth()/4;
		int id = 0;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				Loader.achievement_ico[id] = tileset.getSubimage(x*size, y*size, size, size);
				id++;
			}
		}
	}

	private static void loadTileSet() {
		BufferedImage tileset = MyFile.readImage("tileset/Dungeon_Underground_traps");
		tilesize = tileset.getWidth()/7;
		try {
			ImageIO.write(tileset, "png", new File("debug.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Loader.tileset = new BufferedImage[7 * 5];
		int id = 0;
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 7; x++) {
				Loader.tileset[id] = tileset.getSubimage(x*tilesize, y*tilesize, tilesize, tilesize);
				id++;
			}
		}
	}
}
