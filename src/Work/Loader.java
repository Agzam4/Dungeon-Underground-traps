package Work;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Loader {

	public static int tilesize = 16;
	public static BufferedImage[] tileset;
	public static BufferedImage[] achievement_ico;
	
	public static BufferedImage PLAYER;
	public static BufferedImage DART;

	public static Color GAME_BG_COLOR = Color.BLACK;

	public static BufferedImage ACHIEVEMENTS_BG;

	public static int minQuality = 1;

	private static Color[] colors;
	
	public static void reload(String pack) {
		MyFile.pack = pack;
		PLAYER = MyFile.readImage("tileset/player");
		DART = MyFile.readImage("tileset/dart");
		ACHIEVEMENTS_BG = MyFile.readImage("bg/achievement_bg");
		loadTileSet();
		loadAchievementIco();
		loadColors();
		
		minQuality = 1;
		if(16 < tilesize) {
			minQuality = (int) Math.ceil(tilesize/16d);
		}
	}
	
	private static void loadColors() {
		String[] scolors = MyFile.loadTextFile("colors/colors.colors").split("\n");
		colors = new Color[scolors.length];
		for (int i = 0; i < scolors.length; i++) {
			colors[i] = Color.decode("#" + scolors[i]);
		}
		
		GAME_BG_COLOR = colors[0];
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
