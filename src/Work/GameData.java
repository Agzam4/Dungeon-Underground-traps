package Work;

import java.io.IOException;

import javax.swing.JOptionPane;

public class GameData {

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
	
	public static void complitedAchievements(int id) {
		achievements[id] = true;
	}
	
	public static Long lastSeed = null;

	public static float screenShake = 0.0f;
	public static float visionRadius = 1.5f;

	public static boolean skipFrames = false;
	
	// FIXME:
	public static boolean lavaLight = false; // new BI
	
	
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
	
	private void getTexts() {
		
	}
}
