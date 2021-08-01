package Work;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Main.GamePanel;
import Objects.AchievementBlock;
import Stages.GameStage;
import Stages.Maneger;

public class GameData {

	public static GameStage saveSameStage_obj = loadGameStageObj();
	
	public static boolean isDevMode = loadBooleanArray("dm", 1)[0];
	
	public static final String language = getLanguage();
	
	public static String packName = getPackName();
	public static int totalGold = getTotalInt("g");
	public static int totalDiamonds = getTotalInt("d");
	public static long totalTime = getTotalTime();
	public static long totalWins = getTotalInt("w");
	public static int totalLevels =  getTotalInt("l");
	
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
	
	public static boolean achievements[] = loadBooleanArray("a", 16);//loadAchievements();

	public static String texts[] = loadTexts();
	public static final int TEXT_PLAY = 0;
	public static final int TEXT_SEED = 1;
	public static final int TEXT_SHOP = 2;
	public static final int TEXT_SETTINGS = 3;
	public static final int TEXT_ACHIEVEMENTS = 4;
	public static final int TEXT_EXIT = 5;
	public static final int TEXT_MENU = 6;
	public static final int TEXT_NEXT = 7;
	public static final int TEXT_BACK = 8;
	public static final int TEXT_GAME = 9;
	public static final int TEXT_GRAPHICS = 10;
	public static final int TEXT_AUDIO = 11;
	public static final int TEXT_CONTROL = 12;
	public static final int TEXT_TIME = 13;
	public static final int TEXT_LEVELS = 14;
	public static final int TEXT_WINS = 15;
	public static final int TEXT_GOLD = 16;
	public static final int TEXT_DIAMONDS = 17;
	public static final int TEXT_ACHIEVEMENTS2 = 18;
	public static final int TEXT_FULLSCREEN = 19;
	public static final int TEXT_UNFULLSCREEN = 20;
	public static final int TEXT_RELOAD_GAME = 21;
	public static final int TEXT_DELETE_PROGRESS = 22;
	public static final int TEXT_GAME_QUALITY = 23;
	public static final int TEXT_GRAPHICS_INFO = 24;
	public static final int TEXT_MUSIC = 25;
	public static final int TEXT_SOUNDS = 26;
	public static final int TEXT_SOUND_OF_ACHIEVEMENT = 27;
	public static final int TEXT_VOLUME = 28;
	public static final int TEXT_AUDIO_INFO_0 = 29;
	public static final int TEXT_AUDIO_INFO_1 = 30;
	public static final int TEXT_PAUSE = 31;
	public static final int TEXT_UP = 32;
	public static final int TEXT_LEFT = 33;
	public static final int TEXT_DOWN = 34;
	public static final int TEXT_RIGHT = 35;
	public static final int TEXT_PRESS_KEY_TO_SET = 36;
	public static final int TEXT_RESUME = 37;
	public static final int TEXT_FAST_LOSE = 38;
	
	public static void reloadText() {
		texts = loadTexts();
		GamePanel.reloadTexts();
	}

	private static GameStage loadGameStageObj() {
		try {
			Object o = MyFile.readObject("data/saves/.gmstg");
			System.out.println((GameStage) o);
			return (GameStage) o;
		} catch (ClassNotFoundException | ClassCastException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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

	// TODO:
	public static boolean skipFrames = false;
	public static boolean lavaLight = false; // new BI
	
	public static boolean fullscreen = loadBooleanArray("fs", 1)[0];
	public static int consecutive_wins = 0;
	public static int quality = getQuality();

	public static final int AUDIO_MUSIC = 0;
	public static final int AUDIO_SOUNDS = 1;
	public static final int AUDIO_ACHIEVEMENTS = 2;
	public static int[] defAudio = {2,2,2};
	public static int[] audio = loadIntArray("audio", defAudio);

	public static final int KEY_PAUSE = 0;
	public static final int KEY_JUMP = 1;
	public static final int KEY_LEFT = 2;
	public static final int KEY_DOWN = 3;
	public static final int KEY_RIGHT = 4;
	

	public static String username = loadUserName();


	public static int[] defControl = {KeyEvent.VK_ESCAPE,KeyEvent.VK_UP,
			KeyEvent.VK_LEFT,KeyEvent.VK_DOWN,KeyEvent.VK_RIGHT};
	public static int[] control = loadIntArray("control", defControl);
	
	public static void save() { // TODO
		MyFile.checkSavesFolder();
		
		saveBooleanArray("a", achievements, "achievements");
		saveBooleanArray("fs", new boolean[] {fullscreen}, "fullscreen");
		saveIntArray("control", control, "control");
		saveIntArray("audio", audio, "audio");
		
		try {
			MyFile.writeFile("data/saves/.q", quality + "");
		} catch (IOException e1) {
		}

		try {
			String mydata = "";
			if(MyFile.pack != null)
				for (char c : MyFile.pack.toCharArray()) {
					mydata += (int)c + "\n";
				}
			MyFile.writeFile("data/saves/.pn", MyFile.pack == null ? "" : mydata);
		} catch (IOException e1) {
		}
		
		try {
			saveData(totalTime+"", "tt");
			saveData(totalLevels+"", "tl");
			saveData(totalWins+"", "tw");
			saveData(totalGold+"", "tg");
			saveData(totalDiamonds+"", "td");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error saving totalData:\n" + e.getMessage());
		}

		try {
			MyFile.writeFile("data/saves/.usrnm", username);
		} catch (IOException e) {
		}
		
		/*
		 * 	String achievementsString = "";
		for (boolean a : achievements) {
			achievementsString += a ? ' ' : '\t';
		}
		try {
			MyFile.writeFile("data/saves/.a", achievementsString);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error saving achievements:\n" + e.getMessage());
		}
		 * 
		 */
		
		saveBooleanArray("dm", new boolean[] {isDevMode}, "");
		
		if(GamePanel.maneger.isGameStage()) {
			System.out.println("Saving Game");
			MyFile.saveObject(GamePanel.maneger.getStage(), "data/saves/.gmstg");
		}else {
			MyFile.saveObject(null, "data/saves/.gmstg");
		}
	}

	private static void saveData(String value, String file) throws IOException {
		MyFile.writeFile("data/saves/." + file, value +" " + hashString(value));
	}
	
//	private static boolean[] loadAchievements() {
//		boolean[] a = new boolean[16];
//		char[] achievementsString = (MyFile.readFile("data/saves/.a")+"1234567890123456").toCharArray();
//		for (int i = 0; i < a.length; i++) {
//			if(achievementsString[i] == ' ') {
//				a[i] = true;
//			}
//		}
//		return a;
//	}
	
	
	private static void saveIntArray(String file, int[] is, String text) {
		String saveText = "";
		for (int i = 0; i < is.length; i++) {
			saveText += is[i] + "/";
		}
		try {
			MyFile.writeFile("data/saves/." + file, saveText);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error saving " + text + ":\n" + e.getMessage());
		}
	}
	
	private static int[] loadIntArray(String file, int[] def) {
		int[] is = new int[def.length];
		String[] data;
		try {
			data = MyFile.readFile("data/saves/." + file).split("/");
		} catch (NullPointerException e) {
			return def;
		}
		System.out.println(data.length + " / " + def.length);
		if(data.length != def.length)
			return def;
		for (int i = 0; i < data.length; i++) {
			try {
				is[i] = Integer.parseInt(data[i]);
			} catch (NumberFormatException e) {
				return def;
			}
		}
		return is;
	}
	private static void saveBooleanArray(String file, boolean[] bs, String text) {
		String txt = "";
		for (boolean b : bs) {
			txt += b ? ' ' : '\t';
		}
		try {
			MyFile.writeFile("data/saves/." + file, txt);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error saving " + text + ":\n" + e.getMessage());
		}
	}
	
	private static boolean[] loadBooleanArray(String file, int size) {
		String ssize = "";
		for (int i = 0; i < size; i++) {
			ssize += "A";
		}
		boolean[] bs = new boolean[size];
		char[] cs =  (MyFile.readFile("data/saves/." + file)+ssize).toCharArray();
		for (int i = 0; i < bs.length; i++) {
			if(cs[i] == ' ') {
				bs[i] = true;
			}
		}
		return bs;
	}
	
	private static String getPackName() {
		String name[] = MyFile.readFile("data/saves/.pn").split("\n");
		if(name.length < 1)
			return "";
		char[] cs = new char[name.length];
		for (int i = 0; i < name.length; i++) {
			try {
				cs[i] = (char) Integer.valueOf(name[i]).intValue();
			} catch (NumberFormatException e) {
				return "";
			}
		}
		return new String(cs);
	}
	
	private static int getTotalInt(String type) {
		try {
			String file[] = (MyFile.readFile("data/saves/.t" + type)+" 0 0").split(" ");
			int v = Integer.parseInt(file[0]);
			if(hashString(v+"").equals(file[1]))
				return v;
			else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private static long getTotalTime() {
		try {
			String file[] = (MyFile.readFile("data/saves/.tt")+" 0 0").split(" ");
			long v = Long.parseLong(file[0]);
			if(hashString(v+"").equals(file[1]))
				return v;
			else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private static String[] loadTexts() {
		return MyFile.readPackFile("/text/texts." + language).split("\n");
//		return MyFile.readFileInResource("/text/texts." + language).split("\n");
	}
	
	public static String hashString(String string) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(
					string.getBytes(StandardCharsets.US_ASCII));
			System.out.println(new String(hash));
			return new String(hash);
		} catch (NoSuchAlgorithmException e) {
		}
		return "";
	}

	public static void deleteProgress() {
		achievements = new boolean[16];
		totalDiamonds = 0;
		totalGold = 0;
		totalTime = 0;
		totalLevels = 0;
		totalWins = 0;
		save();
	}
	
	private static int getQuality() {
		try {
			return Integer.parseInt(MyFile.readFile("data/saves/.q"));
		} catch (NumberFormatException e) {
			return 5;
		}
	}
	
	private static String loadUserName() {
		String userName = MyFile.readFile("data/saves/.usrnm");
		if(userName == null || userName.isEmpty())
			return System.getProperty("user.name");
		return userName;
	}
}
