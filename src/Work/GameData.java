package Work;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Objects.AchievementBlock;

public class GameData {
	
	public static final String language = getLanguage();

	public static final int ACHIEVEMENTS_SPIKES = 0;
	public static final int ACHIEVEMENTS_LAVA = 1;
	public static final int ACHIEVEMENTS_GOLD = 2;
	public static final int ACHIEVEMENTS_DIAMOND = 3;
	public static final int ACHIEVEMENTS_CRUSHER = 4;
	public static final int ACHIEVEMENTS_CHEST = 5;
	public static final int ACHIEVEMENTS_BAG_OF_GOLD = 6;
	public static final int ACHIEVEMENTS_BAG_OF_DIAMONDS = 7;
	public static final int ACHIEVEMENTS_REWARD10 = 8;
	public static final int ACHIEVEMENTS_REWARD50 = 9;
	public static final int ACHIEVEMENTS_REWARD100 = 10;
	public static final int ACHIEVEMENTS_REWARD1000 = 11;
	public static final int ACHIEVEMENTS_SEARCH1 = 12;
	public static final int ACHIEVEMENTS_SEARCH3 = 13;
	public static final int ACHIEVEMENTS_SEARCH5 = 14;
	public static final int ACHIEVEMENTS_SEARCH7 = 15;
	
	public static boolean achievements[] = loadAchievements();

	public static final String texts[] = loadTexts();
	public static final int TEXT_PLAY = 0;
	public static final int TEXT_SEED = 1;
	public static final int TEXT_SHOP = 2;
	public static final int TEXT_SETTINGS = 3;
	public static final int TEXT_ACHIEVEMENTS = 4;
	public static final int TEXT_EXIT = 5;
	public static final int TEXT_MENU = 6;
	public static final int TEXT_NEXT = 7;
	public static final int TEXT_BACK = 8;
	
	private static ArrayList<AchievementBlock> blocks = new ArrayList<AchievementBlock>();
	public static void complitedAchievements(int id) {
		if(achievements[id])
			return;
		blocks.add(new AchievementBlock(id, true));
		achievements[id] = true;
	}
	
	private static String getLanguage() {
		String l = System.getProperty("user.language");
		if(l.equals("ru"))
			return l;
		else
			return "en";
	}

	public static void drawAchievementBlock(Graphics2D gf) {
		if(blocks.size() > 0)
			blocks.get(0).draw2(gf);
	}
	
	public static void updateAchievementBlock() {
		if(blocks.size() > 0)
			blocks.get(0).update();
		for (int i = 0; i < blocks.size(); i++) {
			if(blocks.get(i).needRemove()) {
				blocks.remove(i);
			}
		}
	}

	public static Long lastSeed = null;

	public static float screenShake = 0.0f;
	public static float visionRadius = 1.5f;

	public static boolean skipFrames = false;
	
	// FIXME:
	public static boolean lavaLight = false; // new BI
	
	public static int consecutive_wins = 0;
	
	public static void save() {
		MyFile.checkSavesFolder();
		String achievementsString = "";
		for (boolean a : achievements) {
			achievementsString += a ? ' ' : '\t';
		}
		try {
			MyFile.writeFile("data/saves/.a", achievementsString);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error saving achievements:\n" + e.getMessage());
		}
	}
	
	private static boolean[] loadAchievements() {
		System.out.println("User.language: " + System.getProperty("user.language"));
		boolean[] a = new boolean[16];
		char[] achievementsString = (MyFile.readFile("data/saves/.a")+"1234567890123456").toCharArray();
		for (int i = 0; i < a.length; i++) {
			if(achievementsString[i] == ' ') {
				a[i] = true;
			}
		}
		return a;
	}
	
	private static String[] loadTexts() {
		return MyFile.readFileInResource("/text/texts." + language).split("\n");
	}
}
